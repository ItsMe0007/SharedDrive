package com.peoplestrong.timeoff.encashment.service;


import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.impl.pojo.SelectItem;
import com.peoplestrong.timeoff.encashment.pojo.TestRequestTO;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface TestService {

    SelectItem testDetails(TestRequestTO request, UserInfo userInfo) throws AppRuntimeException, IllegalStateException;


}
