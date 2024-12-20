package com.peoplestrong.timeoff.encashment.transport.input;

public class EncashmentTaskActionTO {
    
    private String action;
    
    private String comment;
    
    private String managerComment;
    
    private String withdrawlComment;
    
    private String ruleOutput;
    
    private Integer stageActionId;
    
    private Long employeeEncashmentId;
    
    private Integer employeeId;
    
    private Integer userId;
    
    private Long sysUserWorkflowHistoryId;
    
    private String stageType;
    
    private Integer sysWorkflowStageID;
    
    private String band;
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public String getManagerComment() {
        return managerComment;
    }
    
    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }
    
    public String getWithdrawlComment() {
        return withdrawlComment;
    }
    
    public void setWithdrawlComment(String withdrawlComment) {
        this.withdrawlComment = withdrawlComment;
    }
    
    public String getRuleOutput() {
        return ruleOutput;
    }
    
    public void setRuleOutput(String ruleOutput) {
        this.ruleOutput = ruleOutput;
    }
    
    public Integer getStageActionId() {
        return stageActionId;
    }
    
    public void setStageActionId(Integer stageActionId) {
        this.stageActionId = stageActionId;
    }
    
    public Long getEmployeeEncashmentId() {
        return employeeEncashmentId;
    }
    
    public void setEmployeeEncashmentId(Long employeeEncashmentId) {
        this.employeeEncashmentId = employeeEncashmentId;
    }
    
    public Integer getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Long getSysUserWorkflowHistoryId() {
        return sysUserWorkflowHistoryId;
    }
    
    public void setSysUserWorkflowHistoryId(Long sysUserWorkflowHistoryId) {
        this.sysUserWorkflowHistoryId = sysUserWorkflowHistoryId;
    }
    
    public String getStageType() {
        return stageType;
    }
    
    public void setStageType(String stageType) {
        this.stageType = stageType;
    }
    
    public Integer getSysWorkflowStageID() {
        return sysWorkflowStageID;
    }
    
    public void setSysWorkflowStageID(Integer sysWorkflowStageID) {
        this.sysWorkflowStageID = sysWorkflowStageID;
    }
    
    public String getBand() {
        return band;
    }
    
    public void setBand(String band) {
        this.band = band;
    }
    
    public EncashmentTaskActionTO(String action, String comment, String managerComment, String withdrawlComment, String ruleOutput, Integer stageActionId, Long employeeEncashmentId, Integer employeeId, Integer userId, Long sysUserWorkflowHistoryId, String stageType, Integer sysWorkflowStageID, String band) {
        this.action = action;
        this.comment = comment;
        this.managerComment = managerComment;
        this.withdrawlComment = withdrawlComment;
        this.ruleOutput = ruleOutput;
        this.stageActionId = stageActionId;
        this.employeeEncashmentId = employeeEncashmentId;
        this.employeeId = employeeId;
        this.userId = userId;
        this.sysUserWorkflowHistoryId = sysUserWorkflowHistoryId;
        this.stageType = stageType;
        this.sysWorkflowStageID = sysWorkflowStageID;
        this.band = band;
    }
    
    public EncashmentTaskActionTO() {
    }
}
