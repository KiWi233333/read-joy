package com.readjoy.readjoyapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.readjoy.readjoyapi.common.config.exception.BusinessException;
import com.readjoy.readjoyapi.common.dto.resource.InsertResourceDTO;
import com.readjoy.readjoyapi.common.dto.resource.SelectResourceDTO;
import com.readjoy.readjoyapi.common.dto.resource.UpdateResourceDTO;
import com.readjoy.readjoyapi.common.enums.BoolEnum;
import com.readjoy.readjoyapi.common.enums.ResultStatus;
import com.readjoy.readjoyapi.common.mapper.ResourceMapper;
import com.readjoy.readjoyapi.common.pojo.Admin;
import com.readjoy.readjoyapi.common.pojo.Resource;
import com.readjoy.readjoyapi.common.utils.AssertUtil;
import com.readjoy.readjoyapi.common.utils.LocalFileUtil;
import com.readjoy.readjoyapi.common.utils.RequestHolderUtil;
import com.readjoy.readjoyapi.common.vo.resource.ResourceVO;
import com.readjoy.readjoyapi.repository.AdminRepository;
import com.readjoy.readjoyapi.repository.BookRepository;
import com.readjoy.readjoyapi.repository.ResourceRepository;
import com.readjoy.readjoyapi.service.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * @author 13296
 * @description 针对表【resource(资源表)】的数据库操作Service实现
 * @createDate 2024-12-07 17:55:09
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @jakarta.annotation.Resource
    private ResourceRepository resourceRepository;
    @jakarta.annotation.Resource
    private BookRepository bookRepository;
    @jakarta.annotation.Resource
    private AdminRepository adminRepository;
    @jakarta.annotation.Resource
    private LocalFileUtil localFileUtil;

    /**
     * 获取资源分页列表
     *
     * @param dto 查询条件DTO
     * @return 资源分页列表
     */
    @Override
    public IPage<ResourceVO> getPageByDTO(SelectResourceDTO dto) {
        return resourceRepository.getPageByDTO(dto);
    }

    /**
     * 获取资源详情
     *
     * @param id 资源ID
     * @return 资源详情
     */
    @Override
    public ResourceVO getResourceDetail(Integer id) {
        return resourceRepository.getJoinOnVO(id);
    }

    /**
     * 新增资源
     *
     * @param dto 新增资源DTO
     * @return 新增资源的VO
     */
    @Override
    public Integer addResource(InsertResourceDTO dto) {
        // 查询是否存在图书
        AssertUtil.isNotEmpty(bookRepository.getById(dto.getBookId()), "添加失败，资源图书不存在！");
        // 获取当前用户信息
        final Admin admin = adminRepository.getById(RequestHolderUtil.get().getId());
        AssertUtil.isNotEmpty(admin, "添加资源的用户不存在！");
        // 上传文件
        final String fileUrl = localFileUtil.saveFile(dto.getResourceFile());
        // 获取文件大小
        final Long fileSize = localFileUtil.getFileSize(fileUrl);
        AssertUtil.isTrue(fileSize <= LocalFileUtil.MAX_FILE_SIZE, "上传文件大小不能超过" + LocalFileUtil.formatSize(LocalFileUtil.MAX_FILE_SIZE) + "！");
        final Resource resource = InsertResourceDTO.toResource(dto, fileSize, fileUrl, admin.getUsername());
        try {
            final boolean save = resourceRepository.save(resource);
            AssertUtil.isTrue(save, "新增资源失败！");
        } catch (Exception e) {
            e.printStackTrace();
            // 上传文件失败，删除已上传的文件
            localFileUtil.deleteFile(fileUrl);
            throw new BusinessException(ResultStatus.INSERT_ERR, "新增资源失败！");
        }
        return 1;
    }

    /**
     * 更新资源
     *
     * @param id  资源ID
     * @param dto 更新资源DTO
     * @return 更新数量
     * @description 更新资源信息
     */
    @Override
    public Integer updateResource(Integer id, UpdateResourceDTO dto) {
        // 查询是否存在图书
        if (dto.getBookId() != null) {
            AssertUtil.isNotEmpty(bookRepository.getById(dto.getBookId()), "更新失败，资源图书不存在！");
        }
        // 获取当前用户信息
        final Admin admin = adminRepository.getById(RequestHolderUtil.get().getId());
        AssertUtil.isNotEmpty(admin, "更新资源的用户不存在！");
        // 更新资源信息
        String fileUrl = null;
        Long fileSize = null;
        try {
            // 更新文件
            if (dto.getResourceFile() != null) {
                // 上传文件
                fileUrl = localFileUtil.saveFile(dto.getResourceFile());
                // 获取文件大小
                fileSize = localFileUtil.getFileSize(fileUrl);
                AssertUtil.isTrue(fileSize <= LocalFileUtil.MAX_FILE_SIZE, "上传文件大小不能超过" + LocalFileUtil.formatSize(LocalFileUtil.MAX_FILE_SIZE) + "！");
            }
            final boolean update = resourceRepository.updateById(UpdateResourceDTO.toResource(dto, fileUrl, fileSize, admin.getUsername()));
            AssertUtil.isTrue(update, "更新资源失败！");
            if (update) {// 更新成功，删除原文件
                Resource resource = resourceRepository.getById(id);
                if (resource != null) {
                    localFileUtil.deleteFile(resource.getUrl());
                }
            } else {// 更新失败，删除上传的文件
                if (fileUrl != null) {
                    localFileUtil.deleteFile(fileUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 上传文件失败，删除已上传的文件
            if (fileUrl != null) {
                localFileUtil.deleteFile(fileUrl);
            }
            throw new BusinessException(ResultStatus.UPDATE_ERR, "更新资源失败！");
        }
        return 1;
    }

    /**
     * 批量删除资源 (逻辑删除)
     *
     * @param integers 资源ID数组
     * @return 删除数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteResource(Integer[] integers) {
        // 获取当前用户信息
        final Admin admin = adminRepository.getById(RequestHolderUtil.get().getId());
        AssertUtil.isNotEmpty(admin, "删除资源的用户不存在！");
        // 逻辑删除资源 (更新状态为1)
        final int updateCount = resourceRepository.getBaseMapper().update(new LambdaQueryWrapper<Resource>()
                .setEntity(new Resource().setIsDeleted(BoolEnum.TRUE.getValue()))
                .in(Resource::getResourceId, Arrays.asList(integers)));
        AssertUtil.isTrue(updateCount == integers.length, "部分资源删除失败！");
        return updateCount;
    }
}