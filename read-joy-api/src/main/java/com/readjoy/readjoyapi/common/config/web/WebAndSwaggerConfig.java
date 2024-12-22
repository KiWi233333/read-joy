package com.readjoy.readjoyapi.common.config.web;

import com.readjoy.readjoyapi.common.config.interceptor.AuthInterceptor;
import com.readjoy.readjoyapi.common.config.interceptor.PortFlowInterceptor;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.util.UrlPathHelper;


@Slf4j
@Configuration
@EnableWebMvc
public class WebAndSwaggerConfig implements WebMvcConfigurer { // 覆写addResourceHandlers跨域

    @Value("${file.download.prefix}")
    private String prefixPath;
    @Value("${file.download.auth.prefix}")
    private String prefixAuthPath;

    @Value("${spring.profiles.active}")
    private String activeProfile;
    // 拦截器
    @Resource
    AuthInterceptor authInterceptor;
    @Resource
    private PortFlowInterceptor portFlowInterceptor;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setUrlDecode(false); // 设置不对路径进行解码
        urlPathHelper.setAlwaysUseFullPath(false); // 设置总使用完整路径
        configurer.setUrlPathHelper(urlPathHelper);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resUrl = "file:%s/%s/".formatted(System.getProperty("user.dir").replace("\\", "/"), prefixPath);
        String resAuthUrl = "file:%s/%s/".formatted(System.getProperty("user.dir").replace("\\", "/"), prefixAuthPath);
        log.info("文件下载路径将在目录: " + resUrl);
        registry.addResourceHandler("/" + prefixPath + "/**").addResourceLocations(resUrl);
        registry.addResourceHandler("/" + prefixAuthPath + "/**").addResourceLocations(resAuthUrl);
        registry.addResourceHandler("/static/**").addResourceLocations(
                "classpath:/static/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("悦读时光")
                        .contact(new Contact())
                        .description("悦读时光 's API文档")
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Kiwi2333组")
                );
    }

    // 跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有接口
                .allowCredentials(true) // 是否发送 Cookie
                .allowedOriginPatterns("*") // 支持域
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 支持方法
                .allowedHeaders("*")
                .exposedHeaders("*");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1、身份验证拦截器
        registry.addInterceptor(authInterceptor) // 注册拦截器
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/static/**",
                        "/swagger-resources/**",
                        "/swagger-ui.html/**",
                        "/doc.html/**", // swagger
                        // 管理员
                        "/admin/login",
                        // 用户
                        "/user/login",
                        "/user/register",
                        // 访客
                        "/*" + prefixPath + "/*",
                        // "/book/*",
                        "/book/category/**"
                );
        // 2、端口流量拦截器
        registry.addInterceptor(portFlowInterceptor); // 注册拦截器
    }

}
