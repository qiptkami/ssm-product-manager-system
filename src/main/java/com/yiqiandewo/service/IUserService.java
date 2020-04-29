package com.yiqiandewo.service;

import com.yiqiandewo.domain.User;

import java.util.List;

public interface IUserService {

    List<User> findAll(int page, int size);

    User findById(Integer userId);

    void saveUser(User user);

    void delUser(Integer userId);

    void updateUser(User user);

    List<User> findByStr(String str, int page, int size);
}
