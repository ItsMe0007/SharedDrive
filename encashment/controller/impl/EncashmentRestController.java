package com.peoplestrong.timeoff.encashment.controller.impl;

import com.peoplestrong.sessionmanagement.interceptor.AuthFilter;
import com.peoplestrong.sessionmanagement.to.SessionInfo;
import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.common.controller.AbstractController;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.exception.AppRuntimeExceptionV2;
import com.peoplestrong.timeoff.common.impl.service.CommonService;
import com.peoplestrong.timeoff.common.io.RestRequest;
import com.peoplestrong.timeoff.common.io.RestResponse;
import com.peoplestrong.timeoff.dataservice.model.leave.HrOrgCountryMapping;
import com.peoplestrong.timeoff.dataservice.model.session.HrEmployee;
import com.peoplestrong.timeoff.dataservice.repo.session.HrEmployeeRepository;
import com.peoplestrong.timeoff.encashment.constant.EncashmentErrorConstants;
import com.peoplestrong.timeoff.encashment.constant.IEncashmentErrorConstant;
import com.peoplestrong.timeoff.encashment.flatTO.NewEncashmentRequestInfo;
import com.peoplestrong.timeoff.encashment.pojo.*;
import com.peoplestrong.timeoff.encashment.pojo.base.AppliedEncashmentListCommonTO;

import com.peoplestrong.timeoff.encashment.pojo.base.EncashmentListResponseTO;
import com.peoplestrong.timeoff.encashment.service.EncModuleConfigurationService;
import com.peoplestrong.timeoff.encashment.service.common.SessionService;
import com.peoplestrong.timeoff.encashment.service.EncashmentService;
import com.peoplestrong.timeoff.encashment.service.impl.EncashmentServiceImpl;
import com.peoplestrong.timeoff.encashment.utils.AppLogger;
import com.peoplestrong.timeoff.formsecurity.to.FormSecurityRequestTO;
import com.peoplestrong.timeoff.formsecurity.to.FormSecurityRequestTOV2;
import com.peoplestrong.timeoff.formsecurity.to.SysCredentialTO;
import com.peoplestrong.timeoff.formsecurity.to.UiFormSecurityTO;
import com.peoplestrong.timeoff.leave.flatTO.LeaveTypeTO;
import com.peoplestrong.timeoff.leave.flatTO.NewLeaveRequestInfo;
import com.peoplestrong.timeoff.leave.pojo.KeyValueTO;
import com.peoplestrong.timeoff.leave.pojo.LeaveQuotaTO;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.dataservice.repo.Form.HrFormRolePermissionRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.CountryMappingDao;

import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import com.peoplestrong.timeoff.formsecurity.constant.ITimePayForm;
import com.peoplestrong.timeoff.formsecurity.service.IFormSecurityService;
import com.peoplestrong.timeoff.formsecurity.util.FormSecurityUtil;
import com.peoplestrong.timeoff.leave.service.LeaveDurationService;
import com.peoplestrong.timeoff.otcompoff.helper.TokenHelper;
import com.peoplestrong.timeoff.translation.service.TranslationService;
import com.peoplestrong.timeoff.workflow.to.TimeOffWorkFlowModel;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(path = AppConstant.PRE_URL + AppConstant.ENCASHMENT_PRE_URL)
@Api(tags = { AppConstant.TagName.ENCASHMENT })
public class EncashmentRestController extends AbstractController {

    private static final AppLogger logger = AppLogger.get(EncashmentRestController.class);

    EncashmentServiceImpl getActorIDAsPerLeaveID=new EncashmentServiceImpl();
    EncashmentServiceImpl getLeaveTaskEncashmentDetail=new EncashmentServiceImpl();
    EncashmentServiceImpl getDelegatedActorId=new EncashmentServiceImpl();

