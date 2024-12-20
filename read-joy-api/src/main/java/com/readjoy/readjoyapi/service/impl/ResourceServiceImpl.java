package com.readjoy.readjoyapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.readjoy.readjoyapi.common.config.exception.BusinessException;
import com.readjoy.readjoyapi.common.dto.resource.InsertResourceDTO;
import com.readjoy.readjoyapi.common.dto.resource.SelectResourceDTO;
import com.readjoy.readjoyapi.common.dto.resource.UpdateResourceDTO;
import com.readjoy.readjoyapi.common.enums.BoolEnum;
import com.readjoy.readjoyapi.common.enums.ResultStatus;
import com.readjoy.readjoyapi.common.mapper.ResourceMapper;
import com.readjoy.readjoyapi.common.pojo.Admin;
import com.readjoy.readjoyapi.common.pojo.Book;
import com.readjoy.readjoyapi.common.pojo.Resource;
import com.readjoy.readjoyapi.common.utils.AssertUtil;
import com.readjoy.readjoyapi.common.utils.LocalFileUtil;
import com.readjoy.readjoyapi.common.utils.RequestHolderUtil;
import com.readjoy.readjoyapi.common.utils.UserTokenUtil;
import com.readjoy.readjoyapi.common.vo.resource.UserResourceVO;
import com.readjoy.readjoyapi.common.vo.resource.AdminResourceVO;
import com.readjoy.readjoyapi.repository.AdminRepository;
import com.readjoy.readjoyapi.repository.BookRepository;
import com.readjoy.readjoyapi.repository.ResourceRepository;
import com.readjoy.readjoyapi.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author 13296
 * @description 针对表【resource(资源表)】的数据库操作Service实现
 * @createDate 2024-12-07 17:55:09
 */
