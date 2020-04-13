package xyz.zzd.book.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzd
 * @since 2020-03-25
 */

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Integer sId;

    private String sName;

    private String sPassword;

    private Integer sNewIntegral;

    private Integer sTotalIntegral;

    private String sPhone;

    private String sMail;
}
