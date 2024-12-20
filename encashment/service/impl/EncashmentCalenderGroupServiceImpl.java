package com.peoplestrong.timeoff.encashment.service.impl;

import com.peoplestrong.timeoff.common.constant.TableConstant;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmEncashmentCalenderGroup;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmCalenderBlockDetailRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmCalenderBlockRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmEncashmentCalenderGroupRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.LeaveDurationTypeRepository;
import com.peoplestrong.timeoff.encashment.pojo.CalenderBlockTo;
import com.peoplestrong.timeoff.encashment.pojo.DateRange;
import com.peoplestrong.timeoff.encashment.pojo.TmEncashmentCalendarGroupTo;
import com.peoplestrong.timeoff.encashment.service.EncashmentCalenderGroupService;
import com.peoplestrong.timeoff.leave.helper.LeaveUtil;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EncashmentCalenderGroupServiceImpl implements EncashmentCalenderGroupService {
    
    @Autowired
    TmEncashmentCalenderGroupRepository calenderGroupRepository;

    @Autowired
    LeaveDurationTypeRepository leaveDurationTypeRepository;

    @Autowired
    TmCalenderBlockRepository tmCalenderBlockRepository;

    @Autowired
    TmCalenderBlockDetailRepository tmCalenderBlockDetailRepository;

    @Autowired
    TmEncashmentCalenderGroupRepository encashmentCalenderGroupRepository;
    
    @Override
    public Boolean saveData(TmEncashmentCalendarGroupTo encashmentCalendarGroupTo,UserInfo userInfo) throws Exception {

        try {
            if (encashmentCalendarGroupTo != null) {
                TmEncashmentCalenderGroup encashmentCalenderGroup = new TmEncashmentCalenderGroup();
                encashmentCalenderGroup.setConfigurationName(encashmentCalendarGroupTo.getConfigurationName());
                encashmentCalenderGroup.setLeaveTypeID(encashmentCalendarGroupTo.getLeaveTypeID());
                encashmentCalenderGroup.setAltGroupID(encashmentCalendarGroupTo.getAltGroupID());
                encashmentCalenderGroup.setActive(encashmentCalendarGroupTo.getActive());
                encashmentCalenderGroup.setCalenderBlockDetailID(encashmentCalendarGroupTo.getCalenderBlockDetailID());
                encashmentCalenderGroup.setMaxEncashmentLimit(encashmentCalendarGroupTo.getMaxEncashmentLimit());
                if(encashmentCalendarGroupTo.getMaxEncashmentAllowedInMonth()!=null) {
                    encashmentCalenderGroup.setMaxEncashmentAllowedInMonth(encashmentCalendarGroupTo.getMaxEncashmentAllowedInMonth());
                } else {
                    encashmentCalenderGroup.setMaxEncashmentAllowedInMonth(null);
                }
                if(encashmentCalendarGroupTo.getMaxEncashmentAllowedInQuarter()!=null) {
                    encashmentCalenderGroup.setMaxEncashmentAllowedInQuarter(encashmentCalendarGroupTo.getMaxEncashmentAllowedInQuarter());
                } else {
                    encashmentCalenderGroup.setMaxEncashmentAllowedInQuarter(null);
                }
                if(encashmentCalendarGroupTo.getMaxEncashmentAllowedInYear()!=null) {
                    encashmentCalenderGroup.setMaxEncashmentAllowedInYear(encashmentCalendarGroupTo.getMaxEncashmentAllowedInYear());
                } else {
                    encashmentCalenderGroup.setMaxEncashmentAllowedInYear(null);
                }
                if(encashmentCalendarGroupTo.getMinimumBalanceForEncashment()!=null) {
                    encashmentCalenderGroup.setMinimumBalanceForEncashment(encashmentCalendarGroupTo.getMinimumBalanceForEncashment());
                } else {
                    encashmentCalenderGroup.setMinimumBalanceForEncashment(null);
                }
                if(encashmentCalendarGroupTo.getMinLeaveBalanceAfterEncashment()!=null) {
                    encashmentCalenderGroup.setMinLeaveBalanceAfterEncashment(encashmentCalendarGroupTo.getMinLeaveBalanceAfterEncashment());
                } else {
                    encashmentCalenderGroup.setMinLeaveBalanceAfterEncashment(null);
                }

                encashmentCalenderGroup.setTenantID(userInfo.getTenantId());
                encashmentCalenderGroup.setOrganizationId(userInfo.getOrganizationId());
                encashmentCalenderGroup.setCreatedBy(userInfo.getUserId());
                encashmentCalenderGroup.setCreatedDate(new Date());
                encashmentCalenderGroup.setModifiedBy(userInfo.getUserId());
                encashmentCalenderGroup.setModifiedDate(new Date());
                calenderGroupRepository.save(encashmentCalenderGroup);
                return true;
            }

        } catch(Exception e) {
            throw new AppRuntimeException("Error in saving details");
        }
        return false;
    }

    @Override
    public String validateEcgDetails(TmEncashmentCalendarGroupTo encashmentCalendarGroupTo,Integer orgId) {
        if(encashmentCalendarGroupTo.getConfigurationName()==null || encashmentCalendarGroupTo.getConfigurationName().isEmpty()){
            return "Configuration Name can not be null";
        }

        if(encashmentCalendarGroupTo.getLeaveTypeID()==null){
            return "Please select the Leave Type.";
        }

        if(encashmentCalendarGroupTo.getAltGroupID()==null){
            return "Please select the AltGroup.";
        }
        if(encashmentCalendarGroupTo.getCalenderBlockDetailID()==null){
            return "Please select the Calendar and Sub-Calendar Name";
        }

        if(encashmentCalendarGroupTo.getMaxEncashmentLimit()==null){
            return "Max Encashment Limit can not be null";
        }
        if(encashmentCalendarGroupTo.getMaxEncashmentLimit()<=0){
            return "Max Encashment Limit should be greater than 0.";
        }
        
        if(encashmentCalendarGroupTo.getMaxEncashmentAllowedInMonth()!=null){
            if(encashmentCalendarGroupTo.getMaxEncashmentAllowedInMonth()<=0) {
                return "Max Encashment Allowed In Month should be greater than 0.";
            }
        }

        if(encashmentCalendarGroupTo.getMaxEncashmentAllowedInQuarter()!=null) {
            if (encashmentCalendarGroupTo.getMaxEncashmentAllowedInQuarter() <= 0) {
                return "Max Encashment Allowed In Quarter should be greater than 0.";
            }
        }
        
        if(encashmentCalendarGroupTo.getMaxEncashmentAllowedInYear()!=null){
            if (encashmentCalendarGroupTo.getMaxEncashmentAllowedInYear() <= 0) {
                return "Max Encashment Allowed In Year should be greater than 0.";
            }
        }

        if(encashmentCalendarGroupTo.getMinimumBalanceForEncashment()!=null) {
            if (encashmentCalendarGroupTo.getMinimumBalanceForEncashment() <= 0) {
                return "Minimum Balance For Encashment should be greater than 0.";
            }
        }
        
        if(encashmentCalendarGroupTo.getMinLeaveBalanceAfterEncashment()!=null) {
            if (encashmentCalendarGroupTo.getMinLeaveBalanceAfterEncashment() <= 0) {
                return "Min Leave Balane After Encasment should be greater than 0.";
            }
        }

        String getConfigurationNamefromDB=calenderGroupRepository.findbyconfigurationName(encashmentCalendarGroupTo.getConfigurationName().trim(),orgId);


        if(getConfigurationNamefromDB!=null && getConfigurationNamefromDB.equalsIgnoreCase(encashmentCalendarGroupTo.getConfigurationName().trim())){
            return "This configuration name already exists. Please choose a different name.";
        }
        
        if(encashmentCalendarGroupTo.getMaxEncashmentLimit()!=null){
            String strNum = Float.toString(encashmentCalendarGroupTo.getMaxEncashmentLimit());
            if (strNum.contains(".")) {
                String decimalPart = strNum.split("\\.")[1];
               if(decimalPart.length() > 2){
                   return "Max Encashment Limit should not exceed two decimal place.";
               }
            }

        }
        
        if(encashmentCalendarGroupTo.getMaxEncashmentAllowedInMonth()!=null){
            String strNum = Float.toString(encashmentCalendarGroupTo.getMaxEncashmentAllowedInMonth());
            if (strNum.contains(".")) {
                String decimalPart = strNum.split("\\.")[1];
                if(decimalPart.length() > 2){
                    return "Max Encashment Allowed In Month should not exceed two decimal place.";
                }
            }
        }

        if(encashmentCalendarGroupTo.getMaxEncashmentAllowedInQuarter()!=null){
            String strNum = Float.toString(encashmentCalendarGroupTo.getMaxEncashmentAllowedInQuarter());
            if (strNum.contains(".")) {
                String decimalPart = strNum.split("\\.")[1];
                if(decimalPart.length() > 2){
                    return "Max Encashment Allowed In Quarter should not exceed two decimal place.";
                }
            }
        }

        if(encashmentCalendarGroupTo.getMaxEncashmentAllowedInYear()!=null){
            String strNum = Float.toString(encashmentCalendarGroupTo.getMaxEncashmentAllowedInYear());
            if (strNum.contains(".")) {
                String decimalPart = strNum.split("\\.")[1];
                if(decimalPart.length() > 2){
                    return "Max Encashment Allowed In Year should not exceed two decimal place.";
                }
            }
        }

        if(encashmentCalendarGroupTo.getMinimumBalanceForEncashment()!=null){
            String strNum = Float.toString(encashmentCalendarGroupTo.getMinimumBalanceForEncashment());
            if (strNum.contains(".")) {
                String decimalPart = strNum.split("\\.")[1];
                if(decimalPart.length() > 2){
                    return "Minimum Balance For Encashment should not exceed two decimal place.";
                }
            }
        }

        if(encashmentCalendarGroupTo.getMinLeaveBalanceAfterEncashment()!=null){
            String strNum = Float.toString(encashmentCalendarGroupTo.getMinLeaveBalanceAfterEncashment());
            if (strNum.contains(".")) {
                String decimalPart = strNum.split("\\.")[1];
                if(decimalPart.length() > 2){
                    return "Minimum Balance For Encashment should not exceed two decimal place.";
                }
            }
        }

       Integer count= calenderGroupRepository.result(encashmentCalendarGroupTo.getAltGroupID(), encashmentCalendarGroupTo.getCalenderBlockDetailID(), encashmentCalendarGroupTo.getLeaveTypeID(),orgId);
       
       if(count>0){
           return "Configuration alredy exist.Please change Sub-Calendar Name or Leave Type.";
       }

       List<Object[]> getStartandEndDate=tmCalenderBlockRepository.findStartDateandEndDate(encashmentCalendarGroupTo.getCalenderBlockID(),orgId);
       List<Long> overlappingIds=new ArrayList<>();
       
       for (Object[] dateRange : getStartandEndDate) {
           // Assuming the first element is the start date and the second is the end date
           Integer startDateid = (Integer)  dateRange[0]; // Adjust the type if needed
           Integer endDateid = (Integer) dateRange[1];   // Adjust the type if needed

           // Retrieve overlapping IDs based on the current start and end dates
           List<Long> ids = tmCalenderBlockRepository.getOverlappingIds(startDateid, endDateid,orgId);
           overlappingIds.addAll(ids);
       }

        if(overlappingIds.size()!=0) {
            for (Long overlappingID : overlappingIds) {
                List<Long> calendarBlockDetailId = tmCalenderBlockDetailRepository.findDetailIdbYCalenderBlockId(overlappingID,orgId);

                for(Long detailId:calendarBlockDetailId) {
                    Integer getcount = encashmentCalenderGroupRepository.getcount(detailId, encashmentCalendarGroupTo.getAltGroupID(),orgId);

                    if (getcount > 0 && overlappingID!=encashmentCalendarGroupTo.getCalenderBlockID()) {
                        return "AltGroupId already created for the given parent date.";
                    }
                }
            }
        }
        return "Validation Successfull";
    }
}
