package com.readjoy.readjoyapi.common.utils;

import com.readjoy.readjoyapi.common.config.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
    public static final String DEFAULT_FILE_MIME_TYPE = "application/octet-stream";

    public static final Long MAX_FILE_SIZE = 100 * 1024 * 1024L;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private String rootPath;

    @PostConstruct
    public void init() {
        this.rootPath = System.getProperty("user.dir");
    }

    /**
     * 格式化大小
     *
     * @param size 字节数
     * @return 格式化后的字符串
     */
    public static String formatSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2fKB", (double) size / 1024);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2fMB", (double) size / (1024 * 1024));
        } else {
            return String.format("%.2fGB", (double) size / (1024 * 1024 * 1024));
        }
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
        final UserTokenUtil tokenUtil = RequestHolderUtil.get();
        if (tokenUtil != null) {
            log.info("用户uid: {}, 上传图片, 文件路径: {}", tokenUtil.getId(), saveFullPath);
        }
        // 保存文件
        try {
            Path path = Paths.get(saveFullPath);
            Files.createDirectories(path.getParent());
            file.transferTo(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "%s/%s".formatted(fileDownloadPrefix.replaceFirst("/", ""), fileName); // files/xx
    }



    public String saveAuthFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String prePath = rootPath + "auth/";
        if (originalFilename == null) {
            throw new BusinessException("文件名不能为空");
        }
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = new Date().getTime() + suffixName;
        // 指定上传到文件夹
        String saveFullPath = prePath + "%s/%s".formatted(fileDownloadPrefix, fileName);
        final UserTokenUtil tokenUtil = RequestHolderUtil.get();
        if (tokenUtil != null) {
            log.info("用户uid: {}, 上传图片, 文件路径: {}", tokenUtil.getId(), saveFullPath);
        }
        // 保存文件
        try {
            Path path = Paths.get(saveFullPath);
            Files.createDirectories(path.getParent());
            file.transferTo(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "%s/%s".formatted(fileDownloadPrefix.replaceFirst("/", ""), fileName); // files/xx
    }


    /**
     * 删除文件
     *
     * @param imgUrl 文件路径
     * @return 是否删除成功
     */
    public boolean deleteFile(String imgUrl) {
        String fullPath = rootPath + "%s/%s".formatted(fileDownloadPrefix, imgUrl);
        Path filePath = Path.of(fullPath); // 使用 Path 对象表示文件路径
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath); // 使用 Files.delete 方法删除文件
                return true;
            } catch (IOException e) {
                e.printStackTrace(); // 打印异常堆栈跟踪
                log.error("删除文件失败: {}", fullPath);
                return false; // 处理异常时返回 false
            }
        }
        return false;
    }

    /**
     * 获取文件大小
     *
     * @param fileUrl 文件路径
     * @return 文件大小，单位：字节
     */
    public Long getFileSize(String fileUrl) {
        String fullPath = rootPath + "%s/%s".formatted(fileDownloadPrefix, fileUrl);
        Path filePath = Path.of(fullPath); // 使用 Path 对象表示文件路径
        try {
            return Files.size(filePath); // 使用 Files.size 方法获取文件大小
        } catch (IOException e) {
            log.warn("获取文件大小失败: {}", fullPath);
            e.printStackTrace(); // 打印异常堆栈跟踪
            return null; // 处理异常时返回 0
        }
    }

    /**
     * 获取文件类型
     *
     * @param fileUrl 文件路径
     * @return 文件类型
     */
    public String getFileType(String fileUrl) {
        String fullPath = rootPath + "%s/%s".formatted(fileDownloadPrefix, fileUrl);
        Path filePath = Path.of(fullPath); // 使用 Path 对象表示文件路径
        try {
            return Files.probeContentType(filePath); // 使用 Files.probeContentType 方法获取文件类型
        } catch (IOException e) {
            log.warn("获取文件类型失败: {}", fullPath);
            e.printStackTrace(); // 打印异常堆栈跟踪
            return null; // 处理异常时返回 null
        }
    }
}
