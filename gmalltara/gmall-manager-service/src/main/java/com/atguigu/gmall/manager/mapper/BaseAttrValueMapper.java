package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.bean.BaseAttrValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseAttrValueMapper {
    void insert(@Param("attrValueList")List<BaseAttrValue> attrValueList);
}
