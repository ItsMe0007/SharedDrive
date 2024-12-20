package com.peoplestrong.timeoff.encashment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.common.constant.ModuleConfigConstant;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.impl.service.CommonService;
import com.peoplestrong.timeoff.encashment.pojo.EncashmentApprovalHistoryTO;
import com.peoplestrong.timeoff.common.transport.ProtocolTO;
import com.peoplestrong.timeoff.common.util.DateUtil;
import com.peoplestrong.timeoff.common.util.PSCollectionUtil;
import com.peoplestrong.timeoff.common.util.StringUtil;
import com.peoplestrong.timeoff.dataservice.model.HrEmployeeDependentDetail;
import com.peoplestrong.timeoff.common.util.Utils;
import com.peoplestrong.timeoff.dataservice.model.common.HrEmployeeProfile;
import com.peoplestrong.timeoff.dataservice.model.common.HrPerson;
import com.peoplestrong.timeoff.dataservice.model.common.HrProfile;
import com.peoplestrong.timeoff.dataservice.model.common.SysEntity;
import com.peoplestrong.timeoff.dataservice.model.encashment.*;
import com.peoplestrong.timeoff.dataservice.model.leave.*;
import com.peoplestrong.timeoff.dataservice.model.session.HrEmployee;
import com.peoplestrong.timeoff.dataservice.repo.common.CommonRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.HrEmployeeProfileRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.HrPersonRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.HrProfileRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.SysEntityRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.*;
import com.peoplestrong.timeoff.dataservice.repo.leave.*;
import com.peoplestrong.timeoff.dataservice.repo.session.HrEmployeeRepository;
import com.peoplestrong.timeoff.dataservice.repo.task.HrEmployeeDependentDetailRepository;
import com.peoplestrong.timeoff.encashment.constant.EncashmentConstant;
import com.peoplestrong.timeoff.encashment.constant.EncashmentErrorConstants;
import com.peoplestrong.timeoff.encashment.flatTO.EncashmentTypeTO;
import com.peoplestrong.timeoff.encashment.flatTO.NewEncashmentRequestInfo;
import com.peoplestrong.timeoff.encashment.pojo.*;
import com.peoplestrong.timeoff.dataservice.repo.session.SysUserRepository;
import com.peoplestrong.timeoff.encashment.constant.IEncashmentErrorConstants;
import com.peoplestrong.timeoff.encashment.helper.EncashmentUtil;
import com.peoplestrong.timeoff.encashment.pojo.base.*;
import com.peoplestrong.timeoff.encashment.service.EncashmentService;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentTaskActionRequestTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentTaskActionTO;
import com.peoplestrong.timeoff.formsecurity.constant.IEncashmentForm;
import com.peoplestrong.timeoff.formsecurity.service.IFormSecurityService;
import com.peoplestrong.timeoff.encashment.service.EncModuleConfigurationService;
import com.peoplestrong.timeoff.encashment.service.impl.transaction.EncashmentSubmitTransactionService;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentRequestRestTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentRequestTO;
import com.peoplestrong.timeoff.encashment.validation.impl.EncashmentValidationService;
import com.peoplestrong.timeoff.leave.constant.ILeaveErrorConstants;
import com.peoplestrong.timeoff.leave.constant.LeaveConstant;
import com.peoplestrong.timeoff.leave.flatTO.LeaveTypeTO;
import com.peoplestrong.timeoff.leave.helper.LeaveUtil;
import com.peoplestrong.timeoff.leave.pojo.*;
import com.peoplestrong.timeoff.leave.service.CalendarService;
import com.peoplestrong.timeoff.leave.service.Impl.CommunicationServiceImpl;
import com.peoplestrong.timeoff.leave.service.Impl.GroupServiceImpl;
import com.peoplestrong.timeoff.leave.service.Impl.SysContentServiceImpl;
import com.peoplestrong.timeoff.leave.service.Impl.TimeDimensionServiceImpl;
import com.peoplestrong.timeoff.leave.service.LeaveDurationService;
import com.peoplestrong.timeoff.leave.service.LeaveFuntionalityEnableService;
import com.peoplestrong.timeoff.leave.service.LeaveQuotaService;
import com.peoplestrong.timeoff.leave.service.ModuleConfigurationService;
import com.peoplestrong.timeoff.security.fileUploadSecurity.FileUtil;
import com.peoplestrong.timeoff.workflow.to.*;
import com.peoplestrong.timeoff.dataservice.model.leave.SysWorkflowStageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class EncashmentServiceImpl implements EncashmentService {
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final String CONTENT_CATEGORY = "RelationshipType";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Logger LOGGER = Logger.getLogger(EncashmentServiceImpl.class.toString());
    
    @Autowired
    EmployeeEncashmentRepository employeeEncashmentRepository;
    @Autowired
    SysUserWorkflowHistoryRepository sysUserWorkflowHistoryRepo;
    @Autowired
    SysWorkflowStageRepository sysWorkflowStageRepository;
    @Autowired
    SysWorkflowStageTypeRepository sysWorkflowStageTypeRepository;
    @Autowired
    TimeDimesionRepository timeDimesionRepository;
    @Autowired
    LeaveQuotaService leaveQuotaService;
    @Autowired
    LeaveTypeRepository leaveTypeRepo;
    @Autowired
    TimeDimensionServiceImpl timeDimensionServiceImpl;
    @Autowired
    LeaveReasonRepository leaveReasonRepo;
    @Autowired
    SysEntityRepository sysEntityRepository;
    @Autowired
    EncashmentSubmitTransactionService encashmentTransactionService;
    @Autowired
    EmployeeLeaveDetailRepository employeeLeaveDetailRepo;
    @Autowired
    CommunicationServiceImpl communicationService;
    @Autowired
    CommonRepository commonRepository;
    @Autowired
    HrEmployeeRepository hrEmployeeRepository;
    @Autowired
    EncashmentHistoryRepository encashmentHistoryRepository;
    @Autowired
    LeaveDurationService leaveDurationService;
    @Autowired
    Environment env;
    @Autowired
    GroupServiceImpl groupServiceImpl;
    @Autowired
    LeaveEntitlementRepository leaveEntitlementRepository;
    @Autowired
    LeaveFuntionalityEnableService leaveFuntionalityEnableService;
    @Autowired
    CalendarService calendarService;
    @Autowired
    SysContentServiceImpl sysContentServiceImpl;
    @Autowired
    HrProfileRepository hrProfileRepository;
    @Autowired
    HrPersonRepository hrPersonRepository;
    @Autowired
    SysUserRepository sysUserRepository;
    @Autowired
    EncModuleConfigurationService encModuleConfigurationService;
    @Autowired
    CommonService commonService;
    @Autowired
    ModuleConfigurationService moduleConfigurationService;
    @Autowired
    HrEmployeeProfileRepository hrEmployeeProfileRepository;
    @Autowired
    TmEmployeeEncashmentRepository tmEmployeeEncashmentRepository;
    @Autowired
    TmEmployeeEncashmentDetailRepository encashmentDetailRepository;
    @Autowired
    EncashmentValidationService encashmentValidationService;
    @Autowired
    LeaveTypeRepository leaveTypeRepository;
    @Autowired
    TmEncashmentTypeRepository tmEncashmentTypeRepository;
    @Autowired
    TmCalenderBlockDetailRepository tmCalenderBlockDetailRepository;
    @Autowired
    HrContentTypeRepository hrContentTypeRepository;
    @Autowired
    HrEmployeeDependentDetailRepository hrEmployeeDependentDetailRepository;
    @Autowired
    LeaveQuotaRepository leaveQuotaRepository;
    @Autowired
    TmCalenderBlockRepository tmCalenderBlockRepository;
    @Autowired
    TimeDimensionServiceImpl timeDimensionService;
    @Autowired
    LeaveDurationTypeRepository leaveDurationTypeRepository;
    @Autowired
    TmDependentBasedEncashmentDetailRepository tmDependentBasedEncashmentDetailRepository;
    @Autowired
    IFormSecurityService IformSecurityService;
    @Autowired
    TmEncashmentCalenderGroupRepository calenderGroupRepository;
    @Autowired
    TmEncashmentDependentDetailRepository tmEncashmentDependentDetailRepository;

    @Override
    public EncashmentTaskDetailRequestTO getLeaveTaskEncashmentDetail(UserInfo userInfo , EncashmentTaskRequestTO encashmentRequestTo)
            throws Exception
    {
        EncashmentTaskDetailRequestTO encashmentTaskDetailRequestTO = null;
        String errorMessage = null;
        ResourceBundle resourceBundle = null;
        EncashmentListingTo encashmentListingTo = null;
        List<LeaveQuotaTO> leaveQuotaTOListBalance = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(userInfo.getBundleName()));
            Long encashmentId = encashmentRequestTo.getEncashmentId();
            TmEmployeeEncashment encashmentDetails = tmEmployeeEncashmentRepository.getEmployeeEncashmentByEncashmentId(encashmentId.longValue());
            if (encashmentDetails != null){
                List<LeaveTypeTO> leaveTypes = leaveTypeRepository.getLeavecodeAndDescriptionByLeaveTypeId(userInfo.getOrganizationId(),Collections.singleton(encashmentDetails.getLeaveTypeID()));
                if (leaveTypes != null && !leaveTypes.isEmpty()){
                    LeaveTypeTO leaveTypeTO = leaveTypes.get(0);
                    List<Object[]> encashmentTypeDetails = encashmentDetailRepository.getCalendarBlockAndEncashmentTypeIdByEncashmentId(encashmentId,userInfo.getOrganizationId());
                    Map<Integer,Map<String,Object>> hrContentType = sysContentServiceImpl.getContentMapForEncashment(EncashmentConstant.ENCASHMENT_CONTENT_CATEGORY,userInfo.getOrganizationId());
                    if (encashmentTypeDetails != null && !encashmentTypeDetails.isEmpty()){
                        Set<Long> calendarBlockDetailIds = encashmentTypeDetails.stream().map(encashmentDetail->((Long) encashmentDetail[0])).collect(Collectors.toSet());
                        List<Object[]> calandarBlockDetails = tmCalenderBlockDetailRepository.findCalendarBlockDetailsById(calendarBlockDetailIds,userInfo.getOrganizationId());
                        Map<Long,String> calendarBlockMap = new HashMap<>();
                        if (calandarBlockDetails != null && !calandarBlockDetails.isEmpty()){
                            calandarBlockDetails.forEach(calandarBlockDetail->{
                                calendarBlockMap.put(((Long) calandarBlockDetail[0]), ((String) calandarBlockDetail[1]));
                            });
                        }
                        encashmentListingTo = processEncashmentListingTo(encashmentDetails, encashmentTypeDetails, calendarBlockMap, hrContentType, leaveTypeTO);
                    }
                }
            }
            if (encashmentListingTo != null) {
                if (encashmentListingTo.getEncashmentSysType().equals("Actual Travel Encashment")){
                    Map<Integer,String> relationshipMap = new HashMap<>();
                    List<Object[]> relationData = hrContentTypeRepository.getHrContentTypeAndSysType(EncashmentConstant.RELATION_TYPE_CONTENT_CATEGORY, userInfo.getOrganizationId());
                    if (relationData != null && !relationData.isEmpty()){
                        relationData.forEach(relation->{
                            relationshipMap.put(((Integer) relation[0]),((String) relation[1]));
                        });
                    }
                    List<Object[]> dependentDetails = tmEncashmentDependentDetailRepository.findDependentDetailsByEncashmentId(encashmentId, userInfo.getOrganizationId());
                    if (dependentDetails != null && !dependentDetails.isEmpty()){
                        List<DependentDetailTO> dependentDetailTOList = new ArrayList<>();
                        dependentDetails.forEach(dependent->{
                            DependentDetailTO dependentDetailTO = new DependentDetailTO();
                            dependentDetailTO.setAge(((Integer) dependent[2]));
                            dependentDetailTO.setName(((String) dependent[0]));
                            if (relationshipMap != null && !relationshipMap.isEmpty()){
                                dependentDetailTO.setRelation(relationshipMap.get(((Integer) dependent[1])));
                            }
                            dependentDetailTO.setPercentage(((Integer) dependent[3]));
                            dependentDetailTO.setDependentId(((Integer) dependent[4]));
                            dependentDetailTO.setRelationshipTypeId(((Integer) dependent[1]));
                            dependentDetailTOList.add(dependentDetailTO);
                        });
                        encashmentListingTo.setDependentDetails(dependentDetailTOList);
                    }
                }
                if (encashmentListingTo.getEncashmentCountInDays() == null) {
                    encashmentListingTo.setEncashmentCountInDays(0);
                }
                if (encashmentListingTo.getEncashmentCountInHrs() == null) {
                    encashmentListingTo.setEncashmentCountInHrs((short) 0);
                }
                Integer hourlyWeightage = encashmentListingTo.getHourlyWeightage() == null ?
                        leaveDurationTypeRepository.getHourlyWightageByOrganization(userInfo.getOrganizationId(), true, LeaveConstant.LEAVE_DURATION_FULL) :
                        encashmentListingTo.getHourlyWeightage().intValue() ;
                float count = LeaveUtil.quotaCalculation(encashmentListingTo.getEncashmentCountInDays(), encashmentListingTo.getEncashmentCountInHrs().intValue(), hourlyWeightage);
                encashmentListingTo.setEncashmentCount(count);
                Integer employeeId = userInfo.getEmployeeId();
                if (encashmentRequestTo.getEmployeeId() != null && encashmentRequestTo.getEmployeeId() > 0)
                    employeeId = encashmentRequestTo.getEmployeeId();
                
                encashmentTaskDetailRequestTO = new EncashmentTaskDetailRequestTO();
                encashmentTaskDetailRequestTO.setEncashmentListingTo(encashmentListingTo);
                
                Integer hourlyWeightageForQuota = leaveDurationTypeRepository.getHourlyWightageByOrganization(userInfo.getOrganizationId(),true, "Full Day");
                leaveQuotaTOListBalance = leaveQuotaService.getEmployeeLeaveQuotaBalance(employeeId,userInfo.getOrganizationId(), userInfo.getBundleName());
                if (leaveQuotaTOListBalance != null && !leaveQuotaTOListBalance.isEmpty()) {
                    List<Integer> leaveTypeIdsForQuota = leaveQuotaTOListBalance.stream()
                            .map(LeaveQuotaTO::getLeaveTypeId)
                            .collect(Collectors.toList());
                    List<TmEmployeeLeaveQuota> encashmentQuotaTOList = leaveQuotaRepository.getLeavetypeIdhavingBalanceLeftforExhausedLeavetypesWithoutCalender(employeeId, userInfo.getOrganizationId(), true, leaveTypeIdsForQuota);
                    Map<Integer, Object> encashmentBalanceMap = new HashMap<>();
                    for (TmEmployeeLeaveQuota tmEmployeeLeaveQuota : encashmentQuotaTOList) {
                        Map<String, Object> tempMap = new HashMap<>();
                        if (tmEmployeeLeaveQuota.getEncachmentBalanceInDays() == null) {
                            tmEmployeeLeaveQuota.setEncachmentBalanceInDays(0);
                        }
                        if (tmEmployeeLeaveQuota.getEncachmentBalanceInHrs() == null) {
                            tmEmployeeLeaveQuota.setEncachmentBalanceInHrs(0);
                        }
                        tempMap.put("encashmentBalanceInDays", tmEmployeeLeaveQuota.getEncachmentBalanceInDays());
                        tempMap.put("encashmentBalanceInHrs", tmEmployeeLeaveQuota.getEncachmentBalanceInHrs());
                        Float leaveEncashment = LeaveUtil.quotaCalculation(tmEmployeeLeaveQuota.getEncachmentBalanceInDays(), tmEmployeeLeaveQuota.getEncachmentBalanceInHrs(), hourlyWeightageForQuota);
                        tempMap.put("leaveEncashment", leaveEncashment);
                        encashmentBalanceMap.put(tmEmployeeLeaveQuota.getLeaveTypeId(), tempMap);
                    }
                    List<EncashmentLeaveQuotaTO> encashmentLeaveQuotaTOList = new ArrayList<>();
                    leaveQuotaTOListBalance.forEach(leaveQuotaTO -> {
                        EncashmentLeaveQuotaTO encashmentLeaveQuotaTO = objectMapper.convertValue(leaveQuotaTO, EncashmentLeaveQuotaTO.class);
                        encashmentLeaveQuotaTO.setEncashmentBalanceInDays(((Map<String, Integer>) encashmentBalanceMap.get(leaveQuotaTO.getLeaveTypeId())).get("encashmentBalanceInDays"));
                        encashmentLeaveQuotaTO.setEncashmentBalenceInHrs(((Map<String, Integer>) encashmentBalanceMap.get(leaveQuotaTO.getLeaveTypeId())).get("encashmentBalanceInHrs"));
                        encashmentLeaveQuotaTO.setLeaveEncashment(((Map<String, Float>) encashmentBalanceMap.get(leaveQuotaTO.getLeaveTypeId())).get("leaveEncashment"));
                        encashmentLeaveQuotaTOList.add(encashmentLeaveQuotaTO);
                    });
                    encashmentTaskDetailRequestTO.setLeaveBalance(encashmentLeaveQuotaTOList);
                }
            }
        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            if (resourceBundle != null)
                errorMessage = resourceBundle.getString(EncashmentErrorConstants.FAILED_TOLOAD_SELECTED_TASKDETAILS);
            else errorMessage = e.getMessage();
            throw new AppRuntimeException(errorMessage);
        }
        return encashmentTaskDetailRequestTO;
    }

    private EncashmentListingTo processEncashmentListingTo(TmEmployeeEncashment encashmentDetail, List<Object[]> encashmentTypeDetails, Map<Long,String> calendarBlockMap, Map<Integer,Map<String,Object>> hrContentType, LeaveTypeTO leaveType){
        EncashmentListingTo encashmentListingTo = new EncashmentListingTo();
        if (encashmentDetail != null){
                encashmentListingTo.setEncashmentId(encashmentDetail.getEncashmentID());
                encashmentListingTo.setEncashmentCode(encashmentDetail.getEncashmentTaskCode());
                encashmentListingTo.setLeaveTypeID(encashmentDetail.getLeaveTypeID());
                encashmentListingTo.setStatusMessage(encashmentDetail.getStatusMessage());
                encashmentListingTo.setStatusID(encashmentDetail.getSysWorkflowStageID());
                encashmentListingTo.setComments(encashmentDetail.getComments());
                encashmentListingTo.setManagerComments(encashmentDetail.getManagerComments());
                encashmentListingTo.setEmployeeID(encashmentDetail.getEmployeeID());
                encashmentListingTo.setActive(encashmentDetail.getIsActive());
                encashmentListingTo.setAppliedDate(encashmentDetail.getAppliedDate());
                if (encashmentListingTo.getAppliedDate() != null) {
                    encashmentListingTo.setAppliedDateString(sdf.format(encashmentListingTo.getAppliedDate()));
                }
                encashmentListingTo.setLeaveTypeCode(leaveType.getLeaveTypeCode());
                encashmentListingTo.setApplicationDate(encashmentDetail.getApplicationDate());
                encashmentListingTo.setManagerInitiated(encashmentDetail.getIsManagerInitiated());
                if (encashmentDetail.getFilePath() != null)
                    encashmentListingTo.setFilePath(FileUtil.getEncryptedFilePath(encashmentDetail.getFilePath()));
                encashmentListingTo.setFileName(encashmentDetail.getFileName());
                encashmentListingTo.setEmployeeWithdrawComments(encashmentDetail.getEmployeeWithdrawComments());
                encashmentListingTo.setL1ManagerID(encashmentDetail.getL1ManagerID());
                encashmentListingTo.setL2ManagerID(encashmentDetail.getL2ManagerID());
                encashmentListingTo.setHrManagerID(encashmentDetail.getHrManagerID());
                encashmentListingTo.setStageType(encashmentDetail.getStageType());
                encashmentListingTo.setPreviousStageType(encashmentDetail.getPreviousStageType());
                encashmentListingTo.setUserId(encashmentDetail.getUserID());
                encashmentListingTo.setStageId(encashmentDetail.getSysWorkflowStageID());
                encashmentListingTo.setWorkFlowHistoryId(encashmentDetail.getWorkFlowHistoryID());
                encashmentListingTo.setEncashmentCountInDays(encashmentDetail.getEncashmentCountInDays());
                encashmentListingTo.setEncashmentCountInHrs(encashmentDetail.getEncashmentCountInHrs());
                encashmentListingTo.setTenantId(encashmentDetail.getTenantID());
                encashmentListingTo.setHourlyWeightage(encashmentDetail.getHourlyWeightage());
                encashmentListingTo.setEncashmentTypeContentId(encashmentDetail.getEncashmentTypeContentId());
                if (hrContentType != null && !hrContentType.isEmpty()){
                    encashmentListingTo.setEncashmentSysType(((String) hrContentType.get(encashmentDetail.getEncashmentTypeContentId()).get("sysContentType")));
                    encashmentListingTo.setEncashmentSysTypeId(((Integer) hrContentType.get(encashmentDetail.getEncashmentTypeContentId()).get("sysContentTypeId")));
                }
                if (encashmentTypeDetails != null && !encashmentTypeDetails.isEmpty() && hrContentType != null && !hrContentType.isEmpty()){
                    List<CalendarBlocksTO> calendarBlocksTOS = new ArrayList<>();
                    encashmentTypeDetails.forEach(encashmentTypeDetail->{
                        CalendarBlocksTO calendarBlocksTO = new CalendarBlocksTO();
                        if (calendarBlockMap != null && !calendarBlockMap.isEmpty() && hrContentType != null && !hrContentType.isEmpty()){
                            calendarBlocksTO.setCalendarBlock(calendarBlockMap.get(((Long) encashmentTypeDetail[0])));
                            calendarBlocksTO.setEncashmentTypeId(((Long) encashmentTypeDetail[1]));
                            calendarBlocksTO.setCalendarBlockId(((Long) encashmentTypeDetail[0]));
                            calendarBlocksTOS.add(calendarBlocksTO);
                        }
                    });
                    encashmentListingTo.setCalendarBlocksTOS(calendarBlocksTOS);
                    encashmentListingTo.setEncashmentTypeCode((String) hrContentType.get(encashmentDetail.getEncashmentTypeContentId()).get("hrContentType"));
                    encashmentListingTo.setEncashmentTypeID(encashmentDetail.getEncashmentTypeContentId());
                }
        }
        else {
            throw new AppRuntimeException("No encashment details found.");
        }
        return encashmentListingTo;
    }
    
    @Override
    public List<Integer> getActorIDAsPerEncashmentID(long ContextInstanceID, Integer tenantId) {
        return sysUserWorkflowHistoryRepo.getActorId(ContextInstanceID, tenantId);
    }

    @Override
    public List<Integer> getDelegatedActorId(long ContextInstanceID, Integer tenantId) {
        return sysUserWorkflowHistoryRepo.getDelegatedActorId(ContextInstanceID, tenantId);
    }
    
    @Override
    public List<EncashmentApprovalHistoryTO> getApprovalEncashmentHistory(Long encashmentId, int tenantId, int organizationID) throws Exception {
        EncashmentApprovalHistoryTO EncashmentApprovalHistoryTO = null;
        List<EncashmentApprovalHistoryTO> approvalHistory = null;
        List<TmEmployeeEncashmentHistory> encashmentApprovalHistoryList = encashmentHistoryRepository.findByEncashmentIdAndTenantId(encashmentId,
                tenantId);
        approvalHistory = new ArrayList<EncashmentApprovalHistoryTO>();
        for (TmEmployeeEncashmentHistory history : encashmentApprovalHistoryList) {
            EncashmentApprovalHistoryTO = new EncashmentApprovalHistoryTO();
            EncashmentApprovalHistoryTO.setStage(history.getPreviousStageName());
            if(history.getUserId() == 1){
                EncashmentApprovalHistoryTO.setActorName(LeaveConstant.WORKFLOW_STAGE_STATUS_AUTO_APPROVED);
            } else {
                EncashmentApprovalHistoryTO.setActorName(getActorName(history.getUserId(), organizationID));
            }
            EncashmentApprovalHistoryTO.setComment(history.getComments());
            EncashmentApprovalHistoryTO.setCreationDate(history.getCreatedDate());
            if(history.getCreatedBy() == 1){
                EncashmentApprovalHistoryTO.setActionTakenBy(LeaveConstant.WORKFLOW_STAGE_STATUS_AUTO_APPROVED);
            } else {
                EncashmentApprovalHistoryTO.setActionTakenBy(getActorName(history.getCreatedBy(), organizationID));
            }
            EncashmentApprovalHistoryTO.setActionDate(history.getModifiedDate());
            EncashmentApprovalHistoryTO.setActionName(history.getActionName());
            EncashmentApprovalHistoryTO.setStatus(history.getStatus());
            if (StringUtil.nonEmptyCheck(history.getFilePath())) {
                EncashmentApprovalHistoryTO.setFilepath(FileUtil.getEncryptedFilePath(history.getFilePath()));
            }
            EncashmentApprovalHistoryTO.setUserId(history.getUserId());

            try {
                if (null != history.getFileName()) {
                    String arr[] = history.getFileName().split("./");
                    if (arr.length > 0) {
                        EncashmentApprovalHistoryTO.setFilename(arr[arr.length - 1]);
                    }
                }

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            approvalHistory.add(EncashmentApprovalHistoryTO);
        }
        return approvalHistory;
    }


    public String getActorName(Integer userId, Integer organizationId)
    {
        HrPerson hrPerson = null;
        String actorName = null;
        String firstName = null;
        String lastName = null;
        String middleName = null;
        try {
            HrEmployee hrEmployee = hrEmployeeRepository.findByUserIdAndOrganizationId(userId, organizationId);
            HrEmployeeProfile managerHrEmployeeProfile = hrEmployeeProfileRepository.findByEmployeeID(hrEmployee.getEmployeeId());
            HrProfile managerHrProfile = hrProfileRepository.findByProfileID(managerHrEmployeeProfile.getProfileID());
            hrPerson = hrPersonRepository.findByPersonId(managerHrProfile.getPersonID());

            if (null != hrPerson) {
                firstName = hrPerson.getGivenName();
                lastName = hrPerson.getFamilyName();
                middleName = hrPerson.getMiddleName();

                actorName = firstName;
                if (StringUtil.nonEmptyCheck(middleName)) {
                    actorName = firstName + StringUtil.SPACE + middleName;
                }
                if (StringUtil.nonEmptyCheck(lastName)) {
                    actorName = actorName + StringUtil.SPACE + lastName;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to fetch employee details");
        }
        return actorName;
    }

    @Override
    public List<Object[]> getUserIdFromTmemployeeEncashment(Long encashmentId) {
        List<Object[]> userId = null;
        try {
            userId = employeeEncashmentRepository.findUserIdByEncashmentId(encashmentId);
        }
        catch(DataAccessException e)
        {
            LOGGER.log(Level.INFO, "Exception occured while fetching userid for encashmentId ==> "+ encashmentId + " cause of error "+ e.getMessage(), e);
        }
        return userId;
    }

    @Override
    public NewEncashmentRequestInfo getNewEncashmentRequestInfo(int organizationId, int employeeId, String bundleName) {
        List<LeaveQuotaTO> leaveQuotaTOListBalance = null;
        NewEncashmentRequestInfo newEncashmentRequestInfo = null;
        List<Object[]> calendarGroupDetails = null;
        Map<Long,Map<String,Object>> calendarGroupMap = new HashMap<>();
        Set<Integer> leaveTypeIds = null;
        List<Object[]> leaveType = null;
        List<Object[]> relationData = null;
        ObjectMapper objectMapper = new ObjectMapper();
        List<HrEmployeeDependentDetail> dependentDetails=new ArrayList<>();

        try {
            newEncashmentRequestInfo = new NewEncashmentRequestInfo();
            
            Long altGroupId = groupServiceImpl.getGroupIdByPolicyCode(employeeId, EncashmentConstant.ENCASHMENT_GROUP_POLICY);
            
            if (altGroupId != null && !altGroupId.equals(0L)){
                calendarGroupDetails = calenderGroupRepository.findActiveCalendarGroupDetailsForOrganizationByAltGroup(organizationId, true, altGroupId);
                if (calendarGroupDetails != null && !calendarGroupDetails.isEmpty()){
                    for (Object[] calendarGroupDetail : calendarGroupDetails){
                        Map<String,Object> calendarGroupDetailMap = new HashMap<>();
                        calendarGroupDetailMap.put("leaveTypeId",((Integer) calendarGroupDetail[1]));
                        calendarGroupDetailMap.put("calendarBlockDetailId",((Long) calendarGroupDetail[2]));
                        calendarGroupMap.put(((Long) calendarGroupDetail[0]),calendarGroupDetailMap);
                    }
                }
            }
            
            if (!calendarGroupMap.isEmpty()){
                leaveTypeIds = calendarGroupMap.values().stream()
                        .flatMap(map-> Stream.of(((Integer) map.get("leaveTypeId"))))
                        .collect(Collectors.toSet());
            }
            
            if (leaveTypeIds != null && !leaveTypeIds.isEmpty())
                leaveType = leaveTypeRepository.findLeaveTypeIdByOrganizationIdAndStatus(organizationId,  true, true, leaveTypeIds);
            
            if(leaveType != null && !leaveType.isEmpty()){
                List<LeaveTOListTO> leaveTOList = new ArrayList<>();
                leaveType.forEach(objects -> {
                    LeaveTOListTO leaveTypeTO = new LeaveTOListTO();
                    leaveTypeTO.setLeaveTypeId(((Integer) objects[0]));
                    leaveTypeTO.setLeaveTypeCode(((String) objects[1]));
                    leaveTypeTO.setLeaveTypeDescription(((String) objects[2]));
                    leaveTypeTO.setOrganizationId(organizationId);
                    leaveTypeTO.setActive(((Boolean)objects[3]));
                    leaveTypeTO.setHourly(((Boolean)objects[4]));
                    leaveTypeTO.setLeaveMessage(((String)objects[5]));
                    Set<Long> calendarGroupIds = calendarGroupMap.keySet()
                            .stream()
                            .filter(groupId-> calendarGroupMap.get(groupId).get("leaveTypeId").equals(objects[0]))
                            .collect(Collectors.toSet());
                    leaveTypeTO.setCalendarGroupIds(calendarGroupIds);
                    leaveTOList.add(leaveTypeTO);
                });
                newEncashmentRequestInfo.setLeaveTOList(leaveTOList);
            }

            //fetching encashmentType
            List<Object[]> encashmentTypes = new ArrayList<>();
            Map<Integer,Map<String,Object>> encashmentData = new HashMap<>();
            if (calendarGroupMap != null && !calendarGroupMap.isEmpty())
                encashmentTypes.addAll(tmEncashmentTypeRepository.findEncashmentTypeByOrganization(organizationId, true, calendarGroupMap.keySet()));
            if (encashmentTypes != null && !encashmentTypes.isEmpty())
                encashmentData.putAll(sysContentServiceImpl.getContentMapForEncashment(EncashmentConstant.ENCASHMENT_CONTENT_CATEGORY,organizationId));
            
            if (encashmentData != null && !encashmentData.isEmpty()){
                List<EncashmentTOListTO> encashmentTOList = new ArrayList<>();
                encashmentData.keySet().forEach(hrContentTypeId->{
                    EncashmentTOListTO encashmentTypeTO = new EncashmentTOListTO();
                    Map<String, Object> data = encashmentData.get(hrContentTypeId);
                    if (data != null && !data.isEmpty()){
                        encashmentTypeTO.setEncashmentTypeCode(((String) data.get("hrContentType")));
                        encashmentTypeTO.setSysEncashmentType(((String) data.get("sysContentType")));
                        encashmentTypeTO.setSysEncashmentTypeId(((Integer) data.get("sysContentTypeId")));
                        encashmentTypeTO.setEncashmentTypeContentId(hrContentTypeId);
                    }
                    if (encashmentTypes != null && !encashmentTypes.isEmpty()){
                        Map<Long,Long> encashmentTypeIdMap = encashmentTypes.stream()
                                .filter(encashmentType->hrContentTypeId.equals(encashmentType[1]))
                                .collect(Collectors.toMap(encashmentType-> ((Long) encashmentType[2]), encashmentType->((Long) encashmentType[0])));
                        encashmentTypeTO.setEncashmentTypeIdMap(encashmentTypeIdMap);
                    }
                    encashmentTOList.add(encashmentTypeTO);
                });
                newEncashmentRequestInfo.setEncashmentTOList(encashmentTOList);
            }

            //fetching age based dependent data code
            Map<Integer, String> relationMap = new HashMap<>();
            relationData = hrContentTypeRepository.getHrContentTypeAndSysType(CONTENT_CATEGORY, organizationId);
            if (relationData != null && !relationData.isEmpty()) {
                relationData.forEach(relation -> {
                    if (relation[0] != null && relation[1] != null) {
                        relationMap.put(((Integer) relation[0]), ((String) relation[1]));
                    }
                });
            }
            if(relationMap!=null && !relationMap.isEmpty()) {
                 dependentDetails = hrEmployeeDependentDetailRepository.getAllDependentsDetails(employeeId, relationMap.keySet());
            }
            List<DependentDetailTO> dependentDetailTOList = new ArrayList<>();

            List<TmDependentBasedEncashmentDetail> tmDependentBasedEncashmentDetails = tmDependentBasedEncashmentDetailRepository.findAllByOrganizationId(organizationId);
            Map<Integer, Integer> percentageMap = new HashMap<>();
            if (tmDependentBasedEncashmentDetails != null && !tmDependentBasedEncashmentDetails.isEmpty()) {
                tmDependentBasedEncashmentDetails.forEach(tmDependentBasedEncashmentDetail -> IntStream.range(tmDependentBasedEncashmentDetail.getStartAge(), tmDependentBasedEncashmentDetail.getEndAge())
                        .boxed()
                        .forEach(detail -> percentageMap.put(detail, tmDependentBasedEncashmentDetail.getPercentageEncashment())));
            }
            if (dependentDetails != null && !dependentDetails.isEmpty()) {
                dependentDetails.forEach(hrEmployeeDependentDetail -> {
                    DependentDetailTO dependentDetailTO = new DependentDetailTO();
                    if(hrEmployeeDependentDetail.getDateOfBirth() != null) {
                        LocalDate dateOfBirth = timeDimensionService.getDateFromTimeDimensionId(hrEmployeeDependentDetail.getDateOfBirth());
                        LocalDate currentDate = LocalDate.now();
                        int age = Period.between(dateOfBirth, currentDate).getYears();
                        dependentDetailTO.setAge(age);
                        if (percentageMap.containsKey(age))
                            dependentDetailTO.setPercentage(percentageMap.get(age));
                    }
                    if(hrEmployeeDependentDetail.getDependentId() != null)
                        dependentDetailTO.setDependentId(hrEmployeeDependentDetail.getDependentId());
                    if(hrEmployeeDependentDetail.getName() != null)
                        dependentDetailTO.setName(hrEmployeeDependentDetail.getName());
                    if(hrEmployeeDependentDetail.getRelationshipTypeId() != null && relationMap.containsKey(hrEmployeeDependentDetail.getRelationshipTypeId()))
                        dependentDetailTO.setRelation(relationMap.get(hrEmployeeDependentDetail.getRelationshipTypeId()));
                    if(hrEmployeeDependentDetail.getRelationshipTypeId() != null)
                        dependentDetailTO.setRelationshipTypeId(hrEmployeeDependentDetail.getRelationshipTypeId());
                    dependentDetailTOList.add(dependentDetailTO);
                });
            }
            newEncashmentRequestInfo.setDependentDetailTOList(dependentDetailTOList);

            //fetching calendarBlock data code
            Set<Long> calendarBlockDetailIds = null;
            if (!calendarGroupMap.isEmpty())
                calendarBlockDetailIds = calendarGroupMap.values().stream()
                        .flatMap(values->Stream.of(((Long) values.get("calendarBlockDetailId"))))
                        .collect(Collectors.toSet());
            List<Object[]> calenderBlockDetails = null;
            if (calendarBlockDetailIds != null && !calendarBlockDetailIds.isEmpty()){
                List<Long> calendarBlockIds = tmCalenderBlockRepository.getActiveCalendarBlocksByOrganization(organizationId, true, DateUtil.getTimeDimensionId(new Date()));
                if (calendarBlockIds != null && !calendarBlockIds.isEmpty())
                    calenderBlockDetails = tmCalenderBlockDetailRepository.findCalenderBlockByOrganizationIdAndBlockId(organizationId, calendarBlockDetailIds, calendarBlockIds);
            }
            
            if (calenderBlockDetails != null && !calenderBlockDetails.isEmpty()){
                Map<Long,Object[]> calendarBlockDetailMap = new HashMap<>();
                List<CalenderBlockTOListTO> calendarBlockToList = new ArrayList<>();
                calenderBlockDetails.forEach(calenderBlockDetail->{
                    calendarBlockDetailMap.put(((Long) calenderBlockDetail[0]),calenderBlockDetail);
                });
                if (calendarGroupMap != null && !calendarGroupMap.isEmpty() && calendarBlockDetailMap != null && !calendarBlockDetailMap.isEmpty()){
                    calendarGroupMap.keySet().forEach(calendarGroupId->{
                        CalenderBlockTOListTO calenderBlockTO = new CalenderBlockTOListTO();
                        calenderBlockTO.setCalendarGroupId(calendarGroupId);
                        calenderBlockTO.setCalendarBlockId(((Long) calendarGroupMap.get(calendarGroupId).get("calendarBlockDetailId")));
                        calenderBlockTO.setCalendarBlockCode(((String) calendarBlockDetailMap.get(((Long) calendarGroupMap.get(calendarGroupId).get("calendarBlockDetailId")))[1]));
                        calendarBlockToList.add(calenderBlockTO);
                    });
                }
                newEncashmentRequestInfo.setCalenderBlockTOList(calendarBlockToList);
            }
            
            //fetching leaveQuotaList data
            Integer hourlyWeightage = leaveDurationTypeRepository.getHourlyWightageByOrganization(organizationId,true, "Full Day");
            leaveQuotaTOListBalance = leaveQuotaService.getEmployeeLeaveQuotaBalance(employeeId,organizationId, bundleName);
            if (leaveQuotaTOListBalance != null && !leaveQuotaTOListBalance.isEmpty()) {
                List<Integer> leaveTypeIdsForQuota = leaveQuotaTOListBalance.stream()
                        .map(LeaveQuotaTO::getLeaveTypeId)
                        .collect(Collectors.toList());
                List<TmEmployeeLeaveQuota> encashmentQuotaTOList = leaveQuotaRepository.getLeavetypeIdhavingBalanceLeftforExhausedLeavetypesWithoutCalender(employeeId, organizationId, true, leaveTypeIdsForQuota);
                Map<Integer, Object> encashmentBalanceMap = new HashMap<>();
                for(TmEmployeeLeaveQuota tmEmployeeLeaveQuota : encashmentQuotaTOList){
                    Map<String, Object> tempMap = new HashMap<>();
                    if(tmEmployeeLeaveQuota.getEncachmentBalanceInDays() == null){
                        tmEmployeeLeaveQuota.setEncachmentBalanceInDays(0);
                    }if(tmEmployeeLeaveQuota.getEncachmentBalanceInHrs() == null){
                        tmEmployeeLeaveQuota.setEncachmentBalanceInHrs(0);
                    }
                    tempMap.put("encashmentBalanceInDays", tmEmployeeLeaveQuota.getEncachmentBalanceInDays());
                    tempMap.put("encashmentBalanceInHrs", tmEmployeeLeaveQuota.getEncachmentBalanceInHrs());
                    Float leaveEncashment =  LeaveUtil.quotaCalculation(tmEmployeeLeaveQuota.getEncachmentBalanceInDays(),tmEmployeeLeaveQuota.getEncachmentBalanceInHrs(),hourlyWeightage);
                    tempMap.put("leaveEncashment",leaveEncashment);
                    encashmentBalanceMap.put(tmEmployeeLeaveQuota.getLeaveTypeId(), tempMap);
                }
                List<EncashmentLeaveQuotaTO> encashmentLeaveQuotaTOList = new ArrayList<>();
                leaveQuotaTOListBalance.forEach(leaveQuotaTO -> {
                    EncashmentLeaveQuotaTO encashmentLeaveQuotaTO = objectMapper.convertValue(leaveQuotaTO, EncashmentLeaveQuotaTO.class);
                    encashmentLeaveQuotaTO.setEncashmentBalanceInDays(((Map<String, Integer>) encashmentBalanceMap.get(leaveQuotaTO.getLeaveTypeId())).get("encashmentBalanceInDays"));
                    encashmentLeaveQuotaTO.setEncashmentBalenceInHrs(((Map<String, Integer>) encashmentBalanceMap.get(leaveQuotaTO.getLeaveTypeId())).get("encashmentBalanceInHrs"));
                    encashmentLeaveQuotaTO.setLeaveEncashment(((Map<String, Float>) encashmentBalanceMap.get(leaveQuotaTO.getLeaveTypeId())).get("leaveEncashment"));
                    encashmentLeaveQuotaTOList.add(encashmentLeaveQuotaTO);
                });
                newEncashmentRequestInfo.setLeaveQuotaTOList(encashmentLeaveQuotaTOList);
            }
        }catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            throw new AppRuntimeException(e, e.getMessage());
        }
        return newEncashmentRequestInfo;
    }
    
    @Override
    public List<TimeOffWorkFlowModel> getStageActionList(TimeOffWorkFlowModel request, UserInfo userInfo) {
        List<TimeOffWorkFlowModel> actionJsonList = null;
        TimeOffWorkFlowModel actionJson = null;
        String formName = null;
        Long hrFormId = null;
        try {
            StageRequestTO stageRequestTO = new StageRequestTO();
            stageRequestTO.setStageId(null);
            if (request.getStageId() > 0) {
                stageRequestTO.setStageId(request.getStageId());
            } else {
                formName = request.getStageType();
                try {
                    hrFormId = IformSecurityService.getHrFormIdFromForm(formName, userInfo.getOrganizationId());
                } catch (Exception e) {
                    return actionJsonList;
                }
            }
            stageRequestTO.setHrFormId(hrFormId);
            ProtocolTO protocolTO = new ProtocolTO();
            protocolTO.setTenantID(userInfo.getTenantId());
            protocolTO.setOrganizationID(userInfo.getOrganizationId());
            protocolTO.setEntityID(request.getEntityID() == 0 ? null : request.getEntityID());
            stageRequestTO.setProtocolTO(protocolTO);
            StageResponseTO response = encashmentTransactionService.getCurrentStageInfo(stageRequestTO);
            if (response.getActions() != null && !response.getActions().isEmpty()) {
                actionJsonList = new ArrayList<TimeOffWorkFlowModel>();
                for (ActionResponseTO action : response.getActions()) {
                    actionJson = new TimeOffWorkFlowModel();
                    actionJson.setActionType(action.getActionType());
                    actionJson.setActionName(action.getFormFieldName());
                    actionJson.setStageActionID(action.getActionId());
                    actionJson.setRuleMethod(action.getRuleMethod());
                    actionJson.setStageType(action.getStageType());
                    actionJsonList.add(actionJson);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
        return actionJsonList;
    }
    
    @Override
    public Object  doEncashmentTransaction(EncashmentRequestRestTO request, UserInfo userInfo)
            throws Exception
    {
        String validateStr = null;
        String errorMessage = null;
        String serverName = env.getProperty(AppConstant.HOSTNAME);
        ResourceBundle resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(userInfo.getBundleName()));

        try {

            formatLeaveRequestFieldsForCrossTimeZone(request);

            if (request.getAction() == null) {
                errorMessage = resourceBundle.getString(IEncashmentErrorConstants.NO_STAGE_ACTION_EXCEPTION);
                throw new AppRuntimeException(errorMessage);
            }
            List<Integer> orgIdsForSecondVersionTimeZoneValidation = Arrays.asList(885,229,220,734,817,1089,1194);

            // ************************  Data Get Part - START *****************************//
            List<Integer> encashmentIdsList = new ArrayList<Integer>();
            encashmentIdsList.add(request.getLeaveTypeId());
            List<TmLeaveType> tmLeaveTypeTO = tmEncashmentTypeRepository.getleaveTypeListForUser(userInfo.getOrganizationId(), true, encashmentIdsList);
            if (tmLeaveTypeTO == null || tmLeaveTypeTO.isEmpty()) {
                errorMessage = resourceBundle.getString(IEncashmentErrorConstants.LEAVE_TYPE_NOT_CORRECT);
                throw new AppRuntimeException(errorMessage);
            }
            TmLeaveEntitlement tmLeaveEntitlement = LeaveUtil.getEntitlementForLeaveTypeId(request.getUserId(), userInfo.getOrganizationId(), request.getLeaveTypeId(),
                    groupServiceImpl, leaveEntitlementRepository, userInfo.getBundleName());


            boolean isMultiCalenderEnabled = leaveFuntionalityEnableService
                    .isMultiCalenderEnabled(userInfo.getOrganizationId());

            CalendarTO defaultCalendarTO = calendarService.getDefaultActiveCalendar(userInfo.getOrganizationId(), userInfo.getBundleName());
            CalendarTO leaveCalendarTO = null;
            if(isMultiCalenderEnabled) {
                leaveCalendarTO = calendarService.getLeaveCalendar(userInfo.getOrganizationId(),
                        tmLeaveEntitlement.getCalendarId(), userInfo.getBundleName());
            }else {
                leaveCalendarTO  = defaultCalendarTO;
            }

            Map<Integer, String> sysContentTypeMap = sysContentServiceImpl.getSysContentType("SystemLeaveType", userInfo.getOrganizationId());
            Map<Integer, String> entitlementPeriodMap = sysContentServiceImpl.getHrContentType("EntittlementPeriodType", userInfo.getOrganizationId());
            Map<String, HrContentType> attendanceStatusMap = sysContentServiceImpl.getHrContentTypesBySysContentType("Employee Attendance", 1);
            List<String> params = new ArrayList<String>();
            params.add(ModuleConfigConstant.IS_THAI_RULES_APPLICABLE);
            Integer employeeEntityId = sysUserRepository.getEntityIDByUserID(userInfo.getUserId(), userInfo.getOrganizationId());
            Map<String, EncModuleConfigurationTO> encModuleConfigMap = encModuleConfigurationService
                    .getModuleConfigMapForEntity(userInfo.getOrganizationId(), params, employeeEntityId);
            int rinstateStatusId = 0;
            if (attendanceStatusMap != null && !attendanceStatusMap.isEmpty() && attendanceStatusMap.containsKey("Employee Attendance Reinstate") && attendanceStatusMap.get("Employee Attendance Reinstate") != null) {
                rinstateStatusId = attendanceStatusMap.get("Employee Attendance Reinstate").getTypeId();
            }
            Map<Integer, String> sysDurationTypeMap = leaveDurationService.getDurationTypeMap(userInfo.getOrganizationId(), userInfo.getBundleName(), employeeEntityId);
            Map<String, KeyValueTO> tmLeaveDurationMap = leaveDurationService.getDurationSysTypeMap(userInfo.getOrganizationId(), userInfo.getBundleName(), employeeEntityId);
            // ************************  Data Get Part - END *****************************//
            Integer confirmationDateId = hrEmployeeRepository.getConfirmationDateId(request.getEmployeeId());
            request.setConfirmationDateId(confirmationDateId);
            // ************************  Convert JSON to POJO - START *****************************//
            EncashmentRequestTO encashmentRequestTO = EncashmentUtil.processEncashmentRequestTO(request, userInfo, tmLeaveTypeTO.get(0),
                    tmLeaveEntitlement, defaultCalendarTO, leaveCalendarTO,
                    sysContentTypeMap);

            // ************************  Set Thai Rule Enable *****************************//
            processModuleConfig(encashmentRequestTO,encModuleConfigMap);

            //not needed     processDateFields(encashmentRequestTO, request);
            storeEssentials(encashmentRequestTO, isMultiCalenderEnabled);
            // ************************  Convert JSON to POJO - END *****************************//


            // ************************ Validation - START *****************************//
            validateStr = encashmentValidationService.validationOnEncashmentSubmit(encashmentRequestTO, sysDurationTypeMap, entitlementPeriodMap, tmLeaveDurationMap, resourceBundle,
                    rinstateStatusId);
//            // ************************ Validation - END *****************************//
//

            int calendarEndDate = DateUtil.getTimeDimensionId(encashmentRequestTO.getLeaveCalendarEndDate());

            // ************************ Processing - START *****************************//
            if (!Utils.isNullorEmpty(validateStr)) {
                throw new AppRuntimeException(validateStr);
            } else {
                if (request.requestType != null && request.requestType.equalsIgnoreCase("Encashment")) {
                    return encashmentTransactionService.doEncashmentTransactionNew(encashmentRequestTO, serverName);
                }
                else if (request.requestType != null && request.requestType.equalsIgnoreCase("ProxyEncashment")) {
                    return encashmentTransactionService.doProxyLeaveTransactionNew(encashmentRequestTO, serverName);
                }
            else {
                    errorMessage = resourceBundle.getString(ILeaveErrorConstants.REQUEST_TYPE_NOT_DEFINED);
                    throw new AppRuntimeException(errorMessage);
                }
            }
            // ************************ Processing - END *****************************//
//
        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            errorMessage = resourceBundle.getString(IEncashmentErrorConstants.FAILED_GENERIC_FAILURE);
            throw new AppRuntimeException(e, errorMessage);
        }
    }

    private LocalDate parseStringDateToLocalDate(String date)
    {
        int length = date.length();
        if (length == 8) {
            date = date.replaceAll("-", "-0");
        }
        if (length == 9) {
            if (date.charAt(6) == '-') {
                date = date.substring(0, 5) + '0' + date.substring(5);
            } else {
                String lastChar = date.substring(length - 1);
                date = date.substring(0, length - 1) + '0' + lastChar;
            }
        }

        return LocalDate.parse(date, formatter);
    }

    private void formatLeaveRequestFieldsForCrossTimeZone(EncashmentRequestRestTO request)
            throws ParseException
    {
        if (!StringUtil.isNullOrEmpty(request.getStartDateString())) {
            request.setStartDate(sdf.parse(request.getStartDateString()));
        }
        if (!StringUtil.isNullOrEmpty(request.getEndDateString())) {
            request.setEndDate(sdf.parse(request.getEndDateString()));
        }
        if (!StringUtil.isNullOrEmpty(request.getEdodDateString())) {
            request.setEdodDate(sdf.parse(request.getEdodDateString()));
        }
        if (!StringUtil.isNullOrEmpty(request.getDateOfMarriageString())) {
            request.setDateOfMarriage(sdf.parse(request.getDateOfMarriageString()));
        }
        if (!StringUtil.isNullOrEmpty(request.getCompOffDateString())) {
            request.setCompOffDate(sdf.parse(request.getCompOffDateString()));
        }
        if (!StringUtil.isNullOrEmpty(request.getDateOfDeathString())) {
            request.setDateOfDeath(sdf.parse(request.getDateOfDeathString()));
        }

        if (!StringUtil.isNullOrEmpty(request.getDateOfIntentString())) {
            request.setDateOfIntent(parseStringDateToLocalDate(request.getDateOfIntentString()));
        }
        if (!StringUtil.isNullOrEmpty(request.getDateOfAdoptionString())) {
            request.setDateOfAdoption(parseStringDateToLocalDate(request.getDateOfAdoptionString()));
        }
        if (!StringUtil.isNullOrEmpty(request.getAdoptedDateOfBirthString())) {
            request.setAdoptedDateOfBirth(parseStringDateToLocalDate(request.getAdoptedDateOfBirthString()));
        }
    }
    
    private void processModuleConfig(EncashmentRequestTO encashmentRequestTO,
                                     Map<String, EncModuleConfigurationTO> moduleConfigMap) {
        if (moduleConfigMap == null) {
            encashmentRequestTO.setLapseLeaveRuleAllowed(false);
        } else {
            String value=moduleConfigMap.get(ModuleConfigConstant.IS_THAI_RULES_APPLICABLE).getValue();
            if(value!=null && value.equalsIgnoreCase("True") && encashmentRequestTO.getLeaveCategoryType().equalsIgnoreCase("Periodic_Leave") && !encashmentRequestTO.getEncashmentRequestConfigTO().getEncashmentTypeTO().getIsTenureLeaveEligibility()) {
                encashmentRequestTO.setLapseLeaveRuleAllowed(true);
            }
        }
    }
    
    private void storeEssentials(EncashmentRequestTO encashmentRequestTO, boolean isMultiCalenderEnabled) {

        if (encashmentRequestTO.isProxyLeave()) {
            Map<Long, Set<Integer>> managerCountryWiseRoleMap = commonService
                    .getCountryWiseRoleList(encashmentRequestTO.getUserInfo().getUserId(), encashmentRequestTO.getTenantId());
            encashmentRequestTO.setManagerCountryWiseRoleMap(managerCountryWiseRoleMap);
        }

        HrEmployee hrEmployee = hrEmployeeRepository.findByEmployeeId(encashmentRequestTO.getEmployeeId());
        HrProfile hrProfile = hrProfileRepository.findByProfileID(hrEmployee.getProfileId());
        HrPerson hrPerson = hrPersonRepository.findByPersonId(hrProfile.getPersonID());
        encashmentRequestTO.setHrEmployee(hrEmployee);
        encashmentRequestTO.setHrEmployeeProfile(hrProfile);
        encashmentRequestTO.setHrEmployeePerson(hrPerson);
        encashmentRequestTO.setMultiCalenderEnabled(isMultiCalenderEnabled);

    }
    
    @Override
    public Object doEncashmentTaskAction(EncashmentTaskActionRequestTO request, UserInfo userInfo) throws Exception {
        String serverName = env.getProperty(AppConstant.HOSTNAME);
        ResourceBundle resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(userInfo.getBundleName()));
        try{
            EncashmentRequestTO encashmentRequest = null;
            List<EncashmentRequestTO> requestTOList = null;
            String errorMessage = null;
            if (request != null){
                Boolean isMultiCalenderEnabled = leaveFuntionalityEnableService.isMultiCalenderEnabled(userInfo.getOrganizationId());
                // ************************  Single Task Action - START *****************************//
                if (request.getEncashmentTaskActionTO() != null && (PSCollectionUtil.isNullOrEmpty(request.getBulkTaskList()))) {
                    Optional<TmEmployeeEncashment> tmEmployeeEncashmentOptional = employeeEncashmentRepository.findById(request.getEncashmentTaskActionTO().getEmployeeEncashmentId());
                    TmEmployeeEncashment tmEmployeeEncashment;
                    if (tmEmployeeEncashmentOptional.isPresent())
                        tmEmployeeEncashment = tmEmployeeEncashmentOptional.get();
                    else throw new AppRuntimeException(IEncashmentErrorConstants.NO_ENCASHMENT_FOUND);
                    TmLeaveType tmLeaveType = leaveTypeRepo.findByLeaveTypeId(tmEmployeeEncashment.getLeaveTypeID());
                    Map<Integer,Map<String,Object>> contentTypeMap = sysContentServiceImpl.getContentMapForEncashment(EncashmentConstant.ENCASHMENT_CONTENT_CATEGORY,userInfo.getOrganizationId());
                    List<Object[]> encashmentDetails = encashmentDetailRepository.getCalendarBlockAndEncashmentTypeIdByEncashmentId(tmEmployeeEncashment.getEncashmentID(),userInfo.getOrganizationId());
                    CalendarTO defaultCalendarTO = calendarService.getDefaultActiveCalendar(userInfo.getOrganizationId(), userInfo.getBundleName());
                    SysEntity sysEntity = sysEntityRepository.findByName(EncashmentConstant.ENCASHMENT_ENTITY_NAME);
                    Integer entityId = sysEntity.getEntityId();
                    List<CalendarBlocksTO> calendarBlocksTOList = getEncashmentDetails(encashmentDetails,userInfo.getOrganizationId());
                    TmLeaveEntitlement tmLeaveEntitlement = LeaveUtil.getEntitlementForLeaveTypeId(request.getEncashmentTaskActionTO().getUserId(), tmEmployeeEncashment.getOrganizationId(),
                            tmLeaveType.getLeaveTypeId(), groupServiceImpl, leaveEntitlementRepository, userInfo.getBundleName());
                    CalendarTO leaveCalendarTO = null;
                    List<Object[]> dependentDetails = tmEncashmentDependentDetailRepository.findDependentDetailsByEncashmentId(tmEmployeeEncashment.getEncashmentID(),userInfo.getOrganizationId());
                    if (Boolean.TRUE.equals(isMultiCalenderEnabled)) {
                        leaveCalendarTO = calendarService.getLeaveCalendar(userInfo.getOrganizationId(), tmLeaveEntitlement.getCalendarId(), userInfo.getBundleName());
                    } else {
                        leaveCalendarTO = defaultCalendarTO;
                    }
                    encashmentRequest = EncashmentUtil.processEncashmentTaskActionTO(userInfo, request.getEncashmentTaskActionTO(), tmLeaveType, defaultCalendarTO, leaveCalendarTO, tmEmployeeEncashment, contentTypeMap, calendarBlocksTOList, dependentDetails);
                    encashmentRequest.setMultiCalenderEnabled(isMultiCalenderEnabled);
                    storeEssentials(encashmentRequest, isMultiCalenderEnabled);
                    return encashmentTransactionService.doEncashmentTransaction(encashmentRequest, serverName);
                }
                // ************************  Single Task Action - END *****************************//
                // ************************  Bulk Task Action - START *****************************//
                else if (request.getEncashmentTaskActionTO() == null && (PSCollectionUtil.isNotNullOrEmpty(request.getBulkTaskList()))) {
                    if (request.getActionType() == null) {
                        errorMessage = resourceBundle.getString(ILeaveErrorConstants.NO_STAGE_ACTION_EXCEPTION);
                        throw new AppRuntimeException(errorMessage);
                    }
                    
                    Map<Long, TmEmployeeEncashment> encashmentId_tmEmployeeEncashment = new HashMap<Long, TmEmployeeEncashment>();
                    Map<Integer, TmLeaveType> leaveTypeId_tmLeaveType = new HashMap<Integer, TmLeaveType>();
                    List<Integer> leaveTypeIDs = new ArrayList<Integer>();
                    List<Integer> encashmentContentIds = new ArrayList<>();
                    List<Long> employeeEncashmentIds = new ArrayList<Long>();
                    
                    Long formId = IformSecurityService.getHrFormIdFromForm(IEncashmentForm.ENCASHENT_BULK_TASK_FORM_NAME, userInfo.getOrganizationId());
                    StageRequestTO stageRequestTO = new StageRequestTO();
                    stageRequestTO.setHrFormId(formId);
                    Map<Integer, Map<String,Object>> sysContentTypeMap = sysContentServiceImpl.getContentMapForEncashment(EncashmentConstant.ENCASHMENT_CONTENT_CATEGORY, userInfo.getOrganizationId());
                    CalendarTO defaultCalendarTO = calendarService.getDefaultActiveCalendar(userInfo.getOrganizationId(), userInfo.getBundleName());
                    SysEntity sysEntity = sysEntityRepository.findByName(EncashmentConstant.ENCASHMENT_ENTITY_NAME);
                    Integer entityId = sysEntity.getEntityId();
                    for (EncashmentTaskActionTO requestTask : request.getBulkTaskList()) {
                        employeeEncashmentIds.add(requestTask.getEmployeeEncashmentId());
                    }
                    List<TmEmployeeEncashment> tmEmployeeEncashments = employeeEncashmentRepository.findByEncashmentIDIn(employeeEncashmentIds);
                    for (TmEmployeeEncashment tmEmployeeEncashment : tmEmployeeEncashments) {
                        encashmentId_tmEmployeeEncashment.put(tmEmployeeEncashment.getEncashmentID(), tmEmployeeEncashment);
                        leaveTypeIDs.add(tmEmployeeEncashment.getLeaveTypeID());
                        encashmentContentIds.add(tmEmployeeEncashment.getEncashmentTypeContentId());
                    }
                    List<Object[]> tmEmployeeEncashmentDetails = encashmentDetailRepository.getTmEmployeeEncashmentDetails(encashmentId_tmEmployeeEncashment.keySet(),userInfo.getOrganizationId());
                    Map<Long,List<Object[]>> encashmentDetailMap = new HashMap<>();
                    Set<Long> calendarBlockDetailIds = new HashSet<>();
                    if (tmEmployeeEncashmentDetails != null && !tmEmployeeEncashmentDetails.isEmpty()){
                        tmEmployeeEncashmentDetails.forEach(tmEmployeeEncashmentDetail->{
                            List<Object[]> encashmentDetails = new ArrayList<>();
                            if (encashmentDetailMap.containsKey(((Long) tmEmployeeEncashmentDetail[0])))
                                encashmentDetails.addAll(encashmentDetailMap.get(((Long) tmEmployeeEncashmentDetail[0])));
                            encashmentDetails.add(tmEmployeeEncashmentDetail);
                            encashmentDetailMap.put(((Long) tmEmployeeEncashmentDetail[0]),encashmentDetails);
                            calendarBlockDetailIds.add(((Long) tmEmployeeEncashmentDetail[1]));
                        });
                    }
                    Map<Long,List<CalendarBlocksTO>> calendarBlocksTOMap = new HashMap<>();
                    if (calendarBlockDetailIds != null && !calendarBlockDetailIds.isEmpty()){
                        List<Object[]> calendarBlocks = tmCalenderBlockDetailRepository.findCalendarBlockDetailsById(calendarBlockDetailIds, userInfo.getOrganizationId());
                        Map<Long,String> calendarBlockDetailMap = new HashMap<>();
                        if (calendarBlocks != null && !calendarBlocks.isEmpty()){
                            calendarBlocks.forEach(calendarBlock->{
                                calendarBlockDetailMap.put(((Long) calendarBlock[0]),((String) calendarBlock[1]));
                            });
                        }
                        if (calendarBlockDetailMap != null && !calendarBlockDetailMap.isEmpty()){
                            encashmentDetailMap.keySet().forEach(encashmentId->{
                                List<CalendarBlocksTO> calendarBlocksTOS = new ArrayList<>();
                                encashmentDetailMap.get(encashmentId).forEach(encashmentDetails->{
                                    CalendarBlocksTO calendarBlocksTO = new CalendarBlocksTO();
                                    calendarBlocksTO.setEncashmentTypeId(((Long) encashmentDetails[2]));
                                    calendarBlocksTO.setCalendarGroupId(((Long) encashmentDetails[3]));
                                    calendarBlocksTO.setCalendarBlock(calendarBlockDetailMap.get(((Long) encashmentDetails[1])));
                                    calendarBlocksTO.setCalendarBlockId(((Long) encashmentDetails[1]));
                                    calendarBlocksTOS.add(calendarBlocksTO);
                                });
                                calendarBlocksTOMap.put(encashmentId,calendarBlocksTOS);
                            });
                        }
                    }
                    List<Object[]> dependentDetails = tmEncashmentDependentDetailRepository.findDependentDetailsByEncashmentIds(employeeEncashmentIds);
                    Map<Long,List<Object[]>> dependentDetailsMap = new HashMap<>();
                    if (dependentDetails != null && !dependentDetails.isEmpty()){
                        dependentDetails.forEach(dependent->{
                            List<Object[]> dependents = new ArrayList<>();
                            if (dependentDetailsMap.containsKey(((Long) dependent[5])))
                                dependents.addAll(dependentDetailsMap.get(((Long) dependent[5])));
                            dependents.add(dependent);
                            dependentDetailsMap.put(((Long) dependent[5]),dependents);
                        });
                    }
                    List<TmLeaveType> tmLeaveTypes = leaveTypeRepo.getleaveTypeListForUser(userInfo.getOrganizationId(), true, leaveTypeIDs);
                    for (TmLeaveType tmLeaveType : tmLeaveTypes) {
                        leaveTypeId_tmLeaveType.put(tmLeaveType.getLeaveTypeId(), tmLeaveType);
                    }
                    
                    requestTOList = new ArrayList<>();
                    for (EncashmentTaskActionTO requestTask : request.getBulkTaskList()) {
                        try {
                            TmEmployeeEncashment tmEmployeeEncashment = encashmentId_tmEmployeeEncashment.get(requestTask.getEmployeeEncashmentId());
                            TmLeaveType tmLeaveType = leaveTypeId_tmLeaveType.get(tmEmployeeEncashment.getLeaveTypeID());
                            
                            TmLeaveEntitlement tmLeaveEntitlement = LeaveUtil.getEntitlementForLeaveTypeId(requestTask.getUserId(), userInfo.getOrganizationId(),
                                    tmLeaveType.getLeaveTypeId(), groupServiceImpl, leaveEntitlementRepository, userInfo.getBundleName());
                            
                            CalendarTO leaveCalendarTO = null;
                            if (isMultiCalenderEnabled) {
                                leaveCalendarTO = calendarService.getLeaveCalendar(userInfo.getOrganizationId(),
                                        tmLeaveEntitlement.getCalendarId(), userInfo.getBundleName());
                            } else {
                                leaveCalendarTO = defaultCalendarTO;
                            }
                            
                            requestTOList.add(EncashmentUtil.processEncashmentTaskActionTO(userInfo,requestTask,tmLeaveType,defaultCalendarTO,leaveCalendarTO,tmEmployeeEncashment,sysContentTypeMap,calendarBlocksTOMap.get(tmEmployeeEncashment.getEncashmentID()),dependentDetailsMap.get(tmEmployeeEncashment.getEncashmentID())));
                        } catch (Exception e) {
                            LOGGER.log(Level.INFO, e.getMessage(), e);
                        }
                    }
                    
                    WorkflowHistoryResponseTO responseTO = null;
                    
                    Map<String, String> placeHolder_method = communicationService
                            .getPlaceHolderList(
                                    new ArrayList<String>(Arrays.asList(LeaveConstant.TEMPLATE_PLACEHOLDER_CONTENT_TYPE,
                                            LeaveConstant.TEMPLATE_PLACEHOLDER_MAIL_TYPE)),
                                    LeaveConstant.LEAVE_MODULE_NAME);
                    String nextStageApiUrl = null;
                    nextStageApiUrl = AppConstant.SERVER_PROTOCOL + serverName + AppConstant.WORKFLOW_PRE_URL
                            + AppConstant.WORKFLOW_NEXT_STAGE_INFO_URL;
                    int exceptionCount = 0;
                    int totalTaskCount = PSCollectionUtil.size(requestTOList);
                    
                    Set<Long> encashmentIds = new HashSet<Long>();
                    for (EncashmentRequestTO encashmentRequestTO : requestTOList) {
                        encashmentIds.add(encashmentRequestTO.getEmployeeEncashmentId());
                        encashmentRequestTO.setIsMultiCalenderEnabled(isMultiCalenderEnabled);
                    }
                    
                    for (EncashmentRequestTO encashmentRequestTO : requestTOList) {
                        try {
                            responseTO = (WorkflowHistoryResponseTO) encashmentTransactionService.doBulkLeaveTransactionNew(
                                    encashmentRequestTO, serverName, request.getApproverComments(), stageRequestTO,
                                    request.getActionType(), entityId, nextStageApiUrl, placeHolder_method,
                                    resourceBundle);
                        } catch (Exception e) {
                            exceptionCount++;
                        }
                    }
                    if (exceptionCount > 0) {
                        LOGGER.log(Level.INFO, "Error Message For processing Bulk Requests : " + errorMessage);
                        throw new AppRuntimeException(MessageFormat.format(
                                resourceBundle.getString(ILeaveErrorConstants.EXCEPTION_OCCURED_ACTION_SUCCESSFULLYON),
                                (totalTaskCount - exceptionCount), totalTaskCount));
                    }
                    
                    return responseTO;
                }
                // ************************  Bulk Task Action - END *****************************//
            }
        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            throw new AppRuntimeException(e, resourceBundle.getString(ILeaveErrorConstants.FAILED_GENERIC_FAILURE));
        }
        return null;
    }
    
    private List<CalendarBlocksTO> getEncashmentDetails(List<Object[]> encashmentDetails, Integer organizationId) {
        List<CalendarBlocksTO> calendarBlocksTOS = new ArrayList<>();
        if (encashmentDetails != null && !encashmentDetails.isEmpty()){
            Set<Long> calendarBlockIds = encashmentDetails.stream()
                    .map(encashmentDetail -> ((Long) encashmentDetail[0])).collect(Collectors.toSet());
            List<Object[]> calendarBlocks = tmCalenderBlockDetailRepository.findCalendarBlockDetailsById(calendarBlockIds,organizationId);
            Map<Long,String> calendarBlockDetailMap = new HashMap<>();
            if (calendarBlocks != null && !calendarBlocks.isEmpty()){
                calendarBlocks.forEach(calendarBlock->{
                    calendarBlockDetailMap.put(((Long) calendarBlock[0]),((String) calendarBlock[1]));
                });
            }
            if (!calendarBlockDetailMap.isEmpty()){
                encashmentDetails.forEach(encashmentDetail->{
                    CalendarBlocksTO calendarBlocksTO = new CalendarBlocksTO();
                    calendarBlocksTO.setCalendarGroupId(((Long) encashmentDetail[2]));
                    calendarBlocksTO.setCalendarBlockId(((Long) encashmentDetail[0]));
                    calendarBlocksTO.setEncashmentTypeId(((Long) encashmentDetail[1]));
                    calendarBlocksTO.setCalendarBlock(calendarBlockDetailMap.get(((Long) encashmentDetail[0])));
                    calendarBlocksTOS.add(calendarBlocksTO);
                });
            }
        }
        return calendarBlocksTOS;
    }

    @Override
    public List<EncashmentListResponseTO> getAppliedEncashmentList(UserInfo userInfo, AppliedEncashmentListCommonTO restRequest) throws Exception {
        List<EncashmentListResponseTO> encashmentListResponseTOList = new ArrayList<EncashmentListResponseTO>();

        Integer userId=null;
        Integer employeeId=null;
        if(restRequest.getUserId()!=null&&restRequest.getEmployeeId()!=null) {
            userId = restRequest.getUserId();
            employeeId = restRequest.getEmployeeId();
        }
        try {
            /**
             * Fetch Employee encashment data
             */
            if (userId == null || userId == 0) {
                List<Integer> userIds = hrEmployeeRepository.findUserIdByEmployeeIdAndOrganizationId(employeeId, userInfo.getOrganizationId());
                if ((userIds == null || userIds.isEmpty()) && employeeId!= null && employeeId > 0) {
                    userIds = hrEmployeeRepository.findUserIdByEmployeeIdAndOrganizationId(employeeId, userInfo.getOrganizationId());
                }
                userId=userIds.get(0);
            }
            // only fetch leaves on or after 1 Jan, 2022
            Calendar oldestDate = new GregorianCalendar(2022, Calendar.JANUARY, 1);
            int olddestDateID = DateUtil.getTimeDimensionId(oldestDate.getTime());

            List<EncashmentListingTo> encashmentListingtos;
            if (restRequest.getFromDate() == null && restRequest.getToDate() == null)
                encashmentListingtos = employeeEncashmentRepository.getEmployeeEncashmentByEmployeeId(employeeId, userInfo.getOrganizationId());
            else {
                Integer startDateID = DateUtil.getTimeDimensionId(restRequest.getFromDate());
                Integer endDateID = DateUtil.getTimeDimensionId(restRequest.getToDate());
                encashmentListingtos = employeeEncashmentRepository.getEmployeeEncashmentByDate(employeeId, userInfo.getOrganizationId(), startDateID, endDateID);
            }


            if (PSCollectionUtil.isNotNullOrEmpty(encashmentListingtos)) {
                Set<Long> encashmentTypeIds = new HashSet<Long>();
                Map<Long, String> encashmentTypeCodeByencashmentTypeId = null;
                Set<Integer> leaveTypeIds = new HashSet<Integer>();
                Map<Integer, String> leaveTypeCodeByLeaveTypeId = null;

                Set<Integer> timeDimentionIds = new HashSet<Integer>();
                Map<Integer, Date> dateBytimeDimentionId = null;

                Set<Integer> stageIds = new HashSet<Integer>();
                Map<Integer, String> stageNameByStageId = null;

                Map<Integer, String> workFlowStageTypeByStageId = new HashMap<Integer, String>();

                for (EncashmentListingTo encashmentListingTo : encashmentListingtos) {
                    if (encashmentListingTo != null) {
                        encashmentTypeIds.add(encashmentListingTo.getEncashmentId());
                        leaveTypeIds.add(encashmentListingTo.getLeaveTypeID());
                        timeDimentionIds.add(encashmentListingTo.getApplicationDate());
                        stageIds.add(encashmentListingTo.getStageId());
                    }

                }

                Map<Integer,Map<String,Object>> sysContentMap = sysContentServiceImpl.getContentMapForEncashment(EncashmentConstant.ENCASHMENT_CONTENT_CATEGORY,userInfo.getOrganizationId());

                Map<Long,List<Object[]>> encashmentDetailMap = new HashMap<>();
                Set<Long> calendarBlockDetailIds = new HashSet<>();
                if (encashmentTypeIds != null && !encashmentTypeIds.isEmpty()){
                    List<Object[]> tmEmployeeEncashmentDetails = encashmentDetailRepository.getTmEmployeeEncashmentDetails(encashmentTypeIds,userInfo.getOrganizationId());
                    if (tmEmployeeEncashmentDetails != null && !tmEmployeeEncashmentDetails.isEmpty()){
                        tmEmployeeEncashmentDetails.forEach(tmEmployeeEncashmentDetail -> {
                            calendarBlockDetailIds.add(((Long) tmEmployeeEncashmentDetail[1]));
                            List<Object[]> encashmentDetailsList = new ArrayList<>();
                            if (encashmentDetailMap.containsKey(((Long) tmEmployeeEncashmentDetail[0]))){
                                encashmentDetailsList.addAll(encashmentDetailMap.get(((Long) tmEmployeeEncashmentDetail[0])));
                            }
                            encashmentDetailsList.add(tmEmployeeEncashmentDetail);
                            encashmentDetailMap.put(((Long) tmEmployeeEncashmentDetail[0]),encashmentDetailsList);
                        });
                    }
                }

                Map<Long,List<CalendarBlocksTO>> calendarBlocksMap = new HashMap<>();
                Map<Long,String> calendarBlockDetailMap = new HashMap<>();
                if (encashmentDetailMap != null && !encashmentDetailMap.isEmpty()){
                    if (calendarBlockDetailIds != null && !calendarBlockDetailIds.isEmpty()){
                        List<Object[]> calendarBlocks = tmCalenderBlockDetailRepository.findCalendarBlockDetailsById(calendarBlockDetailIds,userInfo.getOrganizationId());
                        if (calendarBlocks != null && !calendarBlocks.isEmpty()){
                            calendarBlocks.forEach(calendarBlock->{
                                calendarBlockDetailMap.put(((Long) calendarBlock[0]),((String) calendarBlock[1]));
                            });
                        }
                    }
                    if (calendarBlockDetailMap != null && !calendarBlockDetailMap.isEmpty()){
                        encashmentDetailMap.forEach((encashmentId,encashmentDetails)->{
                            List<CalendarBlocksTO> calendarBlocksTOS = new ArrayList<>();
                            encashmentDetails.forEach(encashmentDetail->{
                                if (calendarBlocksMap.containsKey(((Long) encashmentDetail[1]))){
                                    calendarBlocksTOS.addAll(calendarBlocksMap.get(((Long) encashmentDetail[1])));
                                }
                                CalendarBlocksTO calendarBlocksTO = new CalendarBlocksTO();
                                calendarBlocksTO.setCalendarGroupId(((Long) encashmentDetail[3]));
                                calendarBlocksTO.setCalendarBlockId(((Long) encashmentDetail[1]));
                                calendarBlocksTO.setEncashmentTypeId(((Long) encashmentDetail[2]));
                                calendarBlocksTO.setCalendarBlock(calendarBlockDetailMap.get(((Long) encashmentDetail[1])));
                                calendarBlocksTOS.add(calendarBlocksTO);
                            });
                            calendarBlocksMap.put(encashmentId,calendarBlocksTOS);
                        });
                    }
                }

                /**
                 * Fetch leaveTypeCodeByLeaveTypeId
                 */
                if (PSCollectionUtil.isNotNullOrEmpty(leaveTypeIds)) {
                    List<LeaveTypeTO> leaveCodes = leaveTypeRepo.getLeavecodeByLeaveTypeId(userInfo.getOrganizationId(), leaveTypeIds);
                    if (PSCollectionUtil.isNotNullOrEmpty(leaveCodes)) {
                        leaveTypeCodeByLeaveTypeId = new HashMap<Integer, String>();
                        for (LeaveTypeTO leaveTypeTO : leaveCodes) {
                            leaveTypeCodeByLeaveTypeId.put(leaveTypeTO.getLeaveTypeId(), leaveTypeTO.getLeaveTypeCode());
                        }
                    }

                    /**
                     * Fetch encashmentTypeCodeByEncashmentTypeIds
                     */
                    if (PSCollectionUtil.isNotNullOrEmpty(encashmentTypeIds)) {
                        List<Object[]> encashmentTypes = tmEncashmentTypeRepository.getEncashmentcodeByEncashmentTypeId(userInfo.getOrganizationId(), new ArrayList<>(encashmentTypeIds));
                        List<Object[]> encashmentTypeData = hrContentTypeRepository.getHrContentTypeAndSysType("LeaveEncashmentType", userInfo.getOrganizationId());
                        Map<Integer,String> encashmentTypeMap = new HashMap<>();
                        if(encashmentTypeData != null && !encashmentTypeData.isEmpty()){
                            encashmentTypeData.forEach(objects -> {
                                encashmentTypeMap.put(((Integer) objects[0]), ((String) objects[1]));
                            });
                        }
                        List<EncashmentTypeTO> encashmentCodes = new ArrayList<>();
                        for(Object[] encashmentCode:encashmentTypes){
                            EncashmentTypeTO encashmentTypeTO = new EncashmentTypeTO();
                            encashmentTypeTO.setEncashmentTypeID(((Long) encashmentCode[0]));
                            encashmentTypeTO.setEncashmentTypeContentID(((Integer) encashmentCode[1]));
                            encashmentTypeTO.setEncashmentTypeCode(encashmentTypeMap.get(((Integer) encashmentCode[1])));
                            encashmentCodes.add(encashmentTypeTO);
                        }

                        if (PSCollectionUtil.isNotNullOrEmpty(encashmentCodes)) {
                            encashmentTypeCodeByencashmentTypeId = new HashMap<>();
                            for (EncashmentTypeTO encashmentTypeTO : encashmentCodes) {
                                encashmentTypeCodeByencashmentTypeId.put(encashmentTypeTO.getEncashmentTypeID(), encashmentTypeTO.getEncashmentTypeCode());
                            }
                        }
                    }

                }

                /**
                 * Fetch dateBytimeDimentionId
                 */
                if (PSCollectionUtil.isNotNullOrEmpty(timeDimentionIds)) {
                    List<TimeDimensionTO> timeDimentionDates = timeDimesionRepository.getDateById(timeDimentionIds);
                    if (PSCollectionUtil.isNotNullOrEmpty(timeDimentionDates)) {
                        dateBytimeDimentionId = new HashMap<Integer, Date>();
                        for (TimeDimensionTO timeDimensionTO : timeDimentionDates) {
                            dateBytimeDimentionId.put(timeDimensionTO.getTimeDimensionId(), timeDimensionTO.getTheDate());
                        }
                    }
                }
                Integer formId = IformSecurityService.getSysFormIdFromForm(IEncashmentForm.ENCASHMENT_APPROVER_FORM_NAME, userInfo.getOrganizationId());
                List<SysWorkflowStageType> sysWorkflowStageTypes = sysWorkflowStageTypeRepository.findByFormID(formId);
                Map<Integer, String> workflowStageTypeId_workflowStageType = new HashMap<Integer, String>();
                if (PSCollectionUtil.isNotNullOrEmpty(sysWorkflowStageTypes)) {
                    for (SysWorkflowStageType sysWorkflowStageType : sysWorkflowStageTypes) {
                        workflowStageTypeId_workflowStageType.put(sysWorkflowStageType.getWorkflowStageTypeId(), sysWorkflowStageType.getWorkflowStageType());
                    }

                }

                /**
                 * Fetch stageStatus and workflowStageType
                 */
                if (PSCollectionUtil.isNotNullOrEmpty(stageIds)) {
                    List<SysWorkflowStageTO> sysWorkflowStages = sysWorkflowStageRepository.getStagestatusByStageId(stageIds);
                    if (PSCollectionUtil.isNotNullOrEmpty(sysWorkflowStages)) {
                        stageNameByStageId = new HashMap<Integer, String>();
                        for (SysWorkflowStageTO sysWorkflowStageTO : sysWorkflowStages) {
                            stageNameByStageId.put(sysWorkflowStageTO.getWorkflowStageID(), sysWorkflowStageTO.getStageStatus());
                            workFlowStageTypeByStageId.put(sysWorkflowStageTO.getWorkflowStageID(), workflowStageTypeId_workflowStageType.get(sysWorkflowStageTO.getStageTypeID()));
                        }

                    }
                }

                EncashmentListResponseTO encashmentListResponseTO = null;


                Integer userIdOfEmployee = null;
                for (EncashmentListingTo encashmentListingTo : encashmentListingtos) {
                    encashmentListResponseTO = new EncashmentListResponseTO();
                    encashmentListResponseTO.setEmployeeEncashmentId(encashmentListingTo.getEncashmentId());
                    encashmentListResponseTO.setStageId(encashmentListingTo.getStageId() == null?0:encashmentListingTo.getStageId());
                    if (leaveTypeCodeByLeaveTypeId != null && !leaveTypeCodeByLeaveTypeId.isEmpty()){
                        encashmentListResponseTO.setLeaveTypeId(encashmentListingTo.getLeaveTypeID());
                        encashmentListResponseTO.setLeaveType(leaveTypeCodeByLeaveTypeId.get(encashmentListingTo.getLeaveTypeID()));
                    }
                    
                    Integer hourlyWeightage = encashmentListingTo.getHourlyWeightage() != null ? encashmentListingTo.getHourlyWeightage().intValue() : 0;
                    encashmentListResponseTO.setHourlyWeightage(hourlyWeightage);
                    encashmentListResponseTO.setEncashmentCode(encashmentListingTo.getEncashmentCode());
                    encashmentListResponseTO.setStatusMessage(encashmentListingTo.getStatusMessage());
                    encashmentListResponseTO.setActive(encashmentListingTo.isActive());
                    if (calendarBlocksMap != null && !calendarBlocksMap.isEmpty())
                        encashmentListResponseTO.setCalendarBlocksTO(calendarBlocksMap.get(encashmentListingTo.getEncashmentId()));

                    if (encashmentListingTo.getEncashmentCountInDays() == null)
                        encashmentListingTo.setEncashmentCountInDays(0);
                    if (encashmentListingTo.getEncashmentCountInHrs() == null)
                        encashmentListingTo.setEncashmentCountInHrs((short) 0);
                    encashmentListResponseTO.setEncashmentCount(LeaveUtil.quotaCalculation(encashmentListingTo.getEncashmentCountInDays(), encashmentListingTo.getEncashmentCountInHrs().intValue(), hourlyWeightage));


                    encashmentListResponseTO.setApplicationDate(DateUtil.getDateFormat(DateUtil.DATE_FORMAT1, dateBytimeDimentionId.get(encashmentListingTo.getApplicationDate())));

                    if(encashmentListingTo.getStageId()!=null) {
                        encashmentListResponseTO.setStage(stageNameByStageId.get(encashmentListingTo.getStageId()));
                    }
                    if(encashmentListingTo.getStageId()!=null) {
                        encashmentListResponseTO.setWorkflowStageType(workFlowStageTypeByStageId.get(encashmentListingTo.getStageId()));
                    }
                    if (sysContentMap != null && !sysContentMap.isEmpty()){
                        encashmentListResponseTO.setEncashmentType(((String) sysContentMap.get(encashmentListingTo.getEncashmentTypeID()).get("hrContentType")));
                        encashmentListResponseTO.setEncashmentContentId(encashmentListingTo.getEncashmentTypeID());
                        encashmentListResponseTO.setSysEncashmentType(((String) sysContentMap.get(encashmentListingTo.getEncashmentTypeID()).get("sysContentType")));
                        encashmentListResponseTO.setSysEncashmentTypeId(((Integer) sysContentMap.get(encashmentListingTo.getEncashmentTypeID()).get("sysContentTypeId")));
                    }
                    encashmentListResponseTO.setEmployeeId(employeeId);
                    if (encashmentListingTo.getUserId() != null) {
                        encashmentListResponseTO.setUserId(encashmentListingTo.getUserId());
                    } else if (userIdOfEmployee != null) {
                        encashmentListResponseTO.setUserId(userIdOfEmployee);
                    } else {
                        List<Integer> userIdOfEmployeeList = hrEmployeeRepository.findUserIdByEmployeeIdAndOrganizationId(employeeId, userInfo.getOrganizationId());
                        if (userIdOfEmployeeList != null && !userIdOfEmployeeList.isEmpty()) {
                            userIdOfEmployee = userIdOfEmployeeList.get(0);
                            encashmentListResponseTO.setUserId(userIdOfEmployee);
                        }
                    }
                    encashmentListResponseTO.setWorkflowHistoryID(encashmentListingTo.getWorkFlowHistoryId());
                    encashmentListResponseTO.setWithdraw(false);
                    if (workFlowStageTypeByStageId.get(encashmentListingTo.getStageId())
                            .equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED)) {
                        if (encashmentListingTo.getPreviousStageType() != null && encashmentListingTo.getPreviousStageType()
                                .equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_APPROVAL)) {
                            encashmentListResponseTO.setWithdraw(true);
                        } else if (encashmentListingTo.getPreviousStageType() != null && encashmentListingTo
                                .getPreviousStageType().equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_INITIAL)) {
                            encashmentListResponseTO.setWithdraw(true);
                        } else if (encashmentListingTo.getPreviousStageType() != null
                                && encashmentListingTo.getPreviousStageType()
                                .equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED)
                                && encashmentListResponseTO.isActive()) {
                            encashmentListResponseTO.setWithdraw(true);
                        }
                    }
                    
                    encashmentListResponseTOList.add(encashmentListResponseTO);
                }
            }
        } catch (AppRuntimeException ex) {
            throw ex;
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            throw new AppRuntimeException(e, e.getMessage());
        }
        if (encashmentListResponseTOList != null && !encashmentListResponseTOList.isEmpty()) {
            Collections.sort(encashmentListResponseTOList);
        }
        return encashmentListResponseTOList;

    }

    @Override
    public List<EncashmentTaskDetailRequestTO> getEncashmentTasksDetails(List<EncashmentTaskRequestTO> restRequest, UserInfo userInfo) throws Exception {
        List<EncashmentTaskDetailRequestTO> encashmentTaskDetailRequestList = new ArrayList<>();

        String errorMessage = null;
        ResourceBundle resourceBundle = null;

        try {
            for (EncashmentTaskRequestTO encashmentRequestTo : restRequest) {
                EncashmentTaskDetailRequestTO encashmentTaskDetailRequestTO = getLeaveTaskEncashmentDetail(userInfo, encashmentRequestTo);
                if (encashmentTaskDetailRequestTO != null) {
                    encashmentTaskDetailRequestList.add(encashmentTaskDetailRequestTO);
                }
            }
        } catch (AppRuntimeException e) {
            throw e; // Rethrow the application-specific exception
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            if (resourceBundle != null) {
                errorMessage = resourceBundle.getString(EncashmentErrorConstants.FAILED_TOLOAD_SELECTED_TASKDETAILS);
            } else {
                errorMessage = "Error loading encashment task details.";
            }
            throw new AppRuntimeException(errorMessage);
        }

        return encashmentTaskDetailRequestList;
    }
}
