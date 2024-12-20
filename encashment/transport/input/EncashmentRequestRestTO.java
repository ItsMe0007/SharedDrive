package com.peoplestrong.timeoff.encashment.transport.input;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.peoplestrong.timeoff.encashment.pojo.CalendarBlocksTO;
import com.peoplestrong.timeoff.encashment.pojo.base.DependentDetailTO;

public class EncashmentRequestRestTO  implements Serializable {

    @JsonProperty("startDate")
    private Date startDate;

    @JsonProperty("startDateString")
    private String startDateString;

    @JsonProperty("endDate")
    private Date endDate;

    @JsonProperty("endDateString")
    private String endDateString;

    @JsonProperty("edodDate")
    private Date edodDate;

    @JsonProperty("edodDate")
    private String edodDateString;

    @JsonProperty("leaveTypeId")
    private Integer leaveTypeId;

    @JsonProperty("leaveQuotaId")
    private int leaveQuotaId;

    @JsonProperty("leaveReasonId")
    private int leaveReasonId;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("managerComment")
    private String managerComment;

    @JsonProperty("stageActionId")
    private int stageActionId;

    @JsonProperty("ruleOutput")
    private String ruleOutput;

    @JsonProperty("encashmentType")
    private String encashmentType;

    @JsonProperty("encashmentDescription")
    private String encashmentDescription;

    @JsonProperty("action")
    private String action;

    @JsonProperty("attachmentPath")
    private String attachmentPath;

    @JsonProperty("fileName")
    private String fileName;


    private List<CalendarBlocksTO> calendarBlocks;

    @JsonProperty("currentLeaveBalance")
    private double currentLeaveBalance;

    @JsonProperty("durationWeightage")
    private float durationWeightage;

    @JsonProperty("leaveCount")
    private int leaveCount;

    @JsonProperty("contactNumber")
    private String contactNumber;

    @JsonProperty("reachableAddress")
    private String reachableAddress;

    @JsonProperty("leaveReason")
    private String leaveReason;

    @JsonProperty("leaveType")
    private String leaveType;

    @JsonProperty("noticeApply")
    private float noticeApply;

    @JsonProperty("consicutive")
    private int consicutive;

    @JsonProperty("employeeId")
    private int employeeId;

    @JsonProperty("dateOfMarriage")
    private Date dateOfMarriage;

    @JsonProperty("dateOfMarriageString")
    private String dateOfMarriageString;

    private List<DependentDetailTO> dependentDetails;

    @JsonProperty("userId")
    public int userId;

    @JsonProperty("l1ManagerID")
    public int l1ManagerID;

    @JsonProperty("l2ManagerID")
    public int l2ManagerID;

    @JsonProperty("hrManagerID")
    public int hrManagerID;

    @JsonProperty("requestType")
    public String requestType;

    @JsonProperty("selectedCompOff")
    public int selectedCompOff;

    @JsonProperty("compOffDate")
    public Date compOffDate;

    @JsonProperty("compOffDateString")
    private String compOffDateString;

    @JsonProperty("source")
    public String source;

    @JsonProperty("forceFullyFlag")
    public boolean forceFullyFlag;

    @JsonProperty("dateOfDeath")
    public Date dateOfDeath;

    @JsonProperty("dateOfDeathString")
    private String dateOfDeathString;

    @JsonProperty("dateOfIntent")
    private LocalDate dateOfIntent;

    @JsonProperty("dateOfIntentString")
    private String dateOfIntentString;

    @JsonProperty("dateOfAdoption")
    private LocalDate dateOfAdoption;

    @JsonProperty("dateOfAdoptionString")
    private String dateOfAdoptionString;

    @JsonProperty("birthCertificateNumber")
    private String birthCertificateNumber;

    @JsonProperty("adoptedDateOfBirth")
    private LocalDate adoptedDateOfBirth;

    @JsonProperty("adoptedDateOfBirthString")
    private String adoptedDateOfBirthString;

    @JsonProperty("dateOfDelivery")
    private Date dateOfDelivery;

    @JsonProperty("dateOfDeliveryString")
    private String dateOfDeliveryString;


    @JsonProperty("loginUserID")
    public int loginUserID;

    @JsonProperty("loginEmployeeID")
    public int loginEmployeeID;

    @JsonProperty("organizationID")
    public int organizationID;

    @JsonProperty("birthdayYear")
    public int birthdayYear;

    @JsonProperty("punchWarningPopUp")
    public Boolean punchWarningPopUp;

