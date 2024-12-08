package com.readjoy.readjoyapi.common.vo.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 图书展示VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class BookVO {

    @Schema(description = "书籍ID", example = "1")
    private Integer bookId;

    @Schema(description = "国际标准书号", example = "978-3-16-148410-0")
    private String isbn;

    @Schema(description = "书名", example = "Spring实战")
    private String title;

    @Schema(description = "分类ID", example = "100")
    private Integer categoryId;

    @Schema(description = "分类名称", example = "编程")
    private String categoryName;

    @Schema(description = "作者", example = "Craig Walls")
    private String author;

    @Schema(description = "书籍介绍", example = "这是一本关于Spring框架的实用指南。")
    private String introduction;

    @Schema(description = "价格", example = "59.99")
    private Double price;

    @Schema(description = "出版社", example = "人民邮电出版社")
    private String publisher;

    @Schema(description = "封面图片URL", example = "http://example.com/cover.jpg")
    private String coverImageUrl;

    @Schema(description = "出版日期", example = "2019-04-01")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishionDate;

}
