package com.peoplestrong.timeoff.encashment.mss.pojo;

import com.peoplestrong.timeoff.encashment.pojo.CalendarBlocksTO;

import java.util.Date;
import java.util.List;

public class LastEncashmentApplied {
    private Integer applicationDateID;
    private Date applicationDate;
    private String appliedDateString;
    private Integer encashmentCountInDays;
    private Integer encashmentCountInHrs;
    private String status;
    private Integer leaveTypeID;
    private String leaveType;
    private Long encashmentID;
    private String encashmentType;
    private String sysEncashmentType;
    private Integer sysEncashmentTypeId;
    private Integer encashmentContentId;
    private Float encashmentCount;
    private Short hourlyWeightage;
    private List<CalendarBlocksTO> calendarBlocksTOS;

    public Integer getApplicationDateID() {
        return applicationDateID;
    }

    public void setApplicationDateID(Integer applicationDateID) {
        this.applicationDateID = applicationDateID;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getAppliedDateString() {
        return appliedDateString;
    }

    public void setAppliedDateString(String appliedDateString) {
        this.appliedDateString = appliedDateString;
    }

    public Integer getEncashmentCountInDays() {
        return encashmentCountInDays;
    }

    public void setEncashmentCountInDays(Integer encashmentCountInDays) {
        this.encashmentCountInDays = encashmentCountInDays;
    }

    public Integer getEncashmentCountInHrs() {
        return encashmentCountInHrs;
    }

    public void setEncashmentCountInHrs(Integer encashmentCountInHrs) {
        this.encashmentCountInHrs = encashmentCountInHrs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLeaveTypeID() {
        return leaveTypeID;
    }

    public void setLeaveTypeID(Integer leaveTypeID) {
        this.leaveTypeID = leaveTypeID;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Long getEncashmentID() {
        return encashmentID;
    }

    public void setEncashmentID(Long encashmentID) {
        this.encashmentID = encashmentID;
    }

    public String getEncashmentType() {
        return encashmentType;
    }

    public void setEncashmentType(String encashmentType) {
        this.encashmentType = encashmentType;
    }

    public String getSysEncashmentType() {
        return sysEncashmentType;
    }

    public void setSysEncashmentType(String sysEncashmentType) {
        this.sysEncashmentType = sysEncashmentType;
    }

    public Integer getSysEncashmentTypeId() {
        return sysEncashmentTypeId;
    }

    public void setSysEncashmentTypeId(Integer sysEncashmentTypeId) {
        this.sysEncashmentTypeId = sysEncashmentTypeId;
    }

    public Integer getEncashmentContentId() {
        return encashmentContentId;
    }

    public void setEncashmentContentId(Integer encashmentContentId) {
        this.encashmentContentId = encashmentContentId;
    }

    public Float getEncashmentCount() {
        return encashmentCount;
    }

    public void setEncashmentCount(Float encashmentCount) {
        this.encashmentCount = encashmentCount;
    }

    public List<CalendarBlocksTO> getCalendarBlocksTOS() {
        return calendarBlocksTOS;
    }

    public void setCalendarBlocksTOS(List<CalendarBlocksTO> calendarBlocksTOS) {
        this.calendarBlocksTOS = calendarBlocksTOS;
    }
    
    public Short getHourlyWeightage() {
        return hourlyWeightage;
    }
    
    public void setHourlyWeightage(Short hourlyWeightage) {
        this.hourlyWeightage = hourlyWeightage;
    }
}
