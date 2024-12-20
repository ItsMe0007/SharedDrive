package com.peoplestrong.timeoff.encashment.service;

import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.encashment.pojo.EncashmentApprovalHistoryTO;
import com.peoplestrong.timeoff.encashment.flatTO.NewEncashmentRequestInfo;
import com.peoplestrong.timeoff.encashment.pojo.EncashmentTaskDetailRequestTO;
import com.peoplestrong.timeoff.encashment.pojo.EncashmentTaskRequestTO;
import com.peoplestrong.timeoff.encashment.pojo.base.AppliedEncashmentListCommonTO;
import com.peoplestrong.timeoff.encashment.pojo.base.EncashmentListResponseTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentRequestRestTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentTaskActionRequestTO;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.workflow.to.TimeOffWorkFlowModel;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public interface EncashmentService extends Serializable {
    
    EncashmentTaskDetailRequestTO getLeaveTaskEncashmentDetail(UserInfo userInfo , EncashmentTaskRequestTO encashmentRequestTo) throws Exception;
    
    List<Integer> getActorIDAsPerEncashmentID(long encashmentId, Integer tenantId);
    
    List<Integer> getDelegatedActorId(long encashmentId, Integer tenantId);
    
    List<EncashmentApprovalHistoryTO> getApprovalEncashmentHistory(Long encashmentId, int tenantId, int organizationID) throws Exception;

    List<Object[]> getUserIdFromTmemployeeEncashment(Long encashmentId);

    Object doEncashmentTransaction(EncashmentRequestRestTO request, UserInfo userInfo) throws Exception;

    NewEncashmentRequestInfo getNewEncashmentRequestInfo(int organizationId, int employeeId, String bundleName);

    List<TimeOffWorkFlowModel> getStageActionList(TimeOffWorkFlowModel request, com.peoplestrong.timeoff.leave.pojo.UserInfo userInfo)throws AppRuntimeException;

    Object doEncashmentTaskAction(EncashmentTaskActionRequestTO restRequest, UserInfo userInfo) throws Exception;

    List<EncashmentListResponseTO> getAppliedEncashmentList(UserInfo userInfo, AppliedEncashmentListCommonTO restRequest) throws Exception;

    List<EncashmentTaskDetailRequestTO> getEncashmentTasksDetails(List<EncashmentTaskRequestTO> restRequest, UserInfo userInfo) throws Exception;
}
