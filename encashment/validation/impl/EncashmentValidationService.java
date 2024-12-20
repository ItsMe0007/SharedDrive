package com.peoplestrong.timeoff.encashment.validation.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.peoplestrong.timeoff.dataservice.repo.common.HrPersonRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.HrProfileRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmAgeBasedEncashmentDetailRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmEmployeeEncashmentDetailRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmEmployeeEncashmentRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmEncashmentCalenderGroupRepository;
import com.peoplestrong.timeoff.encashment.pojo.CalendarBlocksTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.common.constant.ModuleConfigConstant;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.impl.service.CommonService;
import com.peoplestrong.timeoff.common.transport.ProtocolTO;
import com.peoplestrong.timeoff.common.util.DateUtil;
import com.peoplestrong.timeoff.common.util.PSCollectionUtil;
import com.peoplestrong.timeoff.common.util.RestUtil;
import com.peoplestrong.timeoff.common.util.StringUtil;
import com.peoplestrong.timeoff.common.util.Utils;
import com.peoplestrong.timeoff.dataservice.model.common.HrEmployeeContract;
import com.peoplestrong.timeoff.dataservice.model.common.HrEmploymentType;
import com.peoplestrong.timeoff.dataservice.model.leave.HrEmployeeResignation;
import com.peoplestrong.timeoff.dataservice.model.leave.HrOrgCountryMapping;
import com.peoplestrong.timeoff.dataservice.model.session.HrEmployee;
import com.peoplestrong.timeoff.dataservice.repo.Form.HrFormFieldConfigurationRepository;
import com.peoplestrong.timeoff.dataservice.repo.Form.HrFormFieldRepository;
import com.peoplestrong.timeoff.dataservice.repo.Form.RuleGroupRepository;
import com.peoplestrong.timeoff.dataservice.repo.Form.RulesRepository;
import com.peoplestrong.timeoff.dataservice.repo.Form.UiFormFieldGroupRepository;
import com.peoplestrong.timeoff.dataservice.repo.Form.UiFormFieldRepository;
import com.peoplestrong.timeoff.dataservice.repo.Form.UiFormRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.HrEmployeeContractRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.HrEmploymentTypeRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.SysObjectAttributeRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.SysObjectRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmEncashmentTypeRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.CountryMappingDao;
import com.peoplestrong.timeoff.dataservice.repo.leave.EmployeeLeaveRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.HrContentTypeRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.HrEmployeeResignationRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.LeaveDurationTypeRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.LeaveQuotaRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.TimeDimesionRepository;
import com.peoplestrong.timeoff.dataservice.repo.session.HrEmployeeRepository;
import com.peoplestrong.timeoff.dataservice.repo.session.SysUserRepository;
import com.peoplestrong.timeoff.encashment.constant.EncashmentConstant;
import com.peoplestrong.timeoff.encashment.constant.IEncashmentErrorConstants;
import com.peoplestrong.timeoff.encashment.helper.EncashmentUtil;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentRequestTO;
import com.peoplestrong.timeoff.leave.constant.ILeaveErrorConstants;
import com.peoplestrong.timeoff.leave.constant.ILeaveErrorMessageConstants;
import com.peoplestrong.timeoff.leave.constant.IMessageConstants;
import com.peoplestrong.timeoff.leave.constant.LeaveConstant;
import com.peoplestrong.timeoff.leave.helper.LeaveUtil;
import com.peoplestrong.timeoff.leave.pojo.GroupRequestTO;
import com.peoplestrong.timeoff.leave.pojo.KeyValueTO;
import com.peoplestrong.timeoff.leave.pojo.LeaveGroupConfigTO;
import com.peoplestrong.timeoff.leave.pojo.ModuleConfigurationTO;
import com.peoplestrong.timeoff.leave.pojo.ShiftInputTO;
import com.peoplestrong.timeoff.leave.pojo.ShiftRosterTO;
import com.peoplestrong.timeoff.leave.service.CalendarService;
import com.peoplestrong.timeoff.leave.service.Impl.GroupServiceImpl;
import com.peoplestrong.timeoff.leave.service.LeaveService;
import com.peoplestrong.timeoff.leave.service.ModuleConfigurationService;
import com.peoplestrong.timeoff.leave.service.ShiftService;
import com.peoplestrong.timeoff.workflow.to.FormValidationRequestTO;
import com.peoplestrong.timeoff.workflow.to.FormValidationResponseTO;

import static java.lang.Math.max;


@Service
public class EncashmentValidationService {

    @Autowired
    HrEmploymentTypeRepository employmentTypeRepo;

    @Autowired
    SysObjectRepository sysObjectRepository;

    @Autowired
    SysObjectAttributeRepository sysObjectAttributeRepository;

    @Autowired
    ShiftService shiftService;

    @Autowired
    Environment env;
    @Autowired
    HrEmployeeContractRepository contractRepo;

    @Autowired
    TmEncashmentTypeRepository tmEncashmentTypeRepository;

    @Autowired
    RuleGroupRepository ruleGroupRepository;

    @Autowired
    RulesRepository rulesRepository;

    @Autowired
    HrEmployeeRepository hrEmployeeRepo;

    @Autowired
    HrContentTypeRepository hrContentTypeRepository;
    @Autowired
    TimeDimesionRepository timeDimesionRepository;
    @Autowired
    private CommonService commonService;

    @Autowired
    UiFormRepository uiFormRepository;

    @Autowired
    UiFormFieldGroupRepository uiFormFieldGroupRepository;

    @Autowired
    UiFormFieldRepository uiFormFieldRepository;

    @Autowired
    HrFormFieldRepository hrFormFieldRepository;
    @Autowired
    private CountryMappingDao countryMappingDao;

    @Autowired
    EmployeeLeaveRepository employeeLeaveRepository;

    @Autowired
    SysUserRepository sysUserRepository;

    @Autowired
    ModuleConfigurationService moduleConfigService;

    @Autowired
    GroupServiceImpl groupServiceImpl;

    @Autowired
    HrFormFieldConfigurationRepository hrFormFieldConfigurationRepository;
    @Autowired
    LeaveService IleaveService;

    @Autowired
    LeaveQuotaRepository leaveQuotaRepository;

    @Autowired
    LeaveDurationTypeRepository leaveDurationTypeRepository;

    @Autowired
    private HrEmployeeResignationRepository resignationRepo;
    @Autowired
    HrPersonRepository hrPersonRepository;
    @Autowired
    TmAgeBasedEncashmentDetailRepository tmAgeBasedEncashmentDetailRepository;
    @Autowired
    TmEmployeeEncashmentRepository tmEmployeeEncashmentRepository;
    @Autowired
    TmEncashmentCalenderGroupRepository tmEncashmentCalenderGroupRepository;
    @Autowired
    TmEmployeeEncashmentDetailRepository tmEmployeeEncashmentDetailRepository;

    private static final Logger LOGGER = Logger.getLogger(EncashmentValidationService.class.toString());

    public String validationOnEncashmentSubmit(EncashmentRequestTO encashmentRequestTO, Map<Integer, String> sysDurationTypeMap,
                                               Map<Integer, String> entitlementPeriodMap, Map<String, KeyValueTO> tmLeaveDurationMap,
                                               ResourceBundle resourceBundle, int rinstateStatusId) throws Exception {

        if ("Contract".equalsIgnoreCase(encashmentRequestTO.getSource())) {
            return null;
        }

        String validateStr = null;
        Boolean isHourly = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateString = dateFormat.format(date);
        Integer dateId = timeDimesionRepository.getIdByDate(dateFormat.parse(dateString));
        if(encashmentRequestTO!=null&&encashmentRequestTO.getEncashmentEmployeeTO()!=null
                && encashmentRequestTO.getEncashmentRequestConfigTO()!=null&& encashmentRequestTO.getLeaveCategoryType()!=null){
            if(encashmentRequestTO.getEncashmentRequestConfigTO().getProbationLeaveLimit()!=null&&encashmentRequestTO.getEncashmentEmployeeTO().getConfirmationDateId()!=null
                    &&encashmentRequestTO.getEncashmentRequestConfigTO().getProbationLeaveLimit()>0
                    && encashmentRequestTO.getEncashmentEmployeeTO().getConfirmationDateId().compareTo(dateId)>=0 &&
                    encashmentRequestTO.getLeaveCategoryType().equalsIgnoreCase(LeaveConstant.PERIODIC_LEAVE)) {
                validateStr = validateLeaveLimitForProbationEmploymentType(encashmentRequestTO);
            }
        }

        if (StringUtil.nonEmptyCheck(validateStr)) {
            return validateStr;
        }

        if (null != encashmentRequestTO.getEncashmentRequestConfigTO()
                && null != encashmentRequestTO.getEncashmentRequestConfigTO().getIsHourly()
                && encashmentRequestTO.getEncashmentRequestConfigTO().getIsHourly()) {
            isHourly = true;
        }

        Map<String, ModuleConfigurationTO> moduleConfigMap = getModuleConfigurations(encashmentRequestTO);

        // ************* Common validations for Hourly and Non Hourly
        // ***********************
        validateStr = validationCommon(encashmentRequestTO, sysDurationTypeMap, resourceBundle, rinstateStatusId,
                moduleConfigMap);
        if (StringUtil.nonEmptyCheck(validateStr)) {
            return validateStr;
        }

        if (isHourly) {
            // ************* Validation for Hourly Leaves***********************
            validateStr = validationHourly(encashmentRequestTO, tmLeaveDurationMap, resourceBundle, moduleConfigMap);
        } else {
            // ************* Validation for Non Hourly Leaves ***********************
            validateStr = validationNonHourly(encashmentRequestTO, sysDurationTypeMap, tmLeaveDurationMap,
                    entitlementPeriodMap, resourceBundle, moduleConfigMap);
        }

        return validateStr;

    }

    private String validateLeaveLimitForProbationEmploymentType(EncashmentRequestTO encashmentRequestTO) {
        try {
            Float probationLeaveLimit = encashmentRequestTO.getEncashmentRequestConfigTO().getProbationLeaveLimit();
            Integer eligibleDateForJoining = encashmentRequestTO.getEncashmentRequestConfigTO().getEligibleDateForJoining();
            Integer dateofJoining =0;
            if(eligibleDateForJoining !=null && eligibleDateForJoining==0 && encashmentRequestTO.getEncashmentEmployeeTO()!=null && encashmentRequestTO.getEncashmentEmployeeTO().getJoiningDate()!=null){
                Date doj = encashmentRequestTO.getEncashmentEmployeeTO().getJoiningDate();
                dateofJoining=timeDimesionRepository.getIdByDate(doj);
            }
            float count =0;
            if(encashmentRequestTO.getLeaveDaysCount()!=null) {
                count = encashmentRequestTO.getLeaveDaysCount().floatValue();
            }
            if(count>probationLeaveLimit){
                return ILeaveErrorMessageConstants.LEAVE_LIMIT_ON_PROBATION;
            }
            List<Object[]> countOfLeaves = employeeLeaveRepository.getLeaveCountFromGivenDateId(encashmentRequestTO.getEmployeeId(),encashmentRequestTO.getOrganizationId(),dateofJoining,encashmentRequestTO.getLeaveTypeId());
            if(countOfLeaves!=null){
                for(Object[] to:countOfLeaves){
                    if(to[0]!=null) {
                        count = count + (int)to[0];
                    }
                    if(to[1]!=null){
                        int hrs= (int) to[1];
                        int workingHrs =0;
                        if(to[2]!=null){
                            workingHrs = (int) to[2];
                            float hourlyDays= (float)hrs/(float)workingHrs;
                            count=count+hourlyDays;
                        }
                    }
                }
            }
            if(count>probationLeaveLimit){
                return ILeaveErrorMessageConstants.LEAVE_LIMIT_ON_PROBATION;
            }
            return null;
        }catch(Exception e){
            throw e;
        }
    }
    private Map<String, ModuleConfigurationTO> getModuleConfigurations(EncashmentRequestTO encashmentRequestTO) throws Exception {
        List<String> params = new ArrayList<String>();
        params.add(ModuleConfigConstant.LWP_OCCURRENCE_INTERVAL);
        params.add(ModuleConfigConstant.DAYS_ABSENT);
        params.add(ModuleConfigConstant.MAX_BACK_DATED_LEAVE_APPLICATION_DURATION);
        params.add(ModuleConfigConstant.APPLYING_LWP_MAIN_BALANCE_CHECK);
        params.add(ModuleConfigConstant.APPLYING_HL_MAIN_BALANCE_CHECK);
        params.add(ModuleConfigConstant.BIRTHDAY_LEAVE_BEFORE_DAYS);
        params.add(ModuleConfigConstant.BIRTHDAY_LEAVE_AFTER_DAYS);
        params.add(ModuleConfigConstant.BIRTHDAY_LEAVE_FOR_RANGE);
        params.add(ModuleConfigConstant.ANNIVERSARY_LEAVE_BEFORE_DAYS);
        params.add(ModuleConfigConstant.ANNIVERSARY_LEAVE_AFTER_DAYS);
        params.add(ModuleConfigConstant.DYNAMIC_LEAVE_CALENDAR);
        params.add(ModuleConfigConstant.MAX_DAYS_FORDOD);
        params.add(ModuleConfigConstant.CUT_OFF_DATE_ATTENDANCE_CYCLE_FOR_LEAVE);
        params.add(ModuleConfigConstant.DYNAMIC_WORKFLOW_RULEENGINE_LEAVE);
        params.add(ModuleConfigConstant.ANNIVERSARY_LEAVE_FOR_RANGE);
        params.add(ModuleConfigConstant.BACKDATED_LEAVE_CONFIGURATION);
        params.add(ModuleConfigConstant.PUNCH_PRESENT_CONFIGURATION);
        params.add(ModuleConfigConstant.REG_PRESENT_CONFIGURATION);
        params.add(ModuleConfigConstant.LEAVE_ALLOWED_BEFORE_RESIGNATION_DAY);
        params.add(ModuleConfigConstant.LEAVE_APPLICATION_EXCEPTION_ON_RETIREMENT);
        Integer entityID = sysUserRepository.getEntityIDByUserID(encashmentRequestTO.getUserID(), encashmentRequestTO.getOrganizationId());
        Map<String, ModuleConfigurationTO> moduleConfigMap = moduleConfigService
                .getModuleConfigMapForEntity(encashmentRequestTO.getOrganizationId(), params, entityID);
        return moduleConfigMap;
    }

    public String validationCommon(EncashmentRequestTO encashmentRequestTO, Map<Integer, String> sysDurationTypeMap,
                                   ResourceBundle resourceBundle, int rinstateStatusId, Map<String, ModuleConfigurationTO> moduleConfigMap)
            throws Exception {
        String validateStr = null;
        String errorMessage = null;
        try {

            // ************* Validate Leave For Pending Regularization
            // ***********************
            Map<String, Long> groupPolicyMap = getGroupPolicyMap(encashmentRequestTO.getUserID(),encashmentRequestTO.getBundleName());
            Long altGroupId = groupPolicyMap.get(LeaveConstant.LEAVECONFIG_GROUP_POLICY);
            LeaveGroupConfigTO leaveGroupConfigTO = IleaveService.getLeaveGroupConfig(encashmentRequestTO.getLeaveTypeId(),altGroupId, encashmentRequestTO.getBundleName());

            validateStr = validateForContractualEmployee(encashmentRequestTO, resourceBundle);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }

            validateStr = validateRuleValidationFramework(encashmentRequestTO, resourceBundle, moduleConfigMap);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }

            validateStr = validateAttachmentMandatory(encashmentRequestTO, resourceBundle);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }

            validateStr = validateCurrentBalanceQuota(encashmentRequestTO, resourceBundle);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }


            validateStr = validateForNoticePeriod(encashmentRequestTO, resourceBundle);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }

            validateStr = validateForProbationPeriod(encashmentRequestTO, resourceBundle);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }
            validateStr = validateMinimumBalanceForEncashment(encashmentRequestTO, resourceBundle, altGroupId);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }
            validateStr = validateMinimumBalanceAfterEncashment(encashmentRequestTO, resourceBundle, altGroupId);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }


            validateStr = validateForMinimumEncashmentInTransaction(encashmentRequestTO, resourceBundle);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }

            validateStr = validateForMaximumEncashmentInTransaction(encashmentRequestTO, resourceBundle);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }

            validateStr = validateForAgedBasedEncashmentLimit(encashmentRequestTO, resourceBundle,altGroupId);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }

            validateStr = validateForNonAgedBasedEncashmentLimit(encashmentRequestTO, resourceBundle,altGroupId);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }
            validateStr = validateForMaximumNumberOfTransactions(encashmentRequestTO, resourceBundle,altGroupId);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }

            validateStr = validateForEncashmentAllowedInMonthQuarterYear(encashmentRequestTO, resourceBundle,altGroupId);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }
            
            validateStr = validateMaximumEncashmentLimitInCalendarGroup(encashmentRequestTO, resourceBundle,altGroupId);
            if (StringUtil.nonEmptyCheck(validateStr)) {
                return validateStr;
            }


        } catch (AppRuntimeException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            errorMessage = resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE);
            throw new AppRuntimeException(errorMessage);
        }
        return validateStr;

    }
    private Map<String, Long> getGroupPolicyMap(Integer userId, String bundleName)
            throws AppRuntimeException, Exception {

        Map<String, Long> groupPolicyMap = new HashMap<String, Long>();
        Long altgroupId = 0L;

        GroupRequestTO groupRequestTO = new GroupRequestTO();
        groupRequestTO.setUserId(userId);
        groupRequestTO.setPolicyCode(LeaveConstant.HOLIDAY_CALENDAR_GROUP_POLICY);
        altgroupId = groupServiceImpl.getGroupIdByPolicyCode(groupRequestTO, bundleName);
        groupPolicyMap.put(LeaveConstant.HOLIDAY_CALENDAR_GROUP_POLICY, altgroupId);

        groupRequestTO.setPolicyCode(LeaveConstant.LEAVE_CLUBBING_POLICY);
        altgroupId = groupServiceImpl.getGroupIdByPolicyCode(groupRequestTO, bundleName);
        groupPolicyMap.put(LeaveConstant.LEAVE_CLUBBING_POLICY, altgroupId);

        groupRequestTO.setPolicyCode(LeaveConstant.LEAVECONFIG_GROUP_POLICY);
        altgroupId = groupServiceImpl.getGroupIdByPolicyCode(groupRequestTO, bundleName);
        groupPolicyMap.put(LeaveConstant.LEAVECONFIG_GROUP_POLICY, altgroupId);

        groupRequestTO.setPolicyCode(LeaveConstant.LEAVE_FORCE_SANDWICH_GROUP_POLICY);
        altgroupId = groupServiceImpl.getGroupIdByPolicyCode(groupRequestTO, bundleName);
        groupPolicyMap.put(LeaveConstant.LEAVE_FORCE_SANDWICH_GROUP_POLICY, altgroupId);

        groupRequestTO.setPolicyCode(LeaveConstant.LEAVEENTITLEMENT_GROUP_POLICY);
        altgroupId = groupServiceImpl.getGroupIdByPolicyCode(groupRequestTO, bundleName);
        groupPolicyMap.put(LeaveConstant.LEAVEENTITLEMENT_GROUP_POLICY, altgroupId);

        groupRequestTO.setPolicyCode(LeaveConstant.LEAVE_UNIQUE_STATUS_POLICY);
        altgroupId = groupServiceImpl.getGroupIdByPolicyCode(groupRequestTO, bundleName);
        groupPolicyMap.put(LeaveConstant.LEAVE_UNIQUE_STATUS_POLICY, altgroupId);

        groupRequestTO.setPolicyCode(LeaveConstant.FULL_WEEK_LEAVE_SANDWICH);
        altgroupId = groupServiceImpl.getGroupIdByPolicyCode(groupRequestTO, bundleName);
        groupPolicyMap.put(LeaveConstant.FULL_WEEK_LEAVE_SANDWICH, altgroupId);

        return groupPolicyMap;

    }

    public String validationHourly(EncashmentRequestTO encashmentRequestTO, Map<String, KeyValueTO> tmLeaveDurationMap,
                                   ResourceBundle resourceBundle, Map<String, ModuleConfigurationTO> moduleConfigMap) {
        String validateStr = null;
        return validateStr;
    }

    public String validationNonHourly(EncashmentRequestTO encashmentRequestTO, Map<Integer, String> sysDurationTypeMap,
                                      Map<String, KeyValueTO> tmLeaveDurationMap, Map<Integer, String> entitlementPeriodMap,
                                      ResourceBundle resourceBundle, Map<String, ModuleConfigurationTO> moduleConfigMap) {
        String returnStr = null;
        return returnStr;
    }

    public String validateForContractualEmployee(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle) {

        String errorMess = null;
        Boolean isContractual = null;
        Integer contractStartDateId = null;
        Integer contractEndDateId = null;
        Integer leaveStartDateId = encashmentRequestTO.getStartDateId();
        Integer leaveEndDateId = encashmentRequestTO.getEndDateId();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {

            HrEmployee employee = hrEmployeeRepo.findByEmployeeId(encashmentRequestTO.getEmployeeId());
            isContractual = checkContractual(employee);
            if (isContractual) {

                Integer currentDateId = DateUtil.getTimeDimensionId(DateUtil.getCurrentDate());
                List<HrEmployeeContract> contractList = contractRepo.findPresentExtensionContract(
                        encashmentRequestTO.getEmployeeId(), DateUtil.getDateFromTimeDimensionId(currentDateId));

                if (contractList != null && contractList.size() > 0) {

                    contractStartDateId = DateUtil
                            .getTimeDimensionId(contractList.get(0).getExtensionEffectiveDateID());
                    contractEndDateId = DateUtil.getTimeDimensionId(contractList.get(0).getNewContractEndDateID());
                    if (contractStartDateId == null) {
                        throw new Exception(resourceBundle.getString(IMessageConstants.CONTRACTUAL_SRART_DATE_MESSAGE));
                    }
                    if (contractEndDateId == null) {
                        throw new Exception(resourceBundle.getString(IMessageConstants.CONTRACTUAL_END_DATE_MESSAGE));
                    }

                } else {

                    contractStartDateId = employee.getJoiningDateId();
                    contractEndDateId = employee.getContractEndDateID();
                    if (contractStartDateId == null) {
                        throw new Exception(resourceBundle.getString(IMessageConstants.CONTRACTUAL_SRART_DATE_MESSAGE));
                    }
                    if (contractEndDateId == null) {
                        throw new Exception(resourceBundle.getString(IMessageConstants.CONTRACTUAL_END_DATE_MESSAGE));
                    }
                }
                if ((currentDateId < contractStartDateId || leaveStartDateId < contractStartDateId
                        || leaveEndDateId < contractStartDateId)
                        || (currentDateId > contractEndDateId || leaveStartDateId > contractEndDateId
                        || leaveEndDateId > contractEndDateId)) {

                    errorMess = resourceBundle.getString(IMessageConstants.CONTRACTUAL_LEAVE_DATE_RANGE_ERROR_MESSAGE);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(e.getMessage());
        }
        return errorMess;
    }

    private String validateRuleValidationFramework(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle,
                                                   Map<String, ModuleConfigurationTO> moduleConfigMap) {
        String errorMessage = null;
        String host = env.getProperty(AppConstant.HOSTNAME);
        String formValidationApiUrl = AppConstant.SERVER_PROTOCOL + host + AppConstant.FORM_VALIDATION;
        try {
            if (moduleConfigMap != null && !moduleConfigMap.isEmpty()) {
                String isFrameworkConfigured = null;
                ModuleConfigurationTO moduleConfig = moduleConfigMap
                        .get(ModuleConfigConstant.DYNAMIC_WORKFLOW_RULEENGINE_LEAVE);
                if (moduleConfig != null) {
                    isFrameworkConfigured = moduleConfig.getValue();
                }
                if (isFrameworkConfigured != null && isFrameworkConfigured.trim().equalsIgnoreCase("true")
                        && !EncashmentUtil.checkIfFromUpload(encashmentRequestTO)) {
                    String clientIdName = null;
                    if (encashmentRequestTO.getRequestType().equals("NewLeave")) {
                        clientIdName = "SUBMIT";
                    } else {
                        clientIdName = "APPROVE";
                    }
                    String groupName = "ACTIONBUTTONS";

                    HrEmployee hrEmployee = hrEmployeeRepo.findByEmployeeId(encashmentRequestTO.getEmployeeId());
                    Long employeeCountry = null;
                    if (hrEmployee != null && hrEmployee.getCountryID() != null) {
                        employeeCountry = hrEmployee.getCountryID().longValue();
                    } else {
                        HrOrgCountryMapping country = countryMappingDao
                                .getDefaultCountry(encashmentRequestTO.getUserInfo().getOrganizationId());
                        employeeCountry = country.getCountryID().longValue();
                    }
                    Map<Long, Set<Integer>> countryWiseRoleMap = commonService.getCountryWiseRoleList(
                            encashmentRequestTO.getUserInfo().getUserId(), encashmentRequestTO.getTenantId());

                    BigDecimal hrFormFieldID = getHrFormFieldID(encashmentRequestTO.getRequestType(), groupName,
                            clientIdName, employeeCountry.intValue(), encashmentRequestTO.getOrganizationId());
                    List<BigDecimal> hrFormFieldConfigurationIDs = null;
                    if (null != countryWiseRoleMap.get(employeeCountry)) {
                        hrFormFieldConfigurationIDs = hrFormFieldConfigurationRepository
                                .getHrFormFieldConfigurationID(hrFormFieldID, countryWiseRoleMap.get(employeeCountry));
                    }
                    if (hrFormFieldID == null) {
                        return null;
                    }
                    RestUtil<FormValidationResponseTO> restUtil = new RestUtil<>();
                    FormValidationRequestTO request = new FormValidationRequestTO();
                    request.setEmployeeId(encashmentRequestTO.getEmployeeId());
                    request.setUserID(encashmentRequestTO.getUserID());
                    request.setHrFormFieldId(hrFormFieldID);
                    request.setHrFormFieldConfigurationId(hrFormFieldConfigurationIDs);
                    request.setRuleType("FormValidation");

                    List<Object[]> leaveSysObjects = sysObjectRepository.findObjectIdentifier("LeaveRule");
                    Integer sysObjectID = null;
                    String objectIdentifier = null;
                    if (PSCollectionUtil.isNotNullOrEmpty(leaveSysObjects)) {
                        for (Object[] leaveSysObject : leaveSysObjects) {
                            sysObjectID = (Integer) leaveSysObject[0];
                            objectIdentifier = (String) leaveSysObject[1];
                        }
                        request.setMethodParamMap(
                                prepareSysObjectAttributeList(encashmentRequestTO, sysObjectID, objectIdentifier));
                    }

                    ProtocolTO protocolTO = new ProtocolTO();
                    protocolTO.setTenantID(encashmentRequestTO.getTenantId());
                    protocolTO.setOrganizationID(encashmentRequestTO.getOrganizationId());
                    request.setProtocolTO(protocolTO);
                    String input = Utils.mapToJson(request);
                    LOGGER.log(Level.SEVERE, "FORM_VALIDATION_REQUEST==> " + input);
                    FormValidationResponseTO formValidationResponseTO = restUtil.postHttps(formValidationApiUrl,
                            request, FormValidationResponseTO.class, host);
                    String output = Utils.mapToJson(formValidationResponseTO);
                    LOGGER.log(Level.SEVERE, "FORM_VALIDATION_RESPONSE==> " + output);
                    if (formValidationResponseTO != null) {
                        if (formValidationResponseTO.getResponseCode() != null && formValidationResponseTO
                                .getResponseCode().equals(LeaveConstant.MessageReponse.SUCCESS)) {
                            if (null != formValidationResponseTO.getOutput()) {
                                if (formValidationResponseTO.getOutput()) {
                                    return null;
                                } else {
                                    errorMessage = formValidationResponseTO.getValues().get(0)
                                            .get(encashmentRequestTO.getBundleName().toUpperCase());
                                }
                            }
                        } else {
                            errorMessage = resourceBundle
                                    .getString(ILeaveErrorConstants.ERROR_OCCURED_VALIDATING_FORM_VALIDATION);
                        }
                    } else {
                        // This is fall back logic when tenant has no Rule Engine setup and Rule Engine
                        // is unable to tell us this because of some problem
                        if (PSCollectionUtil.isNotNullOrEmpty(hrFormFieldConfigurationIDs)) {
                            List<Integer> ruleGroupIDs = ruleGroupRepository.getRuleGroupID(hrFormFieldID,
                                    hrFormFieldConfigurationIDs);
                            if (PSCollectionUtil.isNotNullOrEmpty(ruleGroupIDs)) {
                                List<Integer> rules = rulesRepository.getRuleID(ruleGroupIDs);
                                if (PSCollectionUtil.isNotNullOrEmpty(rules)) {
                                    errorMessage = resourceBundle
                                            .getString(ILeaveErrorConstants.ERROR_OCCURED_VALIDATING_FORM_VALIDATION);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage() + " For API  " + formValidationApiUrl, e);
            errorMessage = resourceBundle.getString(ILeaveErrorConstants.ERROR_OCCURED_VALIDATING_FORM_VALIDATION);
            throw new AppRuntimeException(errorMessage);
        }
        return errorMessage;
    }

    private Boolean checkContractual(HrEmployee employee) throws Exception {
        Boolean result = false;

        if (employee != null && employee.getHrEmploymentType() != null) {
            HrEmploymentType contractualEmpType = employmentTypeRepo.findByEmploymentTypeIdAndActiveAndContractual(
                    employee.getHrEmploymentType(), Boolean.TRUE, Boolean.TRUE);
            if (contractualEmpType == null) {
                result = false;
            } else {
                result = true;

            }
        }

        return result;
    }

    private BigDecimal getHrFormFieldID(String formName, String groupName, String clientIdName, Integer countryId,
                                        Integer organizationId) {
        BigDecimal hrFormFieldID;
        try {
            Integer sysformId = (Integer) uiFormRepository.getFormID(formName);
            Integer sysfieldGroupID = (Integer) uiFormFieldGroupRepository.getfieldGroupID(sysformId, groupName);
            Integer sysFormFieldID = (Integer) uiFormFieldRepository.getFormFieldID(sysfieldGroupID, clientIdName);
            if (sysFormFieldID == null) {
                return null;
            }
            hrFormFieldID = (BigDecimal) hrFormFieldRepository.getHrFormFieldID(sysFormFieldID, organizationId,
                    countryId);
        } catch (Exception e) {
            return null;
        }
        return hrFormFieldID;
    }

    public Map<String, Object> prepareSysObjectAttributeList(EncashmentRequestTO encashmentRequestTO, Integer sysObjectID,
                                                             String objectIdentifier) throws Exception {
        Map<String, Object> finalValues = new HashMap<>();

        List<String> attributeCodes = new ArrayList<String>();
        attributeCodes.add("LEAVETYPECODE");
        attributeCodes.add("LEAVEREASONCODE");
        attributeCodes.add("LEAVEDURATIONDD");
        attributeCodes.add("LEAVEDURATIONHH");
        attributeCodes.add("LEAVEDURATIONMM");
        attributeCodes.add("LEAVEWEEKDAY");
        attributeCodes.add("LEAVESHIFT");
        attributeCodes.add("LEAVEMONTHTRANLIMIT");
        attributeCodes.add("LEAVEYEARTRANLIMIT");
        Map<String, String> attributeCode_attributeIdentifier = new HashMap<String, String>();

        List<Object[]> detailRecords = sysObjectAttributeRepository.findAttributeIdentifiers(sysObjectID,
                attributeCodes);
        for (Object[] detailRecord : detailRecords) {
            attributeCode_attributeIdentifier.put((String) detailRecord[0], (String) detailRecord[1]);
        }

        List<String> shiftList = new ArrayList<String>();
        List<String> weekDaysList = new ArrayList<String>();

        String shiftNames = null;
        String weekDayNames = null;

        ShiftInputTO shiftInputTO = new ShiftInputTO();
        shiftInputTO.setEmployeeId(encashmentRequestTO.getEmployeeId());
        shiftInputTO.setUserId(encashmentRequestTO.getUserID());
        //shiftInputTO.setStartDateId(encashmentRequestTO.getDurationTOList().get(0).getDateId());
//        shiftInputTO.setEndDateId(
//                encashmentRequestTO.getDurationTOList().get(encashmentRequestTO.getDurationTOList().size() - 1).getDateId());
        shiftInputTO.setOrganizationId(encashmentRequestTO.getOrganizationId());
        Map<Integer, ShiftRosterTO> shiftMap = shiftService.getEmployeeShiftForDateRange(shiftInputTO,
                encashmentRequestTO.getBundleName());
        Calendar calendar = Calendar.getInstance();
//        for (LeaveDurationTO leaveDurationTO : encashmentRequestTO.getDurationTOList()) {
//            shiftList.add(shiftMap.get(leaveDurationTO.getDateId()).getShiftCode());
//            calendar.setTime(DateUtil.getDateFromTimeDimensionId(leaveDurationTO.getDateId()));
//
//            int selectedweekDay = calendar.get(Calendar.DAY_OF_WEEK);
//
//            if (selectedweekDay == 1)
//                weekDaysList.add("SUNDAY");
//            if (selectedweekDay == 2)
//                weekDaysList.add("MONDAY");
//            if (selectedweekDay == 3)
//                weekDaysList.add("TUESDAY");
//            if (selectedweekDay == 4)
//                weekDaysList.add("WEDNESDAY");
//            if (selectedweekDay == 5)
//                weekDaysList.add("THURSDAY");
//            if (selectedweekDay == 6)
//                weekDaysList.add("FRIDAY");
//            if (selectedweekDay == 7)
//                weekDaysList.add("SATURDAY");
//        }

        shiftNames = StringUtil.convertStrListToString(shiftList);
        weekDayNames = StringUtil.convertStrListToString(weekDaysList);

        int startDateCalendarid = 0;
        if (null != encashmentRequestTO.getEncashmentRequestConfigTO() && null != encashmentRequestTO.getLeaveCalendarStartDate()) {
            startDateCalendarid = DateUtil.getTimeDimensionId(encashmentRequestTO.getLeaveCalendarStartDate());
        }

        int endDateCalendarid = 0;
        if (null != encashmentRequestTO.getEncashmentRequestConfigTO() && null != encashmentRequestTO.getLeaveCalendarEndDate()) {
            endDateCalendarid = DateUtil.getTimeDimensionId(encashmentRequestTO.getLeaveCalendarEndDate());
        }

        Calendar currentCalender = Calendar.getInstance();
        currentCalender.setTime(encashmentRequestTO.getCurrentDate());
        Integer leaveRequestMonthCount = getMaxLeaveRequestWithinMonthOrYear(encashmentRequestTO.getEmployeeId(),
                encashmentRequestTO.getLeaveTypeId(), encashmentRequestTO.getOrganizationId(),
                currentCalender.get(Calendar.MONTH) + 1, 0, null, null, startDateCalendarid, endDateCalendarid);
        leaveRequestMonthCount = leaveRequestMonthCount + 1;
        Integer leaveRequestYearCount = getMaxLeaveRequestWithinMonthOrYear(encashmentRequestTO.getEmployeeId(),
                encashmentRequestTO.getLeaveTypeId(), encashmentRequestTO.getOrganizationId(), 0, 1,
                encashmentRequestTO.getLeaveCalendarStartDate(), encashmentRequestTO.getLeaveCalendarEndDate(), 0, 0);
        leaveRequestYearCount = leaveRequestYearCount + 1;

        if (null != encashmentRequestTO.getEncashmentRequestConfigTO()
                && null != encashmentRequestTO.getEncashmentRequestConfigTO().getEncashmentTypeTO()
                && encashmentRequestTO.getEncashmentRequestConfigTO().getEncashmentTypeTO().getLeaveTypeCode() != null) {
            finalValues.put(objectIdentifier + "." + attributeCode_attributeIdentifier.get("LEAVETYPECODE"),
                    encashmentRequestTO.getEncashmentRequestConfigTO().getEncashmentTypeTO().getLeaveTypeCode());
        }
        if (null != encashmentRequestTO.getLeaveReasonCd() && !EncashmentUtil.checkIfFromUpload(encashmentRequestTO)) {
            finalValues.put(objectIdentifier + "." + attributeCode_attributeIdentifier.get("LEAVEREASONCODE"),
                    encashmentRequestTO.getLeaveReasonCd());
        }
        if (null != encashmentRequestTO.getLeaveCountInDD()) {
            finalValues.put(objectIdentifier + "." + attributeCode_attributeIdentifier.get("LEAVEDURATIONDD"),
                    encashmentRequestTO.getLeaveCountInDD().toString());
        }
        if (null != encashmentRequestTO.getLeaveCountInHH()) {
            finalValues.put(objectIdentifier + "." + attributeCode_attributeIdentifier.get("LEAVEDURATIONHH"),
                    encashmentRequestTO.getLeaveCountInHH().toString());
        }
        if (null != encashmentRequestTO.getLeaveCountInMM()) {
            finalValues.put(objectIdentifier + "." + attributeCode_attributeIdentifier.get("LEAVEDURATIONMM"),
                    encashmentRequestTO.getLeaveCountInMM().toString());
        }

        if (null != shiftNames) {
            finalValues.put(objectIdentifier + "." + attributeCode_attributeIdentifier.get("LEAVESHIFT"), shiftNames);
        }

        if (null != weekDayNames) {
            finalValues.put(objectIdentifier + "." + attributeCode_attributeIdentifier.get("LEAVEWEEKDAY"),
                    weekDayNames);
        }

        finalValues.put(objectIdentifier + "." + attributeCode_attributeIdentifier.get("LEAVEMONTHTRANLIMIT"),
                leaveRequestMonthCount.toString());
        finalValues.put(objectIdentifier + "." + attributeCode_attributeIdentifier.get("LEAVEYEARTRANLIMIT"),
                leaveRequestYearCount.toString());

        return finalValues;
    }

    private Integer getMaxLeaveRequestWithinMonthOrYear(int employeeId, int leaveTypeId, int organizationId, int month,
                                                        int year, Date startDate, Date endDate, int startDateCalendarid, int endDateCalendarid) throws Exception {
        Object object = null;
        Long count = 0l;
        Calendar calender = Calendar.getInstance();
        int leaveRequestCount = 0;
        if (month > 0 && startDateCalendarid != 0 && endDateCalendarid != 0) {
            List<Object[]> resultList = employeeLeaveRepository.getmaxLeaveRequestWithinMonth(employeeId, leaveTypeId,
                    organizationId, startDateCalendarid, endDateCalendarid, Boolean.TRUE);
            if (PSCollectionUtil.isNotNullOrEmpty(resultList)) {
                for (Object[] objects : resultList) {
                    if (objects != null) {
                        if (objects[1] != null) {
                            calender.setTime((Date) objects[1]);
                            Integer resultMonth = (calender.get(Calendar.MONTH) + 1);
                            if (resultMonth.equals(month)) {
                                leaveRequestCount++;
                            }
                        }
                    }
                }
            }

        } else if (year > 0 && null != startDate && null != endDate) {
            object = employeeLeaveRepository.getmaxLeaveRequestWithinYear(employeeId, leaveTypeId, organizationId,
                    DateUtil.getTimeDimensionId(startDate), DateUtil.getTimeDimensionId(endDate), Boolean.TRUE);
            if (object != null) {
                count = (Long) object;
                leaveRequestCount = count.intValue();
            }

        }
        return leaveRequestCount;
    }

    private String validateAttachmentMandatory(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle) {
        try {
            if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentTypeContentId()!=null) {
                Set<Long> encashmentTypeIds = null;
                if (encashmentRequestTO.getCalendarBlocksTOList() != null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()){
                    encashmentTypeIds = encashmentRequestTO.getCalendarBlocksTOList().stream()
                            .map(CalendarBlocksTO::getEncashmentTypeId)
                            .collect(Collectors.toSet());
                }
                List<Boolean> attachmentMandatory = null;
                if (encashmentTypeIds != null && !encashmentTypeIds.isEmpty()){
                    attachmentMandatory = tmEncashmentTypeRepository.getAttachmentMandatory(encashmentRequestTO.getOrganizationId(),true, encashmentTypeIds);
                }
                if (attachmentMandatory != null && !attachmentMandatory.isEmpty()) {
                    for (Boolean isAttachmentRequired : attachmentMandatory){
                        if (isAttachmentRequired &&  (encashmentRequestTO.getAttachmentPath()==null || encashmentRequestTO.getAttachmentPath().isEmpty())){
                            return resourceBundle.getString(EncashmentConstant.ATTACHMENT_MANDATORY);
                        }
                    }
                } else {
                        return null;
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
        }
    }

    private String validateCurrentBalanceQuota(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle) {
        try {
            if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentTypeContentId()!=null) {
               List<Object[]> currentBalanceInDaysHrsList = leaveQuotaRepository.getCurrentBalanceForEncashment(encashmentRequestTO.getEmployeeId(), encashmentRequestTO.getLeaveTypeId(), encashmentRequestTO.getTenantId());
               Integer days=0;
               Integer hrs=0;
                if (currentBalanceInDaysHrsList != null && !currentBalanceInDaysHrsList.isEmpty()) {
                    Object[] currentBalanceInDaysHrs = currentBalanceInDaysHrsList.get(0);
                    if (currentBalanceInDaysHrs.length == 2) {
                        days = (Integer) currentBalanceInDaysHrs[0]; // First element for days
                        hrs = (Integer) currentBalanceInDaysHrs[1];   // Second element for hours
                    }

                    Float currentBalance= getExactBalance(encashmentRequestTO,days, hrs);
                if (currentBalance!=null && currentBalance<encashmentRequestTO.getEncashmentNumberOfDays() ) {
                    return MessageFormat.format(
                            resourceBundle.getString(EncashmentConstant.INSUFFICIENT_LEAVE_BALANCE),encashmentRequestTO.getLeaveType());
                } else {
                    return null;
                }
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
        }
    }

   private Float getExactBalance(EncashmentRequestTO encashmentRequestTO,Integer days,
    Integer hrs){
        Float result=0F;
       List<Integer> hourlyWeightageList = leaveDurationTypeRepository.getHourlyWightageList(encashmentRequestTO.getOrganizationId(), true, LeaveConstant.LEAVE_DURATION_FULL);
       Integer hourlyWeightage = null;
       for (Integer weightage : hourlyWeightageList) {
           if (weightage != null && weightage > 0) {
               hourlyWeightage = weightage;
           }
       }
       Float totalCurrentBalance = LeaveUtil.quotaCalculation(days, hrs, hourlyWeightage)/hourlyWeightage;
        result = LeaveUtil.getRoundOffValue(2, totalCurrentBalance).floatValue();
        return result;
   }

    private String validateForNoticePeriod(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle) {
        try {
            if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentTypeContentId()!=null) {
                Boolean onNoticePeriod=false;
                List<Object[]> noticePeriodList= hrEmployeeRepo.getNoticeAndProbation(encashmentRequestTO.getOrganizationId(),encashmentRequestTO.getEmployeeId());
                Integer employmentStatusID= null;
                String sysTypeForNoticePeriod=null;
                if (noticePeriodList != null && !noticePeriodList.isEmpty()) {
                    Object[] noticePeriod = noticePeriodList.get(0);
                        employmentStatusID = (Integer) noticePeriod[0];
                }
                    List<String> sysTypeForNoticePeriodList = hrContentTypeRepository.getSysTypeForEncashment(EncashmentConstant.EMPLOYEE_TYPE_RESIGNED,employmentStatusID,encashmentRequestTO.getOrganizationId());

                    if (sysTypeForNoticePeriodList != null && !sysTypeForNoticePeriodList.isEmpty()) {
                        sysTypeForNoticePeriod = sysTypeForNoticePeriodList.get(0);
                    }
                 if(sysTypeForNoticePeriod!=null && sysTypeForNoticePeriod.equalsIgnoreCase(EncashmentConstant.EMPLOYEE_TYPE_RESIGNED)){
                     onNoticePeriod=true;
                 }
                     Set<Long> encashmentTypeIds = null;
                     if (encashmentRequestTO.getCalendarBlocksTOList() != null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()){
                         encashmentTypeIds = encashmentRequestTO.getCalendarBlocksTOList().stream()
                                 .map(CalendarBlocksTO::getEncashmentTypeId)
                                 .collect(Collectors.toSet());
                     }
                     List<Boolean> allowedDuringNoticePeriod = null;
                     if (encashmentTypeIds != null && !encashmentTypeIds.isEmpty()){
                         allowedDuringNoticePeriod = tmEncashmentTypeRepository.getAllowDuringNoticePeriod(encashmentRequestTO.getOrganizationId(), true, encashmentTypeIds);
                     }
                     if(allowedDuringNoticePeriod != null && !allowedDuringNoticePeriod.isEmpty()){
                         for (Boolean isAllowedDuringNotice : allowedDuringNoticePeriod){
                             if (Boolean.FALSE.equals(isAllowedDuringNotice) && Boolean.TRUE.equals(onNoticePeriod)) {
                                 return resourceBundle.getString(EncashmentConstant.EMPLOYEE_ON_NOTICE_PERIOD);
                             }
                         }
                     }
                     else{
                         return null;
                     }
                }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
        }
    }

    private String validateForProbationPeriod(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle) {
        try {
            if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentTypeContentId()!=null) {
                Boolean onProbationPeriod=false;
                List<Object[]> probationPeriodList= hrEmployeeRepo.getNoticeAndProbation(encashmentRequestTO.getOrganizationId(),encashmentRequestTO.getEmployeeId());
                Integer confirmationStatusID= null;
                String sysTypeForProbationPeriod=null;
                if (probationPeriodList != null && !probationPeriodList.isEmpty()) {
                    Object[] probationPeriod = probationPeriodList.get(0);
                    confirmationStatusID = (Integer) probationPeriod[1];
                }
                List<String> sysTypeForProbationPeriodList = hrContentTypeRepository.getSysTypeForEncashment(EncashmentConstant.EMPLOYEE_TYPE_ON_PROBATION,confirmationStatusID,encashmentRequestTO.getOrganizationId());

                if (sysTypeForProbationPeriodList != null && !sysTypeForProbationPeriodList.isEmpty()) {
                    sysTypeForProbationPeriod = sysTypeForProbationPeriodList.get(0);
                }

                if(sysTypeForProbationPeriod!=null && sysTypeForProbationPeriod.equalsIgnoreCase(EncashmentConstant.EMPLOYEE_TYPE_ON_PROBATION)){
                    onProbationPeriod=true;
                }
                Set<Long> encashmentTypeIds = null;
                if (encashmentRequestTO.getCalendarBlocksTOList() != null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()){
                    encashmentTypeIds = encashmentRequestTO.getCalendarBlocksTOList().stream()
                            .map(CalendarBlocksTO::getEncashmentTypeId)
                            .collect(Collectors.toSet());
                }
                List<Boolean> allowedDuringProbationPeriod = null;
                if (encashmentTypeIds != null && !encashmentTypeIds.isEmpty()){
                    allowedDuringProbationPeriod = tmEncashmentTypeRepository.getAllowDuringProbationPeriod(encashmentRequestTO.getOrganizationId(), true, encashmentTypeIds);
                }
                if(allowedDuringProbationPeriod != null && !allowedDuringProbationPeriod.isEmpty()){
                    for (Boolean isAllowedDuringProbation : allowedDuringProbationPeriod){
                        if (Boolean.FALSE.equals(isAllowedDuringProbation) && Boolean.TRUE.equals(onProbationPeriod)) {
                            return resourceBundle.getString(EncashmentConstant.EMPLOYEE_ON_PROBATION_PERIOD);
                        }
                    }
                }
                else{
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
        }
    }
    
    public boolean noBalanceCreditInReinstate(int employeeId, int startdateId, int orgId,
                                              ResourceBundle resourceBundle) {
        boolean flag = false;
        try {
            HrEmployeeResignation resignedDetails = resignationRepo.findTopByEmployeeId(employeeId);
            if (resignedDetails != null && resignedDetails.getReinstateDateId() != null) {
                Integer joiningdateId = hrEmployeeRepo.getJoiningDateId(orgId, employeeId);
                if (joiningdateId != null && joiningdateId > 0 && startdateId > 0) {
                    if (startdateId < joiningdateId) {
                        flag = true;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(ILeaveErrorConstants.FAILED_VALIDATION_FAILURE));
        }
        return flag;
        
    }

    private String validateMinimumBalanceForEncashment(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle, Long altGroupID) {
        try {
            if (encashmentRequestTO != null  && encashmentRequestTO.getEncashmentTypeContentId()!=null) {
                List<Long> calendarBlockDetailIDs = new ArrayList<>();
                if(encashmentRequestTO.getCalendarBlocksTOList()!=null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()) {
                    List<CalendarBlocksTO> calendarBlockTOList = encashmentRequestTO.getCalendarBlocksTOList();
                    if (calendarBlockTOList != null) {
                        for (CalendarBlocksTO block : calendarBlockTOList) {
                            if (block != null && block.getCalendarBlockId() != null) {
                                calendarBlockDetailIDs.add(block.getCalendarBlockId());
                            }
                        }
                    }
                }
                List<Object[]> minimumBalanceForEncashmentList= tmEncashmentCalenderGroupRepository.findMinimumBalanceForEncashment(calendarBlockDetailIDs,true,altGroupID,encashmentRequestTO.getLeaveTypeId(),encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getTenantId());
                List<Object[]> currentBalanceInDaysHrsList = leaveQuotaRepository.getCurrentBalanceForEncashment(encashmentRequestTO.getEmployeeId(), encashmentRequestTO.getLeaveTypeId(), encashmentRequestTO.getTenantId());
                Integer days=0;
                Integer hrs=0;
                Float maximumBalanceOfCals =0F;
                if (minimumBalanceForEncashmentList != null && !minimumBalanceForEncashmentList.isEmpty()) {
                     maximumBalanceOfCals = getMaximumBalanceOfCals(minimumBalanceForEncashmentList);
                }
                if ((minimumBalanceForEncashmentList != null && !minimumBalanceForEncashmentList.isEmpty()) && (currentBalanceInDaysHrsList != null && !currentBalanceInDaysHrsList.isEmpty())) {

                    Object[] currentBalanceInDaysHrs = currentBalanceInDaysHrsList.get(0);
                    if (currentBalanceInDaysHrs.length == 2) {
                        days = (Integer) currentBalanceInDaysHrs[0];
                        hrs = (Integer) currentBalanceInDaysHrs[1];
                    }
                    Float currentBalance= getExactBalance(encashmentRequestTO,days, hrs);
                    if(maximumBalanceOfCals!=null && maximumBalanceOfCals>0) {
                        if (currentBalance != null && currentBalance < maximumBalanceOfCals) {
                            return MessageFormat.format(
                                    resourceBundle.getString(EncashmentConstant.INSUFFICIENT_LEAVE_BALANCE_FOR_CALENDAR_BLOCK), encashmentRequestTO.getLeaveType());
                        }
                    }else {
                        return null;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
        }
    }
    private String validateMinimumBalanceAfterEncashment(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle, Long altGroupID) {
        try {
            if (encashmentRequestTO != null  && encashmentRequestTO.getEncashmentTypeContentId()!=null) {
                List<Long> calendarBlockDetailIDs = new ArrayList<>();
                if(encashmentRequestTO.getCalendarBlocksTOList()!=null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()) {
                    List<CalendarBlocksTO> calendarBlockTOList = encashmentRequestTO.getCalendarBlocksTOList();

                    if (calendarBlockTOList != null) {
                        for (CalendarBlocksTO block : calendarBlockTOList) {
                            if (block != null && block.getCalendarBlockId() != null) {
                                calendarBlockDetailIDs.add(block.getCalendarBlockId());
                            }
                        }
                    }
                }
                List<Object[]> minimumBalanceAfterEncashmentList= tmEncashmentCalenderGroupRepository.findMinimumBalanceAfterEncashment(calendarBlockDetailIDs,true,altGroupID,encashmentRequestTO.getLeaveTypeId(),encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getTenantId());
                List<Object[]> currentBalanceInDaysHrsList = leaveQuotaRepository.getCurrentBalanceForEncashment(encashmentRequestTO.getEmployeeId(), encashmentRequestTO.getLeaveTypeId(), encashmentRequestTO.getTenantId());
                Integer days=0;
                Integer hrs=0;
                Float maximumBalanceAfterCals =0F;
                if (minimumBalanceAfterEncashmentList != null && !minimumBalanceAfterEncashmentList.isEmpty()) {
                     maximumBalanceAfterCals = getMaximumBalanceOfCals(minimumBalanceAfterEncashmentList);
                }
                if ((minimumBalanceAfterEncashmentList != null && !minimumBalanceAfterEncashmentList.isEmpty()) && (currentBalanceInDaysHrsList != null && !currentBalanceInDaysHrsList.isEmpty())) {
                    Object[] currentBalanceInDaysHrs = currentBalanceInDaysHrsList.get(0);
                    if (currentBalanceInDaysHrs.length == 2) {
                        days = (Integer) currentBalanceInDaysHrs[0];
                        hrs = (Integer) currentBalanceInDaysHrs[1];
                    }
                    Float currentBalance= getExactBalance(encashmentRequestTO,days, hrs);
                    if(maximumBalanceAfterCals!=null && maximumBalanceAfterCals>0) {
                        if (currentBalance != null && (currentBalance - encashmentRequestTO.getEncashmentNumberOfDays() < maximumBalanceAfterCals)) {
                            return MessageFormat.format(
                                    resourceBundle.getString(EncashmentConstant.INSUFFICIENT_LEAVE_BALANCE_AFTER_CALENDAR_BLOCK), encashmentRequestTO.getLeaveType());
                        }
                    }else {
                        return null;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
        }
    }
    private String validateForMinimumEncashmentInTransaction(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle) {
        try {
            if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentTypeContentId()!=null) {
                Set<Long> encashmentTypeIds = null;
                if (encashmentRequestTO.getCalendarBlocksTOList() != null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()){
                    encashmentTypeIds = encashmentRequestTO.getCalendarBlocksTOList().stream()
                            .map(CalendarBlocksTO::getEncashmentTypeId)
                            .collect(Collectors.toSet());
                }
                List<Float> minimumEncashmentInTransaction = new ArrayList<>();
                Float minimumEncashmentInTransactionSum= 0F;
                if (encashmentTypeIds != null && !encashmentTypeIds.isEmpty()){
                    minimumEncashmentInTransaction = tmEncashmentTypeRepository.getMinimumEncashmentInTransaction(encashmentRequestTO.getOrganizationId(),true, encashmentTypeIds);
                }
                if (minimumEncashmentInTransaction != null && !minimumEncashmentInTransaction.isEmpty()) {
                    for (Float minimumEncashment : minimumEncashmentInTransaction){
                        minimumEncashmentInTransactionSum+= minimumEncashment;
                    }
                    if(minimumEncashmentInTransactionSum!=null && minimumEncashmentInTransactionSum>0) {
                        if (encashmentRequestTO.getEncashmentNumberOfDays() < minimumEncashmentInTransactionSum) {
                            return resourceBundle.getString(EncashmentConstant.MINIMUM_ENCASHMENT_LIMIT);
                        }
                    }
                }
                else{
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
        }
    }
    private String validateForMaximumEncashmentInTransaction(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle) {
        try {
            if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentTypeContentId()!=null) {
                Set<Long> encashmentTypeIds = null;
                if (encashmentRequestTO.getCalendarBlocksTOList() != null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()){
                    encashmentTypeIds = encashmentRequestTO.getCalendarBlocksTOList().stream()
                            .map(CalendarBlocksTO::getEncashmentTypeId)
                            .collect(Collectors.toSet());
                }
                List<Float> maximumEncashmentInTransaction = new ArrayList<>();
                Float maximumEncashmentInTransactionSum= 0F;
                if (encashmentTypeIds != null && !encashmentTypeIds.isEmpty()){
                    maximumEncashmentInTransaction = tmEncashmentTypeRepository.getMaximumEncashmentInTransaction(encashmentRequestTO.getOrganizationId(),true, encashmentTypeIds);
                }
                if (maximumEncashmentInTransaction != null && !maximumEncashmentInTransaction.isEmpty()) {
                    for (Float maximumEncashment : maximumEncashmentInTransaction){
                        maximumEncashmentInTransactionSum+= maximumEncashment;
                    }
                    if(maximumEncashmentInTransactionSum!=null && maximumEncashmentInTransactionSum>0) {
                        if (encashmentRequestTO.getEncashmentNumberOfDays() > maximumEncashmentInTransactionSum) {
                            return resourceBundle.getString(EncashmentConstant.MAXIMUM_ENCASHMENT_TRANSACTION);
                        }
                    }
                }
                else {
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
        }
    }
    private String validateForAgedBasedEncashmentLimit(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle, Long altGroupID) {
        try {
            if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentTypeContentId()!=null) {
                String hrContentType= hrContentTypeRepository.getHrContentType(encashmentRequestTO.getEncashmentTypeContentId(), encashmentRequestTO.getOrganizationId());
                Integer dateOfBirthId=null;
                Calendar cal;
                Calendar nowCal;
                List<Float> ageBasedAgeLimitList = new ArrayList<>();
                Float ageBasedAgeLimit= 0F;
                if(hrContentType.equalsIgnoreCase(EncashmentConstant.AGED_BASED_ENCASHMENT)) {
                    Set<Long> encashmentTypeIds = null;
                    if (encashmentRequestTO.getCalendarBlocksTOList() != null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()) {
                        encashmentTypeIds = encashmentRequestTO.getCalendarBlocksTOList().stream()
                                .map(CalendarBlocksTO::getEncashmentTypeId)
                                .collect(Collectors.toSet());
                    }
                    dateOfBirthId = hrPersonRepository.getDateOfBirthByOrganizationAndEmployee(encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getEmployeeId());
                    Date dateOfBirth = DateUtil.getDateFromTimeDimensionId(dateOfBirthId);
                    cal = Calendar.getInstance();
                    nowCal = Calendar.getInstance();
                    cal.setTime(dateOfBirth);
                    Integer age = nowCal.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
                    List<Integer> ageBasedEncashmentIds = tmEncashmentTypeRepository.getAgeBasedEncashmentIds(encashmentRequestTO.getOrganizationId(), true, encashmentTypeIds);
                    ageBasedAgeLimitList = tmAgeBasedEncashmentDetailRepository.getAgeBasedEncashmentLimit(age, ageBasedEncashmentIds, encashmentRequestTO.getOrganizationId());
                    if (ageBasedAgeLimitList != null && !ageBasedAgeLimitList.isEmpty()) {
                        for (Float ageLimit : ageBasedAgeLimitList) {
                            ageBasedAgeLimit += ageLimit;
                        }
                    }
                    List<Object[]> encashmentCountInDaysHrsList = tmEmployeeEncashmentDetailRepository.getEncashmentTakenCount(encashmentRequestTO.getOrganizationId(), true, encashmentTypeIds,encashmentRequestTO.getEmployeeId());
                    Integer totalEncashmentInDays = 0;
                    Integer totalEncashmentInHrs = 0;
                    if (encashmentCountInDaysHrsList != null && !encashmentCountInDaysHrsList.isEmpty()) {
                        for (Object[] encashment : encashmentCountInDaysHrsList) {
                            if (encashment != null) {
                                Integer days = 0;
                                Short hours = 0;
                                if (encashment.length > 0 && encashment[0] != null) {
                                    days = (Integer) encashment[0];
                                    totalEncashmentInDays += days;
                                }
                                if (encashment.length > 1 && encashment[1] != null) {
                                    hours = (Short) encashment[1];
                                    totalEncashmentInHrs+= hours;
                                }
                            }
                        }
                    }
                    Float totalEncashmentTaken= getExactBalance(encashmentRequestTO,totalEncashmentInDays, totalEncashmentInHrs);

                    if (ageBasedAgeLimit > 0 &&( encashmentRequestTO.getEncashmentNumberOfDays() + totalEncashmentTaken > ageBasedAgeLimit)) {
                        return resourceBundle.getString(EncashmentConstant.AGE_BASED_ENCASHMENT_LIMIT);
                    }

                    List<CalendarBlocksTO> calendarBlockTOList = encashmentRequestTO.getCalendarBlocksTOList();
                    List<Long> encashmentCalenderGroupIDs = new ArrayList<>();
                    if (calendarBlockTOList != null) {
                        for (CalendarBlocksTO block : calendarBlockTOList) {
                            if (block != null && block.getCalendarGroupId() != null) {
                                encashmentCalenderGroupIDs.add(block.getCalendarGroupId());
                            }
                        }
                    }
                    List<Object[]> maximumEncashmentLimitList= tmEncashmentCalenderGroupRepository.findMaximumEncashmentLimit(encashmentCalenderGroupIDs,true,altGroupID,encashmentRequestTO.getLeaveTypeId(),encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getTenantId());
                    Float maximumEncashmentLimit=0F;
                    if (maximumEncashmentLimitList != null && !maximumEncashmentLimitList.isEmpty()) {
                        maximumEncashmentLimit=getSumBalanceOfCals(maximumEncashmentLimitList);
                    }
                    if(maximumEncashmentLimit!=null && maximumEncashmentLimit>0) {
                        if (encashmentRequestTO.getEncashmentNumberOfDays() > (maximumEncashmentLimit - totalEncashmentTaken)) {
                            return resourceBundle.getString(EncashmentConstant.MAXIMUM_ENCASHMENT_LIMIT_EXCEEDED);
                        }
                    }
                   else {
                       return null;
                   }
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
        }
    }

    private String validateForNonAgedBasedEncashmentLimit(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle, Long altGroupID) {
        try {
            if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentTypeContentId()!=null) {
                String hrContentType= hrContentTypeRepository.getHrContentType(encashmentRequestTO.getEncashmentTypeContentId(), encashmentRequestTO.getOrganizationId());
                if(!hrContentType.equalsIgnoreCase(EncashmentConstant.AGED_BASED_ENCASHMENT)) {
                    Float maximumEncashmentLimit=0F;
                    Set<Long> encashmentTypeIds = null;
                    if (encashmentRequestTO.getCalendarBlocksTOList() != null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()) {
                        encashmentTypeIds = encashmentRequestTO.getCalendarBlocksTOList().stream()
                                .map(CalendarBlocksTO::getEncashmentTypeId)
                                .collect(Collectors.toSet());
                    }
                    List<Object[]> encashmentCountInDaysHrsList = tmEmployeeEncashmentDetailRepository.getEncashmentTakenCount(encashmentRequestTO.getOrganizationId(), true, encashmentTypeIds,encashmentRequestTO.getEmployeeId());
                    List<CalendarBlocksTO> calendarBlockTOList = encashmentRequestTO.getCalendarBlocksTOList();
                    List<Long> encashmentCalenderGroupIDs = new ArrayList<>();
                    if (calendarBlockTOList != null) {
                        for (CalendarBlocksTO block : calendarBlockTOList) {
                            if (block != null && block.getCalendarGroupId() != null) {
                                encashmentCalenderGroupIDs.add(block.getCalendarGroupId());
                            }
                        }
                    }
                    List<Object[]> maximumEncashmentLimitList= tmEncashmentCalenderGroupRepository.findMaximumEncashmentLimit(encashmentCalenderGroupIDs,true,altGroupID,encashmentRequestTO.getLeaveTypeId(),encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getTenantId());
                    if (maximumEncashmentLimitList != null && !maximumEncashmentLimitList.isEmpty()) {
                        maximumEncashmentLimit=getSumBalanceOfCals(maximumEncashmentLimitList);
                    }
                    if(maximumEncashmentLimit!=null && maximumEncashmentLimit>0) {
                        if (encashmentRequestTO.getEncashmentNumberOfDays() > maximumEncashmentLimit) {
                            return resourceBundle.getString(EncashmentConstant.MAXIMUM_ENCASHMENT_LIMIT);
                        }
                    }
                    Integer totalEncashmentInDays = 0;
                    Integer totalEncashmentInHrs = 0;
                    if (encashmentCountInDaysHrsList != null && !encashmentCountInDaysHrsList.isEmpty()) {
                        for (Object[] encashment : encashmentCountInDaysHrsList) {
                            if (encashment != null) {
                                Integer days = 0;
                                Short hours = 0;
                                if (encashment.length > 0 && encashment[0] != null) {
                                    days = (Integer) encashment[0];
                                    totalEncashmentInDays += days;
                                }
                                if (encashment.length > 1 && encashment[1] != null) {
                                    hours = (Short) encashment[1];
                                    totalEncashmentInHrs+= hours;
                                }
                            }
                        }
                    }
                    Float totalEncashmentTaken= getExactBalance(encashmentRequestTO,totalEncashmentInDays, totalEncashmentInHrs);
                    if(maximumEncashmentLimit!=null && maximumEncashmentLimit>0) {
                        if (encashmentRequestTO.getEncashmentNumberOfDays() > (maximumEncashmentLimit - totalEncashmentTaken)) {
                            return resourceBundle.getString(EncashmentConstant.MAXIMUM_ENCASHMENT_LIMIT_FOR_SAME_CALENDAR_GROUP);
                        }
                    }
                    else {
                        return null;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
        }
    }

    private String validateForMaximumNumberOfTransactions(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle, Long altGroupID) {
        try {
            if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentTypeContentId()!=null) {
                Set<Long> encashmentTypeIds = null;
                if (encashmentRequestTO.getCalendarBlocksTOList() != null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()){
                    encashmentTypeIds = encashmentRequestTO.getCalendarBlocksTOList().stream()
                            .map(CalendarBlocksTO::getEncashmentTypeId)
                            .collect(Collectors.toSet());
                }
                List<Object> maximumNumberOfTransactionLimitList = tmEncashmentTypeRepository.getMaximumNumberOfTransactionsLimit(encashmentRequestTO.getOrganizationId(), true, encashmentTypeIds);
                Integer totalActiveEncashmentTransactionCount= tmEmployeeEncashmentRepository.getTotalActiveEncashmentTransactionCount(encashmentRequestTO.getOrganizationId(), true, encashmentRequestTO.getEncashmentTypeContentId(),encashmentRequestTO.getEmployeeId());
                Integer maximumNumberOfTransactionLimitCal1=0;
                Integer maximumNumberOfTransactionLimitCal2=0;
                Integer maximumNumberOfTransactionLimit=0;
                if (maximumNumberOfTransactionLimitList != null && !maximumNumberOfTransactionLimitList.isEmpty()) {
                    Object maximumTransactionLimitCal1 = null;
                    Object maximumTransactionLimitCal2 = null;
                    if (maximumNumberOfTransactionLimitList.size() > 0) {
                        maximumTransactionLimitCal1 = maximumNumberOfTransactionLimitList.get(0);
                    }
                    if (maximumNumberOfTransactionLimitList.size() > 1) {
                        maximumTransactionLimitCal2 = maximumNumberOfTransactionLimitList.get(1);
                    }
                    if (maximumTransactionLimitCal1 != null) {
                        maximumNumberOfTransactionLimitCal1 = (Integer) maximumTransactionLimitCal1;
                    }
                    if (maximumTransactionLimitCal2 != null) {
                        maximumNumberOfTransactionLimitCal2 = (Integer) maximumTransactionLimitCal2;
                    }
                }
                maximumNumberOfTransactionLimit= max(maximumNumberOfTransactionLimitCal1,maximumNumberOfTransactionLimitCal2);

                if(maximumNumberOfTransactionLimit!=null && maximumNumberOfTransactionLimit>0) {
                    if (totalActiveEncashmentTransactionCount + 1 > maximumNumberOfTransactionLimit) {
                        return resourceBundle.getString(EncashmentConstant.MAXIMUM_NUMBER_OF_TRANSACTION_LIMIT);
                    }
                }
                    else{
                        return null;
                    }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
        }
    }

    public Float getMaximumBalanceOfCals(List<Object[]> minimumBalanceForEncashmentList){
        Float minimumBalanceCal1=0F;
        Float minimumBalanceCal2=0F;
        Object[] minimumBalanceForEncashmentCal1 =null;
        Object[] minimumBalanceForEncashmentCal2 = null;
        if (minimumBalanceForEncashmentList.size() > 0) {
            minimumBalanceForEncashmentCal1 = minimumBalanceForEncashmentList.get(0);
        }

        if (minimumBalanceForEncashmentList.size() > 1) {
            minimumBalanceForEncashmentCal2 = minimumBalanceForEncashmentList.get(1);
        }
        if (minimumBalanceForEncashmentCal1!=null && minimumBalanceForEncashmentCal1[0] != null) {
            minimumBalanceCal1 += (Float)minimumBalanceForEncashmentCal1[0];
        }
        if (minimumBalanceForEncashmentCal2!=null && minimumBalanceForEncashmentCal2[0] != null) {
            minimumBalanceCal2 += (Float)minimumBalanceForEncashmentCal2[0];
        }
        return max(minimumBalanceCal1,minimumBalanceCal2);
    }

    public String validateForEncashmentAllowedInMonthQuarterYear(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle, Long altGroupID)
    {
        try {
            if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentTypeContentId()!=null) {
                Set<Long> encashmentTypeIds = null;
                if (encashmentRequestTO.getCalendarBlocksTOList() != null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()){
                    encashmentTypeIds = encashmentRequestTO.getCalendarBlocksTOList().stream()
                            .map(CalendarBlocksTO::getEncashmentTypeId)
                            .collect(Collectors.toSet());
                }
            Calendar allowedInMonth = Calendar.getInstance();
            allowedInMonth.set(Calendar.DAY_OF_MONTH, 1);
            Integer allowedMonthStartDateId=DateUtil.getTimeDimensionId(allowedInMonth.getTime());
            allowedInMonth.set(Calendar.DAY_OF_MONTH, allowedInMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
            Integer allowedMonthEndDateId=DateUtil.getTimeDimensionId(allowedInMonth.getTime());

            Calendar allowedInQuarter = Calendar.getInstance();
            Integer currentQuarter = (allowedInQuarter.get(Calendar.MONTH) / 3);
            allowedInQuarter.set(Calendar.DAY_OF_MONTH, 1);
            allowedInQuarter.set(Calendar.MONTH, currentQuarter*3);
            Integer allowedQuarterStartDateId=DateUtil.getTimeDimensionId(allowedInQuarter.getTime());
            allowedInQuarter.add(Calendar.MONTH, 2);
            allowedInQuarter.set(Calendar.DAY_OF_MONTH, allowedInQuarter.getActualMaximum(Calendar.DAY_OF_MONTH));
            Integer allowedQuarterEndDateId=DateUtil.getTimeDimensionId(allowedInQuarter.getTime());

            Calendar allowedInYear = Calendar.getInstance();
            allowedInYear.set(Calendar.DAY_OF_YEAR, 1);
            Integer allowedYearStartDateId=DateUtil.getTimeDimensionId(allowedInYear.getTime());
            allowedInYear.set(Calendar.DAY_OF_YEAR, allowedInYear.getActualMaximum(Calendar.DAY_OF_YEAR));
            Integer allowedYearEndDateId=DateUtil.getTimeDimensionId(allowedInYear.getTime());

            List<CalendarBlocksTO> calendarBlockTOList = encashmentRequestTO.getCalendarBlocksTOList();
            List<Long> encashmentCalenderGroupIDs = new ArrayList<>();
            if (calendarBlockTOList != null) {
                for (CalendarBlocksTO block : calendarBlockTOList) {
                    if (block != null && block.getCalendarGroupId() != null) {
                        encashmentCalenderGroupIDs.add(block.getCalendarGroupId());
                    }
                }
            }
            Float maximumEncashmentAllowedInMonth=0F;
            Float maximumEncashmentAllowedInQuarter=0F;
            Float maximumEncashmentAllowedInYear=0F;
            Object[] maximumEncashmentAllowedCal1 =null;
            Object[] maximumEncashmentAllowedCal2 = null;
            List<Object[]> maximumEncashmentAllowedList = tmEncashmentCalenderGroupRepository.findMaxEncashmentAllowedInMonthQuarterYear(encashmentCalenderGroupIDs, true, altGroupID, encashmentRequestTO.getLeaveTypeId(), encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getTenantId());

            if (maximumEncashmentAllowedList != null && !maximumEncashmentAllowedList.isEmpty() && maximumEncashmentAllowedList.size() > 0) {
                maximumEncashmentAllowedCal1=maximumEncashmentAllowedList.get(0);
            }
            if(maximumEncashmentAllowedList != null && !maximumEncashmentAllowedList.isEmpty() && maximumEncashmentAllowedList.size() > 1){
                maximumEncashmentAllowedCal2=maximumEncashmentAllowedList.get(1);
            }
            Float maximumEncashmentAllowedInMonthCal1=0F;
            Float maximumEncashmentAllowedInMonthCal2=0F;

            if (maximumEncashmentAllowedCal1!=null && maximumEncashmentAllowedCal1[0] != null) {
                maximumEncashmentAllowedInMonthCal1 += (Float)maximumEncashmentAllowedCal1[0];
            }
            if (maximumEncashmentAllowedCal2!=null && maximumEncashmentAllowedCal2[0] != null) {
                maximumEncashmentAllowedInMonthCal2 += (Float)maximumEncashmentAllowedCal2[0];
            }
                maximumEncashmentAllowedInMonth = max(maximumEncashmentAllowedInMonthCal1, maximumEncashmentAllowedInMonthCal2);
                if(maximumEncashmentAllowedInMonth!=null && maximumEncashmentAllowedInMonth>0 ) {
                    List<Object[]> totalEncashmentTakenInMonthDaysHrsList = tmEmployeeEncashmentRepository.getEncashmentTakenCountMonthQuarterYear(encashmentRequestTO.getOrganizationId(),
                            true, encashmentRequestTO.getEmployeeId(), allowedMonthStartDateId, allowedMonthEndDateId);
                    Integer totalEncashmentforMonthInDays = 0;
                    Integer totalEncashmentforMonthInHrs = 0;
                    if (totalEncashmentTakenInMonthDaysHrsList != null && !totalEncashmentTakenInMonthDaysHrsList.isEmpty()) {
                        for (Object[] encashment : totalEncashmentTakenInMonthDaysHrsList) {
                            if (encashment != null) {
                                Integer days = 0;
                                Short hours = 0;
                                if (encashment.length > 0 && encashment[0] != null) {
                                    days = (Integer) encashment[0];
                                    totalEncashmentforMonthInDays += days;
                                }
                                if (encashment.length > 1 && encashment[1] != null) {
                                    hours = (Short) encashment[1];
                                    totalEncashmentforMonthInHrs+= hours;
                                }
                            }
                        }
                    }
                    Float totalEncashmentTakenInMonthDaysHrs= getExactBalance(encashmentRequestTO,totalEncashmentforMonthInDays, totalEncashmentforMonthInHrs);
                if (totalEncashmentTakenInMonthDaysHrs + encashmentRequestTO.getEncashmentNumberOfDays() > maximumEncashmentAllowedInMonth) {
                    return resourceBundle.getString(EncashmentConstant.MAXIMUM_NUMBER_OF_TRANSACTION_LIMIT_WITHIN_MONTH);
                }
            }

            Float maximumEncashmentAllowedInQuarterCal1=0F;
            Float maximumEncashmentAllowedInQuarterCal2=0F;
            if (maximumEncashmentAllowedCal1!=null && maximumEncashmentAllowedCal1[1] != null) {
                maximumEncashmentAllowedInQuarterCal1 += (Float)maximumEncashmentAllowedCal1[1];
            }
            if (maximumEncashmentAllowedCal2!=null && maximumEncashmentAllowedCal2[1] != null) {
                maximumEncashmentAllowedInQuarterCal2 += (Float)maximumEncashmentAllowedCal2[1];
            }
            maximumEncashmentAllowedInQuarter= max(maximumEncashmentAllowedInQuarterCal1,maximumEncashmentAllowedInQuarterCal2);
                if(maximumEncashmentAllowedInQuarter!=null && maximumEncashmentAllowedInQuarter>0 ) {
                    List<Object[]> totalEncashmentTakenInQuarterDaysHrsList = tmEmployeeEncashmentRepository.getEncashmentTakenCountMonthQuarterYear(encashmentRequestTO.getOrganizationId(),
                            true, encashmentRequestTO.getEmployeeId(), allowedQuarterStartDateId, allowedQuarterEndDateId);
                    Integer totalEncashmentforQuarterInDays = 0;
                    Integer totalEncashmentforQuarterInHrs = 0;
                    if (totalEncashmentTakenInQuarterDaysHrsList != null && !totalEncashmentTakenInQuarterDaysHrsList.isEmpty()) {
                        for (Object[] encashment : totalEncashmentTakenInQuarterDaysHrsList) {
                            if (encashment != null) {
                                Integer days = 0;
                                Short hours = 0;
                                if (encashment.length > 0 && encashment[0] != null) {
                                    days = (Integer) encashment[0];
                                    totalEncashmentforQuarterInDays += days;
                                }
                                if (encashment.length > 1 && encashment[1] != null) {
                                    hours = (Short) encashment[1];
                                    totalEncashmentforQuarterInHrs+= hours;
                                }
                            }
                        }
                    }
                    Float totalEncashmentTakenInQuarterDaysHrs= getExactBalance(encashmentRequestTO,totalEncashmentforQuarterInDays, totalEncashmentforQuarterInHrs);
                    if (totalEncashmentTakenInQuarterDaysHrs +  encashmentRequestTO.getEncashmentNumberOfDays() > maximumEncashmentAllowedInQuarter) {
                        return resourceBundle.getString(EncashmentConstant.MAXIMUM_NUMBER_OF_TRANSACTION_LIMIT_WITHIN_QUARTER);
                    }
                }

            Float maximumEncashmentAllowedInYearCal1=0F;
            Float maximumEncashmentAllowedInYearCal2=0F;
            if (maximumEncashmentAllowedCal1!=null && maximumEncashmentAllowedCal1[2] != null) {
                maximumEncashmentAllowedInYearCal1 += (Float)maximumEncashmentAllowedCal1[2];
            }
            if (maximumEncashmentAllowedCal2!=null && maximumEncashmentAllowedCal2[2] != null) {
                maximumEncashmentAllowedInYearCal2 += (Float)maximumEncashmentAllowedCal2[2];
            }
            maximumEncashmentAllowedInYear= max(maximumEncashmentAllowedInYearCal1,maximumEncashmentAllowedInYearCal2);
                if(maximumEncashmentAllowedInYear!=null && maximumEncashmentAllowedInYear>0) {
                    List<Object[]> totalEncashmentTakenInYearDaysHrsList = tmEmployeeEncashmentRepository.getEncashmentTakenCountMonthQuarterYear(encashmentRequestTO.getOrganizationId(),
                            true, encashmentRequestTO.getEmployeeId(), allowedYearStartDateId, allowedYearEndDateId);
                    Integer totalEncashmentforYearInDays = 0;
                    Integer totalEncashmentforYearInHrs = 0;
                    if (totalEncashmentTakenInYearDaysHrsList != null && !totalEncashmentTakenInYearDaysHrsList.isEmpty()) {
                        for (Object[] encashment : totalEncashmentTakenInYearDaysHrsList) {
                            if (encashment != null) {
                                Integer days = 0;
                                Short hours = 0;
                                if (encashment.length > 0 && encashment[0] != null) {
                                    days = (Integer) encashment[0];
                                    totalEncashmentforYearInDays += days;
                                }
                                if (encashment.length > 1 && encashment[1] != null) {
                                    hours = (Short) encashment[1];
                                    totalEncashmentforYearInHrs+= hours;
                                }
                            }
                        }
                    }
                    Float totalEncashmentTakenInYearDaysHrs= getExactBalance(encashmentRequestTO,totalEncashmentforYearInDays, totalEncashmentforYearInHrs);
                    if (totalEncashmentTakenInYearDaysHrs +  encashmentRequestTO.getEncashmentNumberOfDays() > maximumEncashmentAllowedInYear) {
                        return resourceBundle.getString(EncashmentConstant.MAXIMUM_NUMBER_OF_TRANSACTION_LIMIT_WITHIN_YEAR);
                    }
                }
            else{
                return null;
            }
            }

            return null;
            }catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
            }
        }



    public Float getSumBalanceOfCals(List<Object[]> minimumBalanceForEncashmentList){
        Float minimumBalanceCal1=0F;
        Float minimumBalanceCal2=0F;
        Object[] minimumBalanceForEncashmentCal1 =null;
        Object[] minimumBalanceForEncashmentCal2 = null;
        if (minimumBalanceForEncashmentList.size() > 0) {
            minimumBalanceForEncashmentCal1 = minimumBalanceForEncashmentList.get(0);
        }

        if (minimumBalanceForEncashmentList.size() > 1) {
            minimumBalanceForEncashmentCal2 = minimumBalanceForEncashmentList.get(1);
        }
        if (minimumBalanceForEncashmentCal1!=null && minimumBalanceForEncashmentCal1[0] != null) {
            minimumBalanceCal1 += (Float)minimumBalanceForEncashmentCal1[0];
        }
        if (minimumBalanceForEncashmentCal2!=null && minimumBalanceForEncashmentCal2[0] != null) {
            minimumBalanceCal2 += (Float)minimumBalanceForEncashmentCal2[0];
        }
        return (minimumBalanceCal1 + minimumBalanceCal2);
    }
    
    private String validateMaximumEncashmentLimitInCalendarGroup(EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle, Long altGroupID) {
        try {
            if (encashmentRequestTO.getCalendarBlocksTOList() != null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()){
                List<Long> calendrGroupIds = encashmentRequestTO.getCalendarBlocksTOList().stream().map(CalendarBlocksTO::getCalendarGroupId).collect(Collectors.toList());
                List<Object[]> maximumEncashmentLimitList= tmEncashmentCalenderGroupRepository.findMaximumEncashmentLimit(calendrGroupIds,true,altGroupID,encashmentRequestTO.getLeaveTypeId(),encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getTenantId());
                Float maximumEncashmentLimit = 0.0f;
                if (maximumEncashmentLimitList != null && !maximumEncashmentLimitList.isEmpty()) {
                    maximumEncashmentLimit=getSumBalanceOfCals(maximumEncashmentLimitList);
                }
                if (maximumEncashmentLimit != null && maximumEncashmentLimit > 0){
                    Float appliedEncashmentCount = encashmentRequestTO.getEncashmentNumberOfDays();
                    Integer totalEncashmentInDays = 0;
                    Integer totalEncashmentInHrs = 0;
                    List<Object[]> encashmentCountInDaysHrsList = tmEmployeeEncashmentDetailRepository.getEncashmentTakenCountByGroup(encashmentRequestTO.getOrganizationId(), true, calendrGroupIds,encashmentRequestTO.getEmployeeId());
                    if (encashmentCountInDaysHrsList != null && !encashmentCountInDaysHrsList.isEmpty()) {
                        for (Object[] encashment : encashmentCountInDaysHrsList) {
                            if (encashment != null) {
                                Integer days = 0;
                                Short hours = 0;
                                if (encashment.length > 0 && encashment[0] != null) {
                                    days = (Integer) encashment[0];
                                    totalEncashmentInDays += days;
                                }
                                if (encashment.length > 1 && encashment[1] != null) {
                                    hours = (Short) encashment[1];
                                    totalEncashmentInHrs+= hours;
                                }
                            }
                        }
                    }
                    Float totalEncashmentTaken= getExactBalance(encashmentRequestTO,totalEncashmentInDays, totalEncashmentInHrs);
                    if (maximumEncashmentLimit < totalEncashmentTaken + appliedEncashmentCount){
                        return resourceBundle.getString(EncashmentConstant.MAXIMUM_NUMBER_OF_ENCASHMENT_LIMIT_IN_GROUP);
                    } else return null;
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new AppRuntimeException(resourceBundle.getString(IEncashmentErrorConstants.FAILED_VALIDATION_FAILURE));
        }
    }
}
