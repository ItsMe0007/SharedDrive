package com.peoplestrong.timeoff.encashment.transport.input;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.peoplestrong.timeoff.dataservice.model.common.HrPerson;
import com.peoplestrong.timeoff.dataservice.model.common.HrProfile;
import com.peoplestrong.timeoff.dataservice.model.leave.HrOrgCountryMapping;
import com.peoplestrong.timeoff.dataservice.model.session.HrEmployee;
import com.peoplestrong.timeoff.encashment.pojo.CalendarBlocksTO;
import com.peoplestrong.timeoff.encashment.pojo.base.DependentDetailTO;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;

public class EncashmentRequestTO implements Serializable {

    private static final long serialVersionUID = 7442943881457894445L;

    private Integer employeeId;

    private int leaveTypeId;

    private int leaveCount;

    private int leaveCountForNextYear;

    private Date StartDate;

    private String encashmentType;

    private Long employeeEncashmentId;

    private Boolean isMultiCalenderEnabled;

    private Date EndDate;

    private Date currentDate;

    private String action;

    private int tenantId;

    private int organizationId;

    private String leaveCode;

    private String userName;

    private String comments;

    private int leaveReasonId;

    private EncashmentEmployeeTO encashmentEmployeeTO;

    private String leaveReasonCd;

    private String managerComments;

    private String managerBand;

    private long employeeLeaveId;

    private boolean active;

    private Date applicationDate;

    private int loginUser;

    private String leaveType;

    private String leavereason;

    private int stageId;

    private Date stageCompletionDate;

    private String stage;

    private int stageActionId;

    private String ruleOutcome;

    private String ruleOutput;

    private float durationWeightage;

    private Long workflowHistoryID;

    private Double currentBalance;

    private Integer edodDateId;

    private int startDateId;

    private int endDateId;

    private int currentDateId;

    private String leaveDurationtype;

    private int leaveDurationTypeId;

    private int userID;

    private EncashmentRequestConfigTO encashmentRequestConfigTO;
    private int durationTypeID;

    private Integer leaveQuotaId;

    private String attachmentPath;

    private String leaveDescription;

    private String leaveEmployeeContact;

    private String leaveEmployeeAddress;

    private Integer l1ManagerID;

    private Integer l2ManagerID;

    private Integer hrManagerID;

    private String withdrawlComments;

    private String stageType;

    private String requestType;

    private String fileName;

    private boolean directApproved;

    private boolean shiftManagerRole;

    private String gradeDesc;

    private String employeeCategoryTypeCode;

    private int leaveLapseId;

    private Date compOffDate;

    private boolean isProxyLeave;

    private String leaveCategoryType;// Periodic_Leave, EventBased_Leave,EffortBased_Leave

    private Integer leaveCalendarId;

    private Date leaveCalendarStartDate;

    private Date leaveCalendarEndDate;

    private Integer defaultCalendarId;

    private Date defaultCalendarStartDate;

    private Date defaultCalendarEndDate;


    private Double leaveDaysCount;

    private Integer leaveCountInDD;

    private Integer leaveCountInHH;

    private Integer leaveCountInMM;

    private Date selected_MarriageDate;


    private Integer stageIdWhileScreenLoad;

    private String bundleName;

    public String source;

    public boolean forceFullyFlag;

    public boolean isAUAndRevertCase;

    public UserInfo userInfo;

    private Date dateOfDeath;

    private LocalDate dateOfIntent;

    private LocalDate dateOfAdoption;

    private String birthCertificateNumber;

    private LocalDate adoptedDateOfBirth;

    private Integer dateOfDeliveryId;

    private String maxFutureDaysToApply;

    private Boolean lapseLeaveRuleAllowed =false;

    private HrOrgCountryMapping organizationDefaultCountry;
    Map<Long, Set<Integer>> employeeCountryWiseRoleMap;
    Map<Long, Set<Integer>> managerCountryWiseRoleMap;

    private HrEmployee hrEmployee;
    private HrProfile HrEmployeeProfile;
    private HrPerson hrEmployeePerson;

    private HrEmployee hrManager;

    private Integer numberOfTransactionsPerDayLimit;

    private Float mexLeaveApplyForNextYear;

    private Boolean multiCalenderEnabled;

    public boolean hdlRevampEnable;

    private Set<Integer> leaveTypeIds;

