package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.bean.SpuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SpuInfoMapper {
    List<SpuInfo> selectAll(@Param("catalog3Id")String catalog3Id);

    void saveSpu(SpuInfo spuInfo);
}
