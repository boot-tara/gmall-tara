package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.service.BaseAttrInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AttrController {

    @Reference
    BaseAttrInfoService baseAttrInfoService;

    @RequestMapping("getAttrList")
    public List<BaseAttrInfo> getAttrList(String catalog3Id){
        return baseAttrInfoService.getAttrList(catalog3Id);
    }

    @ResponseBody
    @RequestMapping("saveAttr")
    public String saveAttr(BaseAttrInfo baseAttrInfo){
        baseAttrInfoService.saveAttr(baseAttrInfo);
        return "success";
    }

    @GetMapping("/getAttrInfoById/{id}")
    public BaseAttrInfo getAttrInfoById(@PathVariable("id")String id){
        return baseAttrInfoService.getAttrInfoById(id);
    }

    @ResponseBody
    @RequestMapping("/getAttrListByCtg3Id")
    public List<BaseAttrInfo> getAttrListByCtg3Id(String catalog3Id){
        return baseAttrInfoService.getAttrListByCtg3Id(catalog3Id);
    }
}
