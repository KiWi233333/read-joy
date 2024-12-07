package com.readjoy.readjoyapi.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.readjoy.readjoyapi.common.pojo.Resource;
import com.readjoy.readjoyapi.common.service.ResourceService;
import com.readjoy.readjoyapi.common.mapper.ResourceMapper;
import org.springframework.stereotype.Service;

/**
* @author 13296
* @description 针对表【resource(资源表)】的数据库操作Service实现
* @createDate 2024-12-07 17:55:09
*/
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
    implements ResourceService{

}




