package com.whitepoor.login.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MyException extends RuntimeException {

    private int code;
    private String message;

    public MyException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    public MyException(Code code) {
        this(code.getCode(),code.getMsg());
    }
}
