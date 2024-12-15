package com.readjoy.readjoyapi.common.mapper;

import com.readjoy.readjoyapi.common.pojo.Resource;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 13296
* @description 针对表【resource(资源表)】的数据库操作Mapper
* @createDate 2024-12-07 17:55:09
* @Entity com.readjoy.readjoyapi.common.pojo.Resource
*/
@Mapper
public interface ResourceMapper extends MPJBaseMapper<Resource> {


    /**
     * 更新下载次数
     */
    Integer incrementDownloadCount(@Param("url") String url,@Param("step") Integer step);
    /**
     * 更新喜爱啊次数
     */
    Integer incrementLikeCount(@Param("resourceId") Integer resourceId,@Param("step") Integer step);
}




