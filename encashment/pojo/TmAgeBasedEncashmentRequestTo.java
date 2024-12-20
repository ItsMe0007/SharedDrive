package com.peoplestrong.timeoff.encashment.pojo;


import java.io.Serializable;


public class TmAgeBasedEncashmentRequestTo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ageBasedEncashmentId;
    private String configurationName;
    private Boolean active;
    private Integer tenantID;

    private Integer organizationId;


    // Default constructor
    public TmAgeBasedEncashmentRequestTo() {
    }

    public Long getAgeBasedEncashmentId() {
        return ageBasedEncashmentId;
    }

    public void setAgeBasedEncashmentId(Long ageBasedEncashmentId) {
        this.ageBasedEncashmentId = ageBasedEncashmentId;
    }

    // Parameterized constructor
    public TmAgeBasedEncashmentRequestTo(String configurationName, Boolean active, Integer tenantID, Integer OrgazisationId) {
        this.configurationName = configurationName;
        this.active = active;
        this.tenantID = tenantID;
        this.organizationId = organizationId;
    }

    // Getters and Setters
    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
