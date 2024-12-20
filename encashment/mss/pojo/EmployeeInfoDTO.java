package com.peoplestrong.timeoff.encashment.mss.pojo;

import java.io.Serializable;
import java.util.Set;

public class EmployeeInfoDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer employeeID;
    private String employeeName;
    private String employeeCode;
    private String employmentStatus;
    private String profilePic;
    private String employmentType;
    private String confirmationDate;
    private Integer entityID;
    private Set<Long> leaveIds;

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
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

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Set<Long> getLeaveIds() {
        return leaveIds;
    }

    public void setLeaveIds(Set<Long> leaveIds) {
        this.leaveIds = leaveIds;
    }

    public EmployeeInfoDTO(Integer employeeID, String employeeName, String employeeCode, String employmentStatus,
                          String profilePic, String employmentType, String confirmationDate,Integer entityID) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.employeeCode = employeeCode;
        this.employmentStatus = employmentStatus;
        this.profilePic = profilePic;
        this.employmentType = employmentType;
        this.confirmationDate = confirmationDate;
        this.entityID = entityID;
    }
}
