package com.allen.bargains_for_seconds.service;

import com.allen.bargains_for_seconds.dao.UserDao;
import com.allen.bargains_for_seconds.domain.User;
import com.allen.bargains_for_seconds.exception.GlobalException;
import com.allen.bargains_for_seconds.redis.RedisService;
import com.allen.bargains_for_seconds.redis.UserKey;
import com.allen.bargains_for_seconds.result.CodeMsg;
import com.allen.bargains_for_seconds.util.MD5Util;
import com.allen.bargains_for_seconds.util.UUIDUtil;
import com.allen.bargains_for_seconds.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public static final String COOKI_NAME_TOKEN = "token";


    @Autowired
    RedisService redisService;

    public User getById(long id) {
        return userDao.getById(id);
    }


    public User getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        //延长有效期
        if(user != null) {
            addCookie(response, token, user);
        }
        return user;
    }


    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        User user = getById(Long.parseLong(mobile));
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if(!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        // 生成cookie
        String token	 = UUIDUtil.uuid();
        addCookie(response, token, user);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
