package com.peoplestrong.timeoff.encashment.pojo;

import java.math.BigDecimal;

public class EncashmentTypeRequestTO {
    private Long encashmentTypeID;
    private SelectItem encashmentType;
    private SelectItem encashmentCalenderGroup;
    private Boolean active;
    private Boolean attachmentMandatory;
    private Float maxEncashmentAllowed;
    private Float minEncashmentAllowed;
    private Integer maxTransactions;
    private Boolean allowDuringNotice;
    private Boolean allowDuringProbation;
    private SelectItem ageBasedEncashment;
    private SelectItem dependentBasedEncashment;

    public Long getEncashmentTypeID() {
        return encashmentTypeID;
    }

    public void setEncashmentTypeID(Long encashmentTypeID) {
        this.encashmentTypeID = encashmentTypeID;
    }

    public SelectItem getEncashmentType() {
        return encashmentType;
    }

    public void setEncashmentType(SelectItem encashmentType) {
        this.encashmentType = encashmentType;
    }

    public SelectItem getEncashmentCalenderGroup() {
        return encashmentCalenderGroup;
    }

    public void setEncashmentCalenderGroup(SelectItem encashmentCalenderGroup) {
        this.encashmentCalenderGroup = encashmentCalenderGroup;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getAttachmentMandatory() {
        return attachmentMandatory;
    }

    public void setAttachmentMandatory(Boolean attachmentMandatory) {
        this.attachmentMandatory = attachmentMandatory;
    }

    public Float getMaxEncashmentAllowed() {
        return maxEncashmentAllowed;
    }

    public void setMaxEncashmentAllowed(Float maxEncashmentAllowed) {
        this.maxEncashmentAllowed = maxEncashmentAllowed;
    }

    public Float getMinEncashmentAllowed() {
        return minEncashmentAllowed;
    }

    public void setMinEncashmentAllowed(Float minEncashmentAllowed) {
        this.minEncashmentAllowed = minEncashmentAllowed;
    }

    public Integer getMaxTransactions() {
        return maxTransactions;
    }

    public void setMaxTransactions(Integer maxTransactions) {
        this.maxTransactions = maxTransactions;
    }

    public Boolean getAllowDuringNotice() {
        return allowDuringNotice;
    }

    public void setAllowDuringNotice(Boolean allowDuringNotice) {
        this.allowDuringNotice = allowDuringNotice;
    }

    public Boolean getAllowDuringProbation() {
        return allowDuringProbation;
    }

    public void setAllowDuringProbation(Boolean allowDuringProbation) {
        this.allowDuringProbation = allowDuringProbation;
    }

    public SelectItem getAgeBasedEncashment() {
        return ageBasedEncashment;
    }

    public void setAgeBasedEncashment(SelectItem ageBasedEncashment) {
        this.ageBasedEncashment = ageBasedEncashment;
    }

    public SelectItem getDependentBasedEncashment() {
        return dependentBasedEncashment;
    }

    public void setDependentBasedEncashment(SelectItem dependentBasedEncashment) {
        this.dependentBasedEncashment = dependentBasedEncashment;
    }
}
