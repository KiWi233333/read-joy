package com.readjoy.readjoyapi.common.config.interceptor;


import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.readjoy.readjoyapi.common.enums.ResultStatus;
import com.readjoy.readjoyapi.common.enums.SysUserTypeEnum;
import com.readjoy.readjoyapi.common.utils.*;
import com.readjoy.readjoyapi.service.ResourceService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 身份验证拦截器
 *
 * @className: TokenInterceptor
 * @author: Kiwi23333
 * @description: token验证拦截器
 * @date: 2023/4/29 1:47
 */
@Slf4j
@Component
@CrossOrigin
public class AuthInterceptor implements HandlerInterceptor {

    public static final String[] IGNORE_URL = {
            "/book/*"
    };

    @Value("${file.download.auth.prefix}")
    private String fileDownloadAuthPrefix;

    @Resource
    private ResourceService resourceService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return checkToken(request, response, handler);
    }

    private boolean checkToken(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        //拦截器取到请求先进行判断，如果是OPTIONS请求，则放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            // 支持跨域
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods",
                    "GET,POST,PUT,DELETE,OPTIONS");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,accept,Origin,Authorization,Access-Control-Request-Method,Access-Control-Request-Headers");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            return true;
        }
        response.setContentType("application/json;charset=UTF-8");
        // 1、获取token
        String token = request.getHeader(UserTokenUtil.HEADER_NAME);
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(UserTokenUtil.HEADER_NAME);
        }
        String url = request.getRequestURI();
        // 正则判断是否是 /book/* 开头的接口，如果是，则放行
        if (token == null && request.getMethod().equals("GET") && url.startsWith("/book/")) {
            return true;
        }
        if (StringUtils.isBlank(token)) { // token 为空
            response.getWriter().write(JacksonUtil.toJSON(Result.fail("验证错误，您还未登录！")));
            response.sendError(401, "token不能为空!");
            return false;
        }
        // 2、获取token内容
        try {
            final UserTokenUtil userTokenDTO = JWTUtil.getTokenInfoByToken(token);
            if (userTokenDTO == null) {
                response.getWriter().write(JacksonUtil.toJSON(Result.fail("验证错误，您还未登录！")));
                response.sendError(401, "token不能为空!");
                return false;
            }
            // 3、头部存放信息
            RequestHolderUtil.set(userTokenDTO);
            // 4、下载文件权限验证和计数
            checkAuthFileDownload(url);
            // 判断 用户类型 /admin需要管理员权限 /user需要普通用户权限
            final String requestURI = request.getRequestURI();
            log.info("当前用户 uid:{}, userType:{}, 请求接口：{}", userTokenDTO.getId(), userTokenDTO.getUserType(), requestURI);
            if (requestURI.startsWith("/res") || requestURI.startsWith("/" + fileDownloadAuthPrefix)) {// 资源接口不需要验证角色
                return true;
            }
            if (requestURI.startsWith("/admin") && !SysUserTypeEnum.isAdmin(userTokenDTO.getUserType())) {
                response.getWriter().write(JacksonUtil.toJSON(Result.fail("您没有权限访问该接口！")));
                response.sendError(403, "您没有权限访问该接口！");
                return false;
            }
            if (!requestURI.startsWith("/admin") && !SysUserTypeEnum.isUser(userTokenDTO.getUserType())) {
                response.getWriter().write(JacksonUtil.toJSON(Result.fail("您没有权限访问该接口！")));
                response.sendError(403, "您没有权限访问该接口！");
                return false;
            }
        } catch (TokenExpiredException e) {
            response.getWriter().write(JacksonUtil.toJSON(Result.fail(ResultStatus.TOKEN_EXPIRED_ERR)));
            return false;
        } catch (JWTDecodeException | IOException e) {
            response.getWriter().write(JacksonUtil.toJSON(Result.fail(ResultStatus.TOKEN_ERR, "身份校验失败，请重新登陆！")));
            return false;
        }
        return true;
    }

    /**
     * 下载文件权限验证和计数
     *
     * @param url 请求url
     */
    private void checkAuthFileDownload(String url) {
        String resultUrl = url.substring(1);
        if (resultUrl.startsWith(fileDownloadAuthPrefix)) {
            log.info("当前用户下载文件并自增下载次数：{}", resultUrl);
            resourceService.incrementDownloadCount(resultUrl); // 文件 自增下载次数
        }
    }
}
