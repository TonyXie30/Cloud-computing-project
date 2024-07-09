package com.whitepoor.login.config;

/**
 * Define all used return code and corresponding message.<br>
 * Whenever need to return a code to front-end, it is suggested to define clearly here first and use as Code.XXX.
 * Especially error code.
 */
public enum Code {
    SUCCESS(0,"success"),// try not throw an exception, with success message
    MISSING_FIELD(5000, "Missing required field"),
    METHOD_FAILED(5001, "method failed for unknown reason"),
    USER_NOT_EXIST(5002, "User not exist"),

    Not_available(5003, "Not available"),
    LOGIN_FAILED_WRONG_PW(5101,"Login failed: wrong password"),
    REG_FAILED_USER_EXIST(5102,"Register failed: user exist"),
    REG_FAILED_BAD_PW(5103, "Register failed: password should only contain letters, numbers and underline(_)"),
    Event_not_found(5104, "Event not found"),
    Already_joined_event(5105,"Already joined"),
    SUBJECT_NOT_FOUND(5106,"Subject not found"),
    GENDER_NOT_FOUND(5107,"Gender not found")
    ;

    private final int code;
    private final String msg;

    Code(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
