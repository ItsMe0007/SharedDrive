package com.peoplestrong.timeoff.encashment.pojo;

import com.peoplestrong.timeoff.common.constant.TableConstant;

import javax.persistence.*;

public class TmEncashmentCalendarGroupTo {


    private Float maxEncashmentLimit;

    private Float maxEncashmentAllowedInMonth;

    private Float maxEncashmentAllowedInQuarter;

    private Float maxEncashmentAllowedInYear;

    private Float minimumBalanceForEncashment;

    private Float  minLeaveBalanceAfterEncashment;

    private Long encashmentCalenderGroupID;

    private String configurationName;

    private Integer leaveTypeID;

    private String leaveTypeCode;

    private Long altGroupID;

    private String altGroupName;

    private String blockDetailName;

    private Long calenderBlockDetailID;

    private Integer encashmentLimitInDays;

    private Short encashmentLimitInHrs;

    private Integer encashmentAllowedInMonthInDays;

    private Short encashmentAllowedInMonthInHrs;

    private Integer encashmentAllowedInQuarterInDays;

    private Short encashmentAllowedInQuarterInHrs;

    private Integer encashmentAllowedInYearInDays;

    private Short encashmentAllowedInYearInHrs;

    private Integer minimumBalanceForEncashmentInDays;

    private Short minimumBalanceForEncashmentInHrs;


    private Integer minLeaveBalanceAfterEncashmentInDays;


    private Short  minLeaveBalanceAfterEncashmentInHrs;

    private Boolean active;

    private Integer tenantID;

    private Long calenderBlockID;

    public Long getEncashmentCalenderGroupID() {
        return encashmentCalenderGroupID;
    }

