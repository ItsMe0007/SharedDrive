package com.peoplestrong.timeoff.encashment.pojo;


public class EncashmentTaskRequestTO {
    private Long encashmentId;
    private Integer employeeId;

    public Long getEncashmentId() {
        return encashmentId;
    }

    public void setEncashmentId(Long encashmentId) {
        this.encashmentId = encashmentId;
    }
    
    public Integer getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
