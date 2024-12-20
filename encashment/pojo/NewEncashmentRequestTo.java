package com.peoplestrong.timeoff.encashment.pojo;

import com.peoplestrong.timeoff.encashment.flatTO.CalendarBlockDetailTO;
import com.peoplestrong.timeoff.encashment.pojo.base.DependentDetailTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class NewEncashmentRequestTo {
    private String action;
    private int loginUserID;
    private int loginEmployeeID;
    private int organizationID;
    private String comment;
    private int leaveTypeID;
    private Long encashmentTypeID;
    private String leaveType;
    private String encashmentType;
    private List<CalendarBlockDetailTO> calendarBlocks;
    private String attachmentPath;
    private int numberOfDays;
    private String ruleOutput;
    private int stageActionId;
    private int employeeID;
    private int l1ManagerID;
    private int l2ManagerID;
    private int hrManagerID;
    private String requestType;
    private String fileName;
    private List<DependentDetailTO> dependentDetails;
    private String encashmentTaskCode;
    private String comments;
    private String managerComments;
    private Integer StatusID;
    private String statusMessage;
    private Integer applicationDate;
    private Date appliedDate;
    private boolean managerInitiated;
    private String filePath;
    private String employeeWithdrawComments;
    private String stageType;
    private String previousStageType;
    private Integer userID;
    private String userName;
    private Long workFlowHistoryID;
    private Short hourlyWeightage;
    private Integer encashmentCountInDays;
    private Short encashmentCountInHrs;
    private Boolean active;
    private Integer tenantId;
    private Long encashmentID;

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

    public int getLeaveTypeID() {
        return leaveTypeID;
    }

    public void setLeaveTypeID(int leaveTypeID) {
        this.leaveTypeID = leaveTypeID;
    }

    public Long getEncashmentTypeID() {
        return encashmentTypeID;
    }

    public void setEncashmentTypeID(Long encashmentTypeID) {
        this.encashmentTypeID = encashmentTypeID;
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

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
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

    public String getEncashmentTaskCode() {
        return encashmentTaskCode;
    }

    public void setEncashmentTaskCode(String encashmentTaskCode) {
        this.encashmentTaskCode = encashmentTaskCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getManagerComments() {
        return managerComments;
    }

    public void setManagerComments(String managerComments) {
        this.managerComments = managerComments;
    }

    public Integer getStatusID() {
        return StatusID;
    }

    public void setStatusID(Integer statusID) {
        StatusID = statusID;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Integer getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Integer applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(Date appliedDate) {
        this.appliedDate = appliedDate;
    }

    public boolean isManagerInitiated() {
        return managerInitiated;
    }

    public void setManagerInitiated(boolean managerInitiated) {
        this.managerInitiated = managerInitiated;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getEmployeeWithdrawComments() {
        return employeeWithdrawComments;
    }

    public void setEmployeeWithdrawComments(String employeeWithdrawComments) {
        this.employeeWithdrawComments = employeeWithdrawComments;
    }

    public String getStageType() {
        return stageType;
    }

    public void setStageType(String stageType) {
        this.stageType = stageType;
    }

    public String getPreviousStageType() {
        return previousStageType;
    }

    public void setPreviousStageType(String previousStageType) {
        this.previousStageType = previousStageType;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getWorkFlowHistoryID() {
        return workFlowHistoryID;
    }

    public void setWorkFlowHistoryID(Long workFlowHistoryID) {
        this.workFlowHistoryID = workFlowHistoryID;
    }

    public Short getHourlyWeightage() {
        return hourlyWeightage;
    }

    public void setHourlyWeightage(Short hourlyWeightage) {
        this.hourlyWeightage = hourlyWeightage;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Long getEncashmentID() {
        return encashmentID;
    }

    public void setEncashmentID(Long encashmentID) {
        this.encashmentID = encashmentID;
    }
}
