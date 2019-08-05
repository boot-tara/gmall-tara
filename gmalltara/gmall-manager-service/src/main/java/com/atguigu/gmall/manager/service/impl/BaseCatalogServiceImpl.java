package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.BaseCatalog1;
import com.atguigu.gmall.bean.BaseCatalog2;
import com.atguigu.gmall.bean.BaseCatalog3;
import com.atguigu.gmall.manager.mapper.BaseCatalog1Mapper;
import com.atguigu.gmall.manager.mapper.BaseCatalog2Mapper;
import com.atguigu.gmall.manager.mapper.BaseCatalog3Mapper;
import com.atguigu.gmall.service.BaseCatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class BaseCatalogServiceImpl implements BaseCatalogService {

    @Autowired
    BaseCatalog1Mapper baseCatalog1Mapper;


    @Autowired
    BaseCatalog2Mapper baseCatalog2Mapper;

    @Autowired
    BaseCatalog3Mapper baseCatalog3Mapper;

    @Override
    public List<BaseCatalog1> getBaseCatalog1() {
        return baseCatalog1Mapper.getBaseCatalog1();
    }

    @Override
    public List<BaseCatalog2> getBaseCatalog2(String catalog1Id) {
        return baseCatalog2Mapper.getBaseCatalog2(catalog1Id);
    }

    @Override
    public List<BaseCatalog3> getBaseCatalog3(String catalog2Id) {
        return baseCatalog3Mapper.getBaseCatalog3(catalog2Id);
    }
}
