package com.beyond.beatbuddy.group.controller;

import com.beyond.beatbuddy.global.dto.CommonResponse;
import com.beyond.beatbuddy.group.dto.GroupCreateRequest;
import com.beyond.beatbuddy.group.dto.GroupResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
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
            @ApiResponse(responseCode = "200", description = "중복 여부 확인 성공"),
            @ApiResponse(responseCode = "400", description = "groupName 파라미터 누락 또는 20자 초과"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/name-check")
    public ResponseEntity<CommonResponse<Boolean>> checkGroupName(
            @RequestParam
            @NotBlank(message = "그룹명은 필수입니다.")
            @Size(max = 20, message = "그룹명은 최대 20자입니다.") String groupName) {
        boolean isDuplicate = groupService.checkGroupNameDuplicate(groupName);

        return ResponseEntity.ok(
                CommonResponse.success(200, "중복 여부 확인 성공", isDuplicate));
    }

    @Operation(summary = "초대코드 중복 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "중복 여부 확인 성공"),
            @ApiResponse(responseCode = "400", description = "inviteCode 파라미터 누락 또는 형식 오류"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/invite-code-check")
    public ResponseEntity<CommonResponse<Boolean>> checkInviteCode(
            @RequestParam
            @NotBlank(message = "초대코드는 필수입니다.") String inviteCode) {
        boolean isDuplicate = groupService.checkInviteCodeDuplicate(inviteCode);

        return ResponseEntity.ok(
                CommonResponse.success(200, "중복 여부 확인 성공", isDuplicate));
    }

    @Operation(summary = "그룹 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "그룹 생성 성공"),
            @ApiResponse(responseCode = "400", description = "필수 파라미터 누락, 길이 초과 등 유효성 오류"),
            @ApiResponse(responseCode = "409", description = "이미 사용 중인 그룹명 또는 초대 코드"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
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

    @Operation(summary = "초대코드로 그룹 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "그룹 조회 성공"),
            @ApiResponse(responseCode = "400", description = "초대코드 형식이 올바르지 않음"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 초대코드"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/invite/{inviteCode}")
    public ResponseEntity<CommonResponse<GroupResponse>> getGroupByInviteCode(
            @PathVariable
            @NotBlank(message = "초대코드는 필수 입력값입니다.") String inviteCode) {
        GroupResponse response = groupService.getGroupByInviteCode(inviteCode);

        return ResponseEntity.ok(CommonResponse.success(200, "그룹 조회 성공", response));
    }

    @Operation(summary = "그룹 내 닉네임 중복 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "중복 여부 확인 성공"),
            @ApiResponse(responseCode = "400", description = "nickname 파라미터 누락 또는 20자 초과"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 그룹"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{groupId}/nickname-check")
    public ResponseEntity<CommonResponse<Boolean>> checkNickname(
            @PathVariable Long groupId,
            @RequestParam
            @NotBlank(message = "닉네임은 필수입니다.")
            @Size(max = 20, message = "닉네임은 최대 20자입니다.") String nickname) {

        boolean isDuplicate = groupService.checkNicknameDuplicate(groupId, nickname);

        return ResponseEntity.ok(
                CommonResponse.success(200, "중복 여부 확인 성공", isDuplicate));
    }
}