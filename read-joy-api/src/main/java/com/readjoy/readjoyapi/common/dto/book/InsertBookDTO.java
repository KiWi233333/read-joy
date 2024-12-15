package com.readjoy.readjoyapi.common.dto.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.readjoy.readjoyapi.common.pojo.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
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
public class InsertBookDTO {

    @Schema(description = "国际标准书号", example = "978-3-16-148410-0")
    @NotNull(message = "ISBN不能为空！")
    @Length(min = 1, max = 20, message = "ISBN长度必须在1到20之间！")
    private String isbn;

    @Schema(description = "书名", example = "百年孤独")
    @NotNull(message = "书名不能为空！")
    @Length(min = 1, max = 100, message = "书名长度必须在1到100之间！")
    private String title;

    @Schema(description = "分类ID", example = "1")
    @NotNull(message = "分类ID不能为空！")
    @Min(value = 1, message = "分类ID必须大于0")
    private Integer categoryId;

    @Schema(description = "作者", example = "加西亚·马尔克斯")
    @NotNull(message = "作者不能为空！")
    @Length(min = 1, max = 100, message = "作者长度必须在1到100之间！")
    private String author;

    @Schema(description = "书籍介绍", example = "魔幻现实主义文学的代表作")
    @NotNull(message = "书籍介绍不能为空！")
    @Length(max = 1000, message = "书籍介绍长度不超过1000字！")
    private String introduction;

    @Schema(description = "价格", example = "59.80")
    @NotNull(message = "价格不能为空！")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    @DecimalMax(value = "9999999.99", message = "价格必须小于9999999.99！")
    private Double price;

    @Schema(description = "出版社", example = "南海出版公司")
    @NotNull(message = "出版社不能为空！")
    @Length(min = 1, max = 50, message = "出版社长度必须在1到50之间！")
    private String publisher;

    @Schema(description = "封面图片", example = "cover1.jpg")
    @NotNull(message = "封面图片不能为空！")
    private MultipartFile coverImage = null;

    @Schema(description = "出版日期", example = "2024-01-01 00:00:00")
    @NotNull(message = "出版日期不能为空！")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishionDate;


    public static Book toBook(InsertBookDTO dto, String coverImageUrl) {
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
