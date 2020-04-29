package com.yiqiandewo.dao;

import com.yiqiandewo.domain.Role;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleDao {

    @Select("select * from role where username = #{username}")
    Role findByUsername(String username);
}
