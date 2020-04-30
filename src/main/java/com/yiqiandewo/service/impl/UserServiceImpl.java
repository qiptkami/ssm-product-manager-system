package com.yiqiandewo.service.impl;

import com.github.pagehelper.PageHelper;
import com.yiqiandewo.dao.IUserDao;
import com.yiqiandewo.domain.User;
import com.yiqiandewo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public List<User> findAll(int page, int size) {
        PageHelper.startPage(page, size);
        return userDao.findAll();
    }

    @Override
    public User findById(Integer userId) {
        return userDao.findById(userId);
    }

    @Override
    public void saveUser(User user) {
        userDao.saveUser(user);
    }

    @Override
    public void delUser(Integer userId) {
        userDao.delUser(userId);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public List<User> findByStr(String str) {
        List<User> list = userDao.findByName(str);
        List<User> list1 = userDao.findByUsername(str);
        list.addAll(list1);
        return list;
    }
}
