package com.peoplestrong.timeoff.encashment.pojo;


public class DependentBasedEncashmentDetailResponseTo {

    private Long dependentBasedEncashmentId;
    private Integer startAge;
    private Integer endAge;
    private Integer percentageEncashment;
    private Integer tenantID;
    private Integer organizationId;

    public DependentBasedEncashmentDetailResponseTo() {
    }

    public DependentBasedEncashmentDetailResponseTo(Long dependentBasedEncashmentId, Integer startAge, Integer endAge, Integer percentageEncashment, Integer tenantID, Integer organizationId) {
        this.dependentBasedEncashmentId = dependentBasedEncashmentId;
        this.startAge = startAge;
        this.endAge = endAge;
        this.percentageEncashment = percentageEncashment;
        this.tenantID = tenantID;
        this.organizationId = organizationId;
    }

    //Getters and Setters


    public Long getDependentBasedEncashmentId() {
        return dependentBasedEncashmentId;
    }

    public void setDependentBasedEncashmentId(Long dependentBasedEncashmentId) {
        this.dependentBasedEncashmentId = dependentBasedEncashmentId;
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

    public static DependentBasedEncashmentDetailResponseTo Builder() {
        return new DependentBasedEncashmentDetailResponseTo();
    }
}
