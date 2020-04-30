package com.yiqiandewo.controller;

import com.github.pagehelper.PageInfo;
import com.yiqiandewo.domain.User;
import com.yiqiandewo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/findAll")
    @Secured({"ROLE_ROOT","ROLE_USER"})
    public ModelAndView findAll(@RequestParam(name = "page", required = true, defaultValue = "1") Integer page, @RequestParam(name = "size", required = true, defaultValue = "5") Integer size) {
        ModelAndView mv = new ModelAndView();
        List<User> userList = userService.findAll(page, size);
        PageInfo pageInfo = new PageInfo(userList);
        mv.addObject("pageInfo", pageInfo);
        mv.setViewName("userList");
        return mv;
    }

    @RequestMapping("/save")
    public String saveProduct(User user) {
        userService.saveUser(user);
        return "redirect:/user/findAll";
    }

    @RequestMapping("/del/{userId}")
    public String delProduct(@PathVariable Integer userId) {
        userService.delUser(userId);
        return "forward:/user/findAll";
    }

    @RequestMapping("/del")
    public String delProducts(HttpServletRequest request) {

        String[] ids = request.getParameterValues("ids");
        for (String id : ids) {
            if (id != null) {
                userService.delUser(Integer.valueOf(id));
            }
        }
        return "forward:/user/findAll";
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam String str) {
        ModelAndView mv = new ModelAndView();
        if (str != null) {
            List<User> searchList = userService.findByStr(str);
            mv.addObject("searchList", searchList);
            mv.setViewName("/userSearchList");
        }

        return mv;
    }

    @RequestMapping("/findById/{userId}")
    public ModelAndView findById(@PathVariable Integer userId) {
        ModelAndView mv = new ModelAndView();
        User user = userService.findById(userId);
        mv.addObject("user", user);
        mv.setViewName("userUpdate");
        return mv;
    }

    @RequestMapping("/update")
    public String update(User user) {
        userService.updateUser(user);
        return "redirect:/user/findAll";
    }
}
