package com.peoplestrong.timeoff.encashment.pojo.base;

import java.util.Set;

public class LeaveTOListTO {
     private Integer leaveTypeId;
     private String leaveTypeCode;
     private String leaveTypeDescription;
     private Integer organizationId;
     private Boolean active;
     private Boolean hourly;
     private String leaveMessage;
     private Set<Long> calendarGroupIds;
    
    public Integer getLeaveTypeId() {
        return leaveTypeId;
    }
    
    public void setLeaveTypeId(Integer leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }
    
    public String getLeaveTypeCode() {
        return leaveTypeCode;
    }
    
    public void setLeaveTypeCode(String leaveTypeCode) {
        this.leaveTypeCode = leaveTypeCode;
    }
    
    public String getLeaveTypeDescription() {
        return leaveTypeDescription;
    }
    
    public void setLeaveTypeDescription(String leaveTypeDescription) {
        this.leaveTypeDescription = leaveTypeDescription;
    }
    
    public Integer getOrganizationId() {
        return organizationId;
    }
    
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public Boolean getHourly() {
        return hourly;
    }
    
    public void setHourly(Boolean hourly) {
        this.hourly = hourly;
    }
    
    public String getLeaveMessage() {
        return leaveMessage;
    }
    
    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }
    
    public Set<Long> getCalendarGroupIds() {
        return calendarGroupIds;
    }
    
    public void setCalendarGroupIds(Set<Long> calendarGroupIds) {
        this.calendarGroupIds = calendarGroupIds;
    }
    
    public LeaveTOListTO(Integer leaveTypeId, String leaveTypeCode, String leaveTypeDescription, Integer organizationId, Boolean active, Boolean hourly, String leaveMessage, Set<Long> calendarGroupIds) {
        this.leaveTypeId = leaveTypeId;
        this.leaveTypeCode = leaveTypeCode;
        this.leaveTypeDescription = leaveTypeDescription;
        this.organizationId = organizationId;
        this.active = active;
        this.hourly = hourly;
        this.leaveMessage = leaveMessage;
        this.calendarGroupIds = calendarGroupIds;
    }
    
    public LeaveTOListTO() {
    }
}
