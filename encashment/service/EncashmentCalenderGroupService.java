package com.peoplestrong.timeoff.encashment.service;

import com.peoplestrong.timeoff.encashment.pojo.CalenderBlockTo;
import com.peoplestrong.timeoff.encashment.pojo.DateRange;
import com.peoplestrong.timeoff.encashment.pojo.TmEncashmentCalendarGroupTo;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EncashmentCalenderGroupService {

    Boolean saveData(TmEncashmentCalendarGroupTo encashmentCalendarGroupTo,UserInfo userInfo) throws Exception;

    String validateEcgDetails(TmEncashmentCalendarGroupTo encashmentCalendarGroupTo,Integer orgId);
}
