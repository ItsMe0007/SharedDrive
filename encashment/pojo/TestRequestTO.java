package com.peoplestrong.timeoff.encashment.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.peoplestrong.timeoff.leave.pojo.LeaveDurationTO;
import com.peoplestrong.timeoff.leave.pojo.LeaveListingTO;
import com.peoplestrong.timeoff.leave.pojo.LeaveQuotaTO;
import com.peoplestrong.timeoff.translation.interfaces.TranslatedObject;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class TestRequestTO implements TranslatedObject, Serializable {

    private Integer testId;
    private String testName;
    @JsonProperty("inputData")
    private T object;

    public TestRequestTO(Integer testId, String testName) {
        this.testId = testId;
        this.testName = testName;
    }

    public Integer getTestId() {
        return testId;
    }
    public T getObject()
    {
        return object;
    }

    public void setObject(T object)
    {
        this.object = object;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
    public LeaveListingTO leaveListingTO;
    public List<LeaveQuotaTO> leaveQuotaTOList;
    public List<LeaveDurationTO> durationTOList;
    public LeaveListingTO getLeaveListingTO() {
        return leaveListingTO;
    }
    public void setLeaveListingTO(LeaveListingTO leaveListingTO) {
        this.leaveListingTO = leaveListingTO;
    }
    public List<LeaveQuotaTO> getLeaveQuotaTOList() {
        return leaveQuotaTOList;
    }
    public void setLeaveQuotaTOList(List<LeaveQuotaTO> leaveQuotaTOList) {
        this.leaveQuotaTOList = leaveQuotaTOList;
    }
    public List<LeaveDurationTO> getDurationTOList() {
        return durationTOList;
    }
    public void setDurationTOList(List<LeaveDurationTO> durationTOList) {
        this.durationTOList = durationTOList;
    }

}
