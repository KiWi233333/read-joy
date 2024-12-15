package com.readjoy.readjoyapi.common.utils;

import com.readjoy.readjoyapi.common.config.exception.BusinessException;
import lombok.Data;
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
    @Value("${file.download.auth.prefix}")
    private String fileDownloadAuthPrefix;
    public static final String DEFAULT_FILE_MIME_TYPE = "application/octet-stream";

    public static final Long MAX_FILE_SIZE = 100 * 1024 * 1024L;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final String rootPath = System.getProperty("user.dir").replace("\\", "/");

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
        return this.save(file, fileDownloadPrefix);
    }

    /**
     * 保存文件到自定义目录
     *
     * @param file 要保存的文件
     * @return 文件的完整路径
     */
    public String saveFile(MultipartFile file, String folder) {
        return this.save(file, "%s/%s".formatted(fileDownloadPrefix, folder));
    }

    public String saveAuthFile(MultipartFile file) {
        return this.save(file, fileDownloadAuthPrefix);
    }

    private String save(MultipartFile file, String prefix) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new BusinessException("文件名不能为空");
        }
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = new Date().getTime() + suffixName;
        // 指定上传到文件夹
        String saveFullPath = rootPath + "/%s/%s".formatted(prefix, fileName);
        // 保存文件
        try {
            Path path = Paths.get(saveFullPath);
            Files.createDirectories(path.getParent());
            file.transferTo(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "%s/%s".formatted(prefix, fileName); // files/xx
    }


    /**
     * 删除文件
     *
     * @param imgUrl 文件路径
     * @return 是否删除成功
     */
    public boolean deleteFile(String imgUrl) {
        return this.delete(imgUrl);
    }

    /**
     * 删除文件(auth)
     *
     * @param imgUrl 文件路径
     * @return 是否删除成功
     */
    public boolean deleteAuthFile(String imgUrl) {
        return this.delete(imgUrl);
    }

    private boolean delete(String url) {
        String fullPath = "%s/%s".formatted(rootPath, url);
        Path filePath = Path.of(fullPath); // 使用 Path 对象表示文件路径
        final boolean exists = Files.exists(filePath);
        log.warn("删除文件，路径: {} 是否存在: {}", fullPath, exists);
        if (exists) {
            try {
                Files.delete(filePath); // 使用 Files.delete 方法删除文件
                log.info("删除文件成功: {}", fullPath);
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
    public Long getFileSize(String fileUrl) throws IOException {
        String fullPath = "%s/%s".formatted(rootPath, fileUrl);
        Path filePath = Path.of(fullPath); // 使用 Path 对象表示文件路径
        return Files.size(filePath); // 使用 Files.size 方法获取文件大小
    }

    /**
     * 获取文件信息
     *
     * @param fileUrl 文件路径
     * @return 文件信息
     */
    public LocalFileInfo getFileInfo(String fileUrl) throws IOException {
        String fullPath = "%s/%s".formatted(rootPath, fileUrl);
        Path filePath = Path.of(fullPath); // 使用 Path 对象表示文件路径
        LocalFileInfo localFileInfo = new LocalFileInfo();
        localFileInfo.setFileName(filePath.getFileName().toString());
        localFileInfo.setFileType(Files.probeContentType(filePath)); // 使用 Files.probeContentType 方法获取文件类型
        localFileInfo.setFileSize(Files.size(filePath)); // 使用 Files.size 方法获取文件大小
        return localFileInfo;
    }

    @Data
    public static class LocalFileInfo {
        private String fileName;
        private String fileType;
        private Long fileSize;
    }
}
