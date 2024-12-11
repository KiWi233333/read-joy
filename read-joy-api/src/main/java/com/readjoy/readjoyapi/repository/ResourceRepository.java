package com.readjoy.readjoyapi.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.repository.JoinCrudRepository;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.readjoy.readjoyapi.common.dto.resource.SelectResourceDTO;
import com.readjoy.readjoyapi.common.mapper.ResourceMapper;
import com.readjoy.readjoyapi.common.pojo.Book;
import com.readjoy.readjoyapi.common.pojo.Resource;
import com.readjoy.readjoyapi.common.vo.resource.ResourceVO;
import org.springframework.stereotype.Component;

/**
 * @className: ResourceRepository
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 18:22
 */
@Component
public class ResourceRepository extends JoinCrudRepository<ResourceMapper, Resource> {

    public IPage<ResourceVO> getPageByDTO(SelectResourceDTO dto) {
        final MPJLambdaWrapper<Resource> qw = new MPJLambdaWrapper<Resource>()
                .selectAll(Resource.class)
                .selectAs(Book::getTitle, ResourceVO::getBookTitle) // 图书名称
                .and(StringUtils.isNotBlank(dto.getKeyword()), q -> q // 关键字
                        .or().like(Resource::getTitle, dto.getKeyword())
                        .or().like(Resource::getUrl, dto.getKeyword())
                )
                .eq(dto.getBookId() != null, Resource::getBookId, dto.getBookId())
                .leftJoin(Book.class, Book::getBookId, Resource::getBookId);
        // 排序
        if (dto.getSortType() != null && dto.getSortOrder() != null) {
            qw.orderBy(dto.checkIsSortByPubDate(), dto.checkAsc(), Resource::getCreateTime);
            qw.orderBy(dto.checkIsSortByDownloadCount(), dto.checkAsc(), Resource::getDownloadCount);
        }
        // 时间范围
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            qw.between(Resource::getCreateTime, dto.getStartDate(), dto.getEndDate());
        }
        return this.selectJoinListPage(dto.toPage(), ResourceVO.class, qw);
    }

    public ResourceVO getJoinOnVO(Integer id) {
        return this.selectJoinOne(ResourceVO.class, new MPJLambdaWrapper<Resource>()
                .selectAs(Book::getTitle, ResourceVO::getBookTitle) // 图书名称

                .selectAs(Resource::getResourceId, ResourceVO::getResourceId)
                .selectAs(Resource::getTitle, ResourceVO::getTitle)
                .selectAs(Resource::getUrl, ResourceVO::getUrl)
                .selectAs(Resource::getType, ResourceVO::getType)
                .selectAs(Resource::getSize, ResourceVO::getSize)
                .selectAs(Resource::getLikeCount, ResourceVO::getLikeCount)
                .selectAs(Resource::getDownloadCount, ResourceVO::getDownloadCount)
                .selectAs(Resource::getSubmitter, ResourceVO::getSubmitter)
                .selectAs(Resource::getIsDeleted, ResourceVO::getIsDeleted)
                .selectAs(Resource::getCreateTime, ResourceVO::getCreateTime)
                .eq(Resource::getResourceId, id) // 主键查询
                .leftJoin(Book.class, Book::getBookId, Resource::getBookId)
                .last("LIMIT 1")
        );
    }
}
