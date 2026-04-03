package com.beyond.beatbuddy.music.mapper;

import com.beyond.beatbuddy.music.dto.request.SaveTasteRequest;
import com.beyond.beatbuddy.music.dto.response.TrackAnalysisResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MusicMapper {
	void insertAlbumIgnore(@Param("albumId") String albumId,
						   @Param("albumName") String albumName,
						   @Param("coverUrl") String coverUrl);

	void insertMusicFeaturesIgnore(@Param("track") SaveTasteRequest.TrackInfo track,
								   @Param("features") TrackAnalysisResponse features);

	void deleteUserFavMusic(@Param("userId") Long userId);

	void insertUserFavMusic(@Param("userId") Long userId,
							@Param("musicId") String musicId);

	void upsertUserProfile(@Param("userId") Long userId,
						   @Param("tasteVector") String tasteVector);

	void updateIsTasteAnalyzed(@Param("userId") Long userId);
}