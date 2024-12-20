package com.readjoy.readjoyapi.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.repository.JoinCrudRepository;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.readjoy.readjoyapi.common.dto.resource.SelectResourceDTO;
import com.readjoy.readjoyapi.common.mapper.ResourceMapper;
import com.readjoy.readjoyapi.common.pojo.Book;
import com.readjoy.readjoyapi.common.pojo.Resource;
import com.readjoy.readjoyapi.common.vo.other.AdminHomeStatisticsVO;
import com.readjoy.readjoyapi.common.vo.resource.AdminResourceVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @className: ResourceRepository
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 18:22
 */
@Component
public class ResourceRepository extends JoinCrudRepository<ResourceMapper, Resource> {

    public IPage<AdminResourceVO> getPageByDTO(SelectResourceDTO dto) {
        final MPJLambdaWrapper<Resource> qw = new MPJLambdaWrapper<Resource>()
                .select(Resource::getResourceId,
                        Resource::getBookId,
                        Resource::getTitle,
                        Resource::getUrl,
                        Resource::getType,
                        Resource::getSize,
                        Resource::getLikeCount,
                        Resource::getDownloadCount,
                        Resource::getSubmitter,
                        Resource::getIsDeleted,
                        Resource::getCreateTime)
                .selectAs(Book::getTitle, AdminResourceVO::getBookTitle) // 图书名称
                .and(StringUtils.isNotBlank(dto.getKeyword()), q -> q // 关键字
                        .or().like(Resource::getTitle, dto.getKeyword())
                        .or().like(Resource::getUrl, dto.getKeyword())
                )
                .eq(dto.getIsDeleted() != null, Resource::getIsDeleted, dto.getIsDeleted()) // 是否删除 排序
                .eq(dto.getBookId() != null, Resource::getBookId, dto.getBookId())
                .leftJoin(Book.class, Book::getBookId, Resource::getBookId);
        // 排序
        if (dto.getSortType() != null && dto.getSortOrder() != null) {
            qw.orderBy(dto.checkIsSortByPubDate(), dto.checkAsc(), Resource::getCreateTime);
            qw.orderBy(dto.checkIsSortByDownloadCount(), dto.checkAsc(), Resource::getDownloadCount);
            qw.orderBy(dto.checkIsSortByLikeCount(), dto.checkAsc(), Resource::getLikeCount);
        }
        // 时间范围
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            qw.between(Resource::getCreateTime, dto.getStartDate(), dto.getEndDate());
        }
        return this.selectJoinListPage(dto.toPage(), AdminResourceVO.class, qw);
    }

    public AdminResourceVO getJoinOnVO(Integer id) {
        return this.selectJoinOne(AdminResourceVO.class, new MPJLambdaWrapper<Resource>()
                .selectAs(Book::getTitle, AdminResourceVO::getBookTitle) // 图书名称

                .selectAs(Resource::getResourceId, AdminResourceVO::getResourceId)
                .selectAs(Resource::getBookId, AdminResourceVO::getBookId)
                .selectAs(Resource::getTitle, AdminResourceVO::getTitle)
                .selectAs(Resource::getUrl, AdminResourceVO::getUrl)
                .selectAs(Resource::getType, AdminResourceVO::getType)
                .selectAs(Resource::getSize, AdminResourceVO::getSize)
                .selectAs(Resource::getLikeCount, AdminResourceVO::getLikeCount)
                .selectAs(Resource::getDownloadCount, AdminResourceVO::getDownloadCount)
                .selectAs(Resource::getSubmitter, AdminResourceVO::getSubmitter)
                .selectAs(Resource::getIsDeleted, AdminResourceVO::getIsDeleted)
                .selectAs(Resource::getCreateTime, AdminResourceVO::getCreateTime)
                .eq(Resource::getResourceId, id) // 主键查询
                .leftJoin(Book.class, Book::getBookId, Resource::getBookId)
                .last("LIMIT 1")
        );
    }

    public Long sumDownloadCount() {
        return this.list(new LambdaQueryWrapper<Resource>()
                .select(Resource::getResourceId, Resource::getDownloadCount)).stream().mapToLong(Resource::getDownloadCount).sum();
    }

}
