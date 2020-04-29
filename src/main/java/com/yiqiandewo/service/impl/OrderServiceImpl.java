package com.yiqiandewo.service.impl;

import com.github.pagehelper.PageHelper;
import com.yiqiandewo.dao.IOrderDao;
import com.yiqiandewo.dao.IProductDao;
import com.yiqiandewo.dao.IUserDao;
import com.yiqiandewo.domain.Order;
import com.yiqiandewo.domain.Product;
import com.yiqiandewo.domain.User;
import com.yiqiandewo.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IProductDao productDao;

    @Override
    public List<Order> findAll(int page, int size) {
        PageHelper.startPage(page, size);
        List<Order> list = orderDao.findAll();
        return list;
    }

    @Override
    public void del(Integer id) {
        orderDao.del(id);
    }

    @Override
    public Order findById(Integer id) {
        return orderDao.findById(id);
    }

    @Override
    public User findByUsername(String str) {
        return userDao.findOrdersByUsername(str);
    }

    @Override
    public Product findByProductName(String str) {
        return productDao.findOrdersByProductName(str);
    }
}
