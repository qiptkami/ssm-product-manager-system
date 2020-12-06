package com.yiqiandewo.controller;

import com.yiqiandewo.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @RequestMapping("/changePassword")
    public ModelAndView changePassword(@RequestParam String username, @RequestParam String password, @RequestParam String newPassword) {
        ModelAndView mv = new ModelAndView();

        boolean flag = roleService.changePassword(username, password, newPassword);

        if (!flag) {
            //输入的密码不对
            mv.setViewName("/changePwdFail");
        } else {
            mv.setViewName("main");
        }
        return mv;
    }
}
