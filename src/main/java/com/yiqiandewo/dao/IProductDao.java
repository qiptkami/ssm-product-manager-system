package com.yiqiandewo.dao;

import com.yiqiandewo.domain.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductDao {

    @Select("select * from product")
    List<Product> findAll();

    //必须是value
    @Select("select * from product where productName like '%${value}%'")
    List<Product> findByName(String str);

    @Select("select * from product where productDesc like '%${value}%'")
    List<Product> findByDesc(String str);

    @Insert("insert into product(productName, productPrice, productNum, productDesc) values(#{productName}, #{productPrice}, #{productNum}, #{productDesc})")
    void saveProduct(Product product);

    @Delete("delete from product where productId = #{productId}")
    void delProduct(Integer productId);

    @Update("update product set productName = #{productName}, productPrice = #{productPrice}, productNum = #{productNum}, productDesc = #{productDesc} where productId = #{productId}")
    void updateProduct(Product product);

    @Select("select * from product where productId = #{id}")
    Product findById(Integer id);

    @Select("select * from product where productName = #{productName}")
    @Results(id="findOrdersByProductNameMap", value = {
            @Result(id=true, column = "productId", property = "productId"),
            @Result(column = "productId", property = "orders", javaType = java.util.List.class, many = @Many(select = "com.yiqiandewo.dao.IOrderDao.findOrdersByProductId", fetchType = FetchType.LAZY))
    })
    //由商品名称查找出所有购买该商品的订单
    Product findOrdersByProductName(String productName);
}
