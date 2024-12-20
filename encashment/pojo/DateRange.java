package com.peoplestrong.timeoff.encashment.pojo;

import java.util.Date;

public class DateRange {
    
    private Date startDate;
    private Date endDate;
    private String name; // Assuming name is a part of the range

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
