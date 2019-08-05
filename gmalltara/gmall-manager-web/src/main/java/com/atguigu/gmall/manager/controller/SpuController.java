package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseSaleAttr;
import com.atguigu.gmall.bean.SpuInfo;
import com.atguigu.gmall.bean.SpuSaleAttr;
import com.atguigu.gmall.service.BaseSaleAttrService;
import com.atguigu.gmall.service.SpuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpuController {

    @Reference
    SpuService spuService;

    @Reference
    BaseSaleAttrService baseSaleAttrService;

    @RequestMapping("/spuList")
    public List<SpuInfo> getSpuList(String catalog3Id){
        return spuService.selectAll(catalog3Id);
    }

    @RequestMapping("baseSaleAttrList")
    public List<BaseSaleAttr> getBaseSaleAttr(){
        return baseSaleAttrService.selectAll();
    }

    //添加spu的保存功能
    @RequestMapping("saveSpu")
    public String saveSpu(SpuInfo spuInfo){
         spuService.saveSpu(spuInfo);
         return "success";
    }

    //获得销售属性
    @RequestMapping("/getSaleAttrListBySpuId")
    @ResponseBody
    public List<SpuSaleAttr>  getSaleAttrListBySpuId(String spuId){
        return spuService.getSaleAttrListBySpuId(spuId);
    }
}
