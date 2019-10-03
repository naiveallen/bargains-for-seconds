package com.allen.bargains_for_seconds.controller;

import com.allen.bargains_for_seconds.domain.User;
import com.allen.bargains_for_seconds.rabbitmq.MQSender;
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

    @Autowired
    MQSender mqSender;


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


    @GetMapping("/redis/get")
    public Result<User> get() {
        User user = redisService.get(UserKey.getById, ""+1 , User.class);
        return Result.success(user);
    }


    @GetMapping("/userInfo")
    public Result<User> get(User user) {
        return Result.success(user);
    }

    @GetMapping("/mq")
    @ResponseBody
    public Result<String> mq() {
        mqSender.send("Hello MQ!!!");
        return Result.success("success");
    }

}
