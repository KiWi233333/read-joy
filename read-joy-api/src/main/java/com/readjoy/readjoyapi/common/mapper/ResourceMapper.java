package com.readjoy.readjoyapi.common.mapper;

import com.readjoy.readjoyapi.common.pojo.Resource;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13296
* @description 针对表【resource(资源表)】的数据库操作Mapper
* @createDate 2024-12-07 17:55:09
* @Entity com.readjoy.readjoyapi.common.pojo.Resource
*/
@Mapper
public interface ResourceMapper extends MPJBaseMapper<Resource> {

}




