package com.beyond.beatbuddy.music.mapper;

import com.beyond.beatbuddy.music.entity.Album;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlbumMapper {

    void insertAlbum(Album album);
}
