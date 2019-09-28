package com.allen.bargains_for_seconds.controller;

import com.allen.bargains_for_seconds.domain.User;
import com.allen.bargains_for_seconds.redis.RedisService;
import com.allen.bargains_for_seconds.redis.UserKey;
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

    @Autowired
    RedisService redisService;


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

    @GetMapping("/redis/get")
    public Result<User> get() {
        User user = redisService.get(UserKey.getById, ""+1 , User.class);
        return Result.success(user);
    }

    @GetMapping("/redis/set")
    public Result<Boolean> set() {
        User user = new User(1, "allen");
        Boolean res = redisService.set(UserKey.getById, ""+1, user);
        return Result.success(res);
    }


}
