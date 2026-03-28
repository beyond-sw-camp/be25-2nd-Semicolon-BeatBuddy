package com.beyond.beatbuddy.group.controller;

import com.beyond.beatbuddy.global.dto.CommonResponse;
import com.beyond.beatbuddy.group.dto.GroupCreateRequest;
import com.beyond.beatbuddy.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
@Validated
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "그룹명 중복 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "중복 확인 완료"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패")
    })
    @GetMapping("/check-name")
    public ResponseEntity<CommonResponse<Boolean>> checkGroupName(
            @RequestParam
            @NotBlank(message = "그룹명은 필수입니다.")
            @Size(max = 20, message = "그룹명은 최대 20자입니다.") String groupName) {
        boolean isDuplicate = groupService.checkGroupNameDuplicate(groupName);

        return ResponseEntity.ok(
                CommonResponse.success(200, "그룹명 중복 확인 완료", isDuplicate));
    }

    @Operation(summary = "그룹 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "그룹 생성 성공"),
            @ApiResponse(responseCode = "400", description = "중복된 그룹명 또는 초대코드")
    })
    @PostMapping
    public ResponseEntity<CommonResponse<Long>> createGroup(@Valid @RequestBody GroupCreateRequest request) {
        Long creatorId = 1L;  // 로그인 사용자로 변경

        Long groupId = groupService.createGroup(request, creatorId);

        URI location = URI.create(String.format("/api/v1/groups/%d", groupId));

        return ResponseEntity
                .created(location)
                .body(CommonResponse.success(HttpStatus.CREATED.value(), "그룹이 생성되었습니다.", groupId));
    }
}