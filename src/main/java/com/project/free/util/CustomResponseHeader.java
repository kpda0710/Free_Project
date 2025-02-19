package com.project.free.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponseHeader {

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
