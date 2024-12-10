package com.readjoy.readjoyapi.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 书籍表
 * @TableName book
 */
@TableName(value ="book")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Book implements Serializable {
    /**
     * 书籍ID
     */
    @TableId
    private Integer bookId;

    /**
     * 国际标准书号
     */
    private String isbn;

    /**
     * 书名
     */
    private String title;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 作者
     */
    private String author;

    /**
     * 书籍介绍
     */
    private String introduction;

    /**
     * 价格
     */
    private Double price;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 封面图片URL
     */
    private String coverImageUrl;

    /**
     * 出版日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date publishionDate;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}