package com.peoplestrong.timeoff.encashment.pojo.base;

import java.io.Serializable;
import java.util.List;

import com.peoplestrong.timeoff.encashment.pojo.CalendarBlocksTO;
import com.peoplestrong.timeoff.translation.annotation.TranslatorField;
import com.peoplestrong.timeoff.translation.constants.TranslatedEntityName;
import com.peoplestrong.timeoff.translation.interfaces.TranslatedObject;

public class EncashmentListResponseTO implements Serializable, Comparable<com.peoplestrong.timeoff.encashment.pojo.base.EncashmentListResponseTO>, TranslatedObject {

    private static final long serialVersionUID = 2924566323917850704L;

    private int employeeId;

    @TranslatorField(TranslatedFieldName = "leaveType", Table = TranslatedEntityName.TmLeaveType)
    private int leaveTypeId;

    private boolean active;

    @TranslatorField(TranslatedFieldName = "stage", Table = TranslatedEntityName.SysWorkflowStage)
    private int stageId;

    private int userId;

    private String leaveType;

    private String stage;

    private String workflowStageType;

    private Long workflowHistoryID;

    private String statusMessage;

    private Integer hourlyWeightage;

    private Boolean hourly;

    private String encashmentCode;
    
    private Long employeeEncashmentId;

    private float encashmentCount;

    private String applicationDate;
    
    private String sysEncashmentType;
    
    private String encashmentType;
    
    private Integer sysEncashmentTypeId;
    
    private Integer encashmentContentId;
    
    private Boolean withdraw;
    
    private List<CalendarBlocksTO> calendarBlocksTO;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(int leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getWorkflowStageType() {
        return workflowStageType;
    }

    public void setWorkflowStageType(String workflowStageType) {
        this.workflowStageType = workflowStageType;
    }

    public Long getWorkflowHistoryID() {
        return workflowHistoryID;
    }

    public void setWorkflowHistoryID(Long workflowHistoryID) {
        this.workflowHistoryID = workflowHistoryID;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Integer getHourlyWeightage() {
        return hourlyWeightage;
    }

    public void setHourlyWeightage(Integer hourlyWeightage) {
        this.hourlyWeightage = hourlyWeightage;
    }

    public Boolean getHourly() {
        return hourly;
    }

    public void setHourly(Boolean hourly) {
        this.hourly = hourly;
    }

    public String getEncashmentCode() {
        return encashmentCode;
    }

    public void setEncashmentCode(String encashmentCode) {
        this.encashmentCode = encashmentCode;
    }

    public String getEncashmentType() {
        return encashmentType;
    }

    public void setEncashmentType(String encashmentType) {
        this.encashmentType = encashmentType;
    }

    public Long getEmployeeEncashmentId() {
        return employeeEncashmentId;
    }

    public void setEmployeeEncashmentId(Long employeeEncashmentId) {
        this.employeeEncashmentId = employeeEncashmentId;
    }

    public float getEncashmentCount() {
        return encashmentCount;
    }

    public void setEncashmentCount(float encashmentCount) {
        this.encashmentCount = encashmentCount;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
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
    
    public List<CalendarBlocksTO> getCalendarBlocksTO() {
        return calendarBlocksTO;
    }
    
    public void setCalendarBlocksTO(List<CalendarBlocksTO> calendarBlocksTO) {
        this.calendarBlocksTO = calendarBlocksTO;
    }
    
    public Boolean getWithdraw() {
        return withdraw;
    }
    
    public void setWithdraw(Boolean withdraw) {
        this.withdraw = withdraw;
    }
    
    @Override
    public int compareTo(EncashmentListResponseTO obj)
    {
        return obj.getEmployeeEncashmentId().compareTo(this.employeeEncashmentId);
    }
}