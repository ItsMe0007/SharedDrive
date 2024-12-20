package com.peoplestrong.timeoff.encashment.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.util.DateUtil;
import com.peoplestrong.timeoff.common.util.PSCollectionUtil;
import com.peoplestrong.timeoff.common.util.StringUtil;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmEmployeeEncashment;
import com.peoplestrong.timeoff.dataservice.model.leave.*;
import com.peoplestrong.timeoff.dataservice.repo.leave.TmLeaveBalanceRulesRepository;
import com.peoplestrong.timeoff.encashment.flatTO.EncashmentTypeTO;
import com.peoplestrong.timeoff.encashment.pojo.CalendarBlocksTO;
import com.peoplestrong.timeoff.encashment.pojo.base.DependentDetailTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentEmployeeTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentRequestConfigTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentRequestRestTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentRequestTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentTaskActionTO;
import com.peoplestrong.timeoff.leave.flatTO.LeaveEntitlementTO;
import com.peoplestrong.timeoff.leave.helper.LeaveUtil;
import com.peoplestrong.timeoff.leave.pojo.CalendarTO;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.leave.trasnport.output.LeaveBalanceResponseTO;


public final class EncashmentUtil {

    public static EncashmentRequestTO processEncashmentRequestTO(EncashmentRequestRestTO request, UserInfo userInfo,
                                                            TmLeaveType tmLeaveTypeTO, TmLeaveEntitlement tmLeaveEntitlement, CalendarTO defaultCalendarTO, CalendarTO leaveCalendarTO, Map<Integer, String> sysContentTypeMap)
            throws Exception
    {
        EncashmentRequestTO encashmentRequestTO = new EncashmentRequestTO();
        EncashmentRequestConfigTO encashmentRequestConfigTO = new EncashmentRequestConfigTO();
        EncashmentEmployeeTO encashmentEmployeeTO = new EncashmentEmployeeTO();

        try {
            Double currentBalance = request.getCurrentLeaveBalance();
            encashmentRequestTO.setSource(request.getSource());
            encashmentRequestTO.setForceFullyFlag(request.isForceFullyFlag());
            encashmentRequestTO.setUserInfo(userInfo);
            encashmentRequestTO.setAction(request.getAction());
            encashmentRequestTO.setEmployeeId(request.getEmployeeId());
            encashmentRequestTO.setTenantId(userInfo.getTenantId());
            encashmentRequestTO.setOrganizationId(userInfo.getOrganizationId());
            encashmentRequestTO.setBundleName(userInfo.getBundleName());
            encashmentRequestTO.setLeaveReasonId(request.getLeaveReasonId());
            encashmentRequestTO.setLeaveTypeId(request.getLeaveTypeId());
            encashmentRequestTO.setComments(request.getComment());
            encashmentRequestTO.setManagerComments(request.getComment());
            encashmentRequestTO.setActive(true);
            encashmentRequestTO.setApplicationDate(DateUtil.getCurrentDate());
            encashmentRequestTO.setLoginUser(userInfo.getUserId());
            encashmentRequestTO.setStageActionId(request.getStageActionId());
            encashmentRequestTO.setRuleOutput(request.getRuleOutput());
            encashmentRequestTO.setUserName(userInfo.getUserName());
            encashmentRequestTO.setLeaveQuotaId(request.getLeaveQuotaId());
            encashmentRequestTO.setDurationWeightage(request.getDurationWeightage());
            encashmentRequestTO.setAttachmentPath(request.getAttachmentPath());
            encashmentRequestTO.setLeaveType(request.getLeaveType());
            encashmentRequestTO.setLeaveCode(request.getLeaveType());
            encashmentRequestTO.setLeavereason(request.getLeaveReason());
            encashmentRequestTO.setCurrentBalance(currentBalance);
            encashmentRequestTO.setCurrentDate(DateUtil.getCurrentDate());
            encashmentRequestTO.setEncashmentType(request.getEncashmentType());
            encashmentRequestTO.setDependentDetailTOList(request.getDependentDetails());
            encashmentRequestTO.setCalendarBlocksTOList(request.getCalendarBlocks());
            encashmentRequestTO.setCurrentDateId(DateUtil.getTimeDimensionId(DateUtil.getCurrentDate()));
            encashmentRequestTO.setBirthdayCalendarYear(request.getBirthdayYear());
            encashmentRequestTO.setEncashmentNumberOfDays(request.getNumberOfDays());
            encashmentRequestTO.setSysEncashmentType(request.getSysEncashmentType());
            encashmentRequestTO.setSysEncashmentTypeId(request.getSysEncashmentTypeId());
            encashmentRequestTO.setEncashmentTypeContentId(request.getEncashmentTypeContentId());
            if(request.getAnniversaryYear() != null && request.getAnniversaryYear() > 0)
                encashmentRequestTO.setAnniversaryCalendarYear(request.getAnniversaryYear());
            if (request.getContactNumber() != null) {
                encashmentRequestTO.setLeaveEmployeeContact(request.getContactNumber());
            }
            if (request.getReachableAddress() != null) {
                encashmentRequestTO.setLeaveEmployeeAddress(request.getReachableAddress());
            }
            if (tmLeaveTypeTO.getSysLeaveTypeId() != null) {
                encashmentRequestTO.setLeaveCategoryType(sysContentTypeMap.get(tmLeaveTypeTO.getSysLeaveTypeId()));
            }
            encashmentRequestTO.setLeaveLapseId(request.getSelectedCompOff());
            encashmentRequestTO.setCompOffDate(request.getCompOffDate());
            encashmentRequestTO.setUserID(request.getUserId());
            encashmentRequestTO.setL1ManagerID(request.getL1ManagerID());
            encashmentRequestTO.setFileName(request.getFileName());
            encashmentRequestTO.setL2ManagerID(request.getL2ManagerID());
            encashmentRequestTO.setHrManagerID(request.getHrManagerID());
            encashmentRequestTO.setFileName(request.getFileName());
            encashmentRequestTO.setLeaveCount(request.getLeaveCount());
            encashmentRequestTO.setEmployeeEncashmentId(request.getEmployeeEncashmentId());
            encashmentRequestTO.setAttachmentPath(request.getAttachmentPath());
            encashmentRequestTO.setSelected_MarriageDate(request.getDateOfMarriage());
            encashmentRequestTO.setDateOfDeath(request.getDateOfDeath());
            encashmentRequestTO.setPunchWarningPopUp(request.getPunchWarningPopUp());
            encashmentRequestTO.setRegWarningPopUp(request.getRegWarningPopUp());
            if(request.getLoggedInUserId()!=null) {
                encashmentRequestTO.setLoggedInUserId(request.getLoggedInUserId());
            }

            /* Adoption Leave - Start */
            encashmentRequestTO.setDateOfIntent(request.getDateOfIntent());
            encashmentRequestTO.setDateOfAdoption(request.getDateOfAdoption());
            encashmentRequestTO.setBirthCertificateNumber(request.getBirthCertificateNumber());
            encashmentRequestTO.setAdoptedDateOfBirth(request.getAdoptedDateOfBirth());
            /* Adoption Leave - End */

            if (null != request.getEdodDate()) {
                encashmentRequestTO.setEdodDateId(DateUtil.getTimeDimensionId(request.getEdodDate()));
            }
            if (null != request.getDateOfDelivery()) {
                encashmentRequestTO.setDateOfDeliveryId(DateUtil.getTimeDimensionId(request.getDateOfDelivery()));

            }
            if (tmLeaveTypeTO != null) {
                encashmentRequestTO.setLeaveDescription(tmLeaveTypeTO.getLeaveTypeDescription());
                encashmentRequestTO.setNumberOfTransactionsPerDayLimit(tmLeaveTypeTO.getNumberOfTransactionsPerDayLimit());
                if (tmLeaveTypeTO.getCarryforwardeligible() != null) {
                    encashmentRequestConfigTO.setCarryforwardeligible(tmLeaveTypeTO.getCarryForwardEligibleRule());
                }

                if(tmLeaveTypeTO.getApplyNextYearLeave() != null){
                    encashmentRequestConfigTO.setLeaveTypeNextYearAllowed(tmLeaveTypeTO.getApplyNextYearLeave());
                }

                if (tmLeaveTypeTO.getEligibleFromDOJ() != null) {
                    encashmentRequestConfigTO.setLeaveStartDay(tmLeaveTypeTO.getEligibleFromDOJ());
                }
                if (tmLeaveTypeTO.getPastCutOffDate() != null) {
                    encashmentRequestConfigTO.setPastCutOffDate(tmLeaveTypeTO.getPastCutOffDate());
                }

                if (tmLeaveTypeTO.getSysLeaveTypeId() != null) {
                    encashmentRequestTO.setLeaveCategoryType(sysContentTypeMap.get(tmLeaveTypeTO.getSysLeaveTypeId()));
                }

                if (tmLeaveTypeTO.getMaxLimitAllowedInTenure() != null) {
                    encashmentRequestConfigTO.setMaxLimitAllowedInTenure(tmLeaveTypeTO.getMaxLimitAllowedInTenure());
                }
                if (tmLeaveTypeTO.getAllowedDuringNP() != null) {
                    encashmentRequestConfigTO.setAllowedDuringNP(tmLeaveTypeTO.getAllowedDuringNP());
                }
                if (tmLeaveTypeTO.getIsOptional() != null) {
                    encashmentRequestConfigTO.setOptional(tmLeaveTypeTO.getIsOptional());
                }
                if (tmLeaveTypeTO.getEmpInitiatedNegBalAllowed() != null) {
                    encashmentRequestConfigTO.setEmpNegativeApplicable(tmLeaveTypeTO.getEmpInitiatedNegBalAllowed());
                }
                if (tmLeaveTypeTO.getEmpInitiatedNegBalAllowed() != null) {
                    encashmentRequestConfigTO.setManagerNegativeApplicable(tmLeaveTypeTO.getManagerInitiatedNegBalAllowed());
                }
                if (tmLeaveTypeTO.getEmpInitiatedNegBalAllowed() != null) {
                    encashmentRequestConfigTO.setBalanceCheck(tmLeaveTypeTO.getBalanceCheck());
                }
                if (tmLeaveTypeTO.getYearlyRequestLimit() != null) {
                    encashmentRequestConfigTO.setYearlyRequestLimit(tmLeaveTypeTO.getYearlyRequestLimit());
                }
                if (tmLeaveTypeTO.getMonthlyLimitdays() != null) {
                    encashmentRequestConfigTO.setMonthlyLimitdays(tmLeaveTypeTO.getMonthlyLimitdays());
                }
                if (tmLeaveTypeTO.getYearlyLimitdays() != null) {
                    encashmentRequestConfigTO.setYearlyLimitdays(tmLeaveTypeTO.getYearlyLimitdays());
                }
                if (tmLeaveTypeTO.getMonthlyCapping() != null) {
                    encashmentRequestConfigTO.setMonthlyCapping(tmLeaveTypeTO.getMonthlyCapping());
                }
                if (tmLeaveTypeTO.getMonthlyLimitReq() != null) {
                    encashmentRequestConfigTO.setMonthlyLimitReq(tmLeaveTypeTO.getMonthlyLimitReq());
                }
                if (tmLeaveTypeTO.getEdod() != null) {
                    encashmentRequestConfigTO.setEdod(tmLeaveTypeTO.getEdod());
                }
                if (tmLeaveTypeTO.getEdodDays() != null) {
                    encashmentRequestConfigTO.setEdodDays(tmLeaveTypeTO.getEdodDays());
                }else{
                    encashmentRequestConfigTO.setEdodDays(0);
                }
                if (tmLeaveTypeTO.getAfterEdodDays() != null) {
                    encashmentRequestConfigTO.setAfterEdodDays(tmLeaveTypeTO.getAfterEdodDays());
                }else{
                    encashmentRequestConfigTO.setAfterEdodDays(0);
                }
                if (tmLeaveTypeTO.getIsHourly() != null) {
                    encashmentRequestConfigTO.setIsHourly(tmLeaveTypeTO.getIsHourly());
                } else {
                    encashmentRequestConfigTO.setIsHourly(Boolean.FALSE);
                }
                if (tmLeaveTypeTO.getIsShiftConsider() != null) {
                    encashmentRequestConfigTO.setIsShiftConsider(tmLeaveTypeTO.getIsShiftConsider());
                } else {
                    encashmentRequestConfigTO.setIsShiftConsider(Boolean.FALSE);
                }
                if (tmLeaveTypeTO.getIsCashable() != null) {
                    //hard coded fow now for encashment
                    encashmentRequestConfigTO.setIsCashable(true);
                } else {
                    //hard coded fow now for encashment
                    encashmentRequestConfigTO.setIsCashable(Boolean.TRUE);
                }
                if (tmLeaveTypeTO.getAutoApproval() != null) {
                    encashmentRequestConfigTO.setAutoApproval(tmLeaveTypeTO.getAutoApproval());
                } else {
                    encashmentRequestConfigTO.setAutoApproval(Boolean.FALSE);
                }
                if (tmLeaveTypeTO.getAdjustCompForFuture() != null) {
                    encashmentRequestConfigTO.setAdjustCompForFuture(tmLeaveTypeTO.getAdjustCompForFuture());
                } else {
                    encashmentRequestConfigTO.setAdjustCompForFuture(Boolean.FALSE);
                }
                if (tmLeaveTypeTO.getDateOfDelivery() != null) {
                    encashmentRequestConfigTO.setDateOfDelivery(tmLeaveTypeTO.getDateOfDelivery());
                }
                if (tmLeaveTypeTO.getDiffInApplicationAndDeliveryDate() != null) {
                    encashmentRequestConfigTO.setDiffInApplicationAndDeliveryDate(tmLeaveTypeTO.getDiffInApplicationAndDeliveryDate());
                }else{
                    encashmentRequestConfigTO.setDiffInApplicationAndDeliveryDate(0);
                }

                if (StringUtil.nonEmptyCheck(tmLeaveTypeTO.getLeaveTypeDescription())) {
                    encashmentRequestTO.setLeaveDescription(tmLeaveTypeTO.getLeaveTypeDescription());
                }

                EncashmentTypeTO encashmentTypeTO = new EncashmentTypeTO();

                if (null != tmLeaveTypeTO.getReligionId()) {
                    encashmentTypeTO.setReligionId(tmLeaveTypeTO.getReligionId());
                }

                if (null != tmLeaveTypeTO.getLeaveTypeCode()) {
                    encashmentTypeTO.setLeaveTypeCode(tmLeaveTypeTO.getLeaveTypeCode());
                }

                if (null != tmLeaveTypeTO.getGenderId()) {
                    encashmentTypeTO.setGenderId(tmLeaveTypeTO.getGenderId());
                }

                if (tmLeaveTypeTO.getCanCarryForward() != null) {
                    encashmentTypeTO.setCanCarryForward(tmLeaveTypeTO.getCanCarryForward());
                }

                if (tmLeaveTypeTO.getIsSickLeaveEligibility() != null) {
                    encashmentTypeTO.setIsSickLeaveEligibility(tmLeaveTypeTO.getIsSickLeaveEligibility());
                }

                if (tmLeaveTypeTO.getIsTenureLeaveEligibility() != null) {
                    encashmentTypeTO.setIsTenureLeaveEligibility(tmLeaveTypeTO.getIsTenureLeaveEligibility());
                }

                if (tmLeaveTypeTO.getLeaveMultipleInMin() != null) {
                    encashmentTypeTO.setLeaveMultipleInMin(tmLeaveTypeTO.getLeaveMultipleInMin());
                }

                if (tmLeaveTypeTO.getIsSickLeaveEligibility() != null) {
                    encashmentTypeTO.setIsSickLeaveEligibility(tmLeaveTypeTO.getIsSickLeaveEligibility());
                }

                if (tmLeaveTypeTO.getIsTenureLeaveEligibility() != null) {
                    encashmentTypeTO.setIsTenureLeaveEligibility(tmLeaveTypeTO.getIsTenureLeaveEligibility());
                }

                if(tmLeaveTypeTO.getIsAnnualLeaveEligibility() != null){
                    encashmentTypeTO.setIsAnnualLeaveEligibility(tmLeaveTypeTO.getIsAnnualLeaveEligibility());
                }

                if(tmLeaveTypeTO.getYearlyTenure() != null){
                    encashmentTypeTO.setIsYearlyTenure(tmLeaveTypeTO.getYearlyTenure());
                }

                if (tmLeaveTypeTO.getLeaveMultipleInMin() != null) {
                    encashmentTypeTO.setLeaveMultipleInMin(tmLeaveTypeTO.getLeaveMultipleInMin());
                }
                if (tmLeaveTypeTO.getSkipLeaveReason() != null) {
                    encashmentTypeTO.setSkipLeaveReason(tmLeaveTypeTO.getSkipLeaveReason());
                }else {
                    encashmentTypeTO.setSkipLeaveReason(false);
                }
                if (null != tmLeaveTypeTO.getCountryId()) {
                    encashmentTypeTO.setCountryId(tmLeaveTypeTO.getCountryId());
                }

                if (null != tmLeaveTypeTO.getMaxChildrenAllowed()) {
                    encashmentTypeTO.setMaxChildrenAllowed(tmLeaveTypeTO.getMaxChildrenAllowed());
                }

                encashmentRequestConfigTO.setEncashmentTypeTO(encashmentTypeTO);
                if(tmLeaveTypeTO.getNoOfDaysToApplyCarryForwardedLeave()!=null) {
                    encashmentRequestConfigTO.setMexDaysToApplyForCarryForward(tmLeaveTypeTO.getNoOfDaysToApplyCarryForwardedLeave());
                }

            }

            if (null != tmLeaveEntitlement) {
                LeaveEntitlementTO leaveEntitlementTO= new LeaveEntitlementTO();
                if(tmLeaveEntitlement.getEntitlement()!=null) {
                    leaveEntitlementTO.setEntitlement(tmLeaveEntitlement.getEntitlement());
                }
                if(tmLeaveEntitlement.getMaxCarryForwardPerYear()!=null) {
                    leaveEntitlementTO.setMaxCarryForwardPerYear(tmLeaveEntitlement.getMaxCarryForwardPerYear());
                }
                if(tmLeaveEntitlement.getNoticeApply()!=null) {
                    leaveEntitlementTO.setNoticeApply(tmLeaveEntitlement.getNoticeApply());
                }
                leaveEntitlementTO.setOtherLeave(tmLeaveEntitlement.getEntitlementType());
                leaveEntitlementTO.setLapsable(tmLeaveEntitlement.getLapsable());
                leaveEntitlementTO.setLapsablePeriod(tmLeaveEntitlement.getLapsablePeriod());
                leaveEntitlementTO.setRestrictionOnLeaveAvailmentFrequency(tmLeaveEntitlement.getRestrictionOnLeaveAvailmentFrequency());
                leaveEntitlementTO.setEntitlementPeriodTypeId(tmLeaveEntitlement.getEntitlementPeriodTypeID());
                leaveEntitlementTO.setRestrictNoticeApplyOnProxy(tmLeaveEntitlement.getRestrictNoticeApplyOnProxy());
                encashmentRequestConfigTO.setLeaveEntitlementTO(leaveEntitlementTO);
                encashmentRequestConfigTO.setConsecutive(tmLeaveEntitlement.getConsicutive());
                if(tmLeaveEntitlement.getLapseLeaveApplyDays()!=null) {
                    encashmentRequestConfigTO.setMexDaysToApplyForLapseLeave(tmLeaveEntitlement.getLapseLeaveApplyDays());
                }
                if(tmLeaveEntitlement.getEntitlementCheckForChildren()!=null) {
                    encashmentRequestConfigTO.setEntitlementCheckForChildren(tmLeaveEntitlement.getEntitlementCheckForChildren());
                    encashmentRequestConfigTO.setEntitlementBasedOnChildren(tmLeaveEntitlement.getEntitlementBasedOnChildren());
                    encashmentRequestConfigTO.setCountOfChildren(tmLeaveEntitlement.getCountOfChildren());
                }
                if(tmLeaveEntitlement.getProbationLeaveLimit()!=null){
                    encashmentRequestConfigTO.setProbationLeaveLimit(tmLeaveEntitlement.getProbationLeaveLimit());
                }
                if(tmLeaveEntitlement.getEligibleDaysFromDate()!=null){
                    encashmentRequestConfigTO.setEligibleDateForJoining(tmLeaveEntitlement.getEligibleDaysFromDate());
                }
            }

            if (null != tmLeaveEntitlement) {
                LeaveEntitlementTO leaveEntitlementTO= new LeaveEntitlementTO();
                if(tmLeaveEntitlement.getEntitlement()!=null) {
                    leaveEntitlementTO.setEntitlement(tmLeaveEntitlement.getEntitlement());
                }
                if(tmLeaveEntitlement.getMaxCarryForwardPerYear()!=null) {
                    leaveEntitlementTO.setMaxCarryForwardPerYear(tmLeaveEntitlement.getMaxCarryForwardPerYear());
                }
                if(tmLeaveEntitlement.getNoticeApply()!=null) {
                    leaveEntitlementTO.setNoticeApply(tmLeaveEntitlement.getNoticeApply());
                }
                leaveEntitlementTO.setOtherLeave(tmLeaveEntitlement.getEntitlementType());
                leaveEntitlementTO.setLapsable(tmLeaveEntitlement.getLapsable());
                leaveEntitlementTO.setLapsablePeriod(tmLeaveEntitlement.getLapsablePeriod());
                leaveEntitlementTO.setRestrictionOnLeaveAvailmentFrequency(tmLeaveEntitlement.getRestrictionOnLeaveAvailmentFrequency());
                leaveEntitlementTO.setEntitlementPeriodTypeId(tmLeaveEntitlement.getEntitlementPeriodTypeID());
                leaveEntitlementTO.setRestrictNoticeApplyOnProxy(tmLeaveEntitlement.getRestrictNoticeApplyOnProxy());
                encashmentRequestConfigTO.setLeaveEntitlementTO(leaveEntitlementTO);
                encashmentRequestConfigTO.setConsecutive(tmLeaveEntitlement.getConsicutive());
                if(tmLeaveEntitlement.getLapseLeaveApplyDays()!=null) {
                    encashmentRequestConfigTO.setMexDaysToApplyForLapseLeave(tmLeaveEntitlement.getLapseLeaveApplyDays());
                }
                if(tmLeaveEntitlement.getEntitlementCheckForChildren()!=null) {
                    encashmentRequestConfigTO.setEntitlementCheckForChildren(tmLeaveEntitlement.getEntitlementCheckForChildren());
                    encashmentRequestConfigTO.setEntitlementBasedOnChildren(tmLeaveEntitlement.getEntitlementBasedOnChildren());
                    encashmentRequestConfigTO.setCountOfChildren(tmLeaveEntitlement.getCountOfChildren());
                }
                if(tmLeaveEntitlement.getProbationLeaveLimit()!=null){
                    encashmentRequestConfigTO.setProbationLeaveLimit(tmLeaveEntitlement.getProbationLeaveLimit());
                }
                if(tmLeaveEntitlement.getEligibleDaysFromDate()!=null){
                    encashmentRequestConfigTO.setEligibleDateForJoining(tmLeaveEntitlement.getEligibleDaysFromDate());
                }
            }

            //Setting Default Calender Properties
            encashmentRequestTO.setDefaultCalendarStartDate(
                    DateUtil.getDateFromTimeDimensionId(defaultCalendarTO.getStartPeriod()));
            encashmentRequestTO
                    .setDefaultCalendarEndDate(DateUtil.getDateFromTimeDimensionId(defaultCalendarTO.getEndPeriod()));
            encashmentRequestTO.setDefaultCalendarId(defaultCalendarTO.getCalendarId());

            //Setting Leave Calender Properties
            encashmentRequestTO
                    .setLeaveCalendarStartDate(DateUtil.getDateFromTimeDimensionId(leaveCalendarTO.getStartPeriod()));
            encashmentRequestTO
                    .setLeaveCalendarEndDate(DateUtil.getDateFromTimeDimensionId(leaveCalendarTO.getEndPeriod()));
            encashmentRequestTO.setLeaveCalendarId(leaveCalendarTO.getCalendarId());




            if (request.requestType != null && !request.requestType.isEmpty()) {
                encashmentRequestTO.setRequestType(request.requestType);
                if (request.requestType.equalsIgnoreCase("ProxyLeave")) {
                    encashmentRequestTO.setProxyLeave(Boolean.TRUE);
                }

            }

            encashmentRequestTO.setEncashmentRequestConfigTO(encashmentRequestConfigTO);
            encashmentRequestTO.setEncashmentEmployeeTO(encashmentEmployeeTO);
            encashmentRequestTO.setMaxFutureDaysToApply(tmLeaveTypeTO.getMaxFutureDaysToApply());

        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AppRuntimeException("Failed to set leave request object data");
        }

        return encashmentRequestTO;
    }

