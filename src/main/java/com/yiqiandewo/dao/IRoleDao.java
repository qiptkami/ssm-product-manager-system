package com.yiqiandewo.dao;

import com.yiqiandewo.domain.Role;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleDao {

    @Select("select * from role where username = #{username}")
    Role findByUsername(String username);

    @Update("update role set password = #{password} where username = #{username}")
    void changePassword(Role role);

    @Select("select password from role where username = #{username}")
    String findPassword(String username);
}
