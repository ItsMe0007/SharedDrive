package com.peoplestrong.timeoff.encashment.flatTO;

import java.io.Serializable;
import java.util.Date;

public class CalendarBlockTO implements Serializable {
    
    private static final long serialVersionUID = 4882319729919097426L;
    
    private Long calenderBlockID;
    private String blockName;
    private Date startDate;
    private Date endDate;
    private Integer startDateID;
    private Integer endDateID;
    private Boolean active;
    private Integer tenantID;

    public Long getCalenderBlockID() {
        return calenderBlockID;
    }

    public void setCalenderBlockID(Long calenderBlockID) {
        this.calenderBlockID = calenderBlockID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getStartDateID() {
        return startDateID;
    }

    public void setStartDateID(Integer startDateID) {
        this.startDateID = startDateID;
    }

    public Integer getEndDateID() {
        return endDateID;
    }

    public void setEndDateID(Integer endDateID) {
        this.endDateID = endDateID;
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
}
