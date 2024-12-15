package com.readjoy.readjoyapi.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.resource.InsertResourceDTO;
import com.readjoy.readjoyapi.common.dto.resource.SelectResourceDTO;
import com.readjoy.readjoyapi.common.dto.resource.UpdateResourceDTO;
import com.readjoy.readjoyapi.common.vo.resource.UserResourceVO;
import com.readjoy.readjoyapi.common.vo.resource.AdminResourceVO;

import java.util.List;

/**
 * 资源服务层
 *
 * @author 13296
 * @description 针对表【resource(资源表)】的数据库操作Service
 * @createDate 2024-12-07 17:55:09
 */
public interface ResourceService {

    /**
     * 获取资源分页列表
     *
     * @param dto 查询条件DTO
     * @return 资源分页列表
     */
    IPage<AdminResourceVO> getPageByDTO(SelectResourceDTO dto);

    /**
     * 获取资源详情
     *
     * @param id 资源ID
     * @return 资源详情
     */
    AdminResourceVO getResourceDetail(Integer id);

    /**
     * 新增资源
     *
     * @param dto 新增资源DTO
     * @return 新增资源的VO
     */
    Integer addResource(InsertResourceDTO dto);


    /**
     * 更新资源
     *
     * @param id 资源ID
     * @param dto 更新资源DTO
     * @return 更新数量
     * @description 更新资源信息
     */
    Integer updateResource(Integer id, UpdateResourceDTO dto);

    /**
     * 批量删除资源
     *
     * @param integers 资源ID数组
     * @return 删除数量
     */
    Integer batchDeleteResource(Integer[] integers);

    /**
     * 根据书籍ID获取资源列表
     *
     * @param bookId 书籍ID
     * @return 资源列表
     */
    List<UserResourceVO> getResourceListByBooKId(Integer bookId);

    /**
     * 增加资源下载次数
     *
     * @param url 资源URL
     * @return 是否成功
     */
    Integer incrementDownloadCount(String url);

    /**
     * 增加资源点赞次数
     *
     * @param resourceId 资源ID
     * @return 点赞数量
     */
    Integer incrementLikeCount(Integer resourceId);
}
