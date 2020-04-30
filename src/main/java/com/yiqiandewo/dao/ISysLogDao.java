package com.yiqiandewo.dao;

import com.yiqiandewo.domain.SysLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISysLogDao {

    @Select("select * from syslog")
    List<SysLog> findAll();

    @Insert("insert into syslog(startTime, username, ip, url, endTime) values(#{startTime}, #{username}, #{ip} ,#{url}, #{endTime})")
    void save(SysLog sysLog);

    @Delete("delete from syslog where logId = #{id}")
    void del(Integer id);

    @Select("select * from syslog where username = #{username}")
    List<SysLog> findByUsername(String username);

    @Select("select * from syslog where url like '%${value}%'")
    List<SysLog> findByUrl(String str);

}
