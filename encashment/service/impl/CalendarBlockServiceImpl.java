package com.peoplestrong.timeoff.encashment.service.impl;


import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.util.DateUtil;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmCalenderBlock;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmCalenderBlockDetail;
import com.peoplestrong.timeoff.dataservice.model.leave.TmCalendar;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmCalenderBlockDetailRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmCalenderBlockRepository;
import com.peoplestrong.timeoff.encashment.pojo.CalenderBlockTo;
import com.peoplestrong.timeoff.encashment.pojo.DateRange;
import com.peoplestrong.timeoff.encashment.pojo.DateUtils;
import com.peoplestrong.timeoff.encashment.service.CalendarBlockService;
import com.peoplestrong.timeoff.leave.constant.ILeaveErrorConstants;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.leave.validation.Impl.LeaveValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CalendarBlockServiceImpl implements CalendarBlockService {


    @Autowired
    TmCalenderBlockRepository calenderBlockRepository;

    @Autowired
    TmCalenderBlockDetailRepository calenderBlockDetailRepository;

    private static final Logger LOGGER = Logger.getLogger(CalendarBlockServiceImpl.class.toString());

    public String validateRanges(CalenderBlockTo calenderTo, List<DateRange> ranges,Integer orgId) {

        try {

            if(calenderTo.getStartDate()==null){
                return "Start Date of the parent calendar can not be left blank.";
            }

            if(calenderTo.getEndDate()==null){
                return "End Date of the parent calendar can not be left blank.";
            }

            if(calenderTo.getYearseperation()==null){
                return "Year Separation can not be left blank.";
            }

            if(ranges.isEmpty()){
                return "Please generate the subcalendar data inorder to save it.";
            }

            String overallStartDateStr = DateUtils.dateToString(calenderTo.getStartDate());
            String overallEndDateStr = DateUtils.dateToString(calenderTo.getEndDate());

            String blockName= calenderTo.getBlockName().trim();
            String blockNamefoundinDb=calenderBlockRepository.findbyCalenderBlockName(calenderTo.getBlockName().trim(),orgId);


            if(blockNamefoundinDb!=null && blockName.equals(blockNamefoundinDb)){
                return "A parent calendar with this name already exists. Please choose a different name.";
            }


            if(!ranges.get(0).getStartDate().equals(calenderTo.getStartDate()) || !ranges.get(ranges.size()-1).getEndDate().equals(calenderTo.getEndDate())){
                return "The Start Date of the first sub-calendar and the End Date of the last sub-calendar must match the start and end dates of the parent calendar. Please ensure the dates are consistent.";
            }

            // Validate that start date is not after end dateme
            if (!areStartDatesBeforeOrEqualEndDates(ranges)) {
                return "The Start Date cannot be greater than the End Date. Please correct the date range.";
            }

            // Validate bounds
            if (!isWithinBounds(ranges, overallStartDateStr, overallEndDateStr)) {
                return "The selected date range exceeds the boundaries of the parent calendar. Please select a range within the parent calendar's start and end dates.";
            }

            // Validate continuity
            if (!areRangesContinuous(ranges)) {
                return "The Start Date of each sub-calendar must immediately follow the End Date of the previous sub-calendar. Please ensure the dates are continuous.";
            }
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException("Something went wrong while validating ranges.");
        }
        return "Validation successful";
    }

    private Boolean isWithinBounds(List<DateRange> ranges, String overallStartDateStr, String overallEndDateStr) throws ParseException {
        for (DateRange range : ranges) {
            String rangeStartDateStr = DateUtils.dateToString(range.getStartDate());
            String rangeEndDateStr = DateUtils.dateToString(range.getEndDate());

            if (DateUtils.compareDates(rangeStartDateStr, overallStartDateStr) < 0 ||
                    DateUtils.compareDates(rangeEndDateStr, overallEndDateStr) > 0) {
                return false;
            }
        }
        return true;
    }

    private Boolean areRangesContinuous(List<DateRange> ranges) throws ParseException {
        if (ranges.isEmpty()) {
            return true;
        }

        for (int i = 1; i < ranges.size(); i++) {
            DateRange previous = ranges.get(i - 1);
            DateRange current = ranges.get(i);

            String previousEndDateStr = DateUtils.dateToString(previous.getEndDate());
            String currentStartDateStr = DateUtils.dateToString(current.getStartDate());

            if (!DateUtils.isNextDay(previousEndDateStr, currentStartDateStr)) {
                return false;
            }
        }

        return true;
    }

    private Boolean areStartDatesBeforeOrEqualEndDates(List<DateRange> ranges) throws ParseException {
        for (DateRange range : ranges) {
            String startDateStr = DateUtils.dateToString(range.getStartDate());
            String endDateStr = DateUtils.dateToString(range.getEndDate());

            // Check if start date is after end date
            if (DateUtils.compareDates(startDateStr, endDateStr) > 0) {
                return false;
            }
        }
        return true;
    }


    @Transactional
    public Boolean saveData(CalenderBlockTo calenderTo, List<DateRange> ranges,UserInfo userInfo) {
        try {
            // Process and save calenderTo and ranges data
            // Example logic for saving data:
            // 1. Mark existing records as inactive
            // 2. Insert new calendar blocks and details

            insertNewCalendarRecords(calenderTo,ranges,userInfo);

            return true;
        } catch (Exception e) {
            // Handle exception, possibly log it
            return false;
        }
    }


    @Transactional
    public void insertNewCalendarRecords(CalenderBlockTo calenderBlockTo,List<DateRange> ranges,UserInfo userInfo) {
        // Ensure previous records are marked inactive
//        markExistingRecordsInactive();

        Integer strtdateidcalenderBlock= DateUtil.getTimeDimensionId(calenderBlockTo.getStartDate());
        Integer enddateidcalenderBlock= DateUtil.getTimeDimensionId(calenderBlockTo.getEndDate());

        TmCalenderBlock calenderBlock = new TmCalenderBlock();
        calenderBlock.setStartDate(calenderBlockTo.getStartDate());
        calenderBlock.setEndDate(calenderBlockTo.getEndDate());
        calenderBlock.setActive(true);
        calenderBlock.setEndDateID(enddateidcalenderBlock);
        calenderBlock.setStartDateID(strtdateidcalenderBlock);
        calenderBlock.setOrganizationId(userInfo.getOrganizationId());
        calenderBlock.setTenantID(userInfo.getTenantId());
        calenderBlock.setCreatedBy(userInfo.getUserId());
        calenderBlock.setCreatedDate(new Date());
        calenderBlock.setModifiedBy(userInfo.getUserId());
        calenderBlock.setModifiedDate(new Date());
        calenderBlock.setBlockName(calenderBlockTo.getBlockName());
        TmCalenderBlock savedBlock = calenderBlockRepository.save(calenderBlock);
        Long calenderBlockID = savedBlock.getCalenderBlockID();
        // Step 2: Save TmCalenderBlockDetail with the retrieved ID
        
        for (DateRange range : ranges) {
            Integer strtdateidcalenderBlockdetail= DateUtil.getTimeDimensionId(range.getStartDate());
            Integer enddateidcalenderBlockdetail= DateUtil.getTimeDimensionId(range.getEndDate());


            TmCalenderBlockDetail detail = new TmCalenderBlockDetail();
            detail.setCalenderBlockID(calenderBlockID);
            detail.setStartDate(range.getStartDate());
            detail.setEndDate(range.getEndDate());
            detail.setBlockDetailName(range.getName());
            detail.setEndDateID(enddateidcalenderBlockdetail);
            detail.setStartDateID(strtdateidcalenderBlockdetail);
            detail.setOrganizationId(userInfo.getOrganizationId());
            detail.setTenantID(userInfo.getTenantId());
            detail.setCreatedBy(userInfo.getUserId());
            detail.setCreatedDate(new Date());
            detail.setModifiedBy(userInfo.getUserId());
            detail.setModifiedDate(new Date());
            calenderBlockDetailRepository.save(detail);
        }

    }
}
