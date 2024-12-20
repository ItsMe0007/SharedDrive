package com.peoplestrong.timeoff.encashment.pojo;

import java.util.Date;

public class CalenderBlockTo {
    private Long calenderBlockID;
    private Date startDate;
    private Date endDate;
    private Integer yearseperation;
    private  Boolean active;
    private String blockName;


// Getters and setters

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

    public Integer getYearseperation() {
        return yearseperation;
    }

    public void setYearseperation(Integer yearseperation) {
        this.yearseperation = yearseperation;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public Long getCalenderBlockID() {
        return calenderBlockID;
    }

    public void setCalenderBlockID(Long calenderBlockID) {
        this.calenderBlockID = calenderBlockID;
    }


}
