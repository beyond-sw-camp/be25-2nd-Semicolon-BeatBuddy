package com.beyond.beatbuddy.global.security;

import com.beyond.beatbuddy.global.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 권한 없는 요청(본인 소유 아닌 리소스 접근) 시 403 응답 반환
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message("해당 리소스에 접근할 권한이 없습니다.")
                .result(null)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
