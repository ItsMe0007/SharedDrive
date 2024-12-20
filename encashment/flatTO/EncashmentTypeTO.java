package com.peoplestrong.timeoff.encashment.flatTO;

import java.io.Serializable;
import java.util.Date;

import com.peoplestrong.timeoff.translation.annotation.TranslatorField;
import com.peoplestrong.timeoff.translation.constants.TranslatedEntityName;
import com.peoplestrong.timeoff.translation.interfaces.TranslatedObject;

public class EncashmentTypeTO implements Serializable, TranslatedObject {

    private static final long serialVersionUID = 4882319729919097426L;

    @TranslatorField(TranslatedFieldName = "encashmentTypeCode", Table = TranslatedEntityName.TmEncashmentType)
    private Long encashmentTypeID;

    private String encashmentTypeCode;

    private Integer encashmentTypeContentID;

    private Long encashmentCalenderGroupID;

    private Boolean attachmentMandatory;

    private Integer maximumEncashmentInTransactionInDays;

    private Short maximumEncashmentInTransactionInHrs;

    private Integer minimumEncashmentInTransactionInDays;

    private Short minimumEncashmentInTransactionInHrs;

    private Integer maximumNumberOfTransactions;

    private Long ageBasedEncashmentID;

    private Long dependentBasedEncashmentID;

    private Boolean active;

    private Integer tenantID;

    @TranslatorField(TranslatedFieldName = "leaveTypeCode", Table = TranslatedEntityName.TmLeaveType)
    private int leaveTypeId;

    private String leaveTypeCode;

    private String leaveTypeDescription;

    private int genderId;

    private int religionId;

    private int organizationId;

    private boolean hideBalanceDetail;

    public boolean empInitiatedNegBalAllowed;

    public boolean L1InitiatedNegBalAllowed;

    public Integer maxLimitAllowedInTenure;

    public Integer eligibleFromDOJ;

    public Boolean hourly;

    public Boolean isShiftConsider;

    public String leaveMessage;

    public Boolean isEdod;

    public Integer eligibleDays;

    public Boolean quotaEditable;

    private Integer carryforwardeligible;

    private Boolean canCarryForward;

    private Boolean applyNextYearLeave;

    private Boolean isSickLeaveEligibility;

    private Boolean isTenureLeaveEligibility;

    private Integer leaveMultipleInMin;

    public Boolean skipLeaveReason;

    private Integer countryId;

    private Integer maxChildrenAllowed;

    private Boolean isProxyLeaveOnly;

    private String maxFutureDaysToApply;

    private Integer maxcarriedForwardeDaysToApply;

    public Boolean isDateOfDelivery;

    public Date calendarEndDate;

    private String calendarEndDateString;

    private Boolean isAnnualLeaveEligibility;

    private Boolean isYearlyTenure;

    public EncashmentTypeTO() {
        super();
    }

    public EncashmentTypeTO(int leaveTypeId, String leaveTypeCode) {
        super();
        this.leaveTypeId = leaveTypeId;
        this.leaveTypeCode = leaveTypeCode;
    }

    public EncashmentTypeTO(int leaveTypeId, Boolean hourly) {
        super();
        this.leaveTypeId = leaveTypeId;
        this.hourly = hourly;
    }

    public EncashmentTypeTO(String leaveTypeDesc, int leaveTypeId) {
        super();
        this.leaveTypeId = leaveTypeId;
        this.leaveTypeDescription = leaveTypeDesc;
    }
    public EncashmentTypeTO(String leaveTypeDesc, int leaveTypeId, String leaveTypeCode, Integer countryId) {
        super();
        this.leaveTypeCode = leaveTypeCode;
        this.leaveTypeDescription = leaveTypeDesc;
        this.leaveTypeId = leaveTypeId;
        this.countryId=countryId;

    }

    public EncashmentTypeTO(String leaveTypeDesc, int leaveTypeId, String leaveTypeCode, Boolean isShiftConsider) {
        super();
        this.leaveTypeCode = leaveTypeCode;
        this.leaveTypeDescription = leaveTypeDesc;
        this.leaveTypeId = leaveTypeId;
        this.isShiftConsider = isShiftConsider;
    }

    public Boolean getQuotaEditable() {
        return quotaEditable;
    }

    public void setQuotaEditable(Boolean quotaEditable) {
        this.quotaEditable = quotaEditable;
    }

