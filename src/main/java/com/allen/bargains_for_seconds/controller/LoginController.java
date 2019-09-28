package com.allen.bargains_for_seconds.controller;

import com.allen.bargains_for_seconds.domain.User;
import com.allen.bargains_for_seconds.result.Result;
import com.allen.bargains_for_seconds.service.UserService;
import com.allen.bargains_for_seconds.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @ResponseBody
    @PostMapping("/login")
    public Result<Boolean> doLogin(@Valid LoginVo loginVo,
                                   HttpServletResponse response) {
        logger.info(loginVo.toString());
        userService.login(response, loginVo);
        return Result.success(true);
    }
}
