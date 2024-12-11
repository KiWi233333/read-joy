package com.readjoy.readjoyapi.common.config.web;

import com.readjoy.readjoyapi.common.config.interceptor.AuthInterceptor;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


@Slf4j
@Configuration
@EnableWebMvc
public class WebAndSwaggerConfig implements WebMvcConfigurer { // 覆写addResourceHandlers跨域

    @Value("${file.download.prefix}")
    private String prefixPath;

    @Value("${spring.profiles.active}")
    private String activeProfile;
    // 拦截器
    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resUrl = "file:" + System.getProperty("user.dir").replace("\\", "/") + "/files/";
        log.info("文件下载路径将在目录: " + resUrl);
        registry.addResourceHandler("/files/*").addResourceLocations(resUrl);
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
                        "/swagger-resources/**",
                        "/swagger-ui.html/**",
                        "/doc.html/**", // swagger
                        // 管理员
                        "/admin/login",
                        // 用户
                        "/user/login",
                        "/user/register",
                        // 访客
                        prefixPath + "/*",
                        "/book/*",
                        "/book/category/**",
                        "/book/resource/**"
                );
    }

}
