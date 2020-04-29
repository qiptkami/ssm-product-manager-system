package com.yiqiandewo.service;

import com.yiqiandewo.domain.SysLog;

import java.util.List;

public interface ISysLogService {

    List<SysLog> findAll(int page, int size);

    void save(SysLog sysLog);
}
