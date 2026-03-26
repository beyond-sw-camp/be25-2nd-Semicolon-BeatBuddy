package com.beyond.beatbuddy.global.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {
	/*
		@Value가 뭐냐면
		application.yml에 있는 값을 자바 코드에서 꺼내 쓰는 거
		profileUploadDir = "./uploads/profiles"
		groupUploadDir = "./uploads/groups"
		default-profile이랑 default-group 사진 필요함
	*/
	@Value("${file.upload.profile}")
	private String profileUploadDir;

	@Value("${file.upload.group}")
	private String groupUploadDir;

	@Value("${file.default-profile}")
	private String defaultProfileImage;

	@Value("${file.default-group}")
	private String defaultGroupImage;

	// 서버 시작할 때 폴더 없으면 자동 생성
	@PostConstruct
	public void init() {
		new File(profileUploadDir).mkdirs();
		new File(groupUploadDir).mkdirs();
	}

	// 프로필 사진 저장
	public String saveProfileImage(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return defaultProfileImage;  // 기본 이미지 반환
		}
		return save(file, profileUploadDir, "/images/profiles/");
	}

	// 그룹 사진 저장
	public String saveGroupImage(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return defaultGroupImage;  // 기본 이미지 반환
		}
		return save(file, groupUploadDir, "/images/groups/");
	}

	// 파일 저장 공통 로직
	private String save(MultipartFile file, String uploadDir, String urlPrefix) {
		// 확장자 추출
		String originalFilename = file.getOriginalFilename();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

		// UUID로 파일명 중복 방지
		String savedFilename = UUID.randomUUID() + extension;
		String filePath = uploadDir + "/" + savedFilename;

		try {
			file.transferTo(new File(filePath));
		} catch (IOException e) {
			throw new RuntimeException("파일 저장 실패", e);
		}

		// 접근 URL 반환
		return urlPrefix + savedFilename;
	}
}