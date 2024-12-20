package com.peoplestrong.timeoff.encashment.mss.pojo;


import java.util.List;

public class EGenericFilter {
    
    public String filterName;
    public List<Integer> valueList;

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public List<Integer> getValueList() {
        return valueList;
    }

    public void setValueList(List<Integer> valueList) {
        this.valueList = valueList;
    }
}
