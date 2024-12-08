package com.readjoy.readjoyapi.common.utils;

import com.readjoy.readjoyapi.common.config.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * 文件下载工具类
 */
@Component
@Slf4j
public class LocalFileUtil {

    @Value("${file.download.prefix}")
    private String fileDownloadPrefix;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private String rootPath;

    @PostConstruct
    public void init() {
        String rootPath = "";
        if (activeProfile.equals("dev")) {
            rootPath = System.getProperty("user.dir");
        }else if (activeProfile.equals("prod")) { // 生产环境待修改
            rootPath = System.getProperty("user.dir");
        }
        this.rootPath = rootPath;
    }

    /**
     * 保存文件到指定目录
     *
     * @param file 要保存的文件
     * @return 文件的完整路径
     */
    public String saveFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException("文件名不能为空");
        }
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = new Date().getTime() + suffixName;
        // 指定上传到文件夹
        String saveFullPath = rootPath + "%s/%s".formatted(fileDownloadPrefix, fileName);
        log.info("用户uid: {}, 上传图片, 文件路径: {}", RequestHolderUtil.get().getId(), saveFullPath);
        // 保存文件
        try {
            Path path = Paths.get(saveFullPath);
            Files.createDirectories(path.getParent());
            file.transferTo(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "%s/%s".formatted(fileDownloadPrefix, fileName); // files/xx
    }
}
