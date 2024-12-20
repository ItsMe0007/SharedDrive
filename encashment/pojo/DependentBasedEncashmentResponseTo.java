package com.peoplestrong.timeoff.encashment.pojo;

import java.util.List;

public class DependentBasedEncashmentResponseTo {

    private List<DependentBasedEncashmentDetailResponseTo> dependentBasedEncashmentDetailResponseTos;
    private String configurationName;
    private Boolean active;

    public DependentBasedEncashmentResponseTo() {

    }

    public DependentBasedEncashmentResponseTo(List<DependentBasedEncashmentDetailResponseTo> dependentBasedEncashmentDetailResponseTos, String configurationName, Boolean active) {
        this.dependentBasedEncashmentDetailResponseTos = dependentBasedEncashmentDetailResponseTos;
        this.configurationName = configurationName;
        this.active = active;
    }

    public List<DependentBasedEncashmentDetailResponseTo> getDependentBasedEncashmentDetailResponseTos() {
        return dependentBasedEncashmentDetailResponseTos;
    }

    public void setDependentBasedEncashmentDetailResponseTos(List<DependentBasedEncashmentDetailResponseTo> dependentBasedEncashmentDetailResponseTos) {
        this.dependentBasedEncashmentDetailResponseTos = dependentBasedEncashmentDetailResponseTos;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