    private Integer birthdayCalendarYear;

    private Boolean holidayIncluded;

    private String encashmentCode;

    private Boolean weekOffIncluded;

    private Boolean punchWarningPopUp;

    private Boolean regWarningPopUp;

    private Integer loggedInUserId;

    private Integer anniversaryCalendarYear;

    private Integer currentStageId;
    
    private String sysEncashmentType;
    
    private Integer sysEncashmentTypeId;
    
    private Integer encashmentTypeContentId;

    private List<CalendarBlocksTO> calendarBlocksTOList;
    private List<DependentDetailTO> dependentDetailTOList;
    private Float encashmentNumberOfDays;
    private Integer applicationDateId;
    private Integer encashmentCountInDD;
    private Integer encashmentCountInHH;
    private Integer encashmentCountInMM;
    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public int getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(int leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public int getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(int leaveCount) {
        this.leaveCount = leaveCount;
    }

    public int getLeaveCountForNextYear() {
        return leaveCountForNextYear;
    }

    public void setLeaveCountForNextYear(int leaveCountForNextYear) {
        this.leaveCountForNextYear = leaveCountForNextYear;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getLeaveReasonId() {
        return leaveReasonId;
    }

    public void setLeaveReasonId(int leaveReasonId) {
        this.leaveReasonId = leaveReasonId;
    }

    public String getLeaveReasonCd() {
        return leaveReasonCd;
    }

    public void setLeaveReasonCd(String leaveReasonCd) {
        this.leaveReasonCd = leaveReasonCd;
    }

    public String getManagerComments() {
        return managerComments;
    }

    public void setManagerComments(String managerComments) {
        this.managerComments = managerComments;
    }

    public String getManagerBand() {
        return managerBand;
    }

    public void setManagerBand(String managerBand) {
        this.managerBand = managerBand;
    }

    public long getEmployeeLeaveId() {
        return employeeLeaveId;
    }

    public void setEmployeeLeaveId(long employeeLeaveId) {
        this.employeeLeaveId = employeeLeaveId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public int getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(int loginUser) {
        this.loginUser = loginUser;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeavereason() {
        return leavereason;
    }

    public void setLeavereason(String leavereason) {
        this.leavereason = leavereason;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public Date getStageCompletionDate() {
        return stageCompletionDate;
    }

    public void setStageCompletionDate(Date stageCompletionDate) {
        this.stageCompletionDate = stageCompletionDate;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getStageActionId() {
        return stageActionId;
    }

    public void setStageActionId(int stageActionId) {
        this.stageActionId = stageActionId;
    }

    public String getRuleOutcome() {
        return ruleOutcome;
    }

    public void setRuleOutcome(String ruleOutcome) {
        this.ruleOutcome = ruleOutcome;
    }

    public String getRuleOutput() {
        return ruleOutput;
    }

    public void setRuleOutput(String ruleOutput) {
        this.ruleOutput = ruleOutput;
    }

    public float getDurationWeightage() {
        return durationWeightage;
    }

    public void setDurationWeightage(float durationWeightage) {
        this.durationWeightage = durationWeightage;
    }

    public Long getWorkflowHistoryID() {
        return workflowHistoryID;
    }

    public void setWorkflowHistoryID(Long workflowHistoryID) {
        this.workflowHistoryID = workflowHistoryID;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Integer getEdodDateId() {
        return edodDateId;
    }

    public void setEdodDateId(Integer edodDateId) {
        this.edodDateId = edodDateId;
    }

    public int getStartDateId() {
        return startDateId;
    }

    public void setStartDateId(int startDateId) {
        this.startDateId = startDateId;
    }

    public int getEndDateId() {
        return endDateId;
    }

    public void setEndDateId(int endDateId) {
        this.endDateId = endDateId;
    }

    public int getCurrentDateId() {
        return currentDateId;
    }

    public void setCurrentDateId(int currentDateId) {
        this.currentDateId = currentDateId;
    }

    public String getLeaveDurationtype() {
        return leaveDurationtype;
    }

    public void setLeaveDurationtype(String leaveDurationtype) {
        this.leaveDurationtype = leaveDurationtype;
    }

    public int getLeaveDurationTypeId() {
        return leaveDurationTypeId;
    }

    public void setLeaveDurationTypeId(int leaveDurationTypeId) {
        this.leaveDurationTypeId = leaveDurationTypeId;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getDurationTypeID() {
        return durationTypeID;
    }

    public void setDurationTypeID(int durationTypeID) {
        this.durationTypeID = durationTypeID;
    }

    public Integer getLeaveQuotaId() {
        return leaveQuotaId;
    }

    public void setLeaveQuotaId(Integer leaveQuotaId) {
        this.leaveQuotaId = leaveQuotaId;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getLeaveDescription() {
        return leaveDescription;
    }

    public void setLeaveDescription(String leaveDescription) {
        this.leaveDescription = leaveDescription;
    }

    public String getLeaveEmployeeContact() {
        return leaveEmployeeContact;
    }

    public void setLeaveEmployeeContact(String leaveEmployeeContact) {
        this.leaveEmployeeContact = leaveEmployeeContact;
    }

    public String getLeaveEmployeeAddress() {
        return leaveEmployeeAddress;
    }

    public void setLeaveEmployeeAddress(String leaveEmployeeAddress) {
        this.leaveEmployeeAddress = leaveEmployeeAddress;
    }

    public Integer getL1ManagerID() {
        return l1ManagerID;
    }

    public void setL1ManagerID(Integer l1ManagerID) {
        this.l1ManagerID = l1ManagerID;
    }

    public Integer getL2ManagerID() {
        return l2ManagerID;
    }

    public void setL2ManagerID(Integer l2ManagerID) {
        this.l2ManagerID = l2ManagerID;
    }

    public Integer getHrManagerID() {
        return hrManagerID;
    }

    public void setHrManagerID(Integer hrManagerID) {
        this.hrManagerID = hrManagerID;
    }

    public String getWithdrawlComments() {
        return withdrawlComments;
    }

    public void setWithdrawlComments(String withdrawlComments) {
        this.withdrawlComments = withdrawlComments;
    }

    public String getStageType() {
        return stageType;
    }

    public void setStageType(String stageType) {
        this.stageType = stageType;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isDirectApproved() {
        return directApproved;
    }

    public void setDirectApproved(boolean directApproved) {
        this.directApproved = directApproved;
    }

    public boolean isShiftManagerRole() {
        return shiftManagerRole;
    }

    public void setShiftManagerRole(boolean shiftManagerRole) {
        this.shiftManagerRole = shiftManagerRole;
    }

    public String getGradeDesc() {
        return gradeDesc;
    }

    public void setGradeDesc(String gradeDesc) {
        this.gradeDesc = gradeDesc;
    }

    public String getEmployeeCategoryTypeCode() {
        return employeeCategoryTypeCode;
    }

    public void setEmployeeCategoryTypeCode(String employeeCategoryTypeCode) {
        this.employeeCategoryTypeCode = employeeCategoryTypeCode;
    }

    public int getLeaveLapseId() {
        return leaveLapseId;
    }

    public void setLeaveLapseId(int leaveLapseId) {
        this.leaveLapseId = leaveLapseId;
    }

    public Date getCompOffDate() {
        return compOffDate;
    }

    public void setCompOffDate(Date compOffDate) {
        this.compOffDate = compOffDate;
    }

    public boolean isProxyLeave() {
        return isProxyLeave;
    }

    public void setProxyLeave(boolean proxyLeave) {
        isProxyLeave = proxyLeave;
    }

    public String getLeaveCategoryType() {
        return leaveCategoryType;
    }

    public void setLeaveCategoryType(String leaveCategoryType) {
        this.leaveCategoryType = leaveCategoryType;
    }

    public Integer getLeaveCalendarId() {
        return leaveCalendarId;
    }

    public void setLeaveCalendarId(Integer leaveCalendarId) {
        this.leaveCalendarId = leaveCalendarId;
    }

    public Date getLeaveCalendarStartDate() {
        return leaveCalendarStartDate;
    }

    public void setLeaveCalendarStartDate(Date leaveCalendarStartDate) {
        this.leaveCalendarStartDate = leaveCalendarStartDate;
    }

    public Date getLeaveCalendarEndDate() {
        return leaveCalendarEndDate;
    }

    public void setLeaveCalendarEndDate(Date leaveCalendarEndDate) {
        this.leaveCalendarEndDate = leaveCalendarEndDate;
    }

    public Integer getDefaultCalendarId() {
        return defaultCalendarId;
    }

    public void setDefaultCalendarId(Integer defaultCalendarId) {
        this.defaultCalendarId = defaultCalendarId;
    }

    public Date getDefaultCalendarStartDate() {
        return defaultCalendarStartDate;
    }

    public void setDefaultCalendarStartDate(Date defaultCalendarStartDate) {
        this.defaultCalendarStartDate = defaultCalendarStartDate;
    }

    public Date getDefaultCalendarEndDate() {
        return defaultCalendarEndDate;
    }

    public void setDefaultCalendarEndDate(Date defaultCalendarEndDate) {
        this.defaultCalendarEndDate = defaultCalendarEndDate;
    }

    public Double getLeaveDaysCount() {
        return leaveDaysCount;
    }

    public void setLeaveDaysCount(Double leaveDaysCount) {
        this.leaveDaysCount = leaveDaysCount;
    }

    public Integer getLeaveCountInDD() {
        return leaveCountInDD;
    }

    public void setLeaveCountInDD(Integer leaveCountInDD) {
        this.leaveCountInDD = leaveCountInDD;
    }

    public Integer getLeaveCountInHH() {
        return leaveCountInHH;
    }

    public void setLeaveCountInHH(Integer leaveCountInHH) {
        this.leaveCountInHH = leaveCountInHH;
    }

    public Integer getLeaveCountInMM() {
        return leaveCountInMM;
    }

    public void setLeaveCountInMM(Integer leaveCountInMM) {
        this.leaveCountInMM = leaveCountInMM;
    }

    public Date getSelected_MarriageDate() {
        return selected_MarriageDate;
    }

    public void setSelected_MarriageDate(Date selected_MarriageDate) {
        this.selected_MarriageDate = selected_MarriageDate;
    }

    public Integer getStageIdWhileScreenLoad() {
        return stageIdWhileScreenLoad;
    }

    public void setStageIdWhileScreenLoad(Integer stageIdWhileScreenLoad) {
        this.stageIdWhileScreenLoad = stageIdWhileScreenLoad;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isForceFullyFlag() {
        return forceFullyFlag;
    }

    public void setForceFullyFlag(boolean forceFullyFlag) {
        this.forceFullyFlag = forceFullyFlag;
    }

    public boolean isAUAndRevertCase() {
        return isAUAndRevertCase;
    }

    public void setAUAndRevertCase(boolean AUAndRevertCase) {
        isAUAndRevertCase = AUAndRevertCase;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public LocalDate getDateOfIntent() {
        return dateOfIntent;
    }

    public void setDateOfIntent(LocalDate dateOfIntent) {
        this.dateOfIntent = dateOfIntent;
    }

    public LocalDate getDateOfAdoption() {
        return dateOfAdoption;
    }

    public void setDateOfAdoption(LocalDate dateOfAdoption) {
        this.dateOfAdoption = dateOfAdoption;
    }

    public String getBirthCertificateNumber() {
        return birthCertificateNumber;
    }

    public void setBirthCertificateNumber(String birthCertificateNumber) {
        this.birthCertificateNumber = birthCertificateNumber;
    }

    public LocalDate getAdoptedDateOfBirth() {
        return adoptedDateOfBirth;
    }

    public void setAdoptedDateOfBirth(LocalDate adoptedDateOfBirth) {
        this.adoptedDateOfBirth = adoptedDateOfBirth;
    }

    public Integer getDateOfDeliveryId() {
        return dateOfDeliveryId;
    }

    public void setDateOfDeliveryId(Integer dateOfDeliveryId) {
        this.dateOfDeliveryId = dateOfDeliveryId;
    }

    public String getMaxFutureDaysToApply() {
        return maxFutureDaysToApply;
    }

    public void setMaxFutureDaysToApply(String maxFutureDaysToApply) {
        this.maxFutureDaysToApply = maxFutureDaysToApply;
    }

    public Boolean getLapseLeaveRuleAllowed() {
        return lapseLeaveRuleAllowed;
    }

    public void setLapseLeaveRuleAllowed(Boolean lapseLeaveRuleAllowed) {
        this.lapseLeaveRuleAllowed = lapseLeaveRuleAllowed;
    }

    public HrOrgCountryMapping getOrganizationDefaultCountry() {
        return organizationDefaultCountry;
    }

    public void setOrganizationDefaultCountry(HrOrgCountryMapping organizationDefaultCountry) {
        this.organizationDefaultCountry = organizationDefaultCountry;
    }

    public Map<Long, Set<Integer>> getEmployeeCountryWiseRoleMap() {
        return employeeCountryWiseRoleMap;
    }

    public void setEmployeeCountryWiseRoleMap(Map<Long, Set<Integer>> employeeCountryWiseRoleMap) {
        this.employeeCountryWiseRoleMap = employeeCountryWiseRoleMap;
    }

    public Map<Long, Set<Integer>> getManagerCountryWiseRoleMap() {
        return managerCountryWiseRoleMap;
    }

    public void setManagerCountryWiseRoleMap(Map<Long, Set<Integer>> managerCountryWiseRoleMap) {
        this.managerCountryWiseRoleMap = managerCountryWiseRoleMap;
    }

    public HrEmployee getHrEmployee() {
        return hrEmployee;
    }

    public void setHrEmployee(HrEmployee hrEmployee) {
        this.hrEmployee = hrEmployee;
    }

    public HrProfile getHrEmployeeProfile() {
        return HrEmployeeProfile;
    }

    public void setHrEmployeeProfile(HrProfile hrEmployeeProfile) {
        HrEmployeeProfile = hrEmployeeProfile;
    }

    public HrPerson getHrEmployeePerson() {
        return hrEmployeePerson;
    }

    public void setHrEmployeePerson(HrPerson hrEmployeePerson) {
        this.hrEmployeePerson = hrEmployeePerson;
    }

    public HrEmployee getHrManager() {
        return hrManager;
    }

    public void setHrManager(HrEmployee hrManager) {
        this.hrManager = hrManager;
    }

    public Integer getNumberOfTransactionsPerDayLimit() {
        return numberOfTransactionsPerDayLimit;
    }

    public void setNumberOfTransactionsPerDayLimit(Integer numberOfTransactionsPerDayLimit) {
        this.numberOfTransactionsPerDayLimit = numberOfTransactionsPerDayLimit;
    }

    public Float getMexLeaveApplyForNextYear() {
        return mexLeaveApplyForNextYear;
    }

    public void setMexLeaveApplyForNextYear(Float mexLeaveApplyForNextYear) {
        this.mexLeaveApplyForNextYear = mexLeaveApplyForNextYear;
    }

    public Boolean getMultiCalenderEnabled() {
        return multiCalenderEnabled;
    }

    public void setMultiCalenderEnabled(Boolean multiCalenderEnabled) {
        this.multiCalenderEnabled = multiCalenderEnabled;
    }

    public boolean isHdlRevampEnable() {
        return hdlRevampEnable;
    }

    public void setHdlRevampEnable(boolean hdlRevampEnable) {
        this.hdlRevampEnable = hdlRevampEnable;
    }

    public Set<Integer> getLeaveTypeIds() {
        return leaveTypeIds;
    }

    public void setLeaveTypeIds(Set<Integer> leaveTypeIds) {
        this.leaveTypeIds = leaveTypeIds;
    }

    public Integer getBirthdayCalendarYear() {
        return birthdayCalendarYear;
    }

    public void setBirthdayCalendarYear(Integer birthdayCalendarYear) {
        this.birthdayCalendarYear = birthdayCalendarYear;
    }

    public Boolean getHolidayIncluded() {
        return holidayIncluded;
    }

    public void setHolidayIncluded(Boolean holidayIncluded) {
        this.holidayIncluded = holidayIncluded;
    }

    public Boolean getWeekOffIncluded() {
        return weekOffIncluded;
    }

    public void setWeekOffIncluded(Boolean weekOffIncluded) {
        this.weekOffIncluded = weekOffIncluded;
    }

    public Boolean getPunchWarningPopUp() {
        return punchWarningPopUp;
    }

    public void setPunchWarningPopUp(Boolean punchWarningPopUp) {
        this.punchWarningPopUp = punchWarningPopUp;
    }

    public Boolean getRegWarningPopUp() {
        return regWarningPopUp;
    }

    public void setRegWarningPopUp(Boolean regWarningPopUp) {
        this.regWarningPopUp = regWarningPopUp;
    }

    public Integer getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setLoggedInUserId(Integer loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    public Integer getAnniversaryCalendarYear() {
        return anniversaryCalendarYear;
    }

    public void setAnniversaryCalendarYear(Integer anniversaryCalendarYear) {
        this.anniversaryCalendarYear = anniversaryCalendarYear;
    }

    public Integer getCurrentStageId() {
        return currentStageId;
    }

    public void setCurrentStageId(Integer currentStageId) {
        this.currentStageId = currentStageId;
    }

    public EncashmentRequestConfigTO getEncashmentRequestConfigTO() {
        return encashmentRequestConfigTO;
    }

    public void setEncashmentRequestConfigTO(EncashmentRequestConfigTO encashmentRequestConfigTO) {
        this.encashmentRequestConfigTO = encashmentRequestConfigTO;
    }

    public EncashmentEmployeeTO getEncashmentEmployeeTO() {
        return encashmentEmployeeTO;
    }

    public void setEncashmentEmployeeTO(EncashmentEmployeeTO encashmentEmployeeTO) {
        this.encashmentEmployeeTO = encashmentEmployeeTO;
    }

    public Long getEmployeeEncashmentId() {
        return employeeEncashmentId;
    }

    public void setEmployeeEncashmentId(Long employeeEncashmentId) {
        this.employeeEncashmentId = employeeEncashmentId;
    }

    public String getEncashmentCode() {
        return encashmentCode;
    }

    public void setEncashmentCode(String encashmentCode) {
        this.encashmentCode = encashmentCode;
    }

    public String getEncashmentType() {
        return encashmentType;
    }

    public void setEncashmentType(String encashmentType) {
        this.encashmentType = encashmentType;
    }

    public Boolean getIsMultiCalenderEnabled() {
        return isMultiCalenderEnabled;
    }

    public void setIsMultiCalenderEnabled(Boolean isMultiCalenderEnabled) {
        this.isMultiCalenderEnabled = isMultiCalenderEnabled;
    }

    public List<CalendarBlocksTO> getCalendarBlocksTOList() {
        return calendarBlocksTOList;
    }

    public void setCalendarBlocksTOList(List<CalendarBlocksTO> calendarBlocksTOList) {
        this.calendarBlocksTOList = calendarBlocksTOList;
    }

    public List<DependentDetailTO> getDependentDetailTOList() {
        return dependentDetailTOList;
    }

    public void setDependentDetailTOList(List<DependentDetailTO> dependentDetailTOList) {
        this.dependentDetailTOList = dependentDetailTOList;
    }

    public Float getEncashmentNumberOfDays() {
        return encashmentNumberOfDays;
    }

    public void setEncashmentNumberOfDays(Float encashmentNumberOfDays) {
        this.encashmentNumberOfDays = encashmentNumberOfDays;
    }
    
    public String getSysEncashmentType() {
        return sysEncashmentType;
    }
    
    public void setSysEncashmentType(String sysEncashmentType) {
        this.sysEncashmentType = sysEncashmentType;
    }
    
    public Integer getSysEncashmentTypeId() {
        return sysEncashmentTypeId;
    }
    
    public void setSysEncashmentTypeId(Integer sysEncashmentTypeId) {
        this.sysEncashmentTypeId = sysEncashmentTypeId;
    }
    
    public Integer getEncashmentTypeContentId() {
        return encashmentTypeContentId;
    }
    
    public void setEncashmentTypeContentId(Integer encashmentTypeContentId) {
        this.encashmentTypeContentId = encashmentTypeContentId;
    }
    
    public Integer getApplicationDateId() {
        return applicationDateId;
    }
    
    public void setApplicationDateId(Integer applicationDateId) {
        this.applicationDateId = applicationDateId;
    }
    
    public Integer getEncashmentCountInDD() {
        return encashmentCountInDD;
    }
    
    public void setEncashmentCountInDD(Integer encashmentCountInDD) {
        this.encashmentCountInDD = encashmentCountInDD;
    }
    
    public Integer getEncashmentCountInHH() {
        return encashmentCountInHH;
    }
    
    public void setEncashmentCountInHH(Integer encashmentCountInHH) {
        this.encashmentCountInHH = encashmentCountInHH;
    }
    
    public Integer getEncashmentCountInMM() {
        return encashmentCountInMM;
    }
    
    public void setEncashmentCountInMM(Integer encashmentCountInMM) {
        this.encashmentCountInMM = encashmentCountInMM;
    }
}
