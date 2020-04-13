package xyz.zzd.book.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Article implements Serializable {
    @TableId
    private Integer articleId;
    private String title;
    private String strings;
    private String articleImg;
}
