package com.yiqiandewo.controller;

import com.github.pagehelper.PageInfo;
import com.yiqiandewo.domain.Product;
import com.yiqiandewo.service.IProductService;
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
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    //分页
    @RequestMapping("/findAll")
    @Secured({"ROLE_ROOT", "ROLE_GOOD"})
    public ModelAndView findAll(@RequestParam(name = "page", required = true, defaultValue = "1") Integer page, @RequestParam(name = "size", required = true, defaultValue = "5") Integer size) {
        ModelAndView mv = new ModelAndView();
        List<Product> allProducts = productService.findAll(page, size);
        PageInfo pageInfo = new PageInfo(allProducts);
        mv.addObject("pageInfo", pageInfo);
        mv.setViewName("/productList");
        return mv;
    }

    @RequestMapping("/save")
    public String saveProduct(Product product) {
        productService.saveProduct(product);
        return "redirect:/product/findAll";
    }

    @RequestMapping("/del/{productId}")
    public String delProduct(@PathVariable Integer productId) {
        productService.delProduct(productId);
        return "forward:/product/findAll";
    }

    @RequestMapping("/del")
    public String delProducts(HttpServletRequest request) {

        String[] ids = request.getParameterValues("ids");
        for (String id : ids) {
            if (id != null) {
                productService.delProduct(Integer.valueOf(id));
            }
        }
        return "forward:/product/findAll";
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam String str) {
        ModelAndView mv = new ModelAndView();
        if (str != null) {
            List<Product> searchList = productService.findByStr(str);
            mv.addObject("searchList", searchList);
            mv.setViewName("/productSearchList");
        }

        return mv;
    }

    @RequestMapping("/findById/{productId}")
    public ModelAndView findById(@PathVariable Integer productId) {
        ModelAndView mv = new ModelAndView();
        Product product = productService.findById(productId);
        mv.addObject("product", product);
        mv.setViewName("productUpdate");
        return mv;
    }

    @RequestMapping("/update")
    public String update(Product product) {
        productService.updateProduct(product);
        return "redirect:/product/findAll";
    }

}