    @JsonProperty("regWarningPopUp")
    public Boolean regWarningPopUp;

    @JsonProperty("loggedInUserId")
    public Integer loggedInUserId;

    @JsonProperty("anniversaryYear")
    public Integer anniversaryYear;

    @JsonProperty("employeeEncashmentId")
    private Long employeeEncashmentId;


    @JsonProperty("confirmationDateId")
    public Integer confirmationDateId;
    @JsonProperty("numberOfDays")
    public Float numberOfDays;

    @JsonProperty("sysEncashmentType")
    private String sysEncashmentType;
    
    @JsonProperty("sysEncashmentTypeId")
    private Integer sysEncashmentTypeId;
    
    @JsonProperty("encashmentTypeContentId")
    private Integer encashmentTypeContentId;

    public Long encashmentCalenderGroupId;
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }


    public int getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(int leaveCount) {
        this.leaveCount = leaveCount;
    }

    public void setStageActionId(int stageActionId) {
        this.stageActionId = stageActionId;
    }

    public String getRuleOutput() {
        return ruleOutput;
    }

    public void setRuleOutput(String ruleOutput) {
        this.ruleOutput = ruleOutput;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(Integer leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public int getLeaveReasonId() {
        return leaveReasonId;
    }

    public void setLeaveReasonId(int leaveReasonId) {
        this.leaveReasonId = leaveReasonId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public int getStageActionId() {
        return stageActionId;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public int getLeaveQuotaId() {
        return leaveQuotaId;
    }

    public void setLeaveQuotaId(int leaveQuotaId) {
        this.leaveQuotaId = leaveQuotaId;
    }

    public double getCurrentLeaveBalance() {
        return currentLeaveBalance;
    }

    public void setCurrentLeaveBalance(double currentLeaveBalance) {
        this.currentLeaveBalance = currentLeaveBalance;
    }

    public float getDurationWeightage() {
        return durationWeightage;
    }

    public void setDurationWeightage(float durationWeightage) {
        this.durationWeightage = durationWeightage;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getReachableAddress() {
        return reachableAddress;
    }

    public void setReachableAddress(String reachableAddress) {
        this.reachableAddress = reachableAddress;
    }

    public float getNoticeApply() {
        return noticeApply;
    }

    public void setNoticeApply(float noticeApply) {
        this.noticeApply = noticeApply;
    }

    public int getConsicutive() {
        return consicutive;
    }

    public void setConsicutive(int consicutive) {
        this.consicutive = consicutive;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(Date dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public Date getEdodDate() {
        return edodDate;
    }

    public void setEdodDate(Date edodDate) {
        this.edodDate = edodDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getL1ManagerID() {
        return l1ManagerID;
    }

    public void setL1ManagerID(int l1ManagerID) {
        this.l1ManagerID = l1ManagerID;
    }

    public int getL2ManagerID() {
        return l2ManagerID;
    }

    public void setL2ManagerID(int l2ManagerID) {
        this.l2ManagerID = l2ManagerID;
    }

    public int getHrManagerID() {
        return hrManagerID;
    }

    public void setHrManagerID(int hrManagerID) {
        this.hrManagerID = hrManagerID;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }


    public Date getCompOffDate() {
        return compOffDate;
    }

    public void setCompOffDate(Date compOffDate) {
        this.compOffDate = compOffDate;
    }


    public int getSelectedCompOff() {
        return selectedCompOff;
    }

    public void setSelectedCompOff(int selectedCompOff) {
        this.selectedCompOff = selectedCompOff;
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

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public LocalDate getDateOfIntent () {
        return dateOfIntent;
    }

    public void setDateOfIntent (LocalDate dateOfIntent) {
        this.dateOfIntent = dateOfIntent;
    }

    public LocalDate getDateOfAdoption () {
        return dateOfAdoption;
    }

    public void setDateOfAdoption (LocalDate dateOfAdoption) {
        this.dateOfAdoption = dateOfAdoption;
    }

    public String getBirthCertificateNumber () {
        return birthCertificateNumber;
    }

    public void setBirthCertificateNumber (String birthCertificateNumber) {
        this.birthCertificateNumber = birthCertificateNumber;
    }

    public LocalDate getAdoptedDateOfBirth () {
        return adoptedDateOfBirth;
    }

    public void setAdoptedDateOfBirth (LocalDate adoptedDateOfBirth) {
        this.adoptedDateOfBirth = adoptedDateOfBirth;
    }

    public String getStartDateString () {
        return startDateString;
    }

    public void setStartDateString (String startDateString) {
        this.startDateString = startDateString;
    }

    public String getEndDateString () {
        return endDateString;
    }

    public void setEndDateString (String endDateString) {
        this.endDateString = endDateString;
    }

    public String getEdodDateString () {
        return edodDateString;
    }

    public void setEdodDateString (String edodDateString) {
        this.edodDateString = edodDateString;
    }

    public String getDateOfMarriageString () {
        return dateOfMarriageString;
    }

    public void setDateOfMarriageString (String dateOfMarriageString) {
        this.dateOfMarriageString = dateOfMarriageString;
    }

    public String getCompOffDateString () {
        return compOffDateString;
    }

    public void setCompOffDateString (String compOffDateString) {
        this.compOffDateString = compOffDateString;
    }

    public String getDateOfDeathString () {
        return dateOfDeathString;
    }

    public void setDateOfDeathString (String dateOfDeathString) {
        this.dateOfDeathString = dateOfDeathString;
    }

    public String getDateOfIntentString () {
        return dateOfIntentString;
    }

    public void setDateOfIntentString (String dateOfIntentString) {
        this.dateOfIntentString = dateOfIntentString;
    }

    public String getDateOfAdoptionString () {
        return dateOfAdoptionString;
    }

    public void setDateOfAdoptionString (String dateOfAdoptionString) {
        this.dateOfAdoptionString = dateOfAdoptionString;
    }

    public String getAdoptedDateOfBirthString () {
        return adoptedDateOfBirthString;
    }

    public void setAdoptedDateOfBirthString (String adoptedDateOfBirthString) {
        this.adoptedDateOfBirthString = adoptedDateOfBirthString;
    }

    public Date getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(Date dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public String getDateOfDeliveryString() {
        return dateOfDeliveryString;
    }

    public void setDateOfDeliveryString(String dateOfDeliveryString) {
        this.dateOfDeliveryString = dateOfDeliveryString;
    }

    public int getLoginUserID() {
        return loginUserID;
    }

    public void setLoginUserID(int loginUserID) {
        this.loginUserID = loginUserID;
    }

    public int getLoginEmployeeID() {
        return loginEmployeeID;
    }

    public void setLoginEmployeeID(int loginEmployeeID) {
        this.loginEmployeeID = loginEmployeeID;
    }

    public int getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(int organizationID) {
        this.organizationID = organizationID;
    }

    public int getBirthdayYear() {
        return birthdayYear;
    }
    public void setBirthdayYear(int birthdayYear) {
        this.birthdayYear = birthdayYear;
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

    public Integer getAnniversaryYear() {
        return anniversaryYear;
    }
    public void setAnniversaryYear(Integer anniversaryYear) {
        this.anniversaryYear = anniversaryYear;
    }


    public String getEncashmentDescription() {
        return encashmentDescription;
    }

    public void setEncashmentDescription(String encashmentDescription) {
        this.encashmentDescription = encashmentDescription;
    }

    public List<CalendarBlocksTO> getCalendarBlocks() {
        return calendarBlocks;
    }

    public void setCalendarBlocks(List<CalendarBlocksTO> calendarBlocks) {
        this.calendarBlocks = calendarBlocks;
    }

    public List<DependentDetailTO> getDependentDetails() {
        return dependentDetails;
    }

    public void setDependentDetails(List<DependentDetailTO> dependentDetails) {
        this.dependentDetails = dependentDetails;
    }

    public Integer getConfirmationDateId() {
        return confirmationDateId;
    }

    public void setConfirmationDateId(Integer confirmationDateId) {
        this.confirmationDateId = confirmationDateId;
    }

    public Long getEmployeeEncashmentId() {
        return employeeEncashmentId;
    }

    public void setEmployeeEncashmentId(Long employeeEncashmentId) {
        this.employeeEncashmentId = employeeEncashmentId;
    }

    public String getEncashmentType() {
        return encashmentType;
    }

    public void setEncashmentType(String encashmentType) {
        this.encashmentType = encashmentType;
    }

    public Long getEncashmentCalenderGroupId() {
        return encashmentCalenderGroupId;
    }

    public void setEncashmentCalenderGroupId(Long encashmentCalenderGroupId) {
        this.encashmentCalenderGroupId = encashmentCalenderGroupId;
    }

    public Float getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Float numberOfDays) {
        this.numberOfDays = numberOfDays;
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
}