    public int getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(int leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getLeaveTypeCode() {
        return leaveTypeCode;
    }

    public void setLeaveTypeCode(String leaveTypeCode) {
        this.leaveTypeCode = leaveTypeCode;
    }

    public String getLeaveTypeDescription() {
        return leaveTypeDescription;
    }

    public void setLeaveTypeDescription(String leaveTypeDescription) {
        this.leaveTypeDescription = leaveTypeDescription;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public int getReligionId() {
        return religionId;
    }

    public void setReligionId(int religionId) {
        this.religionId = religionId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isHideBalanceDetail() {
        return hideBalanceDetail;
    }

    public void setHideBalanceDetail(boolean hideBalanceDetail) {
        this.hideBalanceDetail = hideBalanceDetail;
    }

    public boolean isEmpInitiatedNegBalAllowed() {
        return empInitiatedNegBalAllowed;
    }

    public void setEmpInitiatedNegBalAllowed(boolean empInitiatedNegBalAllowed) {
        this.empInitiatedNegBalAllowed = empInitiatedNegBalAllowed;
    }

    public Integer getMaxLimitAllowedInTenure() {
        return maxLimitAllowedInTenure;
    }

    public void setMaxLimitAllowedInTenure(Integer maxLimitAllowedInTenure) {
        this.maxLimitAllowedInTenure = maxLimitAllowedInTenure;
    }

    public Integer getEligibleFromDOJ() {
        return eligibleFromDOJ;
    }

    public void setEligibleFromDOJ(Integer eligibleFromDOJ) {
        this.eligibleFromDOJ = eligibleFromDOJ;
    }

    public Boolean getCanCarryForward() {
        return canCarryForward;
    }

    public void setCanCarryForward(Boolean canCarryForward) {
        this.canCarryForward = canCarryForward;
    }

    public Boolean getIsShiftConsider() {
        return isShiftConsider;
    }

    public void setIsShiftConsider(Boolean isShiftConsider) {
        this.isShiftConsider = isShiftConsider;
    }

    public boolean isL1InitiatedNegBalAllowed() {
        return L1InitiatedNegBalAllowed;
    }

    public void setL1InitiatedNegBalAllowed(boolean l1InitiatedNegBalAllowed) {
        L1InitiatedNegBalAllowed = l1InitiatedNegBalAllowed;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public Boolean getIsEdod() {
        return isEdod;
    }

    public void setIsEdod(Boolean isEdod) {
        this.isEdod = isEdod;
    }

    public Integer getEligibleDays() {
        return eligibleDays;
    }

    public void setEligibleDays(Integer eligibleDays) {
        this.eligibleDays = eligibleDays;
    }

    public Boolean getHourly() {
        return hourly;
    }

    public void setHourly(Boolean hourly) {
        this.hourly = hourly;
    }



    public Integer getCarryforwardeligible() {
        return carryforwardeligible;
    }

    public void setCarryforwardeligible(Integer carryforwardeligible) {
        this.carryforwardeligible = carryforwardeligible;
    }

    public Boolean getApplyNextYearLeave() {
        return applyNextYearLeave;
    }

    public void setApplyNextYearLeave(Boolean applyNextYearLeave) {
        this.applyNextYearLeave = applyNextYearLeave;
    }

    public Boolean getIsSickLeaveEligibility() {
        return isSickLeaveEligibility;
    }

    public void setIsSickLeaveEligibility(Boolean isSickLeaveEligibility) {
        this.isSickLeaveEligibility = isSickLeaveEligibility;
    }

    public Boolean getIsTenureLeaveEligibility() {
        return isTenureLeaveEligibility;
    }

    public void setIsTenureLeaveEligibility(Boolean isTenureLeaveEligibility) {
        this.isTenureLeaveEligibility = isTenureLeaveEligibility;
    }

    public Integer getLeaveMultipleInMin() {
        return leaveMultipleInMin;
    }

    public void setLeaveMultipleInMin(Integer leaveMultipleInMin) {
        this.leaveMultipleInMin = leaveMultipleInMin;
    }

    public Boolean getSkipLeaveReason() {
        return skipLeaveReason;
    }

    public void setSkipLeaveReason(Boolean skipLeaveReason) {
        this.skipLeaveReason = skipLeaveReason;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getMaxChildrenAllowed() {
        return maxChildrenAllowed;
    }

    public void setMaxChildrenAllowed(Integer maxChildrenAllowed) {
        this.maxChildrenAllowed = maxChildrenAllowed;
    }

    public Boolean getIsProxyLeaveOnly() {
        return isProxyLeaveOnly;
    }

    public void setIsProxyLeaveOnly(Boolean isProxyLeaveOnly) {
        this.isProxyLeaveOnly = isProxyLeaveOnly;
    }

    public String getMaxFutureDaysToApply() {
        return maxFutureDaysToApply;
    }

    public void setMaxFutureDaysToApply(String maxFutureDaysToApply) {
        this.maxFutureDaysToApply = maxFutureDaysToApply;
    }


    public Boolean getIsDateOfDelivery() {
        return isDateOfDelivery;
    }

    public void setIsDateOfDelivery(Boolean isDateOfDelivery) {
        this.isDateOfDelivery = isDateOfDelivery;
    }

    public Integer getMaxcarriedForwardeDaysToApply() {
        return maxcarriedForwardeDaysToApply;
    }

    public void setMaxcarriedForwardeDaysToApply(Integer maxcarriedForwardeDaysToApply) {
        this.maxcarriedForwardeDaysToApply = maxcarriedForwardeDaysToApply;
    }

    public Date getCalendarEndDate() {
        return calendarEndDate;
    }

    public void setCalendarEndDate(Date calendarEndDate) {
        this.calendarEndDate = calendarEndDate;
    }

    public String getCalendarEndDateString() {
        return calendarEndDateString;
    }

    public void setCalendarEndDateString(String calendarEndDateString) {
        this.calendarEndDateString = calendarEndDateString;
    }

    public Boolean getIsAnnualLeaveEligibility() {
        return isAnnualLeaveEligibility;
    }

    public void setIsAnnualLeaveEligibility(Boolean annualLeaveEligibility) {
        isAnnualLeaveEligibility = annualLeaveEligibility;
    }

    public Boolean getIsYearlyTenure() {
        return isYearlyTenure;
    }

    public void setIsYearlyTenure(Boolean isYearlyTenure) {
        this.isYearlyTenure = isYearlyTenure;
    }

    public Long getEncashmentTypeID() {
        return encashmentTypeID;
    }

    public void setEncashmentTypeID(Long encashmentTypeID) {
        this.encashmentTypeID = encashmentTypeID;
    }

    public String getEncashmentTypeCode() {
        return encashmentTypeCode;
    }

    public void setEncashmentTypeCode(String encashmentTypeCode) {
        this.encashmentTypeCode = encashmentTypeCode;
    }

    public Integer getEncashmentTypeContentID() {
        return encashmentTypeContentID;
    }

    public void setEncashmentTypeContentID(Integer encashmentTypeContentID) {
        this.encashmentTypeContentID = encashmentTypeContentID;
    }

    public Long getEncashmentCalenderGroupID() {
        return encashmentCalenderGroupID;
    }

    public void setEncashmentCalenderGroupID(Long encashmentCalenderGroupID) {
        this.encashmentCalenderGroupID = encashmentCalenderGroupID;
    }

    public Boolean getAttachmentMandatory() {
        return attachmentMandatory;
    }

    public void setAttachmentMandatory(Boolean attachmentMandatory) {
        this.attachmentMandatory = attachmentMandatory;
    }

    public Integer getMaximumEncashmentInTransactionInDays() {
        return maximumEncashmentInTransactionInDays;
    }

    public void setMaximumEncashmentInTransactionInDays(Integer maximumEncashmentInTransactionInDays) {
        this.maximumEncashmentInTransactionInDays = maximumEncashmentInTransactionInDays;
    }

    public Short getMaximumEncashmentInTransactionInHrs() {
        return maximumEncashmentInTransactionInHrs;
    }

    public void setMaximumEncashmentInTransactionInHrs(Short maximumEncashmentInTransactionInHrs) {
        this.maximumEncashmentInTransactionInHrs = maximumEncashmentInTransactionInHrs;
    }

    public Integer getMinimumEncashmentInTransactionInDays() {
        return minimumEncashmentInTransactionInDays;
    }

    public void setMinimumEncashmentInTransactionInDays(Integer minimumEncashmentInTransactionInDays) {
        this.minimumEncashmentInTransactionInDays = minimumEncashmentInTransactionInDays;
    }

    public Short getMinimumEncashmentInTransactionInHrs() {
        return minimumEncashmentInTransactionInHrs;
    }

    public void setMinimumEncashmentInTransactionInHrs(Short minimumEncashmentInTransactionInHrs) {
        this.minimumEncashmentInTransactionInHrs = minimumEncashmentInTransactionInHrs;
    }

    public Integer getMaximumNumberOfTransactions() {
        return maximumNumberOfTransactions;
    }

    public void setMaximumNumberOfTransactions(Integer maximumNumberOfTransactions) {
        this.maximumNumberOfTransactions = maximumNumberOfTransactions;
    }

    public Long getAgeBasedEncashmentID() {
        return ageBasedEncashmentID;
    }

    public void setAgeBasedEncashmentID(Long ageBasedEncashmentID) {
        this.ageBasedEncashmentID = ageBasedEncashmentID;
    }

    public Long getDependentBasedEncashmentID() {
        return dependentBasedEncashmentID;
    }

    public void setDependentBasedEncashmentID(Long dependentBasedEncashmentID) {
        this.dependentBasedEncashmentID = dependentBasedEncashmentID;
    }
}
