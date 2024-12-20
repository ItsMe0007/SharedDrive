package com.peoplestrong.timeoff.encashment.transport.input;

import java.io.Serializable;
import java.util.Date;

import com.peoplestrong.timeoff.leave.flatTO.LeaveEntitlementTO;
import com.peoplestrong.timeoff.encashment.flatTO.EncashmentTypeTO;

public class EncashmentRequestConfigTO implements Serializable {

    private static final long serialVersionUID = 3706081479425175458L;

    private Boolean leaveTypeNextYearAllowed;//don't remove it. leave type

    private Boolean empNegativeApplicable;//leave type don't remove it.

    private Boolean managerNegativeApplicable;// leave type don't remove it.

    private Integer balanceCheck;

    private Integer negativeLeaveApplicable;//leave quota.don't remove it.

    private Float maxNegativeLeaveBalance;//leave quota don't remove it.

    private Float carriedForwardBalance;////leave quota don't remove it.

    private Integer consecutive;// leave quota event based.don't remove it.

    private Float maxCarryForwardPerYear;// leave entitlement

    private Integer yearlyRequestLimit;// leave type yearly request limit.

    private Integer carryforwardeligible;// leave type carryforwardeligible.

    private Float monthlyLimitdays;// leave type yearly request limit.

    private Float yearlyLimitdays;// leave type yearly limit.

    private Boolean monthlyCapping;//leave type monthly Capping.

    private Boolean isHourly;// leave type isHourly.

    private Boolean isShiftConsider;// leave type isShiftConsider.

    private Integer monthlyLimitReq;// leave type monthly limit required.

    private Date pastCutOffDate;// leave type PastCutOffDate required.

    private Boolean isCashable;// leave type isCashable

    private Boolean autoApproval;// leave type autoApproval

    private Integer maxLimitAllowedInTenure;// leave type MaxLimitAllowedInTenure

    private Integer allowedDuringNP;// leave type AllowedDuringNP

    private boolean edod;

    private Integer edodDays;

    private Integer afterEdodDays;

    private boolean isOptional;

    private Boolean allowedDuringNotice;

    private Integer leaveStartDay;

    private LeaveEntitlementTO leaveEntitlementTO;

    private EncashmentTypeTO encashmentTypeTO;

    private Boolean adjustCompForFuture;

    private Boolean dateOfDelivery;

    private Integer diffInApplicationAndDeliveryDate;

    private int mexDaysToApplyForCarryForward;

    private int mexDaysToApplyForLapseLeave;

    private Boolean isLapseLeaveRuleEligible;

    private Boolean entitlementCheckForChildren;

    private Integer countOfChildren;

    private Float entitlementBasedOnChildren;

    private Float probationLeaveLimit;

    private Integer eligibleDateForJoining;

    public Boolean getLeaveTypeNextYearAllowed() {
        return (leaveTypeNextYearAllowed == null ? Boolean.FALSE : leaveTypeNextYearAllowed);
    }
    public void setLeaveTypeNextYearAllowed(Boolean leaveTypeNextYearAllowed) {
        this.leaveTypeNextYearAllowed = leaveTypeNextYearAllowed;
    }

    public Boolean getEmpNegativeApplicable() {
        return (empNegativeApplicable == null ? Boolean.FALSE : empNegativeApplicable);
    }
    public void setEmpNegativeApplicable(Boolean empNegativeApplicable) {
        this.empNegativeApplicable = empNegativeApplicable;
    }
    public Integer getNegativeLeaveApplicable() {
        return negativeLeaveApplicable;
    }
    public void setNegativeLeaveApplicable(Integer negativeLeaveApplicable) {
        this.negativeLeaveApplicable = negativeLeaveApplicable;
    }
    public Float getMaxNegativeLeaveBalance() {
        return maxNegativeLeaveBalance;
    }
    public void setMaxNegativeLeaveBalance(Float maxNegativeLeaveBalance) {
        this.maxNegativeLeaveBalance = maxNegativeLeaveBalance;
    }
    public Float getCarriedForwardBalance() {
        return carriedForwardBalance;
    }
    public void setCarriedForwardBalance(Float carriedForwardBalance) {
        this.carriedForwardBalance = carriedForwardBalance;
    }

