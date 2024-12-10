package com.readjoy.readjoyapi.common.dto.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.readjoy.readjoyapi.common.pojo.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


@Data
@Accessors(chain = true)
public class UpdateBookDTO {

    @Schema(description = "国际标准书号", example = "978-3-16-148410-0")
    @Length(max = 20, message = "ISBN长度必须在1到20之间")
    private String isbn;

    @Schema(description = "书名", example = "百年孤独")
    @Length(max = 30, message = "书名长度必须在1到30之间")
    private String title;

    @Schema(description = "分类ID", example = "1")
    @Min(value = 1, message = "分类ID必须大于0")
    private Integer categoryId;

    @Schema(description = "作者", example = "加西亚·马尔克斯")
    @Length(max = 30, message = "作者长度必须在1到30之间")
    private String author;

    @Schema(description = "书籍介绍", example = "魔幻现实主义文学的代表作")
    @Length(max = 100, message = "书籍介绍长度不能超过100")
    private String introduction;

    @Schema(description = "价格", example = "59.80")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private Double price;

    @Schema(description = "出版社", example = "南海出版公司")
    @Length(max = 20, message = "出版社长度必须在1到20之间")
    private String publisher;

    @Schema(description = "封面图片URL", example = "cover1.jpg")
    private MultipartFile coverImage = null;

    @Schema(description = "出版日期", example = "2024-01-01 00:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishionDate;


    public static Book toBook(UpdateBookDTO dto, String coverImageUrl) {
        return new Book()
                .setIsbn(dto.getIsbn())
                .setTitle(dto.getTitle())
                .setCategoryId(dto.getCategoryId())
                .setAuthor(dto.getAuthor())
                .setIntroduction(dto.getIntroduction())
                .setPrice(dto.getPrice())
                .setPublisher(dto.getPublisher())
                .setCoverImageUrl(coverImageUrl)
                .setPublishionDate(dto.getPublishionDate());
    }
}
