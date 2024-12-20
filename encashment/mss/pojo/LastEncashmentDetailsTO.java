package com.peoplestrong.timeoff.encashment.mss.pojo;

import com.peoplestrong.timeoff.encashment.pojo.CalendarBlocksTO;

import java.util.Date;
import java.util.List;

public class LastEncashmentDetailsTO {

    private Long encashmentId;
    private Date applicationDate;
    private Integer applicationDateId;
    private String appliedDateString;
    private String encashmentType;
    private Integer encashmentContentId;
    private String sysEncashmentType;
    private Integer sysEncashmentTypeId;
    private Integer leaveTypeId;
    private String leaveType;
    private String status;
    private List<CalendarBlocksTO> calendarBlocksTOList;
    private Integer encashmentCountInDays;
    private Short encashmentCountInHrs;
    private Short hourlyWeightage;

    private Float encashmentCount;

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Integer getApplicationDateId() {
        return applicationDateId;
    }

    public void setApplicationDateId(Integer applicationDateId) {
        this.applicationDateId = applicationDateId;
    }

    public String getAppliedDateString() {
        return appliedDateString;
    }

    public void setAppliedDateString(String appliedDateString) {
        this.appliedDateString = appliedDateString;
    }

    public String getEncashmentType() {
        return encashmentType;
    }

    public void setEncashmentType(String encashmentType) {
        this.encashmentType = encashmentType;
    }

    public Integer getEncashmentContentId() {
        return encashmentContentId;
    }

    public void setEncashmentContentId(Integer encashmentContentId) {
        this.encashmentContentId = encashmentContentId;
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

    public Integer getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(Integer leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CalendarBlocksTO> getCalendarBlocksTOList() {
        return calendarBlocksTOList;
    }

    public void setCalendarBlocksTOList(List<CalendarBlocksTO> calendarBlocksTOList) {
        this.calendarBlocksTOList = calendarBlocksTOList;
    }

    public Integer getEncashmentCountInDays() {
        return encashmentCountInDays;
    }

    public void setEncashmentCountInDays(Integer encashmentCountInDays) {
        this.encashmentCountInDays = encashmentCountInDays;
    }

    public Short getEncashmentCountInHrs() {
        return encashmentCountInHrs;
    }

    public void setEncashmentCountInHrs(Short encashmentCountInHrs) {
        this.encashmentCountInHrs = encashmentCountInHrs;
    }

    public Short getHourlyWeightage() {
        return hourlyWeightage;
    }

    public void setHourlyWeightage(Short hourlyWeightage) {
        this.hourlyWeightage = hourlyWeightage;
    }

    public Float getEncashmentCount() {
        return encashmentCount;
    }

    public void setEncashmentCount(Float encashmentCount) {
        this.encashmentCount = encashmentCount;
    }

    public Long getEncashmentId() {
        return encashmentId;
    }

    public void setEncashmentId(Long encashmentId) {
        this.encashmentId = encashmentId;
    }
}
