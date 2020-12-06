# ssm-product-manager-system

首次整合IDEA和GitHub

## 一、所用技术栈

基于spring + springmvc + mybatis的管理系统

使用spring aop 进行日志记录，视图页面使用jsp，spring security登录验证和权限控制，thymeleaf模板引擎渲染页面，mybatis框架简化数据库操作，使用pageHelper对结果进行分页显示



## 二、开发规范

个人项目，所以代码直接推送至主分支。



## 三、开发细节与遇到的问题

### 3.1、对ssm的整合

- 搭建spring

- 搭建springmvc，包括web.xml中配置前端控制器，中文乱码过滤器、然后springmvc.xml中配置视图解析器等。

- spring整合springmvc，就是在启动tomcat时，要加载spring的配置文件，就需要在web.xml中配置监听器，让监听器去监听servletContext域对象的创建和销毁

- spring对mybatis的整合，在`applicationContext.xml`中配置datasource，SqlSessionFactory工厂（生成dao的代理对象），dao所在包（生成该包下的代理对象）

      

### 3.2、时间格式的转化

```java
//从客户端获取的数据到服务器
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private Date orderTime;

public void setOrderTime(Date orderTime) {
    String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderTime);
    Timestamp goodsC_date = Timestamp.valueOf(nowTime);//把时间转换
    this.orderTime = goodsC_date;
}
```



### 3.3、mybatis模糊查询

两种方式：

```sql
@Select("select * from user where username like #{username}")
List<User> users = userDao.findByName("%xx%");
```

```java
@Select("select * from user where username like '%${value}%'")
List<User> users = userDao.findByName("xx");
```

优先使用#方式，因为#能够很大程度防止sql注入



### 3.4、mybatis一对一，一对多查询

使用的是注解的方式

#### 对一

- 首先是一个定单对应一个商品，对用过去就是一对一

    ```java
    @Select("select * from orders where productId = #{id}")
    @Results(id = "findOrdersByProductIdMap", value = {
            @Result(id = true, column = "orderId", property = "orderId"),
            @Result(column = "userId", property = "user", javaType = User.class, one = @One(select = "com.yiqiandewo.dao.IUserDao.findById", fetchType = FetchType.EAGER))
    })
    List<Order> findOrdersByProductId(Integer id);
    ```

    首先根据`select * from orders where productId = #{id}`查询到所有的order，但是此时user属性还没有封装，就需要进一步根据userId查询对应的user，查询所用语句就是one中的select

    而，property就是将one中的select语句查询的结果封装到user中，javaType就是user的java类型。

#### 对多

基于注解方式的对多和对一有点不一样，就是javaType是集合的类型

- 一个用户可以有多个定单

    ```java
    @Select("select userId, username from user where username = #{username}")
    @Results(id = "findOrdersByUsernameMap", value = {
        @Result(id = true, column = "userId", property = "userId"),
        @Result(column = "username", property = "username"),
        @Result(column = "userId", property = "orders", javaType = java.util.List.class, many = @Many(select="com.yiqiandewo.dao.IOrderDao.findOrdersByUserId", fetchType = FetchType.LAZY)),
    })
    //由用户名查找出所有该用户的订单
    User findOrdersByUsername(String username);
    ```

    

- 一个商品可以有多个定单

    ```java
    @Select("select * from product where productName = #{productName}")
    @Results(id="findOrdersByProductNameMap", value = {
        @Result(id=true, column = "productId", property = "productId"),
        @Result(column = "productId", property = "orders", javaType = java.util.List.class, many = @Many(select = "com.yiqiandewo.dao.IOrderDao.findOrdersByProductId", fetchType = FetchType.LAZY))
    })
    //由商品名称查找出所有购买该商品的订单
    Product findOrdersByProductName(String productName);
    ```

    

### 3.5、pageHelper分页查询

导入依赖

```xml
<!-- 分页 -->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.1.2</version>
</dependency>
```

在spring的配置文件中配置

```xml
<!-- SqlSessionFactory工厂 -->
<bean id="sqlSessionFactoryBean" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"></property>
    <!-- PageHelper分页插件 -->
    <property name="plugins">
        <array>
            <bean class="com.github.pagehelper.PageInterceptor">
                <property name="properties">
                    <props>
                        <prop key="helperDialect">mysql</prop>
                        <prop key="reasonable">true</prop>
                    </props>
                </property>
            </bean>
        </array>
    </property>
</bean>
```



Controller

```java
@RequestMapping("/findAll")
public ModelAndView findAll(@RequestParam(name = "page", required = true, defaultValue = "1") Integer page, @RequestParam(name = "size", required = true, defaultValue = "5") Integer size) {
    ModelAndView mv = new ModelAndView();
    List<User> userList = userService.findAll(page, size);
    PageInfo pageInfo = new PageInfo(userList);
    mv.addObject("pageInfo", pageInfo);
    mv.setViewName("userList");
    return mv;
}
```



在Service层中

```java
@Override
public List<User> findAll(int page, int size) {
    //参数page是页码数，size是每页显示条数
    PageHelper.startPage(page, size);
    return userDao.findAll();
}
```



页面中遍历分页显示

```jsp
<c:forEach items="${pageInfo.list}" var="user">
    <tr>
        <td><input name="ids" type="checkbox" value="${user.userId}"></td>
        <td>${user.userId}</td>
        <td>${user.name}</td>
        <td>${user.username}</td>
        <td>${user.qq}</td>
        <td>${user.email}</td>
        <td class="text-center">
            <button type="button" class="btn bg-olive btn-xs"
                    onclick="location.href = '${pageContext.request.contextPath}/user/findById/${user.userId}'">编辑</button>
            <button type="button" class="btn bg-olive btn-xs"
                    onclick="location.href = '${pageContext.request.contextPath}/user/del/${user.userId}'">删除</button>
        </td>
    </tr>
</c:forEach>
```



![image-20201206204331169](./image-20201206204331169.png)

```jsp
<div class="box-footer">
    <div class="pull-left">
        <div class="form-group form-inline">
            总共${pageInfo.pages} 页，共${pageInfo.total} 条数据。 每页
            <select class="form-control" id="changePageSize" onchange="changePageSize()">
                <option>5</option>
                <option>6</option>
                <option>7</option>
                <option>8</option>
                <option>9</option>
                <option>10</option>
            </select> 条
        </div>
    </div>

    <div class="box-tools pull-right">
        <ul class="pagination">
            <li>
                <a href="${pageContext.request.contextPath}/user/findAll?page=1&size=${pageInfo.pageSize}" aria-label="Previous">
                    首页
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/user/findAll?page=${pageInfo.pageNum-1}&size=${pageInfo.pageSize}">
                    上一页
                </a>
            </li>
            <c:forEach begin="1" end="${pageInfo.pages}" var="pageNum">
                <li><a href="${pageContext.request.contextPath}/user/findAll?page=${pageNum}&size=${pageInfo.pageSize}">${pageNum}</a></li>
            </c:forEach>

            <li>
                <a href="${pageContext.request.contextPath}/user/findAll?page=${pageInfo.pageNum+1}&size=${pageInfo.pageSize}">
                    下一页
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/user/findAll?page=${pageInfo.pages}&size=${pageInfo.pageSize}" aria-label="Next">
                    尾页
                </a>
            </li>
        </ul>
    </div>
</div>
```

```javascript
function changePageSize() {
    //获取下拉框的值
    var pageSize = $("#changePageSize").val();
    //向服务器发送请求，改变每页显示条数
    location.href = "${pageContext.request.contextPath}/user/findAll?page=1&size=" + pageSize;
}
```



### 3.6、spring aop日志记录

使用aop切面记录日志

