package com.peoplestrong.timeoff.encashment.pojo;

import com.peoplestrong.timeoff.encashment.pojo.base.DependentDetailTO;
import com.peoplestrong.timeoff.translation.interfaces.TranslatedObject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class EncashmentListingTo implements Serializable, TranslatedObject {

    private static final long serialVersionUID = 2924566323917850704L;

    private Long encashmentId;

    private String encashmentCode;

    private Integer leaveTypeID;

    private String leaveTypeCode;

    private Float encashmentCount;

    private Integer encashmentTypeID;

    private String encashmentTypeCode;

    private List<CalendarBlocksTO> calendarBlocksTOS;

    private String statusMessage;

    private Integer statusID;

    private String comments;

    private String managerComments;

    private Integer employeeID;

    private boolean isActive;

    private Date appliedDate;

    private String appliedDateString;

    private Integer applicationDate;

    private Boolean isManagerInitiated;

    private String filePath;
    
    private List<DependentDetailTO> dependentDetails;
    
    private Integer encashmentTypeContentId;
    
    private Integer encashmentSysTypeId;
    
    private String encashmentSysType;

    public Long getEncashmentId() {
        return encashmentId;
    }

    public void setEncashmentId(Long encashmentId) {
        this.encashmentId = encashmentId;
    }

    public String getEncashmentCode() {
        return encashmentCode;
    }

    public void setEncashmentCode(String encashmentCode) {
        this.encashmentCode = encashmentCode;
    }

    public Integer getLeaveTypeID() {
        return leaveTypeID;
    }

    public void setLeaveTypeID(Integer leaveTypeID) {
        this.leaveTypeID = leaveTypeID;
    }

    public Float getEncashmentCount() {
        return encashmentCount;
    }

    public void setEncashmentCount(Float encashmentCount) {
        this.encashmentCount = encashmentCount;
    }

    public Integer getEncashmentTypeID() {
        return encashmentTypeID;
    }

    public void setEncashmentTypeID(Integer encashmentTypeID) {
        this.encashmentTypeID = encashmentTypeID;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
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

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(Date appliedDate) {
        this.appliedDate = appliedDate;
    }

    public Integer getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Integer applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Boolean getManagerInitiated() {
        return isManagerInitiated;
    }

    public void setManagerInitiated(Boolean managerInitiated) {
        isManagerInitiated = managerInitiated;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEmployeeWithdrawComments() {
        return employeeWithdrawComments;
    }

    public void setEmployeeWithdrawComments(String employeeWithdrawComments) {
        this.employeeWithdrawComments = employeeWithdrawComments;
    }

    public Integer getL1ManagerID() {
        return l1ManagerID;
    }

    public void setL1ManagerID(Integer l1ManagerID) {
        this.l1ManagerID = l1ManagerID;
    }

    public Integer getL2ManagerID() {
        return l2ManagerID;
    }

    public void setL2ManagerID(Integer l2ManagerID) {
        this.l2ManagerID = l2ManagerID;
    }

    public Integer getHrManagerID() {
        return hrManagerID;
    }

    public void setHrManagerID(Integer hrManagerID) {
        this.hrManagerID = hrManagerID;
    }

    public String getStageType() {
        return stageType;
    }

    public void setStageType(String stageType) {
        this.stageType = stageType;
    }

    public Integer getStageId() {
        return stageId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public String getPreviousStageType() {
        return previousStageType;
    }

    public void setPreviousStageType(String previousStageType) {
        this.previousStageType = previousStageType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getWorkFlowHistoryId() {
        return workFlowHistoryId;
    }

    public void setWorkFlowHistoryId(Long workFlowHistoryId) {
        this.workFlowHistoryId = workFlowHistoryId;
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

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Short getHourlyWeightage() {
        return hourlyWeightage;
    }

    public void setHourlyWeightage(Short hourlyWeightage) {
        this.hourlyWeightage = hourlyWeightage;
    }

    private String fileName;

    private String employeeWithdrawComments;

    private Integer l1ManagerID;

    private Integer l2ManagerID;

    private Integer hrManagerID;

    private String stageType;

    private Integer stageId;

    private String previousStageType;

    private Integer userId;

    private Long workFlowHistoryId;

    private Integer encashmentCountInDays;

    private Short encashmentCountInHrs;

    private Integer tenantId;

    private Short hourlyWeightage;

    public String getAppliedDateString() {
        return appliedDateString;
    }

    public void setAppliedDateString(String appliedDateString) {
        this.appliedDateString = appliedDateString;
    }

    public String getLeaveTypeCode() {
        return leaveTypeCode;
    }

    public void setLeaveTypeCode(String leaveTypeCode) {
        this.leaveTypeCode = leaveTypeCode;
    }

    public String getEncashmentTypeCode() {
        return encashmentTypeCode;
    }

    public void setEncashmentTypeCode(String encashmentTypeCode) {
        this.encashmentTypeCode = encashmentTypeCode;
    }

    public List<CalendarBlocksTO> getCalendarBlocksTOS() {
        return calendarBlocksTOS;
    }

    public void setCalendarBlocksTOS(List<CalendarBlocksTO> calendarBlocksTOS) {
        this.calendarBlocksTOS = calendarBlocksTOS;
    }
    
    public List<DependentDetailTO> getDependentDetails() {
        return dependentDetails;
    }
    
    public void setDependentDetails(List<DependentDetailTO> dependentDetails) {
        this.dependentDetails = dependentDetails;
    }
    
    public Integer getEncashmentTypeContentId() {
        return encashmentTypeContentId;
    }
    
    public void setEncashmentTypeContentId(Integer encashmentTypeContentId) {
        this.encashmentTypeContentId = encashmentTypeContentId;
    }
    
    public Integer getEncashmentSysTypeId() {
        return encashmentSysTypeId;
    }
    
    public void setEncashmentSysTypeId(Integer encashmentSysTypeId) {
        this.encashmentSysTypeId = encashmentSysTypeId;
    }
    
    public String getEncashmentSysType() {
        return encashmentSysType;
    }
    
    public void setEncashmentSysType(String encashmentSysType) {
        this.encashmentSysType = encashmentSysType;
    }
    
    public EncashmentListingTo(Long encashmentId, Integer leaveTypeID, Integer encashmentTypeID, String stageType, Integer userId,
                               Long workFlowHistoryId, String encashmentCode, String statusMessage, Short hourlyWeightage,
                               boolean isActive, Integer applicationDate, Integer encashmentCountInDays, Short encashmentCountInHrs, Integer sysWorkflowStageId, String previousStageType)
    {
        this.encashmentId=encashmentId;
        this.leaveTypeID = leaveTypeID;
        this.encashmentTypeID = encashmentTypeID;
        this.stageType = stageType;
        this.userId = userId;
        this.workFlowHistoryId = workFlowHistoryId;
        this.encashmentCode = encashmentCode;
        this.statusMessage = statusMessage;
        this.hourlyWeightage = hourlyWeightage;
        this.isActive = isActive;
        this.applicationDate = applicationDate;
        this.encashmentCountInDays = encashmentCountInDays;
        this.encashmentCountInHrs = encashmentCountInHrs;
        this.stageId = sysWorkflowStageId;
        this.previousStageType = previousStageType;
    }

    public EncashmentListingTo() {
    }
}