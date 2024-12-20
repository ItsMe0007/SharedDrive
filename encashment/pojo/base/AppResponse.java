package com.peoplestrong.timeoff.encashment.pojo.base;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@SuppressWarnings("unused")
public class AppResponse<T> {
    private AppMessageCode message;
    private T responseData;

    public static <T> AppResponse<T> success(T data) {
        return new AppResponse<>(AppMessageCode.success(), data);
    }

    public static <T> AppResponse<T> success(String message, T data) {
        return new AppResponse<>(AppMessageCode.success(message), data);
    }

    public static <T> AppResponse<T> error(String message) {
        return new AppResponse<>(AppMessageCode.error(message), null);
    }

    public static <T> AppResponse<T> unknownError() {
        return new AppResponse<>(AppMessageCode.error("Something went wrong"), null);
    }

    public AppResponse(AppMessageCode message, T responseData) {
        this.message = message;
        this.responseData = responseData;
    }

    public AppMessageCode getMessage() {
        return message;
    }

    public void setMessage(AppMessageCode message) {
        this.message = message;
    }

    public T getResponseData() {
        return responseData;
    }

    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }
}
