package com.yiqiandewo.service.impl;

import com.github.pagehelper.PageHelper;
import com.yiqiandewo.dao.ISysLogDao;
import com.yiqiandewo.domain.SysLog;
import com.yiqiandewo.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sysLogService")
public class SysLogServiceImpl implements ISysLogService {

    @Autowired
    private ISysLogDao sysLogDao;

    @Override
    public List<SysLog> findAll(int page, int size) {
        PageHelper.startPage(page, size);
        return sysLogDao.findAll();
    }

    @Override
    public void save(SysLog sysLog) {
        sysLogDao.save(sysLog);
    }

    @Override
    public void del(Integer logId) {
        sysLogDao.del(logId);
    }

    @Override
    public List<SysLog> findByStr(String str) {
        List<SysLog> list = sysLogDao.findByUsername(str);
        List<SysLog> list1 = sysLogDao.findByUrl(str);
        list.addAll(list1);
        return list;
    }


}
