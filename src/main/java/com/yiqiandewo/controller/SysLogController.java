package com.yiqiandewo.controller;

import com.github.pagehelper.PageInfo;
import com.yiqiandewo.domain.SysLog;
import com.yiqiandewo.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/sysLog")
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService;

    @RequestMapping("/findAll")
    @Secured({"ROLE_ROOT", "ROLE_LOG"})
    public ModelAndView findAll(@RequestParam(name = "page", required = true, defaultValue = "1") Integer page, @RequestParam(name = "size", required = true, defaultValue = "5") Integer size) {
        ModelAndView mv = new ModelAndView();
        List<SysLog> list = sysLogService.findAll(page, size);
        PageInfo pageInfo = new PageInfo(list);
        mv.addObject("pageInfo", pageInfo);
        mv.setViewName("/sysLogList");
        return mv;
    }
}
