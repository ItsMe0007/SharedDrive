package com.peoplestrong.timeoff.encashment.pojo;

public class CalendarBlocksTO {

     private Long  calendarBlockId;
     private String calendarBlock;
     private Long calendarGroupId;
     private Long encashmentTypeId;

    public Long getCalendarBlockId() {
        return calendarBlockId;
    }

    public void setCalendarBlockId(Long calendarBlockId) {
        this.calendarBlockId = calendarBlockId;
    }

    public String getCalendarBlock() {
        return calendarBlock;
    }

    public void setCalendarBlock(String calendarBlock) {
        this.calendarBlock = calendarBlock;
    }

    public Long getCalendarGroupId() {
        return calendarGroupId;
    }

    public void setCalendarGroupId(Long calendarGroupId) {
        this.calendarGroupId = calendarGroupId;
    }

    public Long getEncashmentTypeId() {
        return encashmentTypeId;
    }

    public void setEncashmentTypeId(Long encashmentTypeId) {
        this.encashmentTypeId = encashmentTypeId;
    }

    public CalendarBlocksTO(Long calendarBlockId, String calendarBlock, Long calendarGroupId, Long encashmentTypeId) {
        this.calendarBlockId = calendarBlockId;
        this.calendarBlock = calendarBlock;
        this.calendarGroupId = calendarGroupId;
        this.encashmentTypeId = encashmentTypeId;
    }

    public CalendarBlocksTO() {
    }

}
