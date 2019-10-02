package com.allen.bargains_for_seconds.controller;

import com.allen.bargains_for_seconds.domain.OrderInfo;
import com.allen.bargains_for_seconds.domain.User;
import com.allen.bargains_for_seconds.result.CodeMsg;
import com.allen.bargains_for_seconds.result.Result;
import com.allen.bargains_for_seconds.service.GoodsService;
import com.allen.bargains_for_seconds.service.OrderService;
import com.allen.bargains_for_seconds.vo.GoodsVo;
import com.allen.bargains_for_seconds.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @GetMapping("/order/detail")
    @ResponseBody
    public Result<OrderDetailVo> getOrder(User user,
                                          @RequestParam("orderId") Long orderId) {

        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if(order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);

    }

}
