package com.yiqiandewo.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SysLog implements Serializable {
    private Integer logId;
    private Date startTime;
    private String username;
    private String ip;
    private String url;
    private Date endTime;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
        Timestamp goodsC_date = Timestamp.valueOf(nowTime);//把时间转换
        this.startTime = goodsC_date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime);
        Timestamp goodsC_date = Timestamp.valueOf(nowTime);//把时间转换
        this.endTime = goodsC_date;
    }
}
