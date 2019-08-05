package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.bean.SkuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SkuInfoMapper {
    SkuInfo selectFromDb(@Param("skuId")String skuId);
}
