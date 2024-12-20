package com.peoplestrong.timeoff.encashment.pojo.base;

import java.util.Date;

public class AppliedEncashmentListCommonTO {
    
    public Date fromDate;
    public Date toDate;
    private String fromDateString;
    private String toDateString;
    public Integer employeeId;
    public  Integer userId;

    public AppliedEncashmentListCommonTO() {
    }

    public AppliedEncashmentListCommonTO(Date fromDate, Date toDate, String fromDateString, String toDateString, Integer employeeId, Integer userId) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromDateString = fromDateString;
        this.toDateString = toDateString;
        this.employeeId = employeeId;
        this.userId = userId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getFromDateString() {
        return fromDateString;
    }

    public void setFromDateString(String fromDateString) {
        this.fromDateString = fromDateString;
    }

    public String getToDateString() {
        return toDateString;
    }

    public void setToDateString(String toDateString) {
        this.toDateString = toDateString;
    }
}
