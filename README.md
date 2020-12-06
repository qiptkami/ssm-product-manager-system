# SSM_productManagerSystem

首次整合IDEA和GitHub

## 功能

后台管理系统

### 商品

- 查询所有商品信息

- 添加商品 完成后 跳转到查询所有

- 删除商品 完成后 跳转到查询所有

- 查询某个商品

### 订单

- 分页查询所有订单 同时要展示商品信息 mybatis的一对一

  - pageHelper

- 订单详情 根据订单id查询订单 展示订单信息、产品信息和购买人信息 多表联查

### 登录 没有注册 因为是管理系统

spring security

- 认证

- 授权

### 用户

- 查询所有

- 添加用户 给用户密码加密

### 管理

#### 超级管理 root

对所有的crud

#### 普通管理

对用户的crud

对商品的crud

对订单的crud

### 日志

基于Spring AOP