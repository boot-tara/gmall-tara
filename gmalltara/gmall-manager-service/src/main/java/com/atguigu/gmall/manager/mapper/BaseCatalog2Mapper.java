package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.bean.BaseCatalog2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface BaseCatalog2Mapper {
    List<BaseCatalog2> getBaseCatalog2(@Param("catalog1Id") String catalog1Id);
}
