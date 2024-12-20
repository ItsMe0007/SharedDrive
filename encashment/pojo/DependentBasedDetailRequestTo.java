package com.peoplestrong.timeoff.encashment.pojo;

import com.peoplestrong.timeoff.translation.interfaces.TranslatedObject;

import java.io.Serializable;

public class DependentBasedDetailRequestTo implements TranslatedObject, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer startAge;
    private Integer endAge;
    private Integer percentageEncashment;
    private Integer tenantID;
    private Integer organizationId;

    public DependentBasedDetailRequestTo() {
    }

    public DependentBasedDetailRequestTo(Integer startAge, Integer endAge, Integer percentageEncashment, Integer tenantID, Integer organizationId) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.percentageEncashment = percentageEncashment;
        this.tenantID = tenantID;
        this.organizationId = organizationId;
    }

    public Integer getStartAge() {
        return startAge;
    }

    public void setStartAge(Integer startAge) {
        this.startAge = startAge;
    }

    public Integer getEndAge() {
        return endAge;
    }

    public void setEndAge(Integer endAge) {
        this.endAge = endAge;
    }

    public Integer getPercentageEncashment() {
        return percentageEncashment;
    }

    public void setPercentageEncashment(Integer percentageEncashment) {
        this.percentageEncashment = percentageEncashment;
    }

    public Integer getTenantID() {
        return tenantID;
    }

    public void setTenantID(Integer tenantID) {
        this.tenantID = tenantID;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }
}
