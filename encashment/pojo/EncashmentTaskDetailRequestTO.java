package com.peoplestrong.timeoff.encashment.pojo;

import com.peoplestrong.timeoff.leave.pojo.LeaveQuotaTO;
import com.peoplestrong.timeoff.translation.interfaces.TranslatedObject;

import java.io.Serializable;
import java.util.List;

public class EncashmentTaskDetailRequestTO implements TranslatedObject, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private EncashmentListingTo encashmentListingTo;
    private List<EncashmentLeaveQuotaTO> leaveBalance;

    public EncashmentListingTo getEncashmentListingTo() {
        return encashmentListingTo;
    }

    public void setEncashmentListingTo(EncashmentListingTo encashmentListingTo) {
        this.encashmentListingTo = encashmentListingTo;
    }

    public List<EncashmentLeaveQuotaTO> getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(List<EncashmentLeaveQuotaTO> leaveBalance) {
        this.leaveBalance = leaveBalance;
    }
}
