package com.peoplestrong.timeoff.encashment.mss.service;

import com.peoplestrong.timeoff.common.exception.CountryMappingException;
import com.peoplestrong.timeoff.encashment.mss.pojo.DayViewDTOin;
import com.peoplestrong.timeoff.encashment.mss.pojo.DayViewPageDTO;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

@Service
public interface EncashmentMssService extends Serializable {
    
    DayViewPageDTO dayViewMssPage(UserInfo userInfo, DayViewDTOin dayViewDTOin);

    Map<String, Object> getMssFilters(UserInfo userInfo) throws CountryMappingException;
}
