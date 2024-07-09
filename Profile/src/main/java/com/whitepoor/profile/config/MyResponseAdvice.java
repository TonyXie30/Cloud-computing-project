package com.whitepoor.profile.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitepoor.profile.pojo.GlobalResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Functionality Desc:
 *   全局消息封装。所有在basePackges下的controller的返回值都会经过这里被打包为httpResponse，
 *   默认能return的情况都不是异常，因此处理异常推荐先在Code枚举类定义状态码和消息后，
 *   用throw new MyException(Code)的方式交给全局异常处理器打包。
 *
 * @author Jiachen<zhangjc1999 @ gmail.com>
 * @date   2020/10/30 10:03 下午
 */
@ControllerAdvice(basePackages = {"com.whitepoor.login.controller"})
public class MyResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass,
        ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        System.out.println("__________________");
        if (!(o instanceof GlobalResponse)) {
            GlobalResponse<Object> response = GlobalResponse.builder()
                .code(200)
                .msg("success")
                .data(o)
                .build();
            if (!(o instanceof String)) {
                return response;
            }
            ObjectMapper mapper = new ObjectMapper();
            String responseString = "";
            try {
                responseString = mapper.writeValueAsString(response);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return responseString;
        }
        return o;
    }

}