package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.bean.SpuSaleAttr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SpuSaleAttrMapper {
    void insert(@Param("spuSaleAttrList") List<SpuSaleAttr> spuSaleAttrList);

    List<SpuSaleAttr> getSaleAttrListBySpuId(@Param("spuId") String spuId);
}
