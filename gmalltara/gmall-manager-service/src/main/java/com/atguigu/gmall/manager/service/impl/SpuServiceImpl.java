package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SpuInfo;
import com.atguigu.gmall.bean.SpuSaleAttr;
import com.atguigu.gmall.bean.SpuSaleAttrValue;
import com.atguigu.gmall.manager.mapper.SpuInfoMapper;
import com.atguigu.gmall.manager.mapper.SpuSaleAttrMapper;
import com.atguigu.gmall.manager.mapper.SpuSaleAttrValueMapper;
import com.atguigu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    SpuInfoMapper spuInfoMapper;

    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Override
    public List<SpuInfo> selectAll(String catalog3Id) {
        return spuInfoMapper.selectAll(catalog3Id);
    }

    @Override
    @Transactional
    public void saveSpu(SpuInfo spuInfo) {
        //spuInfo表
        spuInfoMapper.saveSpu(spuInfo);
        //spuSaleAttr表
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for(SpuSaleAttr spuSaleAttr:spuSaleAttrList){
            spuSaleAttr.setSpuId(spuInfo.getId());
        }
        spuSaleAttrMapper.insert(spuSaleAttrList);
        //spuSaleAttrValue表
        for(SpuSaleAttr spuSaleAttr:spuSaleAttrList){
            List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
            for(SpuSaleAttrValue spuSaleAttrValue:spuSaleAttrValueList){
                spuSaleAttrValue.setSpuId(spuInfo.getId());
            }
            spuSaleAttrValueMapper.insert(spuSaleAttrValueList);
        }
    }

    @Override
    public List<SpuSaleAttr> getSaleAttrListBySpuId(String spuId) {
        return spuSaleAttrMapper.getSaleAttrListBySpuId(spuId);
    }

    //获得hash表
    @Override
    public List<SkuInfo> getSkuSaleAttrValueListBySpu(String spuId) {

        return spuSaleAttrValueMapper.getSkuSaleAttrValueListBySpu(spuId);
    }
    //销售属性以及销售属性值
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Map<String, String> stringStringHashMap) {
        return spuSaleAttrValueMapper.getSpuSaleAttrListCheckBySku(stringStringHashMap);
    }
}
