package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.bean.BaseCatalog3;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface BaseCatalog3Mapper {
    List<BaseCatalog3> getBaseCatalog3(@Param("catalog2Id") String catalog2Id);
}
