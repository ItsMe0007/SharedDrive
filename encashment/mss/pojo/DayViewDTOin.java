package com.peoplestrong.timeoff.encashment.mss.pojo;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DayViewDTOin implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public String date;
    public Integer offset;
    public String sortby;
    public String gsearch;
    public Map<String, List<Integer>> filterby;
    public List<EGenericFilter> filterby2;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getSortby() {
        return sortby;
    }

    public void setSortby(String sortby) {
        this.sortby = sortby;
    }

    public String getGsearch() {
        return gsearch;
    }

    public void setGsearch(String gsearch) {
        this.gsearch = gsearch;
    }

    public Map<String, List<Integer>> getFilterby() {
        return filterby;
    }

    public void setFilterby(Map<String, List<Integer>> filterby) {
        this.filterby = filterby;
    }

    public List<EGenericFilter> getFilterby2() {
        return filterby2;
    }

    public void setFilterby2(List<EGenericFilter> filterby2) {
        this.filterby2 = filterby2;
    }
}
