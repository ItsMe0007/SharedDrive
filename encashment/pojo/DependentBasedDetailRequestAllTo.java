package com.peoplestrong.timeoff.encashment.pojo;

import com.peoplestrong.timeoff.translation.interfaces.TranslatedObject;

import java.io.Serializable;
import java.util.List;

public class DependentBasedDetailRequestAllTo implements TranslatedObject, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    DependentBasedRequestTo dependentBasedRequestTo;
    List<DependentBasedDetailRequestTo> encashmentDetails;

    //Default Constructor
    public DependentBasedDetailRequestAllTo() {
    }

    public DependentBasedDetailRequestAllTo(DependentBasedRequestTo dependentBasedRequestTo, List<DependentBasedDetailRequestTo> encashmentDetails) {
        this.dependentBasedRequestTo = dependentBasedRequestTo;
        this.encashmentDetails = encashmentDetails;
    }

    public DependentBasedRequestTo getDependentBasedRequestTo() {
        return dependentBasedRequestTo;
    }

    public void setDependentBasedRequestTo(DependentBasedRequestTo dependentBasedRequestTo) {
        this.dependentBasedRequestTo = dependentBasedRequestTo;
    }

    public List<DependentBasedDetailRequestTo> getEncashmentDetails() {
        return encashmentDetails;
    }

    public void setEncashmentDetails(List<DependentBasedDetailRequestTo> encashmentDetails) {
        this.encashmentDetails = encashmentDetails;
    }
}
