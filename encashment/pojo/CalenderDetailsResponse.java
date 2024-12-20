package com.peoplestrong.timeoff.encashment.pojo;

import com.peoplestrong.timeoff.dataservice.model.encashment.TmCalenderBlock;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmCalenderBlockDetail;

import java.util.List;
import java.util.Optional;

public class CalenderDetailsResponse {
    
    private List<TmCalenderBlockDetail> calenderBlocklistdetails;
    private Optional<TmCalenderBlock> calenderBlock;

    // Constructor
    public CalenderDetailsResponse(List<TmCalenderBlockDetail> calenderBlocklistdetails,
                                   Optional<TmCalenderBlock> calenderBlock) {
        this.calenderBlocklistdetails = calenderBlocklistdetails;
        this.calenderBlock = calenderBlock;
    }

    // Getters and Setters
    public List<TmCalenderBlockDetail> getCalenderBlocklistdetails() {
        return calenderBlocklistdetails;
    }

    public void setCalenderBlocklistdetails(List<TmCalenderBlockDetail> calenderBlocklistdetails) {
        this.calenderBlocklistdetails = calenderBlocklistdetails;
    }

    public Optional<TmCalenderBlock> getCalenderBlock() {
        return calenderBlock;
    }

    public void setCalenderBlock(Optional<TmCalenderBlock> calenderBlock) {
        this.calenderBlock = calenderBlock;
    }
}
