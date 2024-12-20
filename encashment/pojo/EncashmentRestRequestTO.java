package com.peoplestrong.timeoff.encashment.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.peoplestrong.timeoff.encashment.flatTO.CalendarBlockDetailTO;
import com.peoplestrong.timeoff.encashment.pojo.base.DependentDetailTO;

import java.io.Serializable;
import java.util.List;

public class EncashmentRestRequestTO implements Serializable {
    
    @JsonProperty("action")
    private String action;

    @JsonProperty("loginUserID")
    private int loginUserID;

    @JsonProperty("loginEmployeeID")
    private int loginEmployeeID;

    @JsonProperty("organizationID")
    private int organizationID;

    @JsonProperty(" comment")
    private String comment;

    @JsonProperty(" leaveTypeId")
    private int leaveTypeId;

    @JsonProperty(" encashmentTypeId")
    private Long encashmentTypeId;

    @JsonProperty("leaveType")
    private String leaveType;

    @JsonProperty("encashmentType")
    private String encashmentType;

    @JsonProperty("calendarBlocks")
    private List<CalendarBlockDetailTO> calendarBlocks;

    @JsonProperty("attachmentPath")
    private String attachmentPath;

    @JsonProperty("numberOfDays")
    private int numberOfDays;

    @JsonProperty("ruleOutput")
    private String ruleOutput;

    @JsonProperty("stageActionId")
    private int stageActionId;

    @JsonProperty("employeeId")
    private int employeeId;

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("l1ManagerID")
    private int l1ManagerID;

    @JsonProperty("l2ManagerID")
    private int l2ManagerID;

    @JsonProperty("hrManagerID")
    private int hrManagerID;

    @JsonProperty("requestType")
    private String requestType;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("dependentDetails")
    private List<DependentDetailTO> dependentDetails;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getLoginUserID() {
        return loginUserID;
    }

    public void setLoginUserID(int loginUserID) {
        this.loginUserID = loginUserID;
    }

    public int getLoginEmployeeID() {
        return loginEmployeeID;
    }

    public void setLoginEmployeeID(int loginEmployeeID) {
        this.loginEmployeeID = loginEmployeeID;
    }

    public int getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(int organizationID) {
        this.organizationID = organizationID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(int leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public Long getEncashmentTypeId() {
        return encashmentTypeId;
    }

    public void setEncashmentTypeId(Long encashmentTypeId) {
        this.encashmentTypeId = encashmentTypeId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getEncashmentType() {
        return encashmentType;
    }

    public void setEncashmentType(String encashmentType) {
        this.encashmentType = encashmentType;
    }

    public List<CalendarBlockDetailTO> getCalendarBlocks() {
        return calendarBlocks;
    }

    public void setCalendarBlocks(List<CalendarBlockDetailTO> calendarBlocks) {
        this.calendarBlocks = calendarBlocks;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getRuleOutput() {
        return ruleOutput;
    }

    public void setRuleOutput(String ruleOutput) {
        this.ruleOutput = ruleOutput;
    }

    public int getStageActionId() {
        return stageActionId;
    }

    public void setStageActionId(int stageActionId) {
        this.stageActionId = stageActionId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getL1ManagerID() {
        return l1ManagerID;
    }

    public void setL1ManagerID(int l1ManagerID) {
        this.l1ManagerID = l1ManagerID;
    }

    public int getL2ManagerID() {
        return l2ManagerID;
    }

    public void setL2ManagerID(int l2ManagerID) {
        this.l2ManagerID = l2ManagerID;
    }

    public int getHrManagerID() {
        return hrManagerID;
    }

    public void setHrManagerID(int hrManagerID) {
        this.hrManagerID = hrManagerID;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<DependentDetailTO> getDependentDetails() {
        return dependentDetails;
    }

    public void setDependentDetails(List<DependentDetailTO> dependentDetails) {
        this.dependentDetails = dependentDetails;
    }
}
