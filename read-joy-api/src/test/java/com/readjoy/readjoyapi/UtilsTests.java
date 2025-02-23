
package com.readjoy.readjoyapi;

import com.readjoy.readjoyapi.common.utils.LocalFileUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

@SpringBootTest
@Slf4j
class UtilsTests {

    @Resource
    private LocalFileUtil localFileUtil;

    @Test
    void testLocalFileUtil() throws IOException {
        final String fileUrl = localFileUtil.saveFile(new MockMultipartFile("file", "test.txt", "text/plain", "Hello World!".getBytes()));
        log.info("下载地址: " + fileUrl);
        log.info("size: " + localFileUtil.getFileSize(fileUrl));
        log.info("size: " + LocalFileUtil.formatSize(localFileUtil.getFileSize(fileUrl)));
        final boolean deleted = localFileUtil.deleteFile(fileUrl);
        log.info("删除结果: " + deleted);
    }
    @Test
    void testLocalAuthFileUtil() throws IOException {
        final String fileUrl = localFileUtil.saveAuthFile(new MockMultipartFile("file", "test.txt", "text/plain", "Hello World!".getBytes()));
        log.info("下载地址: " + fileUrl);
        log.info("size: " + localFileUtil.getFileSize(fileUrl));
        log.info("size: " + LocalFileUtil.formatSize(localFileUtil.getFileSize(fileUrl)));
        final boolean deleted = localFileUtil.deleteFile(fileUrl);
        log.info("删除结果: " + deleted);
    }

//    @Test
//    void testLocalFiles() throws IOException {
//////        final String fileUrl = localFileUtil.saveFile(new MockMultipartFile("file", "test.txt", "text/plain", "Hello World!".getBytes()));
////        String fullPath = "C:\\Users\\13296\\Desktop\\read-joy\\read-joy-api\\files\\1734165657436.xd";
////        log.info("下载地址: " + fullPath);
////        Path filePath = Path.of(fullPath); // 使用 Path 对象表示文件路径
////        final long size = Files.size(filePath);// 使用 Files.size 方法获取文件大小
////        log.info("size: " + size);
////        log.info("size: " + LocalFileUtil.formatSize(size));
//    }
}
