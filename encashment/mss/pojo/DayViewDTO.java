package com.peoplestrong.timeoff.encashment.mss.pojo;

import java.io.Serializable;

public class DayViewDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer employeeID;
    private Integer organizationID;
    private String employeeName;
    private String employeeCode;
    private String employmentStatus;
    private String profilePic;
    private String employmentType;
    private String confirmationDate;
    private boolean selected;
    private Integer entityID;
    private LastEncashmentApplied lastEncashmentApplied;

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(Integer organizationID) {
        this.organizationID = organizationID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(String confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public LastEncashmentApplied getLastEncashmentApplied() {
        return lastEncashmentApplied;
    }

    public void setLastEncashmentApplied(LastEncashmentApplied lastEncashmentApplied) {
        this.lastEncashmentApplied = lastEncashmentApplied;
    }
}
