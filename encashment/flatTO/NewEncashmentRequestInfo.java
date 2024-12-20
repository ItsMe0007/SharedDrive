package com.peoplestrong.timeoff.encashment.flatTO;

import com.peoplestrong.timeoff.encashment.pojo.EncashmentLeaveQuotaTO;
import com.peoplestrong.timeoff.encashment.pojo.base.CalenderBlockTOListTO;
import com.peoplestrong.timeoff.encashment.pojo.base.DependentDetailTO;
import com.peoplestrong.timeoff.encashment.pojo.base.EncashmentTOListTO;
import com.peoplestrong.timeoff.encashment.pojo.base.LeaveTOListTO;

import java.util.List;
import java.util.Map;

public class NewEncashmentRequestInfo {
    
    private List<LeaveTOListTO> leaveTOList;
    private List<EncashmentTOListTO> encashmentTOList;
    private List<DependentDetailTO> dependentDetailTOList;
    private List<CalenderBlockTOListTO> calenderBlockTOList;
    private List<EncashmentLeaveQuotaTO>  leaveQuotaTOList;
    
    public List<LeaveTOListTO> getLeaveTOList() {
        return leaveTOList;
    }
    
    public void setLeaveTOList(List<LeaveTOListTO> leaveTOList) {
        this.leaveTOList = leaveTOList;
    }
    
    public List<EncashmentTOListTO> getEncashmentTOList() {
        return encashmentTOList;
    }
    
    public void setEncashmentTOList(List<EncashmentTOListTO> encashmentTOList) {
        this.encashmentTOList = encashmentTOList;
    }
    
    public List<DependentDetailTO> getDependentDetailTOList() {
        return dependentDetailTOList;
    }
    
    public void setDependentDetailTOList(List<DependentDetailTO> dependentDetailTOList) {
        this.dependentDetailTOList = dependentDetailTOList;
    }
    
    public List<CalenderBlockTOListTO> getCalenderBlockTOList() {
        return calenderBlockTOList;
    }
    
    public void setCalenderBlockTOList(List<CalenderBlockTOListTO> calenderBlockTOList) {
        this.calenderBlockTOList = calenderBlockTOList;
    }
    
    public List<EncashmentLeaveQuotaTO> getLeaveQuotaTOList() {
        return leaveQuotaTOList;
    }
    
    public void setLeaveQuotaTOList(List<EncashmentLeaveQuotaTO> leaveQuotaTOList) {
        this.leaveQuotaTOList = leaveQuotaTOList;
    }
    
    public NewEncashmentRequestInfo(List<LeaveTOListTO> leaveTOList, List<EncashmentTOListTO> encashmentTOList, List<DependentDetailTO> dependentDetailTOList, List<CalenderBlockTOListTO> calenderBlockTOList, List<EncashmentLeaveQuotaTO> leaveQuotaTOList) {
        this.leaveTOList = leaveTOList;
        this.encashmentTOList = encashmentTOList;
        this.dependentDetailTOList = dependentDetailTOList;
        this.calenderBlockTOList = calenderBlockTOList;
        this.leaveQuotaTOList = leaveQuotaTOList;
    }
    
    public NewEncashmentRequestInfo() {
    }
}
