package com.hmlc.springboot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String bookname;
    private String author;
    private BigDecimal price;
    private String date;
    private Integer count;
    private LocalDateTime updateTime;
    private Integer isDel;
    private Integer isStar;
    private String type;
    private Integer hot;
    private String webUrl;
}