```java
@Component("logAop")
@Aspect
public class LogAop {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ISysLogService sysLogService;

    private Class clazz; //访问的类
    private Method method;//访问的方法

    @Pointcut("execution(* com.yiqiandewo.controller.*.*(..)))")
    private void pt(){}

    @Around("pt()")
    public Object around(ProceedingJoinPoint pcj) {
        Date startTime = new Date();
        Object rtVal = null;

        try {
            clazz = pcj.getTarget().getClass(); //具体要访问的类
            MethodSignature methodSignature = (MethodSignature)pcj.getSignature();
            method = methodSignature.getMethod();

            rtVal = pcj.proceed();  //明确切入点方法的调用
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            Date endTime = new Date();
            String url = "";
            String ip = "";
            //获取url
            if (clazz != null && method != null && clazz != LogAop.class && clazz != SysLogController.class) {
                //1.获取类上的@RequestMapping
                RequestMapping classAnnotation = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                if (classAnnotation != null) {
                    String[] classValue = classAnnotation.value();
                    //2.获取方法上的@RequestMapping
                    RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                    if (methodAnnotation != null) {
                        String[] methodValue = methodAnnotation.value();
                        url = classValue[0] + methodValue[0];

                        //获取访问的ip
                        ip = getIpAddr(request);

                        //获取当前操作的用户
                        SecurityContext context = SecurityContextHolder.getContext();//从上下文中获了当前登录的用户
                        User user = (User) context.getAuthentication().getPrincipal();
                        String username = user.getUsername();

                        //将日志相关信息封装到SysLog对象
                        SysLog sysLog = new SysLog();
                        sysLog.setStartTime(startTime); //执行时长
                        sysLog.setIp(ip);
                        sysLog.setUrl(url);
                        sysLog.setUsername(username);
                        sysLog.setEndTime(endTime);

                        sysLogService.save(sysLog);

                    }
                }
            }
        }
        return rtVal;
    }

    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
```



### 3.7、spring security

#### 3.7.1、spring security xml配置

```xml
<security:global-method-security pre-post-annotations="enabled" secured-annotations="enabled"/>

<!-- 配置不拦截的资源 -->
<security:http pattern="/login.jsp" security="none"/>
<security:http pattern="/fail.jsp" security="none"/>
<security:http pattern="/css/**" security="none"/>
<security:http pattern="/img/**" security="none"/>
<security:http pattern="/plugins/**" security="none"/>
<!--
     配置具体的规则
     auto-config="true"	不用自己编写登录的页面，框架提供默认登录页面
     use-expressions="false"	是否使用SPEL表达式
    -->
<security:http auto-config="true" use-expressions="true">
    <!-- 配置具体的拦截的规则 pattern="请求路径的规则" access="只有配置的角色才能访问系统" -->
    <security:intercept-url pattern="/**" access="hasAnyRole('ROLE_ROOT','ROLE_GOOD','ROLE_LOG','ROLE_USER')"/>

    <!-- 定义跳转的具体的页面 -->
    <security:form-login
                         login-page="/login.jsp"
                         login-processing-url="/login"
                         default-target-url="/index.jsp"
                         authentication-failure-url="/fail.jsp"
                         authentication-success-forward-url="/pages/main.jsp"
                         />
    <!-- 关闭跨域请求 -->
    <security:csrf disabled="true"/>
    <!-- 退出 -->
    <security:logout invalidate-session="true" logout-url="/logout" logout-success-url="/login.jsp"/>

</security:http>

<!-- 切换成数据库中的用户名和密码 -->
<security:authentication-manager>
    <security:authentication-provider user-service-ref="roleService">
        <!--配置加密的方式
             <security:password-encoder ref="passwordEncoder"/>-->
    </security:authentication-provider>
</security:authentication-manager>

<!-- 配置加密类 -->
<bean id="passwordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder"/>
```

登录

```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Role role = roleDao.findByUsername(username);
    User user = new User(role.getUsername(), "{noop}"+role.getPassword(), getAuthority(role));
    return user;
}

public List<SimpleGrantedAuthority> getAuthority(Role role) {
    List<SimpleGrantedAuthority> list = new ArrayList<>();
    list.add(new SimpleGrantedAuthority(role.getType()));

    return list;
}
```



#### 3.7.2、spring security 注解权限控制与页面显示

##### controller

```java
@Secured({"ROLE_ROOT", "ROLE_LOG"}) //只有root 和 log权限才有可以访问
public ModelAndView findAll(@RequestParam(name = "page", required = true, defaultValue = "1") Integer page, @RequestParam(name = "size", required = true, defaultValue = "5") Integer size) {
    ModelAndView mv = new ModelAndView();
    List<SysLog> list = sysLogService.findAll(page, size);
    PageInfo pageInfo = new PageInfo(list);
    mv.addObject("pageInfo", pageInfo);
    mv.setViewName("/sysLogList");
    return mv;
}
```

##### 页面

```html
<li class="nav-item" sec:authorize="hasRole('root')">  <!-- 只有root权限才可以看到标签体内容 -->
    ...
</li>
```



## 四、最后

这是我第一次写项目，功能可能相对简单，但是开始写起来很吃力，一些基础功能可能想不明白，但是自己慢慢磨，越到后面越轻松，经过这个项目，自己对于这些技术的使用也相对熟练。
