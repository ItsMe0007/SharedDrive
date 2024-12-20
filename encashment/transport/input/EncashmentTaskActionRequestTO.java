package com.peoplestrong.timeoff.encashment.transport.input;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class EncashmentTaskActionRequestTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @JsonProperty("bulkTaskList")
    private List<EncashmentTaskActionTO> bulkTaskList;
    
    @JsonProperty("encashmentTaskActionTO")
    private EncashmentTaskActionTO encashmentTaskActionTO;
    
    @JsonProperty("actionType")
    private String actionType;
    
    @JsonProperty("approverComments")
    private String approverComments;
    
    @JsonProperty("loginUserID")
    public int loginUserID;
    
    @JsonProperty("loginEmployeeID")
    public int loginEmployeeID;
    
    @JsonProperty("organizationID")
    public int organizationID;
    
    @JsonProperty("actionTypeFromMobile")
    public String actionTypeFromMobile;
    
    public List<EncashmentTaskActionTO> getBulkTaskList() {
        return bulkTaskList;
    }
    
    public void setBulkTaskList(List<EncashmentTaskActionTO> bulkTaskList) {
        this.bulkTaskList = bulkTaskList;
    }
    
    public EncashmentTaskActionTO getEncashmentTaskActionTO() {
        return encashmentTaskActionTO;
    }
    
    public void setEncashmentTaskActionTO(EncashmentTaskActionTO encashmentTaskActionTO) {
        this.encashmentTaskActionTO = encashmentTaskActionTO;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public String getApproverComments() {
        return approverComments;
    }
    
    public void setApproverComments(String approverComments) {
        this.approverComments = approverComments;
    }
    
    public int getLoginUserID() {
        return loginUserID;
    }
    
    public void setLoginUserID(int loginUserID) {
        this.loginUserID = loginUserID;
    }
    
    public int getLoginEmployeeID() {
        return loginEmployeeID;
    }
    
    public void setLoginEmployeeID(int loginEmployeeID) {
        this.loginEmployeeID = loginEmployeeID;
    }
    
    public int getOrganizationID() {
        return organizationID;
    }
    
    public void setOrganizationID(int organizationID) {
        this.organizationID = organizationID;
    }
    
    public String getActionTypeFromMobile() {
        return actionTypeFromMobile;
    }
    
    public void setActionTypeFromMobile(String actionTypeFromMobile) {
        this.actionTypeFromMobile = actionTypeFromMobile;
    }
    
    public EncashmentTaskActionRequestTO(List<EncashmentTaskActionTO> bulkTaskList, EncashmentTaskActionTO encashmentTaskActionTO, String actionType, String approverComments, int loginUserID, int loginEmployeeID, int organizationID, String actionTypeFromMobile) {
        this.bulkTaskList = bulkTaskList;
        this.encashmentTaskActionTO = encashmentTaskActionTO;
        this.actionType = actionType;
        this.approverComments = approverComments;
        this.loginUserID = loginUserID;
        this.loginEmployeeID = loginEmployeeID;
        this.organizationID = organizationID;
        this.actionTypeFromMobile = actionTypeFromMobile;
    }
    
    public EncashmentTaskActionRequestTO() {
    }
}
