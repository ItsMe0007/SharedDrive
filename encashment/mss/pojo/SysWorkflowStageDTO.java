package com.peoplestrong.timeoff.encashment.mss.pojo;

import com.peoplestrong.timeoff.dataservice.model.leave.SysWorkflowStage;

import java.util.List;


public class SysWorkflowStageDTO {
    
    private static final long serialVersionUID = 1L;

    private List<SysWorkflowStage> stage1;
    private List<SysWorkflowStage> stage2;
    private List<SysWorkflowStage> stage3;

    public List<SysWorkflowStage> getStage1() {
        return stage1;
    }

    public void setStage1(List<SysWorkflowStage> stage1) {
        this.stage1 = stage1;
    }

    public List<SysWorkflowStage> getStage2() {
        return stage2;
    }

    public void setStage2(List<SysWorkflowStage> stage2) {
        this.stage2 = stage2;
    }

    public List<SysWorkflowStage> getStage3() {
        return stage3;
    }

    public void setStage3(List<SysWorkflowStage> stage3) {
        this.stage3 = stage3;
    }
}
