package com.project.free.util;

import com.project.free.exception.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CustomResponse<T> {

    private CustomResponseHeader header;
    private T data;
    private String message;

    public static <T> CustomResponse<T> success(ResponseCode responseCode, T data) {
        return new CustomResponse<T>(new CustomResponseHeader(HttpStatus.OK, responseCode.getCode(), "SUCCESS"), data, responseCode.getMessage());
    }

    public static <T> CustomResponse<T> fail(ResponseCode responseCode, T data) {
        return new CustomResponse<T>(new CustomResponseHeader(HttpStatus.BAD_REQUEST, responseCode.getCode(), "FAIL"), data, responseCode.getMessage());
    }
}
