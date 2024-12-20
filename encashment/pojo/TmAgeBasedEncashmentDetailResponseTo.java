package com.peoplestrong.timeoff.encashment.pojo;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class TmAgeBasedEncashmentDetailResponseTo implements Serializable {
    private List<AgeBasedEncashmentDetailsResponse> ageBasedEncashmentDetailsResponse;

    private String configurationName;

    private Boolean active;

    public TmAgeBasedEncashmentDetailResponseTo() {
    }

    public TmAgeBasedEncashmentDetailResponseTo(List<AgeBasedEncashmentDetailsResponse> ageBasedEncashmentDetailsResponse, String configurationName, Boolean active) {
        this.ageBasedEncashmentDetailsResponse = ageBasedEncashmentDetailsResponse;
        this.configurationName = configurationName;
        this.active = active;
    }

    public List<AgeBasedEncashmentDetailsResponse> getAgeBasedEncashmentDetailsResponse() {
        return ageBasedEncashmentDetailsResponse;
    }

    public void setAgeBasedEncashmentDetailsResponse(List<AgeBasedEncashmentDetailsResponse> ageBasedEncashmentDetailsResponse) {
        this.ageBasedEncashmentDetailsResponse = ageBasedEncashmentDetailsResponse;
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


