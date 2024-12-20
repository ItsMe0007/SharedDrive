package com.peoplestrong.timeoff.encashment.mss.pojo;
import java.util.Date;
public class CalendarBlockDetailsTO {
    
    private Long calendarBlockId;
    private String calendarBlockCode;
    private Integer encashmentCountInBlockInDays;
    private Integer encashmentCountInBlockInHrs;
    private Date startDate;
    private Date endDate;

    public Long getCalendarBlockId() {
        return calendarBlockId;
    }

    public void setCalendarBlockId(Long calendarBlockId) {
        this.calendarBlockId = calendarBlockId;
    }

    public String getCalendarBlockCode() {
        return calendarBlockCode;
    }

    public void setCalendarBlockCode(String calendarBlockCode) {
        this.calendarBlockCode = calendarBlockCode;
    }

    public Integer getEncashmentCountInBlockInDays() {
        return encashmentCountInBlockInDays;
    }

    public void setEncashmentCountInBlockInDays(Integer encashmentCountInBlockInDays) {
        this.encashmentCountInBlockInDays = encashmentCountInBlockInDays;
    }

    public Integer getEncashmentCountInBlockInHrs() {
        return encashmentCountInBlockInHrs;
    }

    public void setEncashmentCountInBlockInHrs(Integer encashmentCountInBlockInHrs) {
        this.encashmentCountInBlockInHrs = encashmentCountInBlockInHrs;
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
}