package com.peoplestrong.timeoff.encashment.pojo;

import java.util.List;

public class EncashmentRequestTo {

    private CalenderBlockTo calenderTo;
    private List<DateRange> ranges;

    // Getters and setters

    public CalenderBlockTo getCalenderTo() {
        return calenderTo;
    }

    public void setCalenderTo(CalenderBlockTo calenderTo) {
        this.calenderTo = calenderTo;
    }

    public List<DateRange> getRanges() {
        return ranges;
    }

    public void setRanges(List<DateRange> ranges) {
        this.ranges = ranges;
    }
}
