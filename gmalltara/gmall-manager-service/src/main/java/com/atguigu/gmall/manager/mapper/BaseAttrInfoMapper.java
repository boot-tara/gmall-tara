package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.bean.BaseAttrInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseAttrInfoMapper {
    List<BaseAttrInfo> getAttrList(@Param("catalog3Id") String catalog3Id);

    void insert(BaseAttrInfo baseAttrInfo);

    BaseAttrInfo getAttrInfoById(@Param("id")String id);

    List<BaseAttrInfo> getAttrListByCtg3Id(@Param("catalog3Id") String catalog3Id);
}
