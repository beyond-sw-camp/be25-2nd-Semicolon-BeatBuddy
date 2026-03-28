package com.beyond.beatbuddy.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> CommonResponse<T> error(int code, String message) {

        return new CommonResponse<>(code, message, null);
    }

    public static <T> CommonResponse<T> success(int code, String message, T data) {

        return new CommonResponse<>(code, message, data);
    }
}