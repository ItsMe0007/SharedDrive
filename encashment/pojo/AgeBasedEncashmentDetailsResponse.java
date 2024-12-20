package com.peoplestrong.timeoff.encashment.pojo;

public class AgeBasedEncashmentDetailsResponse{
    private Long ageBasedEncashmentId;
    private Integer startAge;
    private Integer endAge;
    private Float encashmentLimit;
    private Integer tenantID;
    private Integer organizationId;

    public Long getAgeBasedEncashmentId() {
        return ageBasedEncashmentId;
    }

    public void setAgeBasedEncashmentId(Long ageBasedEncashmentId) {
        this.ageBasedEncashmentId = ageBasedEncashmentId;
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

    public AgeBasedEncashmentDetailsResponse(Long ageBasedEncashmentId, Integer startAge, Integer endAge, Float encashmentLimit, Integer tenantID, Integer organizationId) {
        this.ageBasedEncashmentId = ageBasedEncashmentId;
        this.startAge = startAge;
        this.endAge = endAge;
        this.encashmentLimit = encashmentLimit;
        this.tenantID = tenantID;
        this.organizationId = organizationId;
    }

    public AgeBasedEncashmentDetailsResponse() {
    }

    public static AgeBasedEncashmentDetailsResponse Builder(){
        return new AgeBasedEncashmentDetailsResponse();
    }
}