    public void setEncashmentCalenderGroupID(Long encashmentCalenderGroupID) {
        this.encashmentCalenderGroupID = encashmentCalenderGroupID;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    public Integer getLeaveTypeID() {
        return leaveTypeID;
    }

    public void setLeaveTypeID(Integer leaveTypeID) {
        this.leaveTypeID = leaveTypeID;
    }

    public Long getAltGroupID() {
        return altGroupID;
    }

    public void setAltGroupID(Long altGroupID) {
        this.altGroupID = altGroupID;
    }

    public Long getCalenderBlockDetailID() {
        return calenderBlockDetailID;
    }

    public void setCalenderBlockDetailID(Long calenderBlockDetailID) {
        this.calenderBlockDetailID = calenderBlockDetailID;
    }

    public Integer getEncashmentLimitInDays() {
        return encashmentLimitInDays;
    }

    public void setEncashmentLimitInDays(Integer encashmentLimitInDays) {
        this.encashmentLimitInDays = encashmentLimitInDays;
    }

    public Short getEncashmentLimitInHrs() {
        return encashmentLimitInHrs;
    }

    public void setEncashmentLimitInHrs(Short encashmentLimitInHrs) {
        this.encashmentLimitInHrs = encashmentLimitInHrs;
    }

    public Integer getEncashmentAllowedInMonthInDays() {
        return encashmentAllowedInMonthInDays;
    }

    public void setEncashmentAllowedInMonthInDays(Integer encashmentAllowedInMonthInDays) {
        this.encashmentAllowedInMonthInDays = encashmentAllowedInMonthInDays;
    }

    public Short getEncashmentAllowedInMonthInHrs() {
        return encashmentAllowedInMonthInHrs;
    }

    public void setEncashmentAllowedInMonthInHrs(Short encashmentAllowedInMonthInHrs) {
        this.encashmentAllowedInMonthInHrs = encashmentAllowedInMonthInHrs;
    }

    public Integer getEncashmentAllowedInQuarterInDays() {
        return encashmentAllowedInQuarterInDays;
    }

    public void setEncashmentAllowedInQuarterInDays(Integer encashmentAllowedInQuarterInDays) {
        this.encashmentAllowedInQuarterInDays = encashmentAllowedInQuarterInDays;
    }

    public Short getEncashmentAllowedInQuarterInHrs() {
        return encashmentAllowedInQuarterInHrs;
    }

    public void setEncashmentAllowedInQuarterInHrs(Short encashmentAllowedInQuarterInHrs) {
        this.encashmentAllowedInQuarterInHrs = encashmentAllowedInQuarterInHrs;
    }

    public Integer getEncashmentAllowedInYearInDays() {
        return encashmentAllowedInYearInDays;
    }

    public void setEncashmentAllowedInYearInDays(Integer encashmentAllowedInYearInDays) {
        this.encashmentAllowedInYearInDays = encashmentAllowedInYearInDays;
    }

    public Short getEncashmentAllowedInYearInHrs() {
        return encashmentAllowedInYearInHrs;
    }

    public void setEncashmentAllowedInYearInHrs(Short encashmentAllowedInYearInHrs) {
        this.encashmentAllowedInYearInHrs = encashmentAllowedInYearInHrs;
    }

    public Integer getMinimumBalanceForEncashmentInDays() {
        return minimumBalanceForEncashmentInDays;
    }

    public void setMinimumBalanceForEncashmentInDays(Integer minimumBalanceForEncashmentInDays) {
        this.minimumBalanceForEncashmentInDays = minimumBalanceForEncashmentInDays;
    }

    public Short getMinimumBalanceForEncashmentInHrs() {
        return minimumBalanceForEncashmentInHrs;
    }

    public void setMinimumBalanceForEncashmentInHrs(Short minimumBalanceForEncashmentInHrs) {
        this.minimumBalanceForEncashmentInHrs = minimumBalanceForEncashmentInHrs;
    }


    public Float getMinLeaveBalanceAfterEncashment() {
        return minLeaveBalanceAfterEncashment;
    }

    public void setMinLeaveBalanceAfterEncashment(Float minLeaveBalanceAfterEncashment) {
        this.minLeaveBalanceAfterEncashment = minLeaveBalanceAfterEncashment;
    }

    public Integer getMinLeaveBalanceAfterEncashmentInDays() {
        return minLeaveBalanceAfterEncashmentInDays;
    }

    public void setMinLeaveBalanceAfterEncashmentInDays(Integer minLeaveBalanceAfterEncashmentInDays) {
        this.minLeaveBalanceAfterEncashmentInDays = minLeaveBalanceAfterEncashmentInDays;
    }

    public Short getMinLeaveBalanceAfterEncashmentInHrs() {
        return minLeaveBalanceAfterEncashmentInHrs;
    }

    public void setMinLeaveBalanceAfterEncashmentInHrs(Short minLeaveBalanceAfterEncashmentInHrs) {
        this.minLeaveBalanceAfterEncashmentInHrs = minLeaveBalanceAfterEncashmentInHrs;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getTenantID() {
        return tenantID;
    }

    public void setTenantID(Integer tenantID) {
        this.tenantID = tenantID;
    }

    public String getLeaveTypeCode() {
        return leaveTypeCode;
    }

    public void setLeaveTypeCode(String leaveTypeCode) {
        this.leaveTypeCode = leaveTypeCode;
    }

    public Float getMaxEncashmentAllowedInMonth() {
        return maxEncashmentAllowedInMonth;
    }

    public void setMaxEncashmentAllowedInMonth(Float maxEncashmentAllowedInMonth) {
        this.maxEncashmentAllowedInMonth = maxEncashmentAllowedInMonth;
    }

    public Float getMaxEncashmentAllowedInQuarter() {
        return maxEncashmentAllowedInQuarter;
    }

    public void setMaxEncashmentAllowedInQuarter(Float maxEncashmentAllowedInQuarter) {
        this.maxEncashmentAllowedInQuarter = maxEncashmentAllowedInQuarter;
    }

    public Float getMaxEncashmentAllowedInYear() {
        return maxEncashmentAllowedInYear;
    }

    public void setMaxEncashmentAllowedInYear(Float maxEncashmentAllowedInYear) {
        this.maxEncashmentAllowedInYear = maxEncashmentAllowedInYear;
    }

    public Float getMaxEncashmentLimit() {
        return maxEncashmentLimit;
    }

    public void setMaxEncashmentLimit(Float maxEncashmentLimit) {
        this.maxEncashmentLimit = maxEncashmentLimit;
    }

    public Float getMinimumBalanceForEncashment() {
        return minimumBalanceForEncashment;
    }

    public void setMinimumBalanceForEncashment(Float minimumBalanceForEncashment) {
        this.minimumBalanceForEncashment = minimumBalanceForEncashment;
    }

    public String getAltGroupName() {
        return altGroupName;
    }

    public void setAltGroupName(String altGroupName) {
        this.altGroupName = altGroupName;
    }

    public String getBlockDetailName() {
        return blockDetailName;
    }

    public void setBlockDetailName(String blockDetailName) {
        this.blockDetailName = blockDetailName;
    }

    public Long getCalenderBlockID() {
        return calenderBlockID;
    }

    public void setCalenderBlockID(Long calenderBlockID) {
        this.calenderBlockID = calenderBlockID;
    }
}
