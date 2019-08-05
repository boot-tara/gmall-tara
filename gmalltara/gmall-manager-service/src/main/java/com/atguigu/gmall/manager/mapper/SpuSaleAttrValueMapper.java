package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SpuSaleAttr;
import com.atguigu.gmall.bean.SpuSaleAttrValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SpuSaleAttrValueMapper {
    void insert(@Param("spuSaleAttrValueList") List<SpuSaleAttrValue> spuSaleAttrValueList);

    List<SkuInfo> getSkuSaleAttrValueListBySpu(@Param("spuId") String spuId);

    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Map<String, String> stringStringHashMap);
}
