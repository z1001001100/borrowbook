package xyz.zzd.book.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "r_id", type = IdType.AUTO)
    private Integer rId;

    private Integer rSid;

    private String rIsbn;

    private LocalDateTime borrowTime;

    private LocalDateTime returnTime;
//    续借
    private Integer isRenew;
//过期
    private Integer isOverdue;
//异常
    private Integer isAbnormal;
//归还
    private Integer isReturn;

    private String msg;
}
