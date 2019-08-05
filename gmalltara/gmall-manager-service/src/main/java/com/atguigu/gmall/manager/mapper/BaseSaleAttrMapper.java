package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.bean.BaseSaleAttr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface BaseSaleAttrMapper {
    List<BaseSaleAttr> selectAll();
}
