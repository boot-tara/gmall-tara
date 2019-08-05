package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseCatalog1;
import com.atguigu.gmall.bean.BaseCatalog2;
import com.atguigu.gmall.bean.BaseCatalog3;
import com.atguigu.gmall.service.BaseCatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CatalogController {
    @Reference
    BaseCatalogService baseCatalogService;

    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<BaseCatalog1> getBaseCatalog1(){
        return baseCatalogService.getBaseCatalog1();
    }

    @RequestMapping("getCatalog2")
    @ResponseBody
    public List<BaseCatalog2> getBaseCatalog2(String catalog1Id){
        return baseCatalogService.getBaseCatalog2(catalog1Id);
    }

    @RequestMapping("getCatalog3")
    @ResponseBody
    public List<BaseCatalog3> getBaseCatalog3(String catalog2Id){
        return baseCatalogService.getBaseCatalog3(catalog2Id);
    }

}
