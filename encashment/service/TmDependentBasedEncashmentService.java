package com.peoplestrong.timeoff.encashment.service;

import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmDependentBasedEncashment;
import com.peoplestrong.timeoff.encashment.pojo.*;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;

import java.util.List;

public interface TmDependentBasedEncashmentService {

    DependentBasedDetailResponseTo save(DependentBasedDetailRequestAllTo request, UserInfo userInfo) throws AppRuntimeException;

    List<TmDependentBasedEncashment> findDependentBasedDetail(int tenant) throws AppRuntimeException;

    DependentBasedEncashmentResponseTo findEncashmentDetails(int tenantID, int organizationID, Long dependentBasedEncashmentID) throws AppRuntimeException;

    TmDependentBasedEncashment updateEncashment(DependentBasedRequestTo request, UserInfo userInfo) throws AppRuntimeException;
}
