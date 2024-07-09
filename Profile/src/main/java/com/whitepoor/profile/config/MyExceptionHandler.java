package com.whitepoor.profile.config;

import com.whitepoor.profile.pojo.GlobalResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public GlobalResponse<String> exceptionHandler(MyException e){
        System.out.println("User Defined Exception ！Reason is :"+e);
        return GlobalResponse.<String>builder()
            .code(e.getCode())
            .msg(e.getMessage())
            .build();
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public GlobalResponse<String> exceptionHandler(Exception e){
        System.out.println("Unknown Exception！Reason is :"+e);
        return GlobalResponse.<String>builder()
            .code(-10086)
            .msg(e.getMessage() + Arrays.toString(e.getStackTrace()).replace(",","\n"))
            .build();
    }
}
