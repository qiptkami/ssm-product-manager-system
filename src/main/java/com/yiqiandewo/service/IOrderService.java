package com.yiqiandewo.service;

import com.yiqiandewo.domain.Order;
import com.yiqiandewo.domain.Product;
import com.yiqiandewo.domain.User;

import java.util.List;

public interface IOrderService {

    List<Order> findAll(int page, int size);

    void del(Integer id);

    Order findById(Integer id);

    User findByUsername(String str);

    Product findByProductName(String str);
}
