package xyz.zzd.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.zzd.book.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}

