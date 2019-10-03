package com.allen.bargains_for_seconds.controller;

import com.allen.bargains_for_seconds.domain.MiaoshaOrder;
import com.allen.bargains_for_seconds.domain.OrderInfo;
import com.allen.bargains_for_seconds.domain.User;
import com.allen.bargains_for_seconds.rabbitmq.MQSender;
import com.allen.bargains_for_seconds.rabbitmq.MiaoshaMessage;
import com.allen.bargains_for_seconds.redis.GoodsKey;
import com.allen.bargains_for_seconds.redis.RedisService;
import com.allen.bargains_for_seconds.result.CodeMsg;
import com.allen.bargains_for_seconds.result.Result;
import com.allen.bargains_for_seconds.service.GoodsService;
import com.allen.bargains_for_seconds.service.MiaoshaService;
import com.allen.bargains_for_seconds.service.OrderService;
import com.allen.bargains_for_seconds.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MiaoshaController implements InitializingBean {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender mqSender;

    Map<Long, Boolean> localOverMap = new HashMap<>();

    //实现InitializingBean接口 系统初始化时 会回调这个方法
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null) {
            return;
        }
        for(GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }


    // 1176  5000线程 * 10次请求
    // 秒杀后  商品表中的库存 会出现负数  -102 （10000商品数量  50000请求）
    // 负数解决： 数据库中update的时候添加限制条件 （when stock > 0 update）

    // 2193  5000线程 * 10次请求


    @PostMapping("/miaosha")
    @ResponseBody
    public Result<Integer> miaosha(Model model,
                          User user,
                          @RequestParam("goodsId") Long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }

        // 内存标记 减少redis访问
        if (localOverMap.get(goodsId)) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        // redis预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, ""+goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        // 判断是否已经秒杀到了
//        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//        if (miaoshaOrder != null) {
//            model.addAttribute("msg", CodeMsg.REPEATE_MIAOSHA.getMsg());
//            return "miaosha_fail";
//        }

        // 入队
        MiaoshaMessage message = new MiaoshaMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(message);

        // 0代表 排队中 前端再处理逻辑
        return Result.success(0);





        // 判断库存
//        GoodsVo good = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock = good.getStockCount();
//        if (stock <= 0) {
////            model.addAttribute("msg", CodeMsg.MIAOSHA_OVER.getMsg());
//            return Result.error(CodeMsg.MIAOSHA_OVER);
//        }
        // 判断是否已经秒杀到了
//        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//        if (miaoshaOrder != null) {
//            model.addAttribute("msg", CodeMsg.REPEATE_MIAOSHA.getMsg());
//            return "miaosha_fail";
//        }

//        OrderInfo orderInfo = miaoshaService.miaosha(user, good);
//        model.addAttribute("orderInfo", orderInfo);
//        model.addAttribute("goods", good);
//        return Result.success(orderInfo);

    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @GetMapping("/miaosha/result")
    @ResponseBody
    public Result<Long> getResult(User user,
                                     @RequestParam("goodsId") Long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);

        return Result.success(result);
    }



}