    public Integer getConsecutive() {
        return consecutive;
    }

    public void setConsecutive(Integer consecutive) {
        this.consecutive = consecutive;
    }
    public Integer getYearlyRequestLimit() {
        return yearlyRequestLimit;
    }
    public void setYearlyRequestLimit(Integer yearlyRequestLimit) {
        this.yearlyRequestLimit = yearlyRequestLimit;
    }
    public Float getYearlyLimitdays() {
        return yearlyLimitdays;
    }
    public void setYearlyLimitdays(Float yearlyLimitdays) {
        this.yearlyLimitdays = yearlyLimitdays;
    }
    public Boolean getMonthlyCapping() {
        return (monthlyCapping == null ? Boolean.FALSE : monthlyCapping);
    }
    public void setMonthlyCapping(Boolean monthlyCapping) {
        this.monthlyCapping = monthlyCapping;
    }


    public boolean isEdod() {
        return edod;
    }

    public void setEdod(boolean edod) {
        this.edod = edod;
    }

    public Integer getEdodDays() {
        return edodDays;
    }

    public void setEdodDays(Integer edodDays) {
        this.edodDays = edodDays;
    }

    public Integer getAfterEdodDays() {
        return afterEdodDays;
    }

    public void setAfterEdodDays(Integer afterEdodDays) {
        this.afterEdodDays = afterEdodDays;
    }

    public Integer getLeaveStartDay() {
        return leaveStartDay;
    }

