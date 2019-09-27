package com.allen.bargains_for_seconds.controller;

import com.allen.bargains_for_seconds.domain.User;
import com.allen.bargains_for_seconds.result.CodeMsg;
import com.allen.bargains_for_seconds.result.Result;
import com.allen.bargains_for_seconds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;


    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("hello");
    }


    @GetMapping("/helloerror")
    public Result<String> helloError() {
        return Result.error(CodeMsg.SERVER_ERROR);
    }


    @GetMapping("/user/{id}")
    public Result<User> user(@PathVariable("id") int id) {
        User user = userService.getById(id);
        return Result.success(user);
    }


    @GetMapping("/user/tx")
    public Result<Boolean> tx() {
        boolean tx = userService.tx();
        return Result.success(tx);
    }


}
