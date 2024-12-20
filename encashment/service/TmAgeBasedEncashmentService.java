package com.peoplestrong.timeoff.encashment.service;

import com.peoplestrong.timeoff.dataservice.model.encashment.TmAgeBasedEncashment;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.encashment.pojo.AgeBasedEncashmentRequestTo;
import com.peoplestrong.timeoff.encashment.pojo.AgeBasedEncashmentResponseTo;
import com.peoplestrong.timeoff.encashment.pojo.TmAgeBasedEncashmentDetailResponseTo;
import com.peoplestrong.timeoff.encashment.pojo.TmAgeBasedEncashmentRequestTo;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TmAgeBasedEncashmentService {

    AgeBasedEncashmentResponseTo save(AgeBasedEncashmentRequestTo request, UserInfo userInfo) throws AppRuntimeException;

    List<TmAgeBasedEncashment> findAll(int tenant) throws AppRuntimeException;

    TmAgeBasedEncashmentDetailResponseTo findAgeBasedEncashmentDetails(int tenantID, int organizationID, Long ageBasedEncashmentID) throws AppRuntimeException;

    TmAgeBasedEncashment update(TmAgeBasedEncashmentRequestTo request, UserInfo userInfo) throws AppRuntimeException;

}