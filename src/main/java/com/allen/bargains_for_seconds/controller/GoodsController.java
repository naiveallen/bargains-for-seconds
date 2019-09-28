package com.allen.bargains_for_seconds.controller;

import com.allen.bargains_for_seconds.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoodsController {

    @GetMapping("/goods")
    public String list(Model model, User user) {
//        System.out.println(user.toString());
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        return "goods_list";
    }


}