    public void setLeaveStartDay(Integer leaveStartDay) {
        this.leaveStartDay = leaveStartDay;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    public Float getMonthlyLimitdays() {
        return monthlyLimitdays;
    }

    public void setMonthlyLimitdays(Float monthlyLimitdays) {
        this.monthlyLimitdays = monthlyLimitdays;
    }

    public Integer getMonthlyLimitReq() {
        return monthlyLimitReq;
    }

    public void setMonthlyLimitReq(Integer monthlyLimitReq) {
        this.monthlyLimitReq = monthlyLimitReq;
    }

    public Date getPastCutOffDate() {
        return pastCutOffDate;
    }

    public void setPastCutOffDate(Date pastCutOffDate) {
        this.pastCutOffDate = pastCutOffDate;
    }

    public Boolean getIsCashable() {
        return isCashable;
    }
    public void setIsCashable(Boolean isCashable) {
        this.isCashable = isCashable;
    }

    public Boolean getAutoApproval() {
        return autoApproval;
    }
    public void setAutoApproval(Boolean autoApproval) {
        this.autoApproval = autoApproval;
    }
    public Integer getMaxLimitAllowedInTenure() {
        return maxLimitAllowedInTenure;
    }

    public void setMaxLimitAllowedInTenure(Integer maxLimitAllowedInTenure) {
        this.maxLimitAllowedInTenure = maxLimitAllowedInTenure;
    }

    public Integer getAllowedDuringNP() {
        return allowedDuringNP;
    }

    public void setAllowedDuringNP(Integer allowedDuringNP) {
        this.allowedDuringNP = allowedDuringNP;
    }

    public Boolean isAllowedDuringNotice() {return allowedDuringNotice;}

    public void setAllowedDuringNotice(Boolean allowedDuringNotice) {this.allowedDuringNotice = allowedDuringNotice;}

    public Boolean getIsHourly() {
        return (isHourly == null ? Boolean.FALSE : isHourly);
    }

    public void setIsHourly(Boolean isHourly) {
        this.isHourly = isHourly;
    }

    public Boolean getIsShiftConsider() {
        return (isShiftConsider == null ? Boolean.FALSE : isShiftConsider);
    }

    public void setIsShiftConsider(Boolean isShiftConsider) {
        this.isShiftConsider = isShiftConsider;
    }



    public Integer getCarryforwardeligible() {
        return carryforwardeligible;
    }
    public void setCarryforwardeligible(Integer carryforwardeligible) {
        this.carryforwardeligible = carryforwardeligible;
    }
    public Boolean getManagerNegativeApplicable() {
        return (managerNegativeApplicable == null ? Boolean.FALSE : managerNegativeApplicable);
    }

    public void setManagerNegativeApplicable(Boolean managerNegativeApplicable) {
        this.managerNegativeApplicable = managerNegativeApplicable;
    }

    public Integer getBalanceCheck() {
        return balanceCheck;
    }

    public void setBalanceCheck(Integer balanceCheck) {
        this.balanceCheck = balanceCheck;
    }

    public Float getMaxCarryForwardPerYear() {
        return maxCarryForwardPerYear;
    }

    public void setMaxCarryForwardPerYear(Float maxCarryForwardPerYear) {
        this.maxCarryForwardPerYear = maxCarryForwardPerYear;
    }

    public LeaveEntitlementTO getLeaveEntitlementTO() {
        return leaveEntitlementTO;
    }
    public void setLeaveEntitlementTO(LeaveEntitlementTO leaveEntitlementTO) {
        this.leaveEntitlementTO = leaveEntitlementTO;
    }



    public Boolean getAdjustCompForFuture() {
        return adjustCompForFuture;
    }
    public void setAdjustCompForFuture(Boolean adjustCompForFuture) {
        this.adjustCompForFuture = adjustCompForFuture;
    }
    public Boolean getDateOfDelivery() {
        return dateOfDelivery;
    }
    public void setDateOfDelivery(Boolean dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }
    public Integer getDiffInApplicationAndDeliveryDate() {
        return diffInApplicationAndDeliveryDate;
    }
    public void setDiffInApplicationAndDeliveryDate(Integer diffInApplicationAndDeliveryDate) {
        this.diffInApplicationAndDeliveryDate = diffInApplicationAndDeliveryDate;
    }
    public int getMexDaysToApplyForCarryForward() {
        return mexDaysToApplyForCarryForward;
    }
    public void setMexDaysToApplyForCarryForward(int mexDaysToApplyForCarryForward) {
        this.mexDaysToApplyForCarryForward = mexDaysToApplyForCarryForward;
    }
    public int getMexDaysToApplyForLapseLeave() {
        return mexDaysToApplyForLapseLeave;
    }
    public void setMexDaysToApplyForLapseLeave(int mexDaysToApplyForLapseLeave) {
        this.mexDaysToApplyForLapseLeave = mexDaysToApplyForLapseLeave;
    }
    public Boolean getIsLapseLeaveRuleEligible() {
        return isLapseLeaveRuleEligible;
    }
    public void setIsLapseLeaveRuleEligible(Boolean isLapseLeaveRuleEligible) {
        this.isLapseLeaveRuleEligible = isLapseLeaveRuleEligible;
    }

    public Boolean getEntitlementCheckForChildren() {
        return entitlementCheckForChildren;
    }

    public void setEntitlementCheckForChildren(Boolean entitlementCheckForChildren) {
        this.entitlementCheckForChildren = entitlementCheckForChildren;
    }

    public Integer getCountOfChildren() {
        return countOfChildren;
    }

    public void setCountOfChildren(Integer countOfChildren) {
        this.countOfChildren = countOfChildren;
    }

    public Float getEntitlementBasedOnChildren() {
        return entitlementBasedOnChildren;
    }

    public void setEntitlementBasedOnChildren(Float entitlementBasedOnChildren) {
        this.entitlementBasedOnChildren = entitlementBasedOnChildren;
    }

    public Float getProbationLeaveLimit() {
        return probationLeaveLimit;
    }

    public void setProbationLeaveLimit(Float probationLeaveLimit) {
        this.probationLeaveLimit = probationLeaveLimit;
    }

    public Integer getEligibleDateForJoining() {
        return eligibleDateForJoining;
    }

    public void setEligibleDateForJoining(Integer eligibleDateForJoining) {
        this.eligibleDateForJoining = eligibleDateForJoining;
    }

    public com.peoplestrong.timeoff.encashment.flatTO.EncashmentTypeTO getEncashmentTypeTO() {
        return encashmentTypeTO;
    }

    public void setEncashmentTypeTO(com.peoplestrong.timeoff.encashment.flatTO.EncashmentTypeTO encashmentTypeTO) {
        this.encashmentTypeTO = encashmentTypeTO;
    }
}
