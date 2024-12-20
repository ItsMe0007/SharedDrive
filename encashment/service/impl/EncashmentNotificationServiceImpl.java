package com.peoplestrong.timeoff.encashment.service.impl;

import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.peoplestrong.timeoff.common.constant.MobileNotificationConstants;
import com.peoplestrong.timeoff.common.impl.service.CommonService;
import com.peoplestrong.timeoff.common.io.UserInfo;
import com.peoplestrong.timeoff.encashment.service.EncashmentNotificationService;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentRequestRestTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentTaskActionRequestTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentTaskActionTO;
import com.peoplestrong.timeoff.leave.service.Impl.NotificationServiceImpl;
import com.peoplestrong.timeoff.leave.service.ModuleConfigurationService;
import com.peoplestrong.timeoff.leave.service.NotificationService;
import com.peoplestrong.timeoff.leave.trasnport.input.LeaveTaskActionTO;
import com.peoplestrong.timeoff.leave.trasnport.input.LeaveTaskRequest;
import com.peoplestrong.timeoff.mobile.leave.service.NewLeaveMobileService;
import com.peoplestrong.timeoff.notification.service.MobileNotificationService;
import com.peoplestrong.timeoff.workflow.to.WorkflowHistoryResponseTO;
import org.springframework.stereotype.Service;

import static com.peoplestrong.timeoff.common.constant.ModuleConfigConstant.ENCASHMENT_INITIAL_FORM;
import static com.peoplestrong.timeoff.common.constant.ModuleConfigConstant.MGR_INITIATED_ENCASHMENT_FORM_NAME;
import static com.peoplestrong.timeoff.leave.constant.ILeaveErrorConstants.*;
import static com.peoplestrong.timeoff.leave.constant.LeaveConstant.*;

@Service
public class EncashmentNotificationServiceImpl  implements EncashmentNotificationService {

    public static final Logger logger = LoggerFactory.getLogger(EncashmentNotificationServiceImpl.class);

    private final NewLeaveMobileService newLeaveMobileService;
    private final CommonService commonService;
    private final ModuleConfigurationService moduleConfigurationService;
    private final MobileNotificationService mobileNotificationService;

    @Autowired
    public EncashmentNotificationServiceImpl (NewLeaveMobileService newLeaveMobileService, CommonService commonService,
                                    ModuleConfigurationService moduleConfigurationService, MobileNotificationService mobileNotificationService) {
        this.newLeaveMobileService = newLeaveMobileService;
        this.commonService = commonService;
        this.moduleConfigurationService = moduleConfigurationService;
        this.mobileNotificationService = mobileNotificationService;
    }



    @Override
    public void sendEncMobileNotification(EncashmentRequestRestTO request, Object result, UserInfo userInfo, ResourceBundle rb) {
        try {
            String messageBody;
            String requestType = request.getRequestType();
            switch (requestType) {
                case ENCASHMENT_INITIAL_FORM:
                    messageBody = rb.getString(ENCASHMENT_SUBMIT_MOBILE_NOTIFICATION_BODY) + commonService.getFullName(userInfo.getUserId());
                    newLeaveMobileService.sendMobileNotification((WorkflowHistoryResponseTO) result, userInfo.getOrganizationId(), messageBody,
                            rb.getString(ENCASHMENT_SUBMIT_MOBILE_NOTIFICATION_HEADER), "leaveTask", userInfo.getUserId(), null);
                    break;
                case MGR_INITIATED_ENCASHMENT_FORM_NAME:
                    messageBody = rb.getString(PROXY_ENCASHMENT_SUBMIT_NOTIFICATION_BODY);
                    sendNotificationToEmployee(userInfo.getOrganizationId(), request.getUserId(), rb.getString(PROXY_ENCASHMENT_SUBMIT_NOTIFICATION_HEADER),
                            messageBody);
                    break;
                default:
                    logger.info("Invalid Encashment Request Type");
            }
            //   sendNotificationForAbscondeeReturn(request, result, userInfo, rb);
        } catch (Exception e) {
            logger.error("Error while sending mobile notification for UserID :" + userInfo.getUserId());
        }
    }
    public void sendNotificationToEmployee (int organizationID, Integer userId, String messageTitle, String messageBody) {
        mobileNotificationService.sendNotificationToEmployee(organizationID, userId, messageTitle, messageBody, MobileNotificationConstants.NTYPE_LEAVE_TASK);
    }

