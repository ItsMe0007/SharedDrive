package com.peoplestrong.timeoff.encashment.pojo;

import com.peoplestrong.timeoff.leave.pojo.LeaveQuotaTO;

import javax.persistence.Column;

public class EncashmentLeaveQuotaTO extends LeaveQuotaTO {
    
    @Column(name = "EncachmentBalanceInHrs")
    private Integer encashmentBalenceInHrs;

    @Column(name = "EncachmentBalanceInDays")
    private Integer encashmentBalanceInDays;
    
    private Float leaveEncashment;

    public Integer getEncashmentBalenceInHrs() {
        return encashmentBalenceInHrs;
    }

    public void setEncashmentBalenceInHrs(Integer encashmentBalenceInHrs) {
        this.encashmentBalenceInHrs = encashmentBalenceInHrs;
    }

    public Integer getEncashmentBalanceInDays() {
        return encashmentBalanceInDays;
    }

    public void setEncashmentBalanceInDays(Integer encashmentBalanceInDays) {
        this.encashmentBalanceInDays = encashmentBalanceInDays;
    }

    public Float getLeaveEncashment() {
        return leaveEncashment;
    }

    public void setLeaveEncashment(Float leaveEncashment) {
        this.leaveEncashment = leaveEncashment;
    }
}
