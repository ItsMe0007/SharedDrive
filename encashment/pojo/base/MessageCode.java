package com.peoplestrong.timeoff.encashment.pojo.base;


public enum MessageCode {
    EC200("EC200"), EC201("EC201");

    private final String value;

    MessageCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
