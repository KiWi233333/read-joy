<div align="center" style="margin: 40px auto;">
<img src="src/main/resources/static/favicon.ico" width="128"  height="128" alt="logo">
<h1 align="center" style="margin: 0">悦读时光</h1>
</div>

## 项目简介

悦读时光是一个基于Spring Boot开发的图书馆管理系统，主要功能模块包括：

- 图书模块：包括图书管理、分类管理等功能；
- 用户模块：包括用户管理、用户账号登录注册、身份认证授权等功能；
- 资源模块：包括资源管理、资源上传下载等功能，权限文件和公开文件；
- 评论模块：包括评论管理、评论点赞、审核等功能；
- 公共模块：异步上传下载文件、数据可视化接口等；
- AI模块：提供图书智能推荐、查询等功能；

## 技术栈

| 类型          | 库                                            | 版本号         | 功能描述                              |
|-------------|----------------------------------------------|-------------|-----------------------------------|
| **主框架**     | **Spring Boot**                              | **3.3.5**   | **Spring Boot核心依赖**               |
|             | spring-boot-starter-web                      |             | 提供Web应用支持，包括Tomcat和Spring MVC     |
|             | spring-boot-starter-validation               |             | 提供数据校验能力                          |
| **数据库与ORM** | **MyBatis Plus**                             | **3.5.9**   | **集成MyBatis Plus，增强的MyBatis功能**   |
|             | mybatis-plus-spring-boot3-starter            |             |                                   |
|             | mybatis-plus-jsqlparser                      |             | MyBatis Plus集成JSqlParser可增加扩展，分页等 |
|             | mybatis-plus-join-boot-starter               | 1.5.2       | 提供MyBatis Plus Join多表联查查询支持       |
|             | **MySQL**                                    |             | **MySQL数据库驱动**                    |
|             | mysql-connector-j                            | 8.0.29      |                                   |
| **缓存**      | **Redis**                                    | **3.3.5**   | **提供Redis缓存支持**                   |
|             | spring-boot-starter-data-redis               |             | 用于接口频控、数据缓存等                      |
| **安全**      | **JWT**                                      | **4.2.1**   | **提供JWT身份验证支持**                   |
|             | java-jwt                                     |             | 用户的身份认证和授权                        |
|             | **mica-xss**                                 | **2.7.6**   | **防止XSS攻击**                       |
| **工具类库**    | **Hutool**                                   | **5.8.25**  | **一个Java工具类库，提供各种工具类**            |
|             | hutool-all                                   |             |                                   |
|             | **Lombok**                                   | **1.18.24** | **简化Java对象封装的工具**                 |
|             | lombok                                       |             |                                   |
| **API文档**   | **Swagger**                                  | **4.4.0**   | **提供Swagger3 API文档支持**            |
|             | knife4j-openapi3-jakarta-spring-boot-starter |             |                                   |

## 运行环境

- JDK 17.0+
- MySQL 8.0+

## 运行前准备

- 克隆项目到本地
- 创建数据库，导入SQL脚本
- 修改application-dev.application 中的数据库、Redis配置信息
- 启动项目
