package com.peoplestrong.timeoff.encashment.pojo.base;

public class AppMessageCode {
    private String code;
    private String message;

    public static AppMessageCode success() {
        return new AppMessageCode(MessageCode.EC200.getValue(), "");
    }

    public static AppMessageCode success(String message) {
        return new AppMessageCode(MessageCode.EC200.getValue(), message);
    }

    public static AppMessageCode error(String message) {
        return new AppMessageCode(MessageCode.EC201.getValue(), message);
    }

    public AppMessageCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
