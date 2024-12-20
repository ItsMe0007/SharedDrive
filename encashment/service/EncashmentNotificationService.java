package com.peoplestrong.timeoff.encashment.service;

import java.util.ResourceBundle;

import com.peoplestrong.timeoff.common.io.UserInfo;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentRequestRestTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentTaskActionRequestTO;
import com.peoplestrong.timeoff.leave.trasnport.input.LeaveRequestRestTO;
import com.peoplestrong.timeoff.leave.trasnport.input.LeaveTaskRequest;
import org.springframework.stereotype.Service;

@Service
public interface EncashmentNotificationService {

    void sendEncMobileNotification (EncashmentRequestRestTO request, Object finalResultObject, UserInfo userInfo, ResourceBundle resourceBundle);

    void sendEncMobileNotificationForLeaveAction (Object finalResultObject, UserInfo userInfo, ResourceBundle resourceBundle, EncashmentTaskActionRequestTO request);


}
