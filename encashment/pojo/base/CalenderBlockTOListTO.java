package com.peoplestrong.timeoff.encashment.pojo.base;

public class CalenderBlockTOListTO {
    
    private Long calendarBlockId;
    private String calendarBlockCode;
    private Long calendarGroupId;
    
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
    
    public Long getCalendarGroupId() {
        return calendarGroupId;
    }
    
    public void setCalendarGroupId(Long calendarGroupId) {
        this.calendarGroupId = calendarGroupId;
    }
    
    public CalenderBlockTOListTO(Long calendarBlockId, String calendarBlockCode, Long calendarGroupId) {
        this.calendarBlockId = calendarBlockId;
        this.calendarBlockCode = calendarBlockCode;
        this.calendarGroupId = calendarGroupId;
    }
    
    public CalenderBlockTOListTO() {
    }
}
