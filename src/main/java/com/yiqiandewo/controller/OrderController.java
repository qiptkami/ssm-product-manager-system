package com.yiqiandewo.controller;

import com.github.pagehelper.PageInfo;
import com.yiqiandewo.domain.Order;
import com.yiqiandewo.domain.Product;
import com.yiqiandewo.domain.User;
import com.yiqiandewo.service.IOrderService;
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
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping("/findAll")
    @Secured({"ROLE_ROOT", "ROLE_GOOD"})
    public ModelAndView findAll(@RequestParam(name = "page", required = true, defaultValue = "1") Integer page, @RequestParam(name = "size", required = true, defaultValue = "5") Integer size) {
        ModelAndView mv = new ModelAndView();
        List<Order> list = orderService.findAll(page, size);
        PageInfo pageInfo = new PageInfo(list);
        mv.addObject("pageInfo", pageInfo);
        mv.setViewName("orderList");
        return mv;
    }

    @RequestMapping("/del/{orderId}")
    public String delProduct(@PathVariable Integer orderId) {
        orderService.del(orderId);
        return "forward:/order/findAll";
    }

    @RequestMapping("/del")
    public String delProducts(HttpServletRequest request) {

        String[] ids = request.getParameterValues("ids");
        for (String id : ids) {
            if (id != null) {
                orderService.del(Integer.valueOf(id));
            }
        }
        return "forward:/order/findAll";
    }

    @RequestMapping("/findById/{orderId}")
    public ModelAndView findById(@PathVariable Integer orderId) {
        ModelAndView mv = new ModelAndView();
        Order order = orderService.findById(orderId);
        mv.addObject("order", order);
        mv.setViewName("orderDetail");
        return mv;
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam String str) {
        ModelAndView mv = new ModelAndView();
        if (str != null) {
            User user = orderService.findByUsername(str);
            Product product = orderService.findByProductName(str);
            if (user != null) {
                mv.addObject("user", user);
                mv.setViewName("/orderSearchListUsername");
            } else if (product != null){
                mv.addObject("product", product);
                mv.setViewName("/orderSearchListProductName");
            }

        }

        return mv;
    }
}
