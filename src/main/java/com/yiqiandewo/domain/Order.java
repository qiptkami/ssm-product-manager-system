package com.yiqiandewo.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order implements Serializable {
    private Integer orderId;

    //从客户端获取的数据到服务器
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;
    private Integer productId;
    private Integer userId;
    private Product product;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderTime);
        Timestamp goodsC_date = Timestamp.valueOf(nowTime);//把时间转换
        this.orderTime = goodsC_date;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderTime=" + orderTime +
                ", productId=" + productId +
                ", userId=" + userId +
                ", product=" + product +
                ", user=" + user +
                '}';
    }
}
