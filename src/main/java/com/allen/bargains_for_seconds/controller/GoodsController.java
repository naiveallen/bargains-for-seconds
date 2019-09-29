package com.allen.bargains_for_seconds.controller;

import com.allen.bargains_for_seconds.domain.User;
import com.allen.bargains_for_seconds.service.GoodsService;
import com.allen.bargains_for_seconds.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @GetMapping("/goods")
    public String list(Model model, User user) {
//        System.out.println(user.toString());
        if (user == null) {
            return "login";
        }
        List<GoodsVo> goodsList = goodsService.listGoodsVo();

        model.addAttribute("goodsList", goodsList);
        model.addAttribute("user", user);

        return "goods_list";
    }

    @GetMapping("/goods/{id}")
    public String details(Model model,
                          User user,
                          @PathVariable("id") Long id) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(id);
        model.addAttribute("goods", goods);
        model.addAttribute("user", user);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";

    }


}