    @Override
    public void sendEncMobileNotificationForLeaveAction (Object result, UserInfo userInfo, ResourceBundle rb, EncashmentTaskActionRequestTO request) {

        String actionType = request.getActionType();
        if (actionType != null && !actionType.trim().equals("")) {
            sendEncNotificationToMultipleUsers(userInfo, rb, request);
        } else {
            sendEncNotificationToSingleUser(result, userInfo, rb, request);
        }
    }

    private void sendEncNotificationToMultipleUsers (UserInfo userInfo, ResourceBundle rb, EncashmentTaskActionRequestTO request) {
        String actionType = request.getActionType();
        List<EncashmentTaskActionTO> bulkTaskList = request.getBulkTaskList();
        if (bulkTaskList == null || bulkTaskList.size() == 0) {
            logger.info("Invalid Request");
            return;
        }

        switch (actionType) {
            case WORKFLOW_APPROVE_ACTION:
                for (EncashmentTaskActionTO task: bulkTaskList) {
                    sendNotificationToEmployee(userInfo.getOrganizationId(), task.getUserId(), rb.getString(EMPLOYEE_ENCASHMENT_APPROVAL_NOTIFICATION_HEADER),
                            rb.getString(EMPLOYEE_ENCASHMENT_APPROVAL_NOTIFICATION_BODY));
                }
                break;
            case WORKFLOW_REJECT_ACTION:
                for (EncashmentTaskActionTO task: bulkTaskList) {
                    sendNotificationToEmployee(userInfo.getOrganizationId(), task.getUserId(), rb.getString(EMPLOYEE_LEAVE_REJECTION_NOTIFICATION_HEADER),
                            rb.getString(EMPLOYEE_ENCASHMENT_REJECTION_NOTIFICATION_BODY));
                }
                break;
            default:
                logger.info("Invalid Encashment Request Type");
        }
    }


    private void sendEncNotificationToSingleUser (Object result, UserInfo userInfo, ResourceBundle rb, EncashmentTaskActionRequestTO request) {
        try {
            String messageBody;
            String messageTitle;
            String action = request.getEncashmentTaskActionTO().getAction();
            String fullName = commonService.getFullName(request.getEncashmentTaskActionTO().getUserId());
            switch (action) {
                case WORKFLOW_WITHDRAW_ACTION:
                case WORKFLOW_SUBMIT_ACTION:
                    messageBody = rb.getString(ENCASHMENT_WITHDRAW_MOBILE_NOTIFICATION_BODY) + fullName;
                    messageTitle = rb.getString(ENCASHMENT_WITHDRAW_MOBILE_NOTIFICATION_HEADER);

                    sendNotificationToEmployee(userInfo.getOrganizationId(), request.getEncashmentTaskActionTO().getUserId(),
                            rb.getString(EMPLOYEE_ENCASHMENT_WITHDRAW_MOBILE_NOTIFICATION_HEADER),
                            rb.getString(EMPLOYEE_ENCASHMENT_WITHDRAW_MOBILE_NOTIFICATION_BODY));
                    break;
                case WORKFLOW_APPROVE_ACTION:
                    messageBody = rb.getString(ENCASHMENT_APPROVE_MOBILE_NOTIFICATION_BODY) + fullName;
                    messageTitle = rb.getString(ENCASHMENT_APPROVE_MOBILE_NOTIFICATION_HEADER);
                    sendNotificationToEmployee(userInfo.getOrganizationId(), request.getEncashmentTaskActionTO().getUserId(),
                            rb.getString(EMPLOYEE_ENCASHMENT_APPROVAL_NOTIFICATION_HEADER), rb.getString(EMPLOYEE_ENCASHMENT_APPROVAL_NOTIFICATION_BODY));
                    break;
                case WORKFLOW_REJECT_ACTION:
                    messageBody = rb.getString(ENCASHMENT_REJECT_MOBILE_NOTIFICATION_BODY) + fullName;
                    messageTitle = rb.getString(ENCASHMENT_REJECT_MOBILE_NOTIFICATION_HEADER);
                    sendNotificationToEmployee(userInfo.getOrganizationId(), request.getEncashmentTaskActionTO().getUserId(),
                            rb.getString(EMPLOYEE_ENCASHMENT_REJECTION_NOTIFICATION_HEADER), rb.getString(EMPLOYEE_ENCASHMENT_REJECTION_NOTIFICATION_BODY));
                    break;
                default:
                    logger.info("Invalid Action");
                    return;
            }

            newLeaveMobileService.sendMobileNotification((WorkflowHistoryResponseTO) result, userInfo.getOrganizationId(),
                    messageBody, messageTitle, "leaveTask", userInfo.getUserId(),null);
        } catch (Exception e) {
            logger.error("Error while sending mobile notification for UserID :" + userInfo.getUserId());
        }
    }
}
