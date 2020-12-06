package com.yiqiandewo.dao;

import com.yiqiandewo.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserDao {

    @Select("select * from user")
    List<User> findAll();

    @Select("select * from user where userId = #{userId}")
    User findById(Integer userId);

    //必须是value
    @Select("select * from user where name like '%${value}%'")
    List<User> findByName(String str);

    @Select("select * from user where username like '%${value}%'")
    List<User> findByUsername(String str);

    @Insert("insert into user(name, username, qq, email) values(#{name}, #{username}, #{qq}, #{email})")
    void saveUser(User user);

    @Delete("delete from user where userId = #{userId}")
    void delUser(Integer userId);

    @Update("update user set name = #{name}, username = #{username}, qq = #{qq}, email = #{email} where userId = #{userId}")
    void updateUser(User user);

    @Select("select userId, username from user where username = #{username}")
    @Results(id = "findOrdersByUsernameMap", value = {
            @Result(id = true, column = "userId", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "userId", property = "orders", javaType = java.util.List.class ,many = @Many(select="com.yiqiandewo.dao.IOrderDao.findOrdersByUserId", fetchType = FetchType.LAZY)),
    })
    //由用户名查找出所有该用户的订单
    User findOrdersByUsername(String username);
}
