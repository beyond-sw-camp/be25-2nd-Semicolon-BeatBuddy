package com.beyond.beatbuddy.music.mapper;

import com.beyond.beatbuddy.music.entity.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<Users> findAll();
}
