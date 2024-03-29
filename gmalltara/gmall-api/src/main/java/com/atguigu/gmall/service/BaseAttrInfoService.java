package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.BaseAttrInfo;

import java.util.List;

public interface BaseAttrInfoService {
    List<BaseAttrInfo> getAttrList(String catalog3Id);

    void saveAttr(BaseAttrInfo baseAttrInfo);

    BaseAttrInfo getAttrInfoById(String id);

    List<BaseAttrInfo> getAttrListByCtg3Id(String catalog3Id);
}
