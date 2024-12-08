package com.readjoy.readjoyapi.common.repository;

import com.github.yulichang.repository.JoinCrudRepository;
import com.readjoy.readjoyapi.common.mapper.ResourceMapper;
import com.readjoy.readjoyapi.common.pojo.Resource;
import org.springframework.stereotype.Component;

/**
 * @className: ResourceRepository
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 18:22
 */
@Component
public class ResourceRepository extends JoinCrudRepository<ResourceMapper, Resource> {

}
