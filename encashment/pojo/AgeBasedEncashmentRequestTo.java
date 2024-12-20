package com.peoplestrong.timeoff.encashment.pojo;

import java.util.List;

public class AgeBasedEncashmentRequestTo {
    TmAgeBasedEncashmentRequestTo ageBasedEncashment;
    List<TmAgeBasedEncashmentDetailRequestTo> encashmentDetails;

    public TmAgeBasedEncashmentRequestTo getAgeBasedEncashment() {
        return ageBasedEncashment;
    }

    public void setAgeBasedEncashment(TmAgeBasedEncashmentRequestTo ageBasedEncashment) {
        this.ageBasedEncashment = ageBasedEncashment;
    }

    public List<TmAgeBasedEncashmentDetailRequestTo> getEncashmentDetails() {
        return encashmentDetails;
    }

    public void setEncashmentDetails(List<TmAgeBasedEncashmentDetailRequestTo> encashmentDetails) {
        this.encashmentDetails = encashmentDetails;
    }

    public AgeBasedEncashmentRequestTo() {
    }

    public AgeBasedEncashmentRequestTo(TmAgeBasedEncashmentRequestTo ageBasedEncashment, List<TmAgeBasedEncashmentDetailRequestTo> encashmentDetails) {
        this.ageBasedEncashment = ageBasedEncashment;
        this.encashmentDetails = encashmentDetails;
    }
}
