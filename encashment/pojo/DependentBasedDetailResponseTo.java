package com.peoplestrong.timeoff.encashment.pojo;

import com.peoplestrong.timeoff.dataservice.model.encashment.TmDependentBasedEncashment;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmDependentBasedEncashmentDetail;

import java.util.List;

public class DependentBasedDetailResponseTo {

    TmDependentBasedEncashment tmDependentBasedEncashment;
    List<TmDependentBasedEncashmentDetail> tmDependentDetailsSaved;

    public DependentBasedDetailResponseTo() {
    }

    public DependentBasedDetailResponseTo(TmDependentBasedEncashment tmDependentBasedEncashment, List<TmDependentBasedEncashmentDetail> tmDependentDetailsSaved) {
        this.tmDependentBasedEncashment = tmDependentBasedEncashment;
        this.tmDependentDetailsSaved = tmDependentDetailsSaved;
    }

    public TmDependentBasedEncashment getTmDependentBasedEncashment() {
        return tmDependentBasedEncashment;
    }

    public void setTmDependentBasedEncashment(TmDependentBasedEncashment tmDependentBasedEncashment) {
        this.tmDependentBasedEncashment = tmDependentBasedEncashment;
    }

    public List<TmDependentBasedEncashmentDetail> getTmDependentDetailsSaved() {
        return tmDependentDetailsSaved;
    }

    public void setTmDependentDetailsSaved(List<TmDependentBasedEncashmentDetail> tmDependentDetailsSaved) {
        this.tmDependentDetailsSaved = tmDependentDetailsSaved;
    }
}
