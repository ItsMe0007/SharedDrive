package com.peoplestrong.timeoff.encashment.transport.input;

import java.io.Serializable;
import java.util.Date;

public class EncashmentEmployeeTO implements Serializable {
    
    private static final long serialVersionUID = -2396573011778077211L;

    private Date joiningDate;
    private Date dateOfMarriage;
    private Date birthDate;
    private String employmentType;
    private String employmentStatus;
    private Integer orgUnitId;
    private String grade;
    private String band;
    private Integer employeeReligionId;
    private Integer employeeGenderId;
    private Integer confirmationDateId;

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Date getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(Date dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Integer getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public Integer getEmployeeReligionId() {
        return employeeReligionId;
    }

    public void setEmployeeReligionId(Integer employeeReligionId) {
        this.employeeReligionId = employeeReligionId;
    }

    public Integer getEmployeeGenderId() {
        return employeeGenderId;
    }

    public void setEmployeeGenderId(Integer employeeGenderId) {
        this.employeeGenderId = employeeGenderId;
    }

    public Integer getConfirmationDateId() {
        return confirmationDateId;
    }

    public void setConfirmationDateId(Integer confirmationDateId) {
        this.confirmationDateId = confirmationDateId;
    }
}
