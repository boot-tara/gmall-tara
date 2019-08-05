package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.bean.BaseCatalog1;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface BaseCatalog1Mapper {
    List<BaseCatalog1> getBaseCatalog1();
}