@Service
@Slf4j
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
    public IPage<AdminResourceVO> getPageByDTO(SelectResourceDTO dto) {
        return resourceRepository.getPageByDTO(dto);
    }

    /**
     * 获取资源详情
     *
     * @param id 资源ID
     * @return 资源详情
     */
    @Override
    public AdminResourceVO getResourceDetail(Integer id) {
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
        UserTokenUtil tokenUtil = RequestHolderUtil.get();
        final Admin admin = adminRepository.getById(tokenUtil.getId());
        AssertUtil.isNotEmpty(admin, "添加资源的用户不存在！");
        // 上传文件
        final String fileUrl = localFileUtil.saveAuthFile(dto.getResourceFile());
        AssertUtil.isNotEmpty(fileUrl, "上传文件失败，请稍后再试！");
        log.info("用户uid: {}, 上传图片, 文件路径: {}", tokenUtil.getId(), fileUrl);
        // 获取文件大小
        Long fileSize = null;
        String fileType = null;
        try {
            final LocalFileUtil.LocalFileInfo fileInfo = localFileUtil.getFileInfo(fileUrl);
            fileSize = fileInfo.getFileSize();
            // 获取文件类型
            fileType = fileInfo.getFileType();
        } catch (Exception e) {
            e.printStackTrace();
            // 上传文件失败，删除已上传的文件
            localFileUtil.deleteAuthFile(fileUrl);
            AssertUtil.fail("上传文件失败，请稍后再试！");
        }
        AssertUtil.isTrue(fileSize <= LocalFileUtil.MAX_FILE_SIZE, "上传文件大小不能超过" + LocalFileUtil.formatSize(LocalFileUtil.MAX_FILE_SIZE) + "！");
        final Resource resource = InsertResourceDTO.toResource(dto,
                fileSize,
                fileUrl,
                fileType == null ? LocalFileUtil.DEFAULT_FILE_MIME_TYPE : fileType,
                admin.getUsername());
        try {
            final boolean save = resourceRepository.save(resource);
            AssertUtil.isTrue(save, "新增资源失败！");
        } catch (Exception e) {
            e.printStackTrace();
            // 上传文件失败，删除已上传的文件
            localFileUtil.deleteAuthFile(fileUrl);
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
        String originalFilename = null;
        Long fileSize = null;
        String fileType = null;
        try {
            // 1、若有文件，则更新文件
            if (dto.getResourceFile() != null) {
                // 上传文件
                fileUrl = localFileUtil.saveAuthFile(dto.getResourceFile());
                AssertUtil.isNotEmpty(fileUrl, "上传文件失败，请稍后再试！");
                // 获取文件大小
                final LocalFileUtil.LocalFileInfo fileInfo = localFileUtil.getFileInfo(fileUrl);
                fileSize = fileInfo.getFileSize();
                // 获取文件类型
                fileType = fileInfo.getFileType() == null ? LocalFileUtil.DEFAULT_FILE_MIME_TYPE : fileInfo.getFileType();
                originalFilename = dto.getResourceFile().getOriginalFilename();
                AssertUtil.isTrue(fileSize <= LocalFileUtil.MAX_FILE_SIZE, "上传文件大小不能超过" + LocalFileUtil.formatSize(LocalFileUtil.MAX_FILE_SIZE) + "！");
            }
            // 2、更新资源信息
            Resource oldResource = resourceRepository.getById(id);
            final boolean update = resourceRepository.updateById(UpdateResourceDTO.toResource(
                            dto,
                            fileUrl,
                            fileType,
                            fileSize,
                            admin.getUsername())
                    .setTitle(StringUtils.isBlank(dto.getTitle()) ? originalFilename : dto.getTitle()) // 标题
                    .setResourceId(id)); // id
            AssertUtil.isTrue(update, "更新资源失败！");
            if (update && oldResource != null) {// 更新成功，删除原文件
                localFileUtil.deleteAuthFile(oldResource.getUrl());
            } else if (StringUtils.isNotBlank(fileUrl)) {// 更新失败，删除上传的文件
                localFileUtil.deleteAuthFile(fileUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 上传文件失败，删除已上传的文件
            if (fileUrl != null) {
                localFileUtil.deleteAuthFile(fileUrl);
            }
            throw new BusinessException(ResultStatus.UPDATE_ERR, "更新资源失败！");
        }
        return 1;
    }

    /**
     * 批量删除资源 (逻辑删除)
     *
     * @param ids 资源ID数组
     * @return 删除数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteResource(Integer[] ids) {
        // 查询资源是否存在
        final List<Resource> list = resourceRepository.list(new LambdaQueryWrapper<Resource>()
                .in(Resource::getResourceId, Arrays.asList(ids))
                .eq(Resource::getIsDeleted, BoolEnum.FALSE.getValue()));
        AssertUtil.isTrue(!list.isEmpty() && list.size() == ids.length, "部分资源不存在！");
        // 逻辑删除资源 (更新状态为1)
        final int updateCount = resourceRepository.getBaseMapper().update(new Resource().setIsDeleted(BoolEnum.TRUE.getValue()), new LambdaQueryWrapper<Resource>()
                .in(Resource::getResourceId, Arrays.asList(ids)));
        AssertUtil.isTrue(updateCount == ids.length, "部分资源删除失败！");
        // 删除文件 TODO: 待优化，使用异步删除
        //        int count = 0;
        //        for (Resource resource : list) {
        //            if (localFileUtil.deleteAuthFile(resource.getUrl())) {
        //                count++;
        //            }
        //        }
        //        log.info("删除文件文件数量：{}，成功数量：{}", ids, count);
        return updateCount;
    }


    /**
     * 根据书籍ID获取资源列表
     *
     * @param bookId 书籍ID
     * @return 资源列表
     */
    @Override
    public List<UserResourceVO> getResourceListByBooKId(Integer bookId) {
        // 查询资源列表
        return resourceRepository.selectJoinList(UserResourceVO.class, new MPJLambdaWrapper<Resource>()
                .selectAs(Resource::getTitle, UserResourceVO::getTitle)
                .selectAs(Resource::getResourceId, UserResourceVO::getResourceId)
                .selectAs(Resource::getUrl, UserResourceVO::getUrl)
                .selectAs(Resource::getType, UserResourceVO::getType)
                .selectAs(Resource::getSize, UserResourceVO::getSize)
                .selectAs(Resource::getLikeCount, UserResourceVO::getLikeCount)
                .selectAs(Resource::getDownloadCount, UserResourceVO::getDownloadCount)
                .selectAs(Resource::getCreateTime, UserResourceVO::getCreateTime)
                .eq(Resource::getBookId, bookId)
                .eq(Resource::getIsDeleted, BoolEnum.FALSE.getValue()) // 未删除
                .leftJoin(Book.class, Book::getBookId, Resource::getBookId));
    }

    /**
     * 增加资源下载次数
     *
     * @param url 资源URL
     * @return 是否成功
     */
    @Override
    public Integer incrementDownloadCount(String url) {
        return resourceRepository.getBaseMapper().incrementDownloadCount(url, 1);
    }

    /**
     * 增加资源点赞次数
     *
     * @param resourceId 资源ID
     * @return 点赞数量
     */
    @Override
    public Integer incrementLikeCount(Integer resourceId) {
        return resourceRepository.getBaseMapper().incrementLikeCount(resourceId, 1);
    }
}