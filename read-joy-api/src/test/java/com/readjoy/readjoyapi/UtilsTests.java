
package com.readjoy.readjoyapi;

import com.readjoy.readjoyapi.common.utils.LocalFileUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
@Slf4j
class UtilsTests {

    @Resource
    private LocalFileUtil localFileUtil;

    @Test
    void testLocalFileUtil() {
        final String fileUrl = localFileUtil.saveFile(new MockMultipartFile("file", "test.txt", "text/plain", "Hello World!".getBytes()));
        log.info("下载地址: " + fileUrl);
        log.info("size: " + localFileUtil.getFileSize(fileUrl));
        log.info("size: " + LocalFileUtil.formatSize(localFileUtil.getFileSize(fileUrl)));
        final boolean deleted = localFileUtil.deleteFile(fileUrl);
        log.info("删除结果: " + deleted);
    }

}
