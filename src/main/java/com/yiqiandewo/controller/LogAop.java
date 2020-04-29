package com.yiqiandewo.controller;

import com.yiqiandewo.domain.SysLog;
import com.yiqiandewo.service.ISysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

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
            String methodName = pcj.getSignature().getName(); //获取访问的方法的名称
            Object[] args = pcj.getArgs();//得到方法执行所需的参数
            if (args == null || args.length == 0) {
                method = clazz.getMethod(methodName);
            } else {
                Class[] classArgs = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    classArgs[i] = args[i].getClass();
                }
                method = clazz.getMethod(methodName, classArgs);
            }
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
