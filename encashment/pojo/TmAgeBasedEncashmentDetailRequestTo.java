package com.peoplestrong.timeoff.encashment.pojo;

import java.io.Serializable;

@SuppressWarnings("unused")
public class TmAgeBasedEncashmentDetailRequestTo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer startAge;
    private Integer endAge;
    private Float encashmentLimit;
    private Integer tenantID;

    private Integer organizationId;

    // Default constructor
    public TmAgeBasedEncashmentDetailRequestTo() {
    }

    // Parameterized constructor
    public TmAgeBasedEncashmentDetailRequestTo(Integer startAge, Integer endAge, Float encashmentLimit, Integer tenantID, Integer organizationId) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.encashmentLimit = encashmentLimit;
        this.tenantID = tenantID;
        this.organizationId = organizationId;
    }

    // Getters and Setters
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

    public Float getEncashmentLimit() {
        return encashmentLimit;
    }

    public void setEncashmentLimit(Float encashmentLimit) {
        this.encashmentLimit = encashmentLimit;
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
