package com.atguigu.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SkuSaleAttrValue;
import com.atguigu.gmall.bean.SpuSaleAttr;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;


    @RequestMapping("/{skuId}.html")
    public String item(@PathVariable("skuId")String skuId,Map map){
        //根据skuId查到sku     先查缓存，在查数据库
        SkuInfo skuInfo=skuService.getSkuInfoBySkuId(skuId);
        map.put("skuInfo",skuInfo);
        String spuId = skuInfo.getSpuId();
        //查找改spu下面的所有的商品
        List<SkuInfo>infos=spuService.getSkuSaleAttrValueListBySpu(spuId);
        System.out.println(infos);
        //将该商品集合改成hashmap表
        HashMap<String, String> stringStringHashMap1 = new HashMap<>();
        for(SkuInfo info:infos){
            String v=info.getId();
            List<SkuSaleAttrValue> skuSaleAttrValueList = info.getSkuSaleAttrValueList();
            String k="";
            for(SkuSaleAttrValue skuSaleAttrValue:skuSaleAttrValueList){
                k=k+"|"+skuSaleAttrValue.getSaleAttrValueId();
            }
            stringStringHashMap1.put(k,v);
        }
        String skuJson = JSON.toJSONString(stringStringHashMap1);
        map.put("skuJson",skuJson);

        //销售属性列表，自己的价格check属性
        Map<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("spuId",spuId);
        stringStringHashMap.put("skuId",skuId);
        List<SpuSaleAttr>saleAttrListBySpuId=spuService.getSpuSaleAttrListCheckBySku(stringStringHashMap);
        map.put("spuSaleAttrListCheckBySku",saleAttrListBySpuId);
        return "item";
    }
}
