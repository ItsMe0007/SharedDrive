package com.peoplestrong.timeoff.encashment.pojo;

import com.peoplestrong.timeoff.translation.interfaces.TranslatedObject;

import java.io.Serializable;


public class DependentBasedRequestTo implements Serializable, TranslatedObject {
    
    private static final long serialVersionUID = 1L;
    
    private Long dependentBasedEncashmentID;
    private String configurationName;
    private Boolean active;
    private Integer tenantID;
    private Integer organizationId;
    
    //Default Constructor
    public DependentBasedRequestTo() {
    }

    // Parameterized constructor

    public DependentBasedRequestTo(String configurationName, Boolean active, Integer organizationId, Long dependentBasedEncashmentID, Integer tenantID) {
        this.dependentBasedEncashmentID = dependentBasedEncashmentID;
        this.configurationName = configurationName;
        this.active = active;
        this.organizationId = organizationId;
        this.tenantID = tenantID;
    }

    // Getters and Setters

    public Long getDependentBasedEncashmentID() {
        return dependentBasedEncashmentID;
    }

    public void setDependentBasedEncashmentID(Long dependentBasedEncashmentID) {
        this.dependentBasedEncashmentID = dependentBasedEncashmentID;
    }

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
