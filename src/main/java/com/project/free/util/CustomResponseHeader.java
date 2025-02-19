package com.project.free.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponseHeader {

    @JsonIgnore
    private HttpStatus httpStatus;
    private String code;
    private String message;
}