    public static boolean checkIfFromUpload(EncashmentRequestTO encashmentRequestTO ) {
        if (StringUtil.nonEmptyCheck(encashmentRequestTO.getSource())
                && encashmentRequestTO.getSource().equalsIgnoreCase("Upload") && encashmentRequestTO.isForceFullyFlag()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    public static List<LeaveBalanceResponseTO> processLeaveBalanceRules(
            TmLeaveBalanceRulesRepository leaveBalanceRulesRepository, Integer organizationId, Boolean activeFlag) {
        List<LeaveBalanceResponseTO> convertTo = new ArrayList<LeaveBalanceResponseTO>();
        LeaveBalanceResponseTO leaveBalanceResponseTO = new LeaveBalanceResponseTO();
        try {
            List<TmLeaveBalanceRules> tmLeaveBalanceRules = leaveBalanceRulesRepository
                    .getLeaveBalanceConfig(organizationId, Boolean.TRUE);
            if (PSCollectionUtil.isNotNullOrEmpty(tmLeaveBalanceRules)) {
                for (TmLeaveBalanceRules obj : tmLeaveBalanceRules) {
                    leaveBalanceResponseTO = new LeaveBalanceResponseTO();
                    if (obj.getLeaveBalanceRuleID() != null)
                        leaveBalanceResponseTO.setLeaveBalanceRuleID(obj.getLeaveBalanceRuleID());
                    if (obj.getConfigurationType() != null)
                        leaveBalanceResponseTO.setConfigurationType(obj.getConfigurationType());
                    if (obj.getConfigurationSubType() != null)
                        leaveBalanceResponseTO.setConfigurationSubType(obj.getConfigurationSubType());
                    if (obj.getMultiplierValue() != null)
                        leaveBalanceResponseTO.setMultiplierValue(obj.getMultiplierValue());
                    if (obj.getIsActive() != null)
                        leaveBalanceResponseTO.setIsActive(obj.getIsActive());
                    if (obj.getOrganizationId() != null)
                        leaveBalanceResponseTO.setOrganizationID(obj.getOrganizationId());
                    if (obj.getTenantId() != null)
                        leaveBalanceResponseTO.setTenantID(obj.getTenantId());
                    convertTo.add(leaveBalanceResponseTO);
                }

            }
        } catch (Exception e) {

        }

        return convertTo;
    }
    
    public static EncashmentRequestTO processEncashmentTaskActionTO(UserInfo userInfo, EncashmentTaskActionTO request, TmLeaveType tmLeaveType, CalendarTO defaultCalendarTO, CalendarTO leaveCalendarTO, TmEmployeeEncashment tmEmployeeEncashment, Map<Integer,Map<String,Object>> contentTypeMap, List<CalendarBlocksTO> calendarBlocks, List<Object[]> dependentDetails) {
        EncashmentRequestTO encashmentRequestTO = new EncashmentRequestTO();
        encashmentRequestTO.setCurrentDate(DateUtil.getCurrentDate());
        encashmentRequestTO.setCurrentDateId(DateUtil.getTimeDimensionId(DateUtil.getCurrentDate()));
        if (userInfo.getUserId() != 0) {
            encashmentRequestTO.setLoginUser(userInfo.getUserId());
        }
        if (userInfo.getOrganizationId() != 0) {
            encashmentRequestTO.setOrganizationId(userInfo.getOrganizationId());
        }
        if (userInfo.getTenantId() != 0) {
            encashmentRequestTO.setTenantId(userInfo.getTenantId());
        }
        if (userInfo.getBundleName() != null) {
            encashmentRequestTO.setBundleName(userInfo.getBundleName());
        }
        if (userInfo.getUserName() != null) {
            encashmentRequestTO.setUserName(userInfo.getUserName());
        }
        if (request.getEmployeeId() != null && !request.getEmployeeId().equals(0)) {
            encashmentRequestTO.setEmployeeId(request.getEmployeeId());
        }
        if (request.getUserId() != null && !request.getUserId().equals(0)) {
            encashmentRequestTO.setUserID(request.getUserId());
        }
        if (request.getAction() != null && !request.getAction().isEmpty()) {
            encashmentRequestTO.setAction(request.getAction());
        }
        if (userInfo.getUserId() == 1) {
            encashmentRequestTO.setStageId(request.getSysWorkflowStageID());
        }
        else {
            if (null != request.getStageActionId()) {
                encashmentRequestTO.setStageId(request.getStageActionId());
            }
        }
        if (request.getComment() != null && !request.getComment().isEmpty()) {
            encashmentRequestTO.setComments(request.getComment());
        }
        if (request.getManagerComment() != null && !request.getManagerComment().isEmpty()) {
            encashmentRequestTO.setManagerComments(request.getManagerComment());
        }
        if (request.getWithdrawlComment() != null && !request.getWithdrawlComment().isEmpty()) {
            encashmentRequestTO.setWithdrawlComments(request.getWithdrawlComment());
        }
        if (request.getEmployeeEncashmentId() != null && !request.getEmployeeEncashmentId().equals(0L)) {
            encashmentRequestTO.setEmployeeEncashmentId(request.getEmployeeEncashmentId());
        }
        if (request.getStageActionId() != null && !request.getStageActionId().equals(0)) {
            encashmentRequestTO.setStageActionId(request.getStageActionId());
        }
        if (request.getRuleOutput() != null && !request.getRuleOutput().isEmpty()) {
            encashmentRequestTO.setRuleOutput(request.getRuleOutput());
        }
        if (request.getSysUserWorkflowHistoryId() != null && !request.getSysUserWorkflowHistoryId().equals(0L)) {
            encashmentRequestTO.setWorkflowHistoryID(request.getSysUserWorkflowHistoryId());
        }
        if (request.getStageType() != null && !request.getStageType().isEmpty()) {
            encashmentRequestTO.setStageType(request.getStageType());
        }
        if (tmEmployeeEncashment.getLeaveTypeID() != null) {
            encashmentRequestTO.setLeaveTypeId(tmEmployeeEncashment.getLeaveTypeID());
        }
        
        if (request.getSysWorkflowStageID() != null) {
            encashmentRequestTO.setStageIdWhileScreenLoad(request.getSysWorkflowStageID());
        }
        if (request.getBand() != null) {
            encashmentRequestTO.setManagerBand(request.getBand());
        }
        
        if (null != tmLeaveType){
            encashmentRequestTO.setLeaveType(tmLeaveType.getLeaveTypeCode());
            encashmentRequestTO.setLeaveTypeId(tmLeaveType.getLeaveTypeId());
            encashmentRequestTO.setLeaveDescription(tmLeaveType.getLeaveTypeDescription());
        }
        
        //Setting Encashment type properties
        if (calendarBlocks != null && !calendarBlocks.isEmpty()){
            encashmentRequestTO.setCalendarBlocksTOList(calendarBlocks);
        }
        
        //Setting Default Calender Properties
        encashmentRequestTO.setDefaultCalendarStartDate(DateUtil.getDateFromTimeDimensionId(defaultCalendarTO.getStartPeriod()));
        encashmentRequestTO.setDefaultCalendarEndDate(DateUtil.getDateFromTimeDimensionId(defaultCalendarTO.getEndPeriod()));
        encashmentRequestTO.setDefaultCalendarId(defaultCalendarTO.getCalendarId());
        
        //Setting Leave Calender Properties
        encashmentRequestTO.setLeaveCalendarStartDate(DateUtil.getDateFromTimeDimensionId(leaveCalendarTO.getStartPeriod()));
        encashmentRequestTO.setLeaveCalendarEndDate(DateUtil.getDateFromTimeDimensionId(leaveCalendarTO.getEndPeriod()));
        encashmentRequestTO.setLeaveCalendarId(leaveCalendarTO.getCalendarId());
        if (request.getUserId() != null) {
            encashmentRequestTO.setLoggedInUserId(request.getUserId());
        }
        
        if (dependentDetails != null && !dependentDetails.isEmpty()){
            List<DependentDetailTO> dependentDetailTOS = new ArrayList<>();
            dependentDetails.forEach(dependentDetail->{
                DependentDetailTO dependentDetailTO = new DependentDetailTO();
                dependentDetailTO.setName(((String) dependentDetail[0]));
                dependentDetailTO.setRelationshipTypeId(((Integer) dependentDetail[1]));
                dependentDetailTO.setAge(((Integer) dependentDetail[2]));
                dependentDetailTO.setPercentage(((Integer) dependentDetail[3]));
                dependentDetailTO.setDependentId(((Integer) dependentDetail[4]));
                dependentDetailTOS.add(dependentDetailTO);
            });
            encashmentRequestTO.setDependentDetailTOList(dependentDetailTOS);
        }
        
        //Setting encashment properties
        if (null != tmEmployeeEncashment) {
            encashmentRequestTO.setApplicationDateId(tmEmployeeEncashment.getApplicationDate());
            encashmentRequestTO.setEncashmentTypeContentId(tmEmployeeEncashment.getEncashmentTypeContentId());
            if (contentTypeMap != null && !contentTypeMap.isEmpty()){
                encashmentRequestTO.setSysEncashmentType(((String) contentTypeMap.get(tmEmployeeEncashment.getEncashmentTypeContentId()).get("sysContentType")));
                encashmentRequestTO.setSysEncashmentTypeId(((Integer) contentTypeMap.get(tmEmployeeEncashment.getEncashmentTypeContentId()).get("sysContentTypeId")));
                encashmentRequestTO.setEncashmentType(((String) contentTypeMap.get(tmEmployeeEncashment.getEncashmentTypeContentId()).get("hrContentType")));
            }
            encashmentRequestTO.setEncashmentCountInDD(tmEmployeeEncashment.getEncashmentCountInDays());
            if (null != tmEmployeeEncashment.getEncashmentCountInHrs()) {
                Integer hourlyWeightageHH = LeaveUtil.getHour(tmEmployeeEncashment.getEncashmentCountInHrs().toString());
                Integer hourlyWeightageMM = LeaveUtil.getMinutes(tmEmployeeEncashment.getEncashmentCountInHrs().toString());
                encashmentRequestTO.setEncashmentCountInHH(hourlyWeightageHH);
                encashmentRequestTO.setEncashmentCountInMM(hourlyWeightageMM);
            }
        }
        return encashmentRequestTO;
    }
}
