package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseAttrValue;
import com.atguigu.gmall.manager.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manager.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.service.BaseAttrInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class BaseAttrInfoServiceImpl implements BaseAttrInfoService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getAttrList(String catalog3Id) {
        return baseAttrInfoMapper.getAttrList(catalog3Id);
    }

    @Override
    @Transactional
    public void saveAttr(BaseAttrInfo baseAttrInfo) {
        //插入baseattrInfo
        baseAttrInfoMapper.insert(baseAttrInfo);
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for(BaseAttrValue baseAttrValue:attrValueList){
            baseAttrValue.setAttrId(baseAttrInfo.getId());
        }
        System.out.println(attrValueList);
        baseAttrValueMapper.insert(attrValueList);
    }

    @Override
    public BaseAttrInfo getAttrInfoById(String id) {

        return baseAttrInfoMapper.getAttrInfoById(id);
    }

    @Override
    public List<BaseAttrInfo> getAttrListByCtg3Id(String catalog3Id) {
        return baseAttrInfoMapper.getAttrListByCtg3Id(catalog3Id);
    }
}
