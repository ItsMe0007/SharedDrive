package com.peoplestrong.timeoff.encashment.service;

import com.peoplestrong.timeoff.encashment.pojo.CalenderBlockTo;
import com.peoplestrong.timeoff.encashment.pojo.DateRange;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CalendarBlockService {

    public String validateRanges(CalenderBlockTo calenderTo, List<DateRange> ranges,Integer orgId);

    public Boolean saveData(CalenderBlockTo calenderTo, List<DateRange> ranges, UserInfo userInfo);
}
