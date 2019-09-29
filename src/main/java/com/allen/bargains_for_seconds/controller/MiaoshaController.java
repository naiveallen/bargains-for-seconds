package com.allen.bargains_for_seconds.controller;

import com.allen.bargains_for_seconds.domain.MiaoshaOrder;
import com.allen.bargains_for_seconds.domain.OrderInfo;
import com.allen.bargains_for_seconds.domain.User;
import com.allen.bargains_for_seconds.result.CodeMsg;
import com.allen.bargains_for_seconds.service.GoodsService;
import com.allen.bargains_for_seconds.service.MiaoshaService;
import com.allen.bargains_for_seconds.service.OrderService;
import com.allen.bargains_for_seconds.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MiaoshaController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @PostMapping("/miaosha")
    public String miaosha(Model model,
                          RedirectAttributes attributes,
                          User user,
                          @RequestParam("goodsId") Long goodsId) {
        if (user == null) {
            return "login";
        }

        // 判断库存
        GoodsVo good = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = good.getStockCount();
        if (stock <= 0) {
            model.addAttribute("msg", CodeMsg.MIAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }
        // 判断是否已经秒杀到了
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null) {
            model.addAttribute("msg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        OrderInfo orderInfo = miaoshaService.miaosha(user, good);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", good);
        return "order_detail";

    }


}
