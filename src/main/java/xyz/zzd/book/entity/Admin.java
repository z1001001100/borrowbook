package xyz.zzd.book.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

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
//@Document(indexName = "java1",type = "admin",shards = 1,replicas = 0)
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
//    @Id
    private String username;

    private String password;

    private String role;

}
