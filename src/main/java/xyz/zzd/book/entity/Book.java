package xyz.zzd.book.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zzd
 * @since 2020-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@Document(indexName = "java", type = "books",shards = 1,replicas = 0)   // 指定索引跟类型
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
//    @Id
    private String isbn;
//    @Field(index = true,type = FieldType.Keyword)
//    @JsonAlias("bName")
    private String bName;
//    @Field(index = true,type = FieldType.Keyword)
    private String bAuthor;
//出版社
//    @Field(index = true,type = FieldType.Keyword)
    private String bPublishingHouse;
//简介
//    @Field(index = true,type = FieldType.Keyword)
    private String bSummary;
//图片地址
//    @Field(index = true,type = FieldType.Keyword)
    private String bImgPath;
//图书库存
//    @Field(index = true,type = FieldType.Keyword)
    private Integer bReserve;
//图书总库存
//    @Field(index = true,type = FieldType.Keyword)
    private Integer bTotal;
//图书所在书库
//    @Field(index = false,type = FieldType.Keyword)
    private String location;
//图书索书号
//    @Field(index = false,type = FieldType.Keyword)
    private String callNumber;
}
