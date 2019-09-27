package com.allen.bargains_for_seconds.service;

import com.allen.bargains_for_seconds.dao.UserDao;
import com.allen.bargains_for_seconds.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(int id) {
        return userDao.getUserById(id);
    }

    @Transactional
    public boolean tx() {
        User user1 = new User(5, "555555");
        User user2 = new User(2, "222222");
        userDao.insert(user1);
        userDao.insert(user2);
        return true;
    }

}
