package com.yiqiandewo.dao;

import com.yiqiandewo.domain.Order;
import com.yiqiandewo.domain.Product;
import com.yiqiandewo.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDao {

    @Select("select * from orders")//同时还要查出订单对应的产品和用于
    @Results(id="orderMap", value = {
            @Result(id=true, column = "orderId", property = "orderId"),
            @Result(column = "orderTime", property = "orderTime"),
            @Result(column = "productId", property = "product", javaType = Product.class, one = @One(select = "com.yiqiandewo.dao.IProductDao.findById", fetchType = FetchType.EAGER)),
            @Result(column = "userId", property = "user", javaType = User.class, one = @One(select = "com.yiqiandewo.dao.IUserDao.findById", fetchType = FetchType.EAGER))
    })
    List<Order> findAll();

    @Select("select * from orders where orderId = #{id}")
    @ResultMap("orderMap")
    Order findById(Integer id);

    @Select("delete from orders where orderId = #{id}")
    void del(Integer id);

    @Select("select * from orders where userId = #{id}")
    @Results(id="findOrdersByUserIdMap", value = {
            @Result(id=true, column = "orderId", property = "orderId"),
            @Result(column = "productId", property = "product", javaType = Product.class, one = @One(select = "com.yiqiandewo.dao.IProductDao.findById", fetchType = FetchType.EAGER))
    })
    List<Order> findOrdersByUserId(Integer id);

    @Select("select * from orders where productId = #{id}")
    @Results(id = "findOrdersByProductIdMap", value = {
            @Result(id = true, column = "orderId", property = "orderId"),
            @Result(column = "userId", property = "user", javaType = User.class, one = @One(select = "com.yiqiandewo.dao.IUserDao.findById", fetchType = FetchType.EAGER))
    })
    List<Order> findOrdersByProductId(Integer id);

}