    @Autowired
    private CommonService commonService;
    @Autowired
    private EncashmentService encashmentService;
    @Autowired
    FormSecurityUtil formSecurityUtil;
    @Autowired
    private TranslationService translationService;
    @Autowired
    IFormSecurityService formSecurityService;
    @Autowired
    private CountryMappingDao countryMappingDao;
    @Autowired
    HrFormRolePermissionRepository hrFormRolePermissionRepository;
    @Autowired
    private TokenHelper tokenHelper;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private EncashmentServiceImpl encashmentServiceImpl;
    @Autowired
    private LeaveDurationService leaveDurationService;
    @Autowired
    private HrEmployeeRepository hrEmployeeRepository;
    @Autowired
    private EncModuleConfigurationService encModuleConfigurationService;
    
    @ApiOperation("getEncashmentTaskDetail")
    @RequestMapping(path = "/getEncashmentTaskDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppResponse<Object> getEncashmentTaskDetail(@RequestBody EncashmentTaskRequestTO restRequest) throws Exception {
        EncashmentTaskDetailRequestTO apiResponseObj = null;
        String errorMessage = null;
        ResourceBundle resourceBundle = null;
        Boolean isValidRequest = Boolean.FALSE;
        Boolean abscondedEmp = Boolean.FALSE;
        try {
            UserInfo userInfo = sessionService.getUserInfo();

            if (userInfo != null) {
                if (userInfo.getEmployeeId() == null) {
                    return AppResponse.error("Invalid Request");
                }
            } else {
                return AppResponse.error("Invalid Request");
            }

            resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(userInfo.getBundleName()));

            apiResponseObj = encashmentService.getLeaveTaskEncashmentDetail(userInfo,restRequest);
            int l1ManagerId = apiResponseObj.getEncashmentListingTo().getL1ManagerID() == null ? 0 : apiResponseObj.getEncashmentListingTo().getL1ManagerID();
            int l2ManagerId = apiResponseObj.getEncashmentListingTo().getL2ManagerID() == null ? 0 : apiResponseObj.getEncashmentListingTo().getL2ManagerID();
            int hrManagerId = apiResponseObj.getEncashmentListingTo().getHrManagerID() == null ? 0 : apiResponseObj.getEncashmentListingTo().getHrManagerID();

            if (apiResponseObj != null && apiResponseObj.getEncashmentListingTo() != null && apiResponseObj.getEncashmentListingTo().getEmployeeID() != null
                    && ((apiResponseObj.getEncashmentListingTo().getEmployeeID().equals(userInfo.getEmployeeId())) || (userInfo.getEmployeeId() == l1ManagerId)
                    || (userInfo.getEmployeeId() == l2ManagerId) || (userInfo.getEmployeeId() == hrManagerId))) {
                isValidRequest = Boolean.TRUE;
            } else {
                long contextInstanceId = restRequest.getEncashmentId().longValue();
                List<Integer> actorId = encashmentService.getActorIDAsPerEncashmentID(contextInstanceId, userInfo.getTenantId());
                if (actorId != null && !actorId.isEmpty() && actorId.get(0) != null && Objects.equals(userInfo.getUserId(), actorId.get(0))) {
                    isValidRequest = Boolean.TRUE;
                }
                if (!isValidRequest) {
                    List<Integer> delegatedActorId = encashmentService.getDelegatedActorId(contextInstanceId, userInfo.getTenantId());
                    if (delegatedActorId != null && !delegatedActorId.isEmpty() && delegatedActorId.get(0) != null && actorId.get(0) != null && Objects.equals(userInfo.getUserId(), delegatedActorId.get(0))) {
                        isValidRequest = Boolean.TRUE;
                    }
                }
                if (!isValidRequest) {
                    Long hrFormID = formSecurityService.getHrFormIdFromForm(ITimePayForm.MGR_INITIATED_LEAVE_FORM_NAME, userInfo.getOrganizationId());
                    List<Integer> roleIds = hrFormRolePermissionRepository.getRoleIDs(hrFormID);
                    List<Integer> commonRoleList = userInfo.getRoleList().stream().filter(roleIds::contains).collect(toList());
                    if (commonRoleList != null && !commonRoleList.isEmpty() && commonRoleList.size() > 0) {
                        isValidRequest = Boolean.TRUE;
                    }
                }
            }
            if (!isValidRequest) {
                return AppResponse.error("Invalid Request");
            }

            int orgId = userInfo.getOrganizationId();
            int bundleId = commonService.getBundleId(userInfo.getBundleName(), orgId);
            List<EncashmentListingTo> list = new ArrayList<>();
            list.add(apiResponseObj.getEncashmentListingTo());

        } catch (AppRuntimeException e) {
            return AppResponse.error(e.getDisplayMsg());
        } catch (Exception e) {
            if(resourceBundle != null)
                errorMessage = resourceBundle.getString(EncashmentErrorConstants.ENCASHMENTTASKDETAIL_API_NOTRESPONDING);
            return AppResponse.error(errorMessage);
        }
        return AppResponse.success("Success Message",apiResponseObj);
    }
    
    @ApiOperation("getEncashmentApprovalHistory")
    @RequestMapping(path = "/encashment-approval-history", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppResponse<Object> getEncashmentApprovalHistory(@RequestBody EncashmentTaskRequestTO restRequest)
            throws Exception
    {
        List<EncashmentApprovalHistoryTO> apiResponseObj = null;
        String errorMessage = null;
        ResourceBundle resourceBundle = null;
        UserInfo userInfo = sessionService.getUserInfo();
        Integer userid = null;
        Integer l1ManagerID = null;
        Integer l2ManagerID = null;
        Integer hrManagerID = null;
        Boolean isValidRequest = Boolean.FALSE;
        
        try {
            resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME,
                    new Locale(userInfo.getBundleName()));
            Long encashmentId = restRequest.getEncashmentId();
            apiResponseObj = encashmentService.getApprovalEncashmentHistory(encashmentId, userInfo.getTenantId(),
                    userInfo.getOrganizationId());
            /* Validating the relationship between the userid linked to the encashmentId and the userid linked to the AuthToken.
            1. Encashment applied by employee, and employee clicking on "View Detail" button present on ""My encashment" page
            2. Proxy encashment applied by manager, and manager watching the employee encashment history by clicking on "View Detail" button
             */
            if(apiResponseObj != null && apiResponseObj.size()>0 && apiResponseObj.get(0) != null && apiResponseObj.get(0).getUserId() != null && apiResponseObj.get(0).getUserId() == userInfo.getUserId())
            {
                isValidRequest = Boolean.TRUE;
            }
            else {
                List<Object[]> userId = encashmentService.getUserIdFromTmemployeeEncashment(encashmentId);
                for(Object[] obj : userId)
                {
                    userid = obj[0] == null ? 0 :(Integer) obj[0];
                    l1ManagerID = obj[1] == null ? 0 :(Integer) obj[1];
                    l2ManagerID = obj[2] == null ? 0 :(Integer) obj[2];
                    hrManagerID = obj[3] == null ? 0 :(Integer) obj[3];

                }
                /* Validating the relationship between the userid linked to the encashmentId and the userid linked to the AuthToken.
                   1. Proxy encashment applied by manager, and employee clicking on "View Detail" button present on ""My encashment" page
             */
                if(userId != null && userid == userInfo.getUserId() || l1ManagerID == userInfo.getEmployeeId() || l2ManagerID == userInfo.getEmployeeId() || hrManagerID == userInfo.getEmployeeId()) {
                    isValidRequest = Boolean.TRUE;
                }
                else
                {
                    // Handling cases where Maneger (other then L1, L2 , hr manager) having rights for approving employee encashment
                    long contextInstanceId = encashmentId;
                    List<Integer> actorId = encashmentService.getActorIDAsPerEncashmentID(contextInstanceId, userInfo.getTenantId());
                    if (actorId != null && !actorId.isEmpty() && actorId.get(0) != null && Objects.equals(userInfo.getUserId(), actorId.get(0))) {

                        isValidRequest = Boolean.TRUE;
                    }
                    if (!isValidRequest)
                    {
                        Long hrFormID = formSecurityService.getHrFormIdFromForm(ITimePayForm.MGR_INITIATED_LEAVE_FORM_NAME, userInfo.getOrganizationId());
                        List<Integer> roleIds = hrFormRolePermissionRepository.getRoleIDs(hrFormID);
                        List<Integer> commonRoleList = userInfo.getRoleList().stream().filter(roleIds::contains).collect(toList());
                        if(commonRoleList !=null && !commonRoleList.isEmpty() && commonRoleList.size() > 0)
                        {
                            isValidRequest = Boolean.TRUE;
                        }
                    }
                }
            }
            if(!isValidRequest)
            {
                return AppResponse.error("Invalid Request");
            }
        }
        catch (AppRuntimeException e) {
            return AppResponse.error(e.getDisplayMsg());
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            if (resourceBundle != null)
                errorMessage = resourceBundle.getString(EncashmentErrorConstants.APPROVAL_HISTORY_API_ERROR);
            else errorMessage = e.getMessage();
            return AppResponse.error(errorMessage);
        }
        return AppResponse.success("Success Message",apiResponseObj);

    }

    @ApiOperation("getFormSecurityDetailsV2")
    @RequestMapping(path = "/formsecurity/formConfiguration/v2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<?> getFormConfigurationV2(HttpServletRequest httpServletRequest, @RequestBody RestRequest<FormSecurityRequestTOV2> request)
            throws Exception
    {
        Map<String, String[]> securityMap = null;
        Map<String, UiFormSecurityTO> formSecurityMap = null;
        Set<Integer> roleListByEntity = null;
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            FormSecurityRequestTOV2 securityData = request.getObject();
            if (securityData == null) {
                throw new AppRuntimeException("Security data not found.");
            } else if (securityData.formName == null) {
                throw new AppRuntimeException("FormName is missing.");
            }
            roleListByEntity=commonService.getUserRoleListByEntity(userInfo.getUserId(),userInfo.getTenantId(),securityData.entityID);
            SysCredentialTO sysCredentialTO = new SysCredentialTO(securityData.formName,roleListByEntity, userInfo.getOrganizationId(), userInfo.getTenantId(),
                    securityData.stageID, securityData.tableName, securityData.instanceID, userInfo.getUserId(), securityData.bundle);
            sysCredentialTO.setPlatform(securityData.platform);
            sysCredentialTO.setCountryId(getCountryIDForReportee(securityData, userInfo));
            sysCredentialTO.setEntityID(securityData.entityID);
            formSecurityMap = formSecurityService.getFormConfiguration(sysCredentialTO);
            if (formSecurityMap == null) {
                throw new AppRuntimeException("Exception in fetching form security.");
            }
            securityMap = formSecurityUtil.processSecurityMapIntoRepsonse(formSecurityMap, sysCredentialTO);
        } catch (AppRuntimeException e) {
            return RestResponse.build().add("data", securityMap).error("Request submission failed", e.getDisplayMsg());
        } catch (AppRuntimeExceptionV2 exc) {
            return RestResponse.build().add("data", null).errorData("Request submission failed", exc.getDisplayMsg());
        } catch (Exception e) {
            return RestResponse.build().add("data", securityMap).error("Request failed", e.getMessage());
        }
        return RestResponse.build().add("data", securityMap).info("Success");
    }

    private Long getCountryIDForReportee (FormSecurityRequestTOV2 securityData, UserInfo userInfo) {
        Long selectedCountryID = securityData.getSelectedCountryID();
        if (selectedCountryID != null && selectedCountryID != 0) {
            return selectedCountryID;
        }
        Integer userID = securityData.getUserID();
        HrEmployee hrEmployee = hrEmployeeRepository.findByUserIdAndOrganizationId(userID, userInfo.getOrganizationId());
        if (hrEmployee != null && hrEmployee.getCountryID() != null) {
            return hrEmployee.getCountryID().longValue();
        } else {
            HrOrgCountryMapping country = countryMappingDao.getDefaultCountry(userInfo.getOrganizationId());
            return country.getCountryID().longValue();
        }
    }
    
    @ApiOperation("getFormSecurityDetails")
    @RequestMapping(path = "/formsecurity/formConfiguration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<?> getFormConfiguration(HttpServletRequest httpServletRequest, @RequestBody RestRequest<FormSecurityRequestTO> request)
            throws Exception
    {
        Map<String, String[]> securityMap = null;
        Map<String, UiFormSecurityTO> formSecurityMap = null;
        Set<Integer> roleListByEntity = null;
        try {
            UserInfo userInfo =  sessionService.getUserInfo();
            FormSecurityRequestTO securityData = request.getObject();
            if (securityData == null) {
                
                throw new AppRuntimeException("Security data not found.");
            } else if (securityData.formName == null) {
                throw new AppRuntimeException("FormName is missing.");
            }
            roleListByEntity=commonService.getUserRoleListByEntity(userInfo.getUserId(),userInfo.getTenantId(),securityData.entityID);
            SysCredentialTO sysCredentialTO = new SysCredentialTO(securityData.formName, roleListByEntity, userInfo.getOrganizationId(), userInfo.getTenantId(),
                    securityData.stageID, securityData.tableName, securityData.instanceID, userInfo.getUserId(), securityData.bundle);
            sysCredentialTO.setPlatform(securityData.platform);
            sysCredentialTO.setCountryId(getCountryID(userInfo));
            sysCredentialTO.setEntityID(securityData.entityID);
            formSecurityMap = formSecurityService.getFormConfiguration(sysCredentialTO);
            if (formSecurityMap == null) {
                throw new AppRuntimeException("Exception in fetching form security.");
            }
            securityMap = formSecurityUtil.processSecurityMapIntoRepsonse(formSecurityMap, sysCredentialTO);
        } catch (AppRuntimeException e) {
            return RestResponse.build().add("data", securityMap).error("Request submission failed", e.getDisplayMsg());
        } catch (AppRuntimeExceptionV2 exc) {
            return RestResponse.build().add("data", null).errorData("Request submission failed", exc.getDisplayMsg());
        }catch (Exception e) {
            return RestResponse.build().add("data", securityMap).error("Request failed", e.getMessage());
        }
        return RestResponse.build().add("data", securityMap).info("Success");
    }
    
    private Long getCountryID (com.peoplestrong.timeoff.leave.pojo.UserInfo userInfo) {
        if (userInfo.getCountryID() != null) {
            return userInfo.getCountryID();
        } else {
            HrOrgCountryMapping country = countryMappingDao.getDefaultCountry(userInfo.getOrganizationId());
            return country.getCountryID().longValue();
        }
    }
    
    @ApiOperation("newEncashmentRequestData")
    @RequestMapping(path = "/newEncashmentRequestInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppResponse<Object> getNewEncashmentRequestInfo(@RequestBody EncashmentEmployeeInfoTO encashmentEmployeeInfoTO)throws Exception{
        AppResponse<Object> appResponse=null;
        NewEncashmentRequestInfo apiResponseObj = null;
        try {
            com.peoplestrong.timeoff.leave.pojo.UserInfo userInfo = sessionService.getUserInfo();
            if(encashmentEmployeeInfoTO != null && encashmentEmployeeInfoTO.getUserId() != null && encashmentEmployeeInfoTO.employeeId != null && encashmentEmployeeInfoTO.getEmployeeId() != 0 && encashmentEmployeeInfoTO.getUserId() != 0)
                apiResponseObj = encashmentService.getNewEncashmentRequestInfo(userInfo.getOrganizationId(),encashmentEmployeeInfoTO.getEmployeeId(),userInfo.getBundleName());
            else
                apiResponseObj = encashmentService.getNewEncashmentRequestInfo(userInfo.getOrganizationId(),userInfo.getEmployeeId(),userInfo.getBundleName());
            appResponse = AppResponse.success("Success Message",apiResponseObj);
        } catch (AppRuntimeException e) {
            logger.error(logger.getShortLog(e), e);
            appResponse = AppResponse.error(e.getDisplayMsg());
        } catch (Exception e) {    logger.error("Something went wrong again!!!", e);
            appResponse = AppResponse.unknownError();
        }
        return appResponse;
    }
    
    
    @ApiOperation("getStageActionList")
    @RequestMapping(path = "/stageActions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<Object> getStageActionsInfo(HttpServletRequest httpServletRequest, @RequestBody RestRequest<TimeOffWorkFlowModel> restRequest)
            throws Exception
    {
        AppResponse<Object> appResponse;
        List<TimeOffWorkFlowModel> actionJsonList = null;
        TimeOffWorkFlowModel request = restRequest.getObject();
        com.peoplestrong.timeoff.leave.pojo.UserInfo userInfo = sessionService.getUserInfo();
        actionJsonList = encashmentService.getStageActionList(request, userInfo);
        if (actionJsonList != null)
            return RestResponse.build().add("actionJsonList", actionJsonList).info("Success");
        else
            return RestResponse.build().add("actionJsonList", null).error("Request Failed.", "No action buttons tagged with " + request.getStageType() + " stage.");
    }
    
    @RequestMapping(path = "/durationTypes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<Object> getdurationTypes(HttpServletRequest httpServletRequest, @RequestBody RestRequest<Void> restRequest)
            throws Exception
    {
        Map<String, KeyValueTO> durationSysTypeMap = null;
        String errorMessage = null;
        ResourceBundle resourceBundle = null;
        try {
            com.peoplestrong.timeoff.leave.pojo.UserInfo userInfo = sessionService.getUserInfo();
            resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(userInfo.getBundleName()));
            SessionInfo sessionInfo = AuthFilter.threadLocal.get();
            Integer employeeEntityId = sessionInfo.getUserDetails().getEntityId();
            durationSysTypeMap = leaveDurationService.getDurationSysTypeMap(userInfo.getOrganizationId(), userInfo.getBundleName(), employeeEntityId);
            
            if(durationSysTypeMap != null && !durationSysTypeMap.isEmpty()) {
                
                List<KeyValueTO> keyValueList = new ArrayList<KeyValueTO>();
                
                for(Map.Entry<String,KeyValueTO> key: durationSysTypeMap.entrySet()) {
                    keyValueList.add(key.getValue());
                }
                
                int bundleId = commonService.getBundleId(userInfo.getBundleName(), userInfo.getOrganizationId());
                
                keyValueList = (List<KeyValueTO>) translationService.translateLabels(keyValueList, bundleId, userInfo.getOrganizationId());
                
                KeyValueTO updatedTO;
                for(Map.Entry<String,KeyValueTO> key: durationSysTypeMap.entrySet()) {
                    KeyValueTO keyValueTO = key.getValue();
                    Optional<KeyValueTO> updatedTOOptional = keyValueList.stream().filter(TO -> TO.getKey().equals(keyValueTO.getKey())).findFirst();
                    if (updatedTOOptional.isPresent()){
                        updatedTO = updatedTOOptional.get();
                        durationSysTypeMap.put(key.getKey(), updatedTO);
                    }
                }
                
            }
            
        } catch (AppRuntimeException e) {
            return RestResponse.build().add("data", durationSysTypeMap).error("Request failed", e.getDisplayMsg());
        } catch (Exception e) {
            if (resourceBundle != null)
                errorMessage = resourceBundle.getString(IEncashmentErrorConstant.LEAVEDURATIONTYPE_API_NOTRESPONDING);
            else errorMessage = e.getMessage();
            return RestResponse.build().add("data", durationSysTypeMap).error("Request failed", errorMessage);
        }
        return RestResponse.build().add("data", durationSysTypeMap).info("Success");
    }

    @ApiOperation("getAppliedEncashmentList")
    @RequestMapping(path = "/appliedEncashmentList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppResponse<Object> getAppliedEncashmentList(@RequestBody AppliedEncashmentListCommonTO restRequest)
            throws Exception {

        //restRequest parameter is included to extract user information
        UserInfo userInfo = sessionService.getUserInfo();

        // Validating the encashment list common TO
            if (restRequest == null){
            return AppResponse.error("Invalid Request");
        }
        // Set defaults if AppliedEncashmentListCommonTO is missing
        if (restRequest == null || restRequest.getEmployeeId() == null) {
            restRequest = new AppliedEncashmentListCommonTO();
            restRequest.setEmployeeId(userInfo.getEmployeeId());
            restRequest.setUserId(userInfo.getUserId());
        }

        /* Here, userInfo fetched from user_token is compared with that of auth_token */
        if (userInfo == null || userInfo.getEmployeeId()==null
                || userInfo.getUserId()==0
                || userInfo.getOrganizationId()==0) {
            return AppResponse.error("Invalid Request");
        }
        // Fetching the applied encashment list using the service
        List<EncashmentListResponseTO> apiResponseObj = encashmentService.getAppliedEncashmentList(userInfo, restRequest);

        // Getting the bundle ID for translation
        int bundleId = commonService.getBundleId(userInfo.getBundleName(), userInfo.getOrganizationId());

        // Processing workflow history IDs for translation of status messages
        if (apiResponseObj != null && !apiResponseObj.isEmpty()) {
            List<Long> workflowHistoryIdList = new ArrayList<>();
            for (EncashmentListResponseTO to : apiResponseObj) {
                workflowHistoryIdList.add(to.getWorkflowHistoryID());
            }
            if (bundleId != 1) {
                Map<Long, String> translatedWorkflowStatusMap = translationService.getTranslatedStatusValueForWorkFlowHistory(workflowHistoryIdList,
                        bundleId, userInfo.getOrganizationId(), "statusMessage");
                for (EncashmentListResponseTO to : apiResponseObj) {
                    to.setStatusMessage(translatedWorkflowStatusMap.get(to.getWorkflowHistoryID()));
                }
            }
        }

        return AppResponse.success("Success Message",apiResponseObj);

    }

    @ApiOperation("getApprovalHistorySecurityDetails")
    @RequestMapping(path = "/formsecurity/approval-history-security", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<?> getApprovalHistorySecurity(HttpServletRequest httpServletRequest, @RequestBody RestRequest<FormSecurityRequestTO> request)
            throws Exception
    {
        Map<String, String[]> securityMap = null;
        Map<String, UiFormSecurityTO> formSecurityMap = null;
        Set<Integer> roleListByEntity = null;
        try {
            com.peoplestrong.timeoff.common.io.UserInfo userInfo = super.getUserInfo(request);
            FormSecurityRequestTO securityData = request.getObject();
            if (securityData == null) {
                throw new AppRuntimeException("Security data not found.");
            } else if (securityData.formName == null) {
                throw new AppRuntimeException("FormName is missing.");
            }
            roleListByEntity=commonService.getUserRoleListByEntity(userInfo.getUserId(),userInfo.getTenantId(),securityData.entityID);
            SysCredentialTO sysCredentialTO = new SysCredentialTO(securityData.formName, roleListByEntity , userInfo.getOrganizationId(), userInfo.getTenantId(),
                    securityData.stageID, securityData.tableName, securityData.instanceID, userInfo.getUserId(), securityData.bundle);
            sysCredentialTO.setPlatform(securityData.platform);
            sysCredentialTO.setCountryId(userInfo.getCountryID());
            sysCredentialTO.setFormName(ITimePayForm.LEAVE_APPROVER_FORM_NAME);
            sysCredentialTO.setEntityID(securityData.entityID);
            formSecurityMap = formSecurityService.getFormConfiguration(sysCredentialTO);
            if (formSecurityMap == null) {
                throw new AppRuntimeException("Exception in fetching form security.");
            }
            sysCredentialTO.setFormName(ITimePayForm.LEAVE_APPROVER_HISTORY_FORM_NAME);
            securityMap = formSecurityUtil.processSecurityMapIntoRepsonse(formSecurityMap, sysCredentialTO);
        } catch (AppRuntimeException e) {
            return RestResponse.build().add("data", securityMap).error("Request submission failed", e.getDisplayMsg());
        } catch (Exception e) {
            return RestResponse.build().add("data", securityMap).error("Request failed", e.getMessage());
        }
        return RestResponse.build().add("data", securityMap).info("Success");
    }

    /**
     * DIY-2 END
     */

    private NewLeaveRequestInfo mapLeaveQuotaWithLeaveType(NewLeaveRequestInfo obj) {

        List<LeaveQuotaTO> quotaList = obj.getLeaveQuotaTOList();
        List<LeaveTypeTO> typeList = obj.getLeaveTOList();

        if(quotaList != null) {
            for (LeaveQuotaTO leaveQuota : quotaList) {
                Optional<LeaveTypeTO> result = typeList.stream().filter(
                        type -> type.getLeaveTypeId() == leaveQuota.getLeaveTypeId()).findFirst();

                if(result.isPresent()) {
                    leaveQuota.setLeaveTypeCode(result.get().getLeaveTypeCode());
                }

            }
        }
        return obj;
    }
    
    @ApiOperation("getEncashmentTasksDetails")
    @RequestMapping(path = "/getEncashmentTasksDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppResponse<Object> getEncashmentTaskDetails(@RequestBody List<EncashmentTaskRequestTO> restRequest)
            throws Exception
    {
        List<EncashmentTaskDetailRequestTO> apiResponseObj = null;
        String errorMessage = null;
        ResourceBundle resourceBundle = null;

        try {
            UserInfo userInfo = sessionService.getUserInfo();

            if (userInfo != null) {
                if (userInfo.getEmployeeId() == null) {
                    return AppResponse.error("Invalid Request");
                }
            } else {
                return AppResponse.error("Invalid Request");
            }
            resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(userInfo.getBundleName()));

            apiResponseObj = encashmentService.getEncashmentTasksDetails(restRequest, userInfo);

            int orgId = userInfo.getOrganizationId();
            int bundleId = commonService.getBundleId(userInfo.getBundleName(), orgId);

            for (EncashmentTaskDetailRequestTO detail : apiResponseObj) {
                List<EncashmentListingTo> list = new ArrayList<EncashmentListingTo>();
                list.add(detail.getEncashmentListingTo());
            }

        } catch (AppRuntimeException e) {
            return AppResponse.error(e.getDisplayMsg());
        } catch (Exception e) {
            if (resourceBundle != null)
                errorMessage = resourceBundle.getString(EncashmentErrorConstants.ENCASHMENTTASKDETAILS_API_NOTRESPONDING);
            else errorMessage = e.getMessage();
            return AppResponse.error(errorMessage);
        }
        return AppResponse.success("Success", apiResponseObj);
    }
    
    @ApiOperation("getMultiCalendarModuleConfig")
    @RequestMapping(path = "/isMultiCalendarEnabled", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppResponse<Boolean> getEncashmentTaskDetails(){
        Boolean isMultiCalendarEnabled = null;
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            
            if (userInfo != null) {
                if (userInfo.getEmployeeId() == null) {
                    return AppResponse.error("Invalid Request");
                }
            } else {
                return AppResponse.error("Invalid Request");
            }
            
            isMultiCalendarEnabled = encModuleConfigurationService.getIsMultiCalendarEnabledModuleConfig(userInfo.getOrganizationId());
            
        } catch (AppRuntimeException e) {
            return AppResponse.error(e.getDisplayMsg());
        } catch (Exception e) {
            return AppResponse.error(e.getMessage() == null ? "Invalid Request" : e.getMessage());
        }
        return AppResponse.success("Success", isMultiCalendarEnabled);
    }

}
