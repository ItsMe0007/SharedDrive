package com.peoplestrong.timeoff.encashment.pojo;

import com.peoplestrong.timeoff.dataservice.model.encashment.TmAgeBasedEncashment;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmAgeBasedEncashmentDetail;

import java.util.List;

public class AgeBasedEncashmentResponseTo {

    List<TmAgeBasedEncashmentDetail> tmAgeBasedEncashmentDetailsSavedListinDb;
    TmAgeBasedEncashment tmAgeBasedEncashment;

    public List<TmAgeBasedEncashmentDetail> getTmAgeBasedEncashmentDetailsSavedListinDb() {
        return tmAgeBasedEncashmentDetailsSavedListinDb;
    }

    public void setTmAgeBasedEncashmentDetailsSavedListinDb(List<TmAgeBasedEncashmentDetail> tmAgeBasedEncashmentDetailsSavedListinDb) {
        this.tmAgeBasedEncashmentDetailsSavedListinDb = tmAgeBasedEncashmentDetailsSavedListinDb;
    }

    public TmAgeBasedEncashment getTmAgeBasedEncashment() {
        return tmAgeBasedEncashment;
    }

    public void setTmAgeBasedEncashment(TmAgeBasedEncashment tmAgeBasedEncashment) {
        this.tmAgeBasedEncashment = tmAgeBasedEncashment;
    }

    public AgeBasedEncashmentResponseTo() {
    }

    public AgeBasedEncashmentResponseTo(List<TmAgeBasedEncashmentDetail> tmAgeBasedEncashmentDetailsSavedListinDb, TmAgeBasedEncashment tmAgeBasedEncashment) {
        this.tmAgeBasedEncashmentDetailsSavedListinDb = tmAgeBasedEncashmentDetailsSavedListinDb;
        this.tmAgeBasedEncashment = tmAgeBasedEncashment;
    }
}

