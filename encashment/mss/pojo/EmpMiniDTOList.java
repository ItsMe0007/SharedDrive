package com.peoplestrong.timeoff.encashment.mss.pojo;

import java.io.Serializable;
import java.util.List;

public class EmpMiniDTOList implements Serializable {
    
    private static final long serialVersionUID = 1L;

    public List<EmpMiniDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmpMiniDTO> employees) {
        this.employees = employees;
    }

    private List<EmpMiniDTO> employees;
}
