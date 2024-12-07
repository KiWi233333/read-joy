package com.readjoy.readjoyapi.common.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.readjoy.readjoyapi.common.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13296
* @description 针对表【admin(管理员表)】的数据库操作Mapper
* @createDate 2024-12-07 17:55:09
* @Entity com.readjoy.readjoyapi.common.pojo.Admin
*/
@Mapper
public interface AdminMapper extends MPJBaseMapper<Admin> {

}




