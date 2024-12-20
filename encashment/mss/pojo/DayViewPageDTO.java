package com.peoplestrong.timeoff.encashment.mss.pojo;


import java.io.Serializable;
import java.util.List;
public class DayViewPageDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public Integer records;
    public List<DayViewDTO> dayViewList;

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public List<DayViewDTO> getDayViewList() {
        return dayViewList;
    }

    public void setDayViewList(List<DayViewDTO> dayViewList) {
        this.dayViewList = dayViewList;
    }
}
