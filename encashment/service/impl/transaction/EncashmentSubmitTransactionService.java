package com.peoplestrong.timeoff.encashment.service.impl.transaction;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.peoplestrong.timeoff.common.io.UserInfo;
import com.peoplestrong.timeoff.dataservice.model.encashment.*;
import com.peoplestrong.timeoff.dataservice.model.leave.*;
import com.peoplestrong.timeoff.dataservice.repo.encashment.*;
import com.peoplestrong.timeoff.dataservice.repo.leave.*;
import com.peoplestrong.timeoff.dataservice.repo.session.HrEmployeeRepository;
import com.peoplestrong.timeoff.encashment.validation.impl.EncashmentValidationService;
import com.peoplestrong.timeoff.formsecurity.constant.IEncashmentForm;
import com.peoplestrong.timeoff.formsecurity.service.IFormSecurityService;
import com.peoplestrong.timeoff.leave.pojo.*;
import com.peoplestrong.timeoff.leave.service.Impl.*;
import com.peoplestrong.timeoff.leave.trasnport.output.LeaveBalanceResponseTO;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.peoplestrong.timeoff.attendance.punchinout.util.constants.TimeManagementConstants;
import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.transport.ProtocolTO;
import com.peoplestrong.timeoff.common.transport.StageTransitionTO;
import com.peoplestrong.timeoff.common.util.DateUtil;
import com.peoplestrong.timeoff.common.util.IConstants;
import com.peoplestrong.timeoff.common.util.PSCollectionUtil;
import com.peoplestrong.timeoff.common.util.RestUtil;
import com.peoplestrong.timeoff.common.util.StringUtil;
import com.peoplestrong.timeoff.common.util.Utils;
import com.peoplestrong.timeoff.common.workflow.WorkflowService;
import com.peoplestrong.timeoff.dataservice.cache.LeaveRedisCache;
import com.peoplestrong.timeoff.dataservice.model.common.SysEntity;
import com.peoplestrong.timeoff.dataservice.model.session.NotificationTaskDataObject;
import com.peoplestrong.timeoff.dataservice.repo.common.CommonRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.SysEntityRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.SysObjectAttributeRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.SysObjectRepository;
import com.peoplestrong.timeoff.dataservice.repo.session.SysUserRepository;
import com.peoplestrong.timeoff.dataservice2.domain.TmEmployeeLeaveView;
import com.peoplestrong.timeoff.dataservice2.domain.TransactionInfo;
import com.peoplestrong.timeoff.encashment.constant.EncashmentConstant;
import com.peoplestrong.timeoff.encashment.constant.IEncashmentErrorConstants;
import com.peoplestrong.timeoff.encashment.helper.EncashmentUtil;
import com.peoplestrong.timeoff.encashment.pojo.CalendarBlocksTO;
import com.peoplestrong.timeoff.encashment.pojo.base.DependentDetailTO;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentRequestTO;
import com.peoplestrong.timeoff.leave.constant.ILeaveErrorConstants;
import com.peoplestrong.timeoff.leave.constant.LeaveConstant;
import com.peoplestrong.timeoff.leave.helper.LeaveUtil;
import com.peoplestrong.timeoff.leave.service.LeaveDurationService;
import com.peoplestrong.timeoff.security.fileUploadSecurity.FileUtil;
import com.peoplestrong.timeoff.workflow.to.ActionResponseTO;
import com.peoplestrong.timeoff.workflow.to.RoleActorTO;
import com.peoplestrong.timeoff.workflow.to.StageRequestTO;
import com.peoplestrong.timeoff.workflow.to.StageResponseTO;
import com.peoplestrong.timeoff.workflow.to.TimeOffWorkFlowModel;
import com.peoplestrong.timeoff.workflow.to.TransitionRequestTO;
import com.peoplestrong.timeoff.workflow.to.TransitionResponseTO;
import com.peoplestrong.timeoff.workflow.to.WorkflowHistoryRequestTO;
import com.peoplestrong.timeoff.workflow.to.WorkflowHistoryResponseTO;

@Transactional(rollbackFor = Exception.class)
@Service
public class EncashmentSubmitTransactionService {

    private static final Logger LOGGER = Logger.getLogger(EncashmentSubmitTransactionService.class.toString());
    
    @Autowired
    Environment environment;
    
    @Autowired
    SysContentServiceImpl sysContentServiceImpl;
    
    @Autowired
    TmLeaveLapseRepository tmLeaveLapseRepository;
    
    @Autowired
    SysWorkflowStageActionRepository sysWorkflowStageActionRepository;
    
    @Autowired
    SysWorkflowStageTypeRepository sysWorkflowStageTypeRepository;
    
    @Autowired
    LeaveQuotaRepository leaveQuotaRepository;
    
    @Autowired
    LeaveDurationService leaveDurationService;
    
    @Autowired
    Environment env;
    
    @Autowired
    SysWorkflowStageActionTypeRepository sysWorkflowStageActionTypeRepository;
    
    @Autowired
    SysWorkflowStageRepository sysWorkflowStageRepository;
    
    @Autowired
    TimeDimensionServiceImpl timeDimensionServiceImpl;
    
    @Autowired
    CompOffLeaveMappingRepository compOffLeaveMappingRepository;
    
    @Autowired
    CommonRepository commonRepo;
    
    @Autowired
    HrEmployeeResignationRepository resignationRepo;
    
    @Autowired
    WorkflowService workflowService;
    
    @Autowired
    LeaveRedisCache leaveRedisCache;
    
    @Autowired
    SysUserRepository sysUserRepository;
    
    @Autowired
    EncashmentHistoryRepository encashmentHistoryRepository;
    
    @Autowired
    TmEncashmentDependentDetailRepository encashmentDependentDetail;
    
    @Autowired
    HrEmployeeResignationHistoryRepository resignationHistoryRepo;
    
    @Autowired
    TmLeaveBalanceRulesRepository leaveBalanceRulesRepository;

    @Autowired
    SysObjectAttributeRepository sysObjectAttributeRepository;

    @Autowired
    SysObjectRepository sysObjectRepository;

    @Autowired
    TmEmployeeEncashmentRepository employeeEncashmentRepository;

    @Autowired
    TmEmployeeEncashmentDetailRepository employeeEncashmentDetailRepository;

    @Autowired
    SysEntityRepository sysEntityRepository;
    
    @Autowired
    EncashmentValidationService encashmentValidationService;
    
    @Autowired
    LeaveDurationTypeRepository leaveDurationTypeRepository;

    @Autowired
    HrEmployeeRepository hrEmployeeRepository;

    @Autowired
    LeaveServiceImpl leaveServiceImpl;
    
    @Autowired
    SysWorkflowTypeRepository sysWorkflowTypeRepository;
    
    @Autowired
    SysWorkflowRepository sysWorkflowRepository;
    
    @Autowired
    CommunicationServiceImpl communicationService;
    
    @Autowired
    IFormSecurityService IformSecurityService;
    
    @Autowired
    TmEncashmentCalenderGroupRepository tmEncashmentCalenderGroupRepository;
    
    @Autowired
    GroupServiceImpl groupServiceImpl;
    
    @Transactional(rollbackFor = Exception.class)
    public Object doEncashmentTransactionNew(EncashmentRequestTO encashmentRequestTO, String serverName) {
        return doEncashmentTransaction(encashmentRequestTO, serverName);
    }

    public Object doEncashmentTransaction(EncashmentRequestTO encashmentRequestTO,
                                          String serverName)
    {
        String errorMessage = null;
        Object resultObject = null;
        ResourceBundle resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(encashmentRequestTO.getBundleName()));
        try {
            TmEmployeeEncashment tmEmployeeEncashment = null;
            TmEmployeeLeaveQuota tmEmployeeLeaveQuota = null;
            Map<String, KeyValueTO> tmLeaveDurationMap = null;
            String ruleOutCome = null;
            TransitionResponseTO transitionResponse = null;
            String nextStageApiUrl = null;

            ruleOutCome = getDecisionTransitionForEncashment(encashmentRequestTO);
            encashmentRequestTO.setRuleOutcome(ruleOutCome);
            serverName = environment.getProperty(AppConstant.HOSTNAME);
            nextStageApiUrl = AppConstant.SERVER_PROTOCOL + serverName + AppConstant.WORKFLOW_PRE_URL + AppConstant.WORKFLOW_NEXT_STAGE_INFO_URL;
            /**
             * <pre>
             * Here below is workflow API Call where next stage information would be fetched
             * </pre>
             */
            transitionResponse = getNextStageInfo(encashmentRequestTO, nextStageApiUrl, serverName);

            List<Object[]> result = commonRepo.getStageTransitionList(transitionResponse.getTransitionId());
            StageTransitionTO transition = null;
            if (result != null && !result.isEmpty()) {
                transition = LeaveUtil.convertToStageTransitionTO(result.get(0));
            }

            SysEntity sysEntity = sysEntityRepository.findByName(EncashmentConstant.ENCASHMENT_ENTITY_NAME);
            Integer entityId = sysEntity.getEntityId();
            if (encashmentRequestTO.getEmployeeEncashmentId() > 0) {
                tmEmployeeEncashment = employeeEncashmentRepository.findByEncashmentID(encashmentRequestTO.getEmployeeEncashmentId());
                if (tmEmployeeEncashment != null) {
                    encashmentRequestTO.setLeaveTypeId(tmEmployeeEncashment.getLeaveTypeID());
                    tmEmployeeEncashment.setModifiedBy(encashmentRequestTO.getLoginUser());
                    tmEmployeeEncashment.setModifiedDate(DateUtil.getCurrentDate());

                    verifyMultipleTab(encashmentRequestTO, tmEmployeeEncashment);
                } else {
                    errorMessage = resourceBundle.getString(ILeaveErrorConstants.NO_LEAVE_RECORD_EXCEPTION);
                    throw new AppRuntimeException(errorMessage);
                }

            }
            List<TmEmployeeEncashmentDetail> tmEmployeeEncashmentDetailList = employeeEncashmentDetailRepository.getTmEmployeeEncashmentDetailByEmployeeEncashmentId(encashmentRequestTO.getEmployeeEncashmentId(),encashmentRequestTO.getOrganizationId());
            HRResignationTO resignedEmpTO = new HRResignationTO();
            Integer employeeEntityId = sysUserRepository.getEntityIDByUserID(encashmentRequestTO.getUserID(), encashmentRequestTO.getOrganizationId());
            if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentRequestConfigTO() != null && encashmentRequestTO.getEncashmentRequestConfigTO().getIsCashable() != null
                    && encashmentRequestTO.getEncashmentRequestConfigTO().getIsCashable()) {
                List<HrEmployeeResignation> resignedEmpList = new ArrayList<HrEmployeeResignation>();
                resignedEmpList = resignationRepo.findByEmployeeIdAndActive(encashmentRequestTO.getEmployeeId(), Boolean.TRUE);
                if (PSCollectionUtil.isNotNullOrEmpty(resignedEmpList)) {
                    resignedEmpTO = getResignationTO(resignedEmpList);
                }
            }
            if (encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(LeaveConstant.WORKFLOW_SUBMIT_ACTION)
                    && (encashmentRequestTO.getStageType() == null || encashmentRequestTO.getStageType().equals(LeaveConstant.WORKFLOW_STAGE_TYPE_INITIAL))) {
                /**
                 * <pre>
                 * ********************************** EMPLOYEE SUBMITTING ENCASHMENT ***********************************<br>
                 *
                 * Employee Submitting Encashment
                 * </pre>
                 */
                tmLeaveDurationMap = leaveDurationService.getDurationSysTypeMap(encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getBundleName(), employeeEntityId);
                tmEmployeeLeaveQuota = getPresentQuota(encashmentRequestTO);
                TmLeaveLapse tmLeaveLapse = getLeaveLapse(encashmentRequestTO, tmEmployeeEncashment);
                tmEmployeeEncashment = prepareTmEmployeeEncashmentEntity(encashmentRequestTO, transitionResponse, tmLeaveLapse);
                resultObject = submitNewEncashmentRequest(encashmentRequestTO, tmEmployeeEncashment, tmEmployeeLeaveQuota, tmLeaveLapse, transitionResponse, tmLeaveDurationMap, serverName, entityId, transition);
            } else if (encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(LeaveConstant.WORKFLOW_APPROVE_ACTION) && encashmentRequestTO.getStageType() != null
                    && encashmentRequestTO.getStageType().equals(LeaveConstant.WORKFLOW_STAGE_TYPE_APPROVAL)) {
                /**
                 * <pre>
                 * ********************************** MANAGER APPROVE PENDING ENCASHMENT ***********************************<br>
                 *
                 * Employee has submitted Encashment -> Manager Approving it
                 * </pre>
                 */
                tmEmployeeEncashment.setManagerComments(encashmentRequestTO.getManagerComments());
                tmEmployeeEncashment.setStatusMessage(transitionResponse.getToMessage());
                tmEmployeeEncashment.setSysWorkflowStageID(transitionResponse.getNextStageId());
                tmEmployeeEncashment.setStageType(transitionResponse.getNextStageType());
                tmEmployeeEncashment.setPreviousStageType(transitionResponse.getPreviousStageType());
                tmEmployeeLeaveQuota = getPresentQuota(encashmentRequestTO);
                updateEncashmentBalanceInQuota(true, tmEmployeeEncashment, tmEmployeeLeaveQuota, encashmentRequestTO);
                resultObject = approveLeaveRequestWorkFlow(encashmentRequestTO, tmEmployeeEncashment, transitionResponse, serverName, entityId, transition, tmEmployeeEncashmentDetailList);

            }
            else if (encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(LeaveConstant.WORKFLOW_WITHDRAW_ACTION) && encashmentRequestTO.getStageType() != null
                    && encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_APPROVAL)) {
                /**
                 * <pre>
                 * ********************************** EMPLOYEE REVERT ENCASHMENT REQUEST *************************************************<br>
                 *
                 * Employee has submitted Encashment -> Employee Reverting it
                 * </pre>
                 */
                tmLeaveDurationMap = leaveDurationService.getDurationSysTypeMap(encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getBundleName(), employeeEntityId);
                Long encashmentID = null;
                Integer contextEntityId= null;
                SysEntity sysEntityLeave = sysEntityRepository.findByName(EncashmentConstant.ENCASHMENT_ENTITY_NAME);
                if (tmEmployeeEncashment != null && tmEmployeeEncashment.getEncashmentID() != null && tmEmployeeEncashment.getEncashmentID() != 0) {
                    encashmentID = tmEmployeeEncashment.getEncashmentID();
                }
                if(sysEntityLeave!=null && sysEntityLeave.getEntityId()!=null && sysEntityLeave.getEntityId()!=0){
                    contextEntityId = sysEntityLeave.getEntityId();
                }
                encashmentRequestTO.setCurrentStageId(commonRepo.getCurrentStageId(encashmentID,contextEntityId));
                tmEmployeeLeaveQuota = getPresentQuota(encashmentRequestTO);
                tmEmployeeEncashment.setSysWorkflowStageID(transitionResponse.getNextStageId());
                tmEmployeeEncashment.setEmployeeWithdrawComments(encashmentRequestTO.getWithdrawlComments());
                tmEmployeeEncashment.setStatusMessage(transitionResponse.getToMessage());
                tmEmployeeEncashment.setStageType(transitionResponse.getNextStageType());
                tmEmployeeEncashment.setPreviousStageType(transitionResponse.getPreviousStageType());
                if (transitionResponse.getNextStageType().equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED)) {
                    tmEmployeeEncashment.setIsActive(false);
                }
                boolean requestBeforeReinstate = false;
                requestBeforeReinstate = encashmentValidationService.noBalanceCreditInReinstate(encashmentRequestTO.getEmployeeId(), encashmentRequestTO.getApplicationDateId(),
                        encashmentRequestTO.getOrganizationId(), resourceBundle);
                resultObject = updateEncashmentDataWithdrawCase(encashmentRequestTO, serverName, tmEmployeeEncashmentDetailList, tmEmployeeEncashment, tmEmployeeLeaveQuota,
                        tmLeaveDurationMap, transitionResponse, entityId, transition, resignedEmpTO, requestBeforeReinstate);
            }
            else if (encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_REJECT_ACTION) && encashmentRequestTO.getStageType() != null
                    && encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_APPROVAL)) {
                /**
                 * <pre>
                 * ********************************** MANAGER REJECT PENDING ENCASHMENT ******************************************<br>
                 *
                 * Employee has submitted Encashment -> Manager Rejecting it
                 * </pre>
                 */
                tmLeaveDurationMap = leaveDurationService.getDurationSysTypeMap(encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getBundleName(), employeeEntityId);
                Long encashmentID = null;
                Integer contextEntityId= null;
                SysEntity sysEntityEncashment = sysEntityRepository.findByName(EncashmentConstant.ENCASHMENT_ENTITY_NAME);
                if(tmEmployeeEncashment!=null && tmEmployeeEncashment.getEncashmentID()!=null && tmEmployeeEncashment.getEncashmentID()!=0) {
                    encashmentID = tmEmployeeEncashment.getEncashmentID();
                }
                if(sysEntityEncashment!=null && sysEntityEncashment.getEntityId()!=null && sysEntityEncashment.getEntityId()!=0){
                    contextEntityId = sysEntityEncashment.getEntityId();
                }
                encashmentRequestTO.setCurrentStageId(commonRepo.getCurrentStageId(encashmentID,contextEntityId));
                tmEmployeeLeaveQuota = getPresentQuota(encashmentRequestTO);
                boolean requestBeforeReinstate = false;
                requestBeforeReinstate = encashmentValidationService.noBalanceCreditInReinstate(encashmentRequestTO.getEmployeeId(), encashmentRequestTO.getApplicationDateId(),
                        encashmentRequestTO.getOrganizationId(), resourceBundle);
                SysWorkflow sysWorkflow = null;
                Map<String, HrContentType> hrcontentMap = sysContentServiceImpl.getHrContentTypesBySysContentType("workflow status", 1);
                Integer entityID = sysUserRepository.getEntityIDByUserID(encashmentRequestTO.getUserID(), encashmentRequestTO.getOrganizationId());
                if (PSCollectionUtil.isMapNotNullOrEmpty(hrcontentMap)) {
                    if (hrcontentMap.containsKey("Active")) {
                        Integer typeId = hrcontentMap.get("Active").getTypeId();
                        SysWorkflowType sysWorkflowType = sysWorkflowTypeRepository.findByWorkFlowType(EncashmentConstant.ENCASHMENT_MODULE_NAME);
                        sysWorkflow = sysWorkflowRepository.findByWorkflowTypeIDAndOrganizationIdAndStatusAndEntityID(sysWorkflowType.getWorkflowTypeId(), encashmentRequestTO.getOrganizationId(),
                                typeId,entityID);
                        
                    }
                }
                Integer formId = IformSecurityService.getSysFormIdFromForm(IEncashmentForm.ENCASHMENT_APPROVER_FORM_NAME, encashmentRequestTO.getOrganizationId());
                SysWorkflowStageType stageType = sysWorkflowStageTypeRepository.findWorkflowStageTypeIDByFormIDAndWorkflowStageTypeAndWorkflowTypeID(formId, "Completed",
                        sysWorkflow.getWorkflowTypeID());
                Integer stageTypeID = stageType.getWorkflowStageTypeId();
                SysWorkflowStageTO stageTO = sysWorkflowStageRepository.findStageIdByStageTypeAndWorkflowID(stageTypeID, sysWorkflow.getWorkflowId());
                transitionResponse.setNextStageId(stageTO.getWorkflowStageID());
                transitionResponse.setNextStageType(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED);
                transition.setWorkflowNextStageType(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED);
                transition.setNextStageID(stageTO.getWorkflowStageID());
                tmEmployeeEncashment.setManagerComments(encashmentRequestTO.getManagerComments());
                tmEmployeeEncashment.setStatusMessage(transitionResponse.getToMessage());
                tmEmployeeEncashment.setSysWorkflowStageID(transitionResponse.getNextStageId());
                tmEmployeeEncashment.setStageType(transitionResponse.getNextStageType());
                tmEmployeeEncashment.setPreviousStageType(transitionResponse.getPreviousStageType());
                tmEmployeeEncashment.setIsActive(false);
                resultObject = updateEncashmentDataRejectCase(encashmentRequestTO, serverName, tmEmployeeEncashmentDetailList, tmEmployeeEncashment, tmEmployeeLeaveQuota,
                        tmLeaveDurationMap, transitionResponse, entityId, transition, resignedEmpTO, requestBeforeReinstate);
            }
            else if ((encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_WITHDRAW_ACTION) && encashmentRequestTO.getStageType() != null
                    && (encashmentRequestTO.getStageType() == null || encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_INITIAL))) ||
                    (encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_SUBMIT_ACTION) && encashmentRequestTO.getStageType() != null
                            && encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_ENCASHMENT_CANCEL_REJECTED))) {
                /**
                 * <pre>
                 * ********************************** EMPLOYEE WITHDRAW APPROVED ENCASHMENT ***************************************<br>
                 *
                 * Employee has submitted Encashment -> Manager approved it - > Employee Withdrawing it
                 * </pre>
                 */
                tmEmployeeEncashment.setStatusMessage(transitionResponse.getToMessage());
                tmEmployeeEncashment.setSysWorkflowStageID(transitionResponse.getNextStageId());
                tmEmployeeEncashment.setEmployeeWithdrawComments(encashmentRequestTO.getWithdrawlComments());
                tmEmployeeEncashment.setStageType(transitionResponse.getNextStageType());
                tmEmployeeEncashment.setPreviousStageType(transitionResponse.getPreviousStageType());
                
                if (transitionResponse.getNextStageType().equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED)) {
                    tmEmployeeEncashment.setIsActive(false);
                    tmLeaveDurationMap = leaveDurationService.getDurationSysTypeMap(encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getBundleName(), employeeEntityId);
                    tmEmployeeLeaveQuota = getPresentQuota(encashmentRequestTO);
                    Integer actionID = commonRepo.getActionID("CANCEL");
                    transition.setActionID(actionID);
                    
                    boolean requestBeforeReinstate = encashmentValidationService.noBalanceCreditInReinstate(encashmentRequestTO.getEmployeeId(), encashmentRequestTO.getApplicationDateId(),
                            encashmentRequestTO.getOrganizationId(), resourceBundle);
                    
                    resultObject = updateEncashmentDataApprovalCancelationApprovalCase(encashmentRequestTO, serverName, tmEmployeeEncashmentDetailList, tmEmployeeEncashment, tmEmployeeLeaveQuota, tmLeaveDurationMap, transitionResponse, entityId, transition, resignedEmpTO, requestBeforeReinstate);
                } else {
                    resultObject = withdrawEncashmentRequestWorkFlow(encashmentRequestTO, tmEmployeeEncashmentDetailList, tmEmployeeEncashment, transitionResponse, serverName, entityId, transition);
                }
                
                
            }
            else if (encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_APPROVE_ACTION) && encashmentRequestTO.getStageType() != null
                    && encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_CANCEL_APPROVAL)) {
                /**
                 * <pre>
                 * ********************************** MANAGER APPROVE ENCASHMENT CANCELLATION REQUEST **********************************<br>
                 *
                 * Employee has submitted Encashment -> Manager approved it -> Employee withdraw it -> Manager is going to approve it
                 * </pre>
                 */
                tmLeaveDurationMap = leaveDurationService.getDurationSysTypeMap(encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getBundleName(), employeeEntityId);
                Long encashmentID = null;
                Integer contextEntityId= null;
                SysEntity sysEntityEncashment = sysEntityRepository.findByName(EncashmentConstant.ENCASHMENT_ENTITY_NAME);
                if(tmEmployeeEncashment!=null && tmEmployeeEncashment.getEncashmentID()!=null && tmEmployeeEncashment.getEncashmentID()!=0) {
                    encashmentID = tmEmployeeEncashment.getEncashmentID();
                }
                if(sysEntityEncashment!=null && sysEntityEncashment.getEntityId()!=null && sysEntityEncashment.getEntityId()!=0){
                    contextEntityId = sysEntityEncashment.getEntityId();
                }
                encashmentRequestTO.setCurrentStageId(commonRepo.getCurrentStageId(encashmentID,contextEntityId));
                tmEmployeeLeaveQuota = getPresentQuota(encashmentRequestTO);
                tmEmployeeEncashment.setManagerComments(encashmentRequestTO.getManagerComments());
                tmEmployeeEncashment.setStatusMessage(transitionResponse.getToMessage());
                tmEmployeeEncashment.setSysWorkflowStageID(transitionResponse.getNextStageId());
                tmEmployeeEncashment.setStageType(transitionResponse.getNextStageType());
                tmEmployeeEncashment.setPreviousStageType(transitionResponse.getPreviousStageType());
                if (transitionResponse.getNextStageType().equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED)) {
                    tmEmployeeEncashment.setIsActive(false);
                }
                Integer actionID = commonRepo.getActionID("CANCEL");
                transition.setActionID(actionID);
                
                boolean requestBeforeReinstate = false;
                requestBeforeReinstate = encashmentValidationService.noBalanceCreditInReinstate(encashmentRequestTO.getEmployeeId(), encashmentRequestTO.getApplicationDateId(),
                        encashmentRequestTO.getOrganizationId(), resourceBundle);
                updateEncashmentBalanceInQuota(false,tmEmployeeEncashment,tmEmployeeLeaveQuota,encashmentRequestTO);
                resultObject = updateEncashmentDataApprovalCancelationApprovalCase(encashmentRequestTO, serverName, tmEmployeeEncashmentDetailList, tmEmployeeEncashment, tmEmployeeLeaveQuota,
                        tmLeaveDurationMap, transitionResponse, entityId, transition, resignedEmpTO, requestBeforeReinstate);
            }
            else if (encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_REJECT_ACTION) && encashmentRequestTO.getStageType() != null
                    && encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_CANCEL_APPROVAL)) {
                /**
                 * <pre>
                 * ********************************** MANAGER REJECT ENCASHMENT CANCELLATION REQUEST ************************************
                 *
                 * Employee has submitted Encashment -> Manager approved it -> Employee withdraw it -> Manager is going to Reject it
                 * </pre>
                 */
                
                SysWorkflow sysWorkflow = null;
                Integer entityID = sysUserRepository.getEntityIDByUserID(encashmentRequestTO.getUserID(), encashmentRequestTO.getOrganizationId());
                Map<String, HrContentType> hrcontentMap = sysContentServiceImpl.getHrContentTypesBySysContentType("workflow status", 1);
                if (PSCollectionUtil.isMapNotNullOrEmpty(hrcontentMap)) {
                    if (hrcontentMap.containsKey("Active")) {
                        Integer typeId = hrcontentMap.get("Active").getTypeId();
                        SysWorkflowType sysWorkflowType = sysWorkflowTypeRepository.findByWorkFlowType(EncashmentConstant.ENCASHMENT_MODULE_NAME);
                        sysWorkflow = sysWorkflowRepository.findByWorkflowTypeIDAndOrganizationIdAndStatusAndEntityID(sysWorkflowType.getWorkflowTypeId(), encashmentRequestTO.getOrganizationId(),
                                typeId,entityID);
                    }
                }
                Integer formId = IformSecurityService.getSysFormIdFromForm(IEncashmentForm.ENCASHMENT_APPROVER_FORM_NAME,encashmentRequestTO.getOrganizationId());
                SysWorkflowStageType stageType = sysWorkflowStageTypeRepository.findWorkflowStageTypeIDByFormIDAndWorkflowStageTypeAndWorkflowTypeID(formId, EncashmentConstant.WORKFLOW_STAGE_TYPE_ENCASHMENT_CANCEL_REJECTED,
                        sysWorkflow.getWorkflowTypeID());
                Integer stageTypeID = stageType.getWorkflowStageTypeId();
                SysWorkflowStageTO stageTO = sysWorkflowStageRepository.findStageIdByStageTypeAndWorkflowID(stageTypeID, sysWorkflow.getWorkflowId());
                transitionResponse.setNextStageId(stageTO.getWorkflowStageID());
                transitionResponse.setNextStageType(EncashmentConstant.WORKFLOW_STAGE_TYPE_ENCASHMENT_CANCEL_REJECTED);
                transition.setWorkflowNextStageType(EncashmentConstant.WORKFLOW_STAGE_TYPE_ENCASHMENT_CANCEL_REJECTED);
                transition.setNextStageID(stageTO.getWorkflowStageID());
                tmEmployeeEncashment.setManagerComments(encashmentRequestTO.getManagerComments());
                tmEmployeeEncashment.setStatusMessage(transitionResponse.getToMessage());
                tmEmployeeEncashment.setSysWorkflowStageID(transitionResponse.getNextStageId());
                tmEmployeeEncashment.setStageType(transitionResponse.getNextStageType());
                tmEmployeeEncashment.setPreviousStageType(transitionResponse.getPreviousStageType());
                resultObject = rejectEncashmentRequestWorkFlow(encashmentRequestTO, tmEmployeeEncashmentDetailList, tmEmployeeEncashment, transitionResponse, serverName, entityId, transition);
            } else if (encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_MANAGER_WITHDRAW_ACTION)
                    && encashmentRequestTO.getStageType() != null && encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_INITIAL)) {
                /**
                 * <pre>
                 * ********************************** MANAGER REVERT ENCASHMENT REQUEST *************************************************<br>
                 *
                 * Employee has submitted Encashment -> Manager Reverting it via Encashment History
                 * </pre>
                 */
                tmLeaveDurationMap = leaveDurationService.getDurationSysTypeMap(encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getBundleName(), employeeEntityId);
                tmEmployeeLeaveQuota = getPresentQuota(encashmentRequestTO);
                updateEncashmentBalanceInQuota(false,tmEmployeeEncashment,tmEmployeeLeaveQuota,encashmentRequestTO);
                tmEmployeeEncashment.setManagerComments(encashmentRequestTO.getManagerComments());
                tmEmployeeEncashment.setStatusMessage(transitionResponse.getToMessage());
                tmEmployeeEncashment.setSysWorkflowStageID(transitionResponse.getNextStageId());
                tmEmployeeEncashment.setStageType(transitionResponse.getNextStageType());
                tmEmployeeEncashment.setPreviousStageType(transitionResponse.getPreviousStageType());
                tmEmployeeEncashment.setIsActive(false);
                boolean requestBeforeReinstate = false;
                requestBeforeReinstate = encashmentValidationService.noBalanceCreditInReinstate(encashmentRequestTO.getEmployeeId(), encashmentRequestTO.getStartDateId(),
                        encashmentRequestTO.getOrganizationId(), resourceBundle);
                
                resultObject = updateEncashmentDataApprovalCancelationApprovalCase(encashmentRequestTO, serverName, tmEmployeeEncashmentDetailList, tmEmployeeEncashment, tmEmployeeLeaveQuota,
                        tmLeaveDurationMap, transitionResponse, entityId, transition, resignedEmpTO, requestBeforeReinstate);
            }
            else {
                errorMessage = resourceBundle.getString(IEncashmentErrorConstants.NO_STAGE_ACTION_EXCEPTION);
                throw new AppRuntimeException(errorMessage);
            }

            // communication code
//            sendworkFlowMails(encashmentRequestTO, tmEmployeeEncashment, transitionResponse, tmEmployeeLeaveQuota,
//                    ((WorkflowHistoryResponseTO) resultObject).getSysUserWorkflowHistorys().get(0), null, tmLeaveDurationMap);
        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            errorMessage = resourceBundle.getString(ILeaveErrorConstants.FAILED_CODE_PROBLEM_FAILURE);
            throw new AppRuntimeException(e, errorMessage);
        }
        NotificationTaskDataObject notificationTaskDataObject = new NotificationTaskDataObject();
        String moduleName = EncashmentConstant.ENCASHMENT_MODULE_NAME;
        Long instanceId = ((WorkflowHistoryResponseTO) resultObject).getSysUserWorkflowHistorys().get(0).getContextInstanceID();
        int stageID = ((WorkflowHistoryResponseTO) resultObject).getSysUserWorkflowHistorys().get(0).getNextStageId();
        if(encashmentRequestTO!=null && encashmentRequestTO.getHrEmployee()!=null){
            String employeeCode = encashmentRequestTO.getHrEmployee().getEmployeeCode();
            String firstName = encashmentRequestTO.getHrEmployeePerson().getGivenName();
            String middleName = encashmentRequestTO.getHrEmployeePerson().getMiddleName();
            String lastName = encashmentRequestTO.getHrEmployeePerson().getFamilyName();
            String employeeName = null;
            employeeName = firstName;
            if (middleName != null && !middleName.equals(IConstants.BLANK_STRING)) {
                employeeName = firstName + IConstants.EMPLTY_STRING + middleName;
            }
            if (lastName != null && !lastName.equals(IConstants.BLANK_STRING)) {
                employeeName = employeeName + IConstants.EMPLTY_STRING + lastName;
            }
            String photoPath=encashmentRequestTO.getHrEmployee().getPhotoPath();
            notificationTaskDataObject.setEmployeeCode(employeeCode);
            notificationTaskDataObject.setEmployeeName(employeeName);
            notificationTaskDataObject.setPhotoPath(photoPath);
        }
        String designation=commonRepo.getManagerDesignation(encashmentRequestTO.getUserID());
        Boolean absconded = commonRepo.getAbscondingStatus(encashmentRequestTO.getEmployeeId(), encashmentRequestTO.getOrganizationId());
        notificationTaskDataObject.setModuleName(moduleName);
        notificationTaskDataObject.setInstanceID(instanceId);
        notificationTaskDataObject.setStageId(stageID);

        notificationTaskDataObject.setDesignation(designation);

        ((WorkflowHistoryResponseTO) resultObject).setNotificationTaskDataObject(notificationTaskDataObject);
        return resultObject;
    }

    private String getDecisionTransitionForEncashment (EncashmentRequestTO encashmentRequestTO)
    {
        String decisicionTransition = null;
        String classToBeLoaded = null;
        Class<?> implClass = null;
        Method method = null;
        Object transitionString = null;
        Object implInstance = null;
        classToBeLoaded = EncashmentConstant.TRANSITION_RULE_IMPL;
        int stageActionId = 0;
        String ruleMethod = null;
        try {
            stageActionId = encashmentRequestTO.getStageActionId();
            ruleMethod = encashmentRequestTO.getRuleOutput();
            implClass = Class.forName(classToBeLoaded);
            method = implClass.getDeclaredMethod(ruleMethod, new Class[]{EncashmentRequestTO.class});
            implInstance = implClass.newInstance();
            transitionString = method.invoke(implInstance, encashmentRequestTO);
        } catch (Exception e) {
            throw new AppRuntimeException(e, EncashmentConstant.STAGE_TRANSITION_EXCEPTION + stageActionId);
        }
        if( transitionString instanceof String ) {
            decisicionTransition = (String) transitionString;
        }
        return decisicionTransition;
    }

    @Transactional(propagation = Propagation.NEVER)
    private TransitionResponseTO getNextStageInfo(EncashmentRequestTO encashmentRequestTO, String url, String host)
    {
        String errorMessage = null;
        TransitionRequestTO transitionRequestTO = null;
        ResourceBundle resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(encashmentRequestTO.getBundleName()));
        RestUtil<TransitionResponseTO> restUtil = new RestUtil<>();
        transitionRequestTO = new TransitionRequestTO();
        transitionRequestTO.setEmployeeId(encashmentRequestTO.getEmployeeId());
        transitionRequestTO.setUserID(encashmentRequestTO.getUserID());
        transitionRequestTO.setStageActionId(encashmentRequestTO.getStageActionId());
        transitionRequestTO.setEntityName(EncashmentConstant.ENCASHMENT_ENTITY_NAME);
        transitionRequestTO.setRuleOutcome(encashmentRequestTO.getRuleOutcome());
        List<Object[]> encashmentSysObjects = sysObjectRepository.findObjectIdentifier("EncashmentRule");
        Integer sysObjectID = null;
        String objectIdentifier = null;
        if (PSCollectionUtil.isNotNullOrEmpty(encashmentSysObjects)) {
            for (Object[] leaveSysObject : encashmentSysObjects) {
                sysObjectID = (Integer) leaveSysObject[0];
                objectIdentifier = (String) leaveSysObject[1];
            }
            transitionRequestTO
                    .setMethodParamMap(prepareSysObjectAttributeList(encashmentRequestTO, sysObjectID, objectIdentifier));
        }

        ProtocolTO protocolTO = new ProtocolTO();
        protocolTO.setTenantID(encashmentRequestTO.getTenantId());
        protocolTO.setOrganizationID(encashmentRequestTO.getOrganizationId());
        transitionRequestTO.setProtocolTO(protocolTO);
        try {
            TransitionResponseTO transitionResponseTO = nextStagePlatformAPI(transitionRequestTO, encashmentRequestTO, null,null,
                    restUtil, url, host);
            if (transitionResponseTO == null
                    || EncashmentConstant.MessageReponse.FAIL.equalsIgnoreCase(transitionResponseTO.getResponseCode())
                    || transitionResponseTO.getTransitionId() == null || transitionResponseTO.getNextStageId() == null
                    || transitionResponseTO.getRoleActorsMap() == null
                    || transitionResponseTO.getRoleActorsMap().isEmpty()) {
                if (transitionResponseTO != null
                        && StringUtil.nonEmptyCheck(transitionResponseTO.getResponseMessage())) {
                    throw new AppRuntimeException(
                            "Workflow API did'nt respond correctly." + transitionResponseTO.getResponseMessage());
                } else {
                    throw new AppRuntimeException("Workflow API did'nt respond correctly");
                }

            }
            return transitionResponseTO;
        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            errorMessage = resourceBundle.getString(IEncashmentErrorConstants.ERROR_OCCURED_GETTING_NEXTSTAGE_INFO);
            LOGGER.log(Level.INFO, e.getMessage(), e);
            throw new AppRuntimeException(errorMessage);
        }
    }

    public Map<String, Object> prepareSysObjectAttributeList(EncashmentRequestTO encashmentRequestTO, Integer sysObjectID, String objectIdentifier)
    {
        Map<String, Object> finalValues = new HashMap<>();

        String leaveTypeCdAttributeIdentifier = (String) sysObjectAttributeRepository.findAttributeIdentifier(sysObjectID, "LEAVETYPECODE");
        String leaveReosonCdAttributeIdentifier = sysObjectAttributeRepository.findAttributeIdentifier(sysObjectID, "LEAVEREASONCODE");
        String leaveDurationDayAttributeIdentifier = sysObjectAttributeRepository.findAttributeIdentifier(sysObjectID, "LEAVEDURATIONDD");
        String leaveDurationHourAttributeIdentifier = sysObjectAttributeRepository.findAttributeIdentifier(sysObjectID, "LEAVEDURATIONHH");
        String leaveDurationMinAttributeIdentifier = sysObjectAttributeRepository.findAttributeIdentifier(sysObjectID, "LEAVEDURATIONMM");
        
        if (null != encashmentRequestTO.getLeaveReasonCd() && !EncashmentUtil.checkIfFromUpload(encashmentRequestTO)) {
            finalValues.put(objectIdentifier + "." + leaveReosonCdAttributeIdentifier, encashmentRequestTO.getLeaveReasonCd());
        }
        if (null != encashmentRequestTO.getLeaveCountInDD()) {
            finalValues.put(objectIdentifier + "." + leaveDurationDayAttributeIdentifier, encashmentRequestTO.getLeaveCountInDD().toString());
        }
        if (null != encashmentRequestTO.getLeaveCountInHH()) {
            finalValues.put(objectIdentifier + "." + leaveDurationHourAttributeIdentifier, encashmentRequestTO.getLeaveCountInHH().toString());
        }
        if (null != encashmentRequestTO.getLeaveCountInMM()) {
            finalValues.put(objectIdentifier + "." + leaveDurationMinAttributeIdentifier, encashmentRequestTO.getLeaveCountInMM().toString());
        }

        return finalValues;
    }

    public String verifyMultipleTab (EncashmentRequestTO encashmentRequestTO, TmEmployeeEncashment tmEmployeeEncashment)
    {

        if( null != encashmentRequestTO.getStageIdWhileScreenLoad() && 0 != encashmentRequestTO.getStageIdWhileScreenLoad()
                && null != tmEmployeeEncashment && null != tmEmployeeEncashment.getSysWorkflowStageID()
                && 0 != tmEmployeeEncashment.getSysWorkflowStageID() ) {
            if( !encashmentRequestTO.getStageIdWhileScreenLoad().equals(tmEmployeeEncashment.getSysWorkflowStageID()) ) {
                /**
                 * Here if child window(After clocking three dots) already
                 * opened in multiple tabs and action taken in one tab, stageId
                 * does not get loaded again from database, hence above
                 * condition works
                 */
                throw new AppRuntimeException(EncashmentConstant.MULTIPLE_ACTIONS_ON_ENCASHMENT_ERROR_MESSAGE);
            } else {
                /**
                 * <pre>
                 * Here if child window(After clocking three dots) is opened
                 * after action taken in first tab, stageId load again from
                 * database and hence become equal on screen and database, hence
                 * it is not useful now, now we have another parameter which is
                 * not refreshed from database leaveRequestTO.getStageType()
                 * hence we will use it compare now
                 * </pre>
                 */
                SysWorkflowStageTO sysWorkflowStageTO = sysWorkflowStageRepository
                        .getStageStatusByStageId(tmEmployeeEncashment.getSysWorkflowStageID());

                SysWorkflowStageType sysWorkflowStageType = sysWorkflowStageTypeRepository
                        .findByWorkflowStageTypeId(sysWorkflowStageTO.getStageTypeID());
                if( null != encashmentRequestTO.getStageType() ) {

                    if( encashmentRequestTO.getStageType().equalsIgnoreCase(LeaveConstant.WORKFLOW_STAGE_TYPE_INITIAL)
                            && sysWorkflowStageType.getWorkflowStageType()
                            .equalsIgnoreCase(LeaveConstant.WORKFLOW_STAGE_TYPE_COMPLETED) ) {

                        /**
                         * <pre>
                         * Employee has submitted Leave -> Manager approved it -> Employee Withdrawing it
                         * This exceptional code is to handle above scenario, other wise above Scenario stopped working
                         * </pre>
                         */

                    } else if( !sysWorkflowStageType.getWorkflowStageType()
                            .equalsIgnoreCase(encashmentRequestTO.getStageType()) ) {
                        throw new AppRuntimeException(LeaveConstant.MULTIPLE_ACTIONS_ON_LEAVE_ERROR_MESSAGE);
                    }

                }
            }
        }
        return null;
    }

    private HRResignationTO getResignationTO (List < HrEmployeeResignation > resignedEmpList)
    {
        HrEmployeeResignation res = null;
        HRResignationTO resTO = null;
        if( PSCollectionUtil.isNotNullOrEmpty(resignedEmpList) ) {
            resTO = new HRResignationTO();
            res = resignedEmpList.get(0);
            if( res != null && res.getResignationId() != null )
                resTO.setResignationId(res.getResignationId());
            if( res != null && res.getLeaveBalanceAsOnLwd() != null && !res.getLeaveBalanceAsOnLwd().isEmpty() )
                resTO.setLeaveBalanceAsOnLwd(res.getLeaveBalanceAsOnLwd());
        }
        return resTO;
    }

    public TmEmployeeLeaveQuota getPresentQuota(EncashmentRequestTO encashmentRequestTO)
    {
        String errorMessage = null;
        TmEmployeeLeaveQuota tmEmployeeLeaveQuota = null;
        if (encashmentRequestTO == null) {
            return null;
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(encashmentRequestTO.getBundleName()));
        List<TmEmployeeLeaveQuota> quotas = null;
        if (encashmentRequestTO.getMultiCalenderEnabled() != null && encashmentRequestTO.getMultiCalenderEnabled()) {
            quotas = leaveQuotaRepository.findByOrganizationIdAndEmployeeIdAndLeaveTypeIdAndActive(
                    encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getEmployeeId(),
                    encashmentRequestTO.getLeaveTypeId(), Boolean.TRUE);
        } else {
            quotas = leaveQuotaRepository.findByOrganizationIdAndEmployeeIdAndLeaveTypeIdAndCalendarIdAndActive(
                    encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getEmployeeId(),
                    encashmentRequestTO.getLeaveTypeId(), encashmentRequestTO.getDefaultCalendarId(), Boolean.TRUE);
        }
        
        if (PSCollectionUtil.isNullOrEmpty(quotas)) {
            errorMessage = resourceBundle.getString(ILeaveErrorConstants.NOQUOTA_PRESENT_LEAVETYPE);
            throw new AppRuntimeException(errorMessage);
        } else if (PSCollectionUtil.size(quotas) > 1) {
            errorMessage = resourceBundle.getString(ILeaveErrorConstants.MULTIPLE_LEAVEQUOTAS_FOR_LEAVETYPE);
            throw new AppRuntimeException(errorMessage);
        } else {
            tmEmployeeLeaveQuota = quotas.get(0);
        }
        return tmEmployeeLeaveQuota;
    }

    private TmLeaveLapse getLeaveLapse(EncashmentRequestTO encashmentRequestTO, TmEmployeeEncashment tmEmployeeEncashment)
    {

        TmLeaveLapse tmLeaveLapse = null;
        ResourceBundle resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(encashmentRequestTO.getBundleName()));
        if (!"CompOff".equalsIgnoreCase(encashmentRequestTO.getLeaveDescription())) {
            return null;
        }

        if (null != tmEmployeeEncashment && null != tmEmployeeEncashment.getEncashmentID()) {
            CompOffLeaveMapping compOffLeaveMapping = compOffLeaveMappingRepository.findByOrganizationIdAndLeaveIdAndEmployeeId(encashmentRequestTO.getOrganizationId(),
                    tmEmployeeEncashment.getEncashmentID().intValue(), encashmentRequestTO.getEmployeeId());
            tmLeaveLapse = tmLeaveLapseRepository.findByLeaveLapseId(compOffLeaveMapping.getLeaveLapseId());
        } else if (0 != encashmentRequestTO.getLeaveLapseId()) {
            tmLeaveLapse = tmLeaveLapseRepository.findByLeaveLapseIdAndActive(encashmentRequestTO.getLeaveLapseId(), Boolean.TRUE);
        } else if (null != encashmentRequestTO.getCompOffDate()) {
            tmLeaveLapse = tmLeaveLapseRepository.findByCompOffDateIdAndActive(DateUtil.getTimeDimensionId(encashmentRequestTO.getCompOffDate()), Boolean.TRUE);
        }

        if (null == tmLeaveLapse) {
            // When user is trying to avail CompOff against wrong date
            String selectedDate = null;
            if (0 != encashmentRequestTO.getLeaveLapseId()) {
                selectedDate = DateUtil.getDateFormat(DateUtil.DATE_FORMAT3, DateUtil.getDateFromTimeDimensionId((int) encashmentRequestTO.getLeaveLapseId()));
            } else if (null != encashmentRequestTO.getCompOffDate()) {
                selectedDate = DateUtil.getDateFormat(DateUtil.DATE_FORMAT3, encashmentRequestTO.getCompOffDate());
            }
            String errorMsg = resourceBundle.getString(ILeaveErrorConstants.NOCOMPOFF_AVAILABLE_FOR_WORKINGON) + encashmentRequestTO.getStartDate()
                    + resourceBundle.getString(ILeaveErrorConstants.PLEASE_SELECT_VALIDDATE);
            throw new AppRuntimeException(errorMsg);
        } else if (null == tmEmployeeEncashment) {
            // When User trying to avail compOff after Lapse Date
            if (tmLeaveLapse.getLapseDateId() != null && tmLeaveLapse.getLapseDateId() > 0 && encashmentRequestTO.getStartDateId() > tmLeaveLapse.getLapseDateId()) {
                String compofLapseDate = DateUtil.getDateFormat(DateUtil.DATE_FORMAT3, DateUtil.getDateFromTimeDimensionId((int) tmLeaveLapse.getLapseDateId()));
                String errorMsg = resourceBundle.getString(ILeaveErrorConstants.YOUCANNOT_APPLY_THISCO_LEAVEAFTER_LAPSEDATE) + compofLapseDate + ".";
                throw new AppRuntimeException(errorMsg);

            }

        }

        return tmLeaveLapse;

    }

    private TmEmployeeEncashment prepareTmEmployeeEncashmentEntity (EncashmentRequestTO encashmentRequestTO, TransitionResponseTO
            transitionResponseTO, TmLeaveLapse tmLeaveLapse) throws Exception {
        TmEmployeeEncashment tmEmployeeEncashment = new TmEmployeeEncashment();
        Integer[] encashmentInDaysHrs = null;
        String encashmentCode = generateEncashmentCode(encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getEmployeeId());
        tmEmployeeEncashment.setEncashmentTaskCode(encashmentCode);
        tmEmployeeEncashment.setLeaveTypeID(encashmentRequestTO.getLeaveTypeId());
        tmEmployeeEncashment.setComments(encashmentRequestTO.getComments());
        tmEmployeeEncashment.setOrganizationId(encashmentRequestTO.getOrganizationId());
        tmEmployeeEncashment.setTenantID(encashmentRequestTO.getTenantId());
        tmEmployeeEncashment.setFileName(encashmentRequestTO.getFileName());
        tmEmployeeEncashment.setUserID(encashmentRequestTO.getUserID());
        tmEmployeeEncashment.setEncashmentTypeContentId(encashmentRequestTO.getEncashmentTypeContentId());
        if( encashmentRequestTO.getAttachmentPath() != null ) {
            tmEmployeeEncashment.setFilePath(FileUtil.getDecryptedFilePath(encashmentRequestTO.getAttachmentPath()));
        }
        tmEmployeeEncashment.setStatusMessage(transitionResponseTO.getToMessage());
        tmEmployeeEncashment.setIsActive(true);
        if( encashmentRequestTO.getRequestType() != null && encashmentRequestTO.getRequestType().equals("ProxyLeave") ) {
            tmEmployeeEncashment.setIsManagerInitiated(true);
        } else {
            tmEmployeeEncashment.setIsManagerInitiated(false);
        }
        tmEmployeeEncashment.setEmployeeID(encashmentRequestTO.getEmployeeId());
        tmEmployeeEncashment.setSysWorkflowStageID(transitionResponseTO.getNextStageId());
        tmEmployeeEncashment.setCreatedDate(DateUtil.getCurrentDate());
        tmEmployeeEncashment.setCreatedBy(encashmentRequestTO.getLoginUser());
        tmEmployeeEncashment.setModifiedBy(encashmentRequestTO.getLoginUser());
        tmEmployeeEncashment.setModifiedDate(DateUtil.getCurrentDate());
        tmEmployeeEncashment.setApplicationDate(timeDimensionServiceImpl.convertDateToTimeDimensionId(DateUtil.getCurrentDate()));
        tmEmployeeEncashment.setL1ManagerID(encashmentRequestTO.getL1ManagerID());
        tmEmployeeEncashment.setL2ManagerID(encashmentRequestTO.getL2ManagerID());
        tmEmployeeEncashment.setHrManagerID(encashmentRequestTO.getHrManagerID());
        tmEmployeeEncashment.setAppliedDate(DateUtil.getCurrentDate());
        tmEmployeeEncashment.setEncashmentCountInDays(0);
        tmEmployeeEncashment.setEncashmentCountInHrs((short) 0);
        if(encashmentRequestTO.getEncashmentNumberOfDays()!=null){
            List<Integer> hourlyWeightageList = leaveDurationTypeRepository.getHourlyWightageList(encashmentRequestTO.getOrganizationId(), true, LeaveConstant.LEAVE_DURATION_FULL);
            Integer hourlyWeightage = null;
            
            Float encashmentCount= Float.valueOf(encashmentRequestTO.getEncashmentNumberOfDays());
            for (Integer weightage : hourlyWeightageList) {
                if (weightage != null && weightage > 0) {
                    hourlyWeightage = weightage;
                }
            }
            if (hourlyWeightage != null)
                tmEmployeeEncashment.setHourlyWeightage(hourlyWeightage.shortValue());
            encashmentInDaysHrs = LeaveUtil.getDaysAndHours(encashmentCount,hourlyWeightage);
            if(encashmentInDaysHrs[0] != null) {
                tmEmployeeEncashment.setEncashmentCountInDays(encashmentInDaysHrs[0]);
            }
            if(encashmentInDaysHrs[1]!=null) {
                tmEmployeeEncashment.setEncashmentCountInHrs((encashmentInDaysHrs[1]).shortValue());
            }
            
            
        }

        return tmEmployeeEncashment;
    }

    private String generateEncashmentCode (Integer organizationID, int employeeId)
    {
        String leaveCode = null;
        String formattedOrgId = getFormattedOrgId(organizationID);
        long timeStamp = new Date().getTime();
        String empIdBase36 = Integer.toString(employeeId, 36).toUpperCase();
        String timeStampBase36 = Long.toString(timeStamp, 36).toUpperCase();
        leaveCode = empIdBase36 + formattedOrgId + timeStampBase36;
        leaveCode = StringUtils.leftPad(leaveCode, 18, '0');
        leaveCode = EncashmentConstant.ENCASHMENT_CODE_START_CHARACTER + leaveCode;
        return leaveCode;
    }

    private String getFormattedOrgId ( int num)
    {
        DecimalFormat df = new DecimalFormat(LeaveConstant.SEQUENCE_FORMAT_PATTERN);
        String number = df.format(num);
        return number;
    }

    private TmEmployeeLeaveView prepareTmEmployeeLeaveEntity2 (EncashmentRequestTO
                                                                       encashmentRequestTO, TransitionResponseTO transitionResponseTO, TmLeaveLapse tmLeaveLapse)
    {
        TmEmployeeLeaveView tmEmployeeLeaveView = new TmEmployeeLeaveView();
        TransactionInfo transactionInfo = new TransactionInfo();

        transactionInfo.setAppliedDate(DateUtil.getDateFormat(DateUtil.DATE_FORMAT1, encashmentRequestTO.getApplicationDate()));
        transactionInfo.setEmployeeId(encashmentRequestTO.getEmployeeId());
        transactionInfo.setFromDate(DateUtil.getDateFormat(DateUtil.DATE_FORMAT1, encashmentRequestTO.getStartDate()));
        transactionInfo.setTillDate(DateUtil.getDateFormat(DateUtil.DATE_FORMAT1, encashmentRequestTO.getEndDate()));
        transactionInfo.setLeaveTypeId(encashmentRequestTO.getLeaveTypeId());
        transactionInfo.setLeaveTypeName(encashmentRequestTO.getEncashmentRequestConfigTO().getEncashmentTypeTO().getLeaveTypeCode());
        transactionInfo.setOrgId(encashmentRequestTO.getOrganizationId());
        transactionInfo.setTenantId(encashmentRequestTO.getTenantId());
        transactionInfo.setLeaveTypeId(encashmentRequestTO.getLeaveTypeId());
        transactionInfo.setStageId(transitionResponseTO.getNextStageId());
        transactionInfo.setTenantId(encashmentRequestTO.getTenantId());
        transactionInfo.setIsHourly(Boolean.FALSE);
        if( null != encashmentRequestTO.getEncashmentRequestConfigTO() && null != encashmentRequestTO.getEncashmentRequestConfigTO().getIsHourly()
                && encashmentRequestTO.getEncashmentRequestConfigTO().getIsHourly() ) {
            transactionInfo.setIsHourly(Boolean.TRUE);
        }

        SysWorkflowStageTO sysWorkflowStages = sysWorkflowStageRepository.getStageStatusByStageId(transitionResponseTO.getNextStageId());
        transactionInfo.setStageStatus(sysWorkflowStages.getStageStatus());
        transactionInfo.setStatusMessage(transitionResponseTO.getToMessage());

        tmEmployeeLeaveView.setEmployeeId(encashmentRequestTO.getEmployeeId());
        tmEmployeeLeaveView.setFromDateId(encashmentRequestTO.getStartDateId());
        tmEmployeeLeaveView.setToDateId(encashmentRequestTO.getEndDateId());
        tmEmployeeLeaveView.setLeaveTypeId(encashmentRequestTO.getLeaveTypeId());
        tmEmployeeLeaveView.setOrganizationId(encashmentRequestTO.getOrganizationId());
        tmEmployeeLeaveView.setStatusId(transitionResponseTO.getNextStageId());
        tmEmployeeLeaveView.setTenantId(encashmentRequestTO.getTenantId());
        tmEmployeeLeaveView.setTransactionInfo(transactionInfo);
        tmEmployeeLeaveView.setAudit(encashmentRequestTO.getLoginUser());
        tmEmployeeLeaveView.setActive(Boolean.TRUE);
        return tmEmployeeLeaveView;
    }
    public Object submitNewEncashmentRequest(EncashmentRequestTO encashmentRequestTO, TmEmployeeEncashment tmEmployeeEncashment, TmEmployeeLeaveQuota tmEmployeeLeaveQuota, TmLeaveLapse tmLeaveLapse,
                                        TransitionResponseTO transitionResponse, Map<String, KeyValueTO> tmLeaveDurationMap, String serverName, Integer entityId, StageTransitionTO transition)
            throws Exception

    {
        List<TmEmployeeEncashmentDetail> tmEmployeeEncashementDetailList = new ArrayList<>();
        Object[] objectArray = populateEmployeeLeaveDetails(encashmentRequestTO, tmEmployeeEncashment, transitionResponse.getNextStageId(), tmEmployeeEncashementDetailList);
        Map<Integer, List<TmEmployeeEncashmentDetail>> tmEmployeeEncashmentDetailMap = (Map<Integer, List<TmEmployeeEncashmentDetail>>) objectArray[0];
        HRResignationTO resignedEmpTO = new HRResignationTO();
        if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentRequestConfigTO() != null && encashmentRequestTO.getEncashmentRequestConfigTO().getIsCashable() != null
                && encashmentRequestTO.getEncashmentRequestConfigTO().getIsCashable()) {

            List<HrEmployeeResignation> resignedEmpList = new ArrayList<HrEmployeeResignation>();
            resignedEmpList = resignationRepo.findByEmployeeIdAndActive(encashmentRequestTO.getEmployeeId(), Boolean.TRUE);
            if (PSCollectionUtil.isNotNullOrEmpty(resignedEmpList)) {
                resignedEmpTO = getResignationTO(resignedEmpList);
            }
        }
        return saveEncashmentDetails(tmEmployeeEncashment, tmEmployeeLeaveQuota, tmEmployeeEncashmentDetailMap,  tmLeaveLapse, encashmentRequestTO, transitionResponse, tmLeaveDurationMap, serverName, entityId, transition, resignedEmpTO);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Object saveEncashmentDetails(TmEmployeeEncashment tmEmployeeEncashment, TmEmployeeLeaveQuota tmEmployeeLeaveQuota,Map<Integer, List<TmEmployeeEncashmentDetail>> tmEmployeeEncashmentDetailMap,
                                   TmLeaveLapse tmLeaveLapse, EncashmentRequestTO encashmentRequestTO, TransitionResponseTO transitionResponse, Map<String, KeyValueTO> tmLeaveDurationMap, String serverName, Integer entityId,
                                   StageTransitionTO transition, HRResignationTO resignedEmpTO)
            throws Exception
    {
        WorkflowHistoryResponseTO objectResult = null;

        String leaveBalanceAsOnLwd = null;
        String oldLeaveBalanceAsOnLwd = null;
        Integer employeeEntityId = sysUserRepository.getEntityIDByUserID(encashmentRequestTO.getUserID(), encashmentRequestTO.getOrganizationId());
        leaveBalanceAsOnLwd = creditDebitLeaveQuotaFromTmEmployeeEncashment(encashmentRequestTO, tmEmployeeEncashment, tmEmployeeLeaveQuota, tmLeaveDurationMap,
                EncashmentConstant.DEBIT, resignedEmpTO);
        
        if (resignedEmpTO != null && leaveBalanceAsOnLwd != null) {
            Long resignationId = resignedEmpTO.getResignationId();
            HrEmployeeResignation res = resignationRepo.findByResignationId(resignationId);
            if (res != null)
                oldLeaveBalanceAsOnLwd = res.getLeaveBalanceAsOnLwd();
            resignationRepo.updateLWDLeaveBalance(encashmentRequestTO.getLoginUser(), resignationId, new Date(), leaveBalanceAsOnLwd);
            insertResignationHistory(res, encashmentRequestTO.getLoginUser(), oldLeaveBalanceAsOnLwd);
        }

        ResourceBundle resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(encashmentRequestTO.getBundleName()));
        String validateStr=null;
        if (!Utils.isNullorEmpty(validateStr)) {
            throw new AppRuntimeException(validateStr);
        }
        saveEncashmentData(tmEmployeeEncashment,tmEmployeeEncashmentDetailMap, tmEmployeeLeaveQuota, tmLeaveLapse, encashmentRequestTO, resourceBundle, null);

        encashmentRequestTO.setEmployeeEncashmentId(tmEmployeeEncashment.getEncashmentID());
        encashmentRequestTO.setEncashmentCode(tmEmployeeEncashment.getEncashmentTaskCode());
        objectResult = saveWorkFlowHistory(serverName, transitionResponse, encashmentRequestTO, entityId, transition);
        if (objectResult.getWorkflowIDs() != null && !objectResult.getWorkflowIDs().isEmpty()) {
            tmEmployeeEncashment.setWorkFlowHistoryID(objectResult.getWorkflowIDs().get(0));
        } else {
            throw new AppRuntimeException("Action could not be completed due to temporary reasons, please try again.");
        }

        TmEmployeeEncashmentHistory tmEmployeeEncashmentHistory = saveTmEmployeeEncashmentHistory(tmEmployeeEncashment, encashmentRequestTO, transitionResponse);
        saveTmEncashmentDependentDetailData(tmEmployeeEncashment, encashmentRequestTO);
        return objectResult;

    }

    private void insertResignationHistory (HrEmployeeResignation resignation,int loginUser, String
            oldLeaveBalanceAsOnLwd)
    {

        if( resignation != null ) {
            HrEmployeeResignationHistory resignHistory = new HrEmployeeResignationHistory();
            if( resignation.getResignationId() != null )
                resignHistory.setResignationId(resignation.getResignationId());
            if( oldLeaveBalanceAsOnLwd != null )
                resignHistory.setLeaveBalanceAsOnLwd(oldLeaveBalanceAsOnLwd);
            if( resignation.getTenantId() != null )
                resignHistory.setTenantId(resignation.getTenantId());
            if( resignation.getCreatedBy() != null )
                resignHistory.setCreatedBy(resignation.getCreatedBy());
            resignHistory.setUserId(loginUser);
            if( resignation.getCreatedDate() != null )
                resignHistory.setCreatedDate(resignation.getCreatedDate());
            if( resignation.getModifiedBy() != null )
                resignHistory.setModifiedBy(resignation.getModifiedBy());
            if( resignation.getModifiedDate() != null )
                resignHistory.setModifiedDate(resignation.getModifiedDate());
            if( resignation.getIsAbsconding() != null )
                resignHistory.setIsAbsconding(resignation.getIsAbsconding());
            else
                resignHistory.setIsAbsconding(Boolean.FALSE);
            resignationHistoryRepo.save(resignHistory);

        }

    }

    private void saveEncashmentData(TmEmployeeEncashment tmEmployeeEncashment,Map<Integer, List<TmEmployeeEncashmentDetail>> tmEmployeeEncashmentDetailMap, TmEmployeeLeaveQuota tmEmployeeLeaveQuota,
                               TmLeaveLapse tmLeaveLapse, EncashmentRequestTO encashmentRequestTO, ResourceBundle resourceBundle, List<String> dateIdHalfTypeList)
    {
        employeeEncashmentRepository.save(tmEmployeeEncashment);
        Long encashmentId = tmEmployeeEncashment.getEncashmentID();

        List<TmEmployeeEncashmentDetail> tmEmployeeEncashmentDetails = new ArrayList<TmEmployeeEncashmentDetail>();
        for (Map.Entry<Integer, List<TmEmployeeEncashmentDetail>> entry : tmEmployeeEncashmentDetailMap.entrySet()) {

            List<TmEmployeeEncashmentDetail> tmEmployeeEncashmentDetail2 = entry.getValue();
            for (TmEmployeeEncashmentDetail tmEmployeeEncashmentDetail : tmEmployeeEncashmentDetail2) {
                tmEmployeeEncashmentDetail.setEncashmentID(encashmentId);
          }
            tmEmployeeEncashmentDetails.addAll(tmEmployeeEncashmentDetail2);
        }
        try {
            employeeEncashmentDetailRepository.saveAll(tmEmployeeEncashmentDetails);
       }catch(DataIntegrityViolationException e) {
            if(e.getCause() instanceof ConstraintViolationException) {
                LOGGER.log(Level.SEVERE, "DuplicationError: LeaveDuplication ------Employeeid---"+tmEmployeeEncashment.getEmployeeID()+"---timestamp---"+new Date().getTime());
                throw new AppRuntimeException(resourceBundle.getString("Error while applying encashment"));
            }
        }

        if (null != tmEmployeeLeaveQuota) {
            updateEmployeeLeaveQuota(tmEmployeeLeaveQuota, encashmentRequestTO.getLoginUser());
            leaveQuotaRepository.save(tmEmployeeLeaveQuota);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private void updateEmployeeLeaveQuota (TmEmployeeLeaveQuota tmEmployeeLeaveQuota, Integer loggedInuserID)
    {
        int roundOFfValue = 2;// use later
        if( null != tmEmployeeLeaveQuota && tmEmployeeLeaveQuota.getLeaveQuotaId() != null && tmEmployeeLeaveQuota.getLeaveQuotaId() > 0 ) {
            if( tmEmployeeLeaveQuota.getCurrentBalanceInDays() > 0 || tmEmployeeLeaveQuota.getCurrentBalanceInHrs() > 0 ) {
                leaveQuotaRepository.updateLeaveQuota(loggedInuserID, tmEmployeeLeaveQuota.getCurrentBalanceInDays(), tmEmployeeLeaveQuota.getCurrentBalanceInHrs(),
                        DateUtil.getCurrentDate(), tmEmployeeLeaveQuota.getLeaveQuotaId());
            }
            tmEmployeeLeaveQuota.setModifiedDate(DateUtil.getCurrentDate());
            tmEmployeeLeaveQuota.setModifiedBy(loggedInuserID);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private Object approveLeaveRequestWorkFlow (EncashmentRequestTO encashmentRequestTO, TmEmployeeEncashment tmEmployeeEncashment, TransitionResponseTO transitionResponse, String serverName, Integer entityId, StageTransitionTO transition, List<TmEmployeeEncashmentDetail> tmEmployeeEncashmentDetailList) throws
            Exception
    {
        short hourlyWeightage = tmEmployeeEncashment.getHourlyWeightage();
        WorkflowHistoryResponseTO resultObject = null;
        employeeEncashmentRepository.save(tmEmployeeEncashment);
        updateEmployeeEncashmentDetails(encashmentRequestTO, transitionResponse, tmEmployeeEncashmentDetailList,null);
        TmEmployeeLeaveQuota leaveQuota = getPresentQuota(encashmentRequestTO);
        encashmentRequestTO.setEmployeeEncashmentId(tmEmployeeEncashment.getEncashmentID());
        encashmentRequestTO.setEncashmentCode(tmEmployeeEncashment.getEncashmentTaskCode());
        encashmentRequestTO.setApplicationDateId(tmEmployeeEncashment.getApplicationDate());
        encashmentRequestTO.setTenantId(tmEmployeeEncashment.getTenantID());
        resultObject = saveWorkFlowHistory(serverName, transitionResponse, encashmentRequestTO, entityId, transition);
        if( resultObject.getWorkflowIDs() != null && !resultObject.getWorkflowIDs().isEmpty() ) {
            tmEmployeeEncashment.setWorkFlowHistoryID(resultObject.getWorkflowIDs().get(0));
        } else {
            throw new AppRuntimeException("Action could not be completed (current task not found), please try again.");
        }
        TmEmployeeEncashmentHistory tmEmployeeEncashmentHistory = saveTmEmployeeEncashmentHistory(tmEmployeeEncashment, encashmentRequestTO, transitionResponse);
        return resultObject;

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private WorkflowHistoryResponseTO saveWorkFlowHistory (String serverName, TransitionResponseTO
            transitionResponse, EncashmentRequestTO encashmentRequestTO, Integer entityId,
                                                           StageTransitionTO transition)
            throws AppRuntimeException

    {
        Long sessionID = null;
        WorkflowHistoryRequestTO workflowHistoryRequestTO = null;
        WorkflowHistoryResponseTO workflowHistoryResponseTO = null;
        workflowHistoryRequestTO = new WorkflowHistoryRequestTO();
        if( transitionResponse.getRoleActorsMap() == null || transitionResponse.getRoleActorsMap().isEmpty() ) {
            throw new AppRuntimeException(LeaveConstant.WORKFLOW_ROLE_ACTOR__ERROR);
        }

        if( transitionResponse.getRoleActorsMap().get(LeaveConstant.EMPLOYEE_ROLE) != null && !transitionResponse.getRoleActorsMap().get(LeaveConstant.EMPLOYEE_ROLE).isEmpty() ) {
            workflowHistoryRequestTO.setRoleActors(transitionResponse.getRoleActorsMap().get(LeaveConstant.EMPLOYEE_ROLE));
        } else if( transitionResponse.getRoleActorsMap().get(LeaveConstant.L1_MANAGER_ROLE) != null
                && !transitionResponse.getRoleActorsMap().get(LeaveConstant.L1_MANAGER_ROLE).isEmpty() ) {
            workflowHistoryRequestTO.setRoleActors(transitionResponse.getRoleActorsMap().get(LeaveConstant.L1_MANAGER_ROLE));
        } else if( transitionResponse.getRoleActorsMap().get(LeaveConstant.L2_MANAGER_ROLE) != null
                && !transitionResponse.getRoleActorsMap().get(LeaveConstant.L2_MANAGER_ROLE).isEmpty() ) {
            workflowHistoryRequestTO.setRoleActors(transitionResponse.getRoleActorsMap().get(LeaveConstant.L2_MANAGER_ROLE));
        } else if( transitionResponse.getRoleActorsMap().get(LeaveConstant.HR_MANAGER_ROLE) != null
                && !transitionResponse.getRoleActorsMap().get(LeaveConstant.HR_MANAGER_ROLE).isEmpty() ) {
            workflowHistoryRequestTO.setRoleActors(transitionResponse.getRoleActorsMap().get(LeaveConstant.HR_MANAGER_ROLE));
        } else if( transitionResponse.getRoleActorsMap().get(LeaveConstant.PRACTICE_HEAD_ROLE) != null
                && !transitionResponse.getRoleActorsMap().get(LeaveConstant.PRACTICE_HEAD_ROLE).isEmpty() ) {
            workflowHistoryRequestTO.setRoleActors(transitionResponse.getRoleActorsMap().get(LeaveConstant.PRACTICE_HEAD_ROLE));
        } else if( transitionResponse.getRoleActorsMap().get(LeaveConstant.FUNCTION_HEAD_ROLE) != null
                && !transitionResponse.getRoleActorsMap().get(LeaveConstant.FUNCTION_HEAD_ROLE).isEmpty() ) {
            workflowHistoryRequestTO.setRoleActors(transitionResponse.getRoleActorsMap().get(LeaveConstant.FUNCTION_HEAD_ROLE));
        } else if( transitionResponse.getRoleActorsMap().get(LeaveConstant.FUNCTIONAL_MANAGER_ROLE) != null
                && !transitionResponse.getRoleActorsMap().get(LeaveConstant.FUNCTIONAL_MANAGER_ROLE).isEmpty() ) {
            workflowHistoryRequestTO.setRoleActors(transitionResponse.getRoleActorsMap().get(LeaveConstant.FUNCTIONAL_MANAGER_ROLE));
        } else {
            for (Map.Entry<String, List<RoleActorTO>> actors : transitionResponse.getRoleActorsMap().entrySet()) {
                workflowHistoryRequestTO.setRoleActors(actors.getValue());
            }

        }
        workflowHistoryRequestTO.setTaskCode(encashmentRequestTO.getEncashmentCode());
        workflowHistoryRequestTO.setUserId(encashmentRequestTO.getUserID());
        workflowHistoryRequestTO.setOrganizationId(encashmentRequestTO.getOrganizationId());
        workflowHistoryRequestTO.setTaskStatus(transitionResponse.getToMessage());
        workflowHistoryRequestTO.setUsername(encashmentRequestTO.getUserName());
        workflowHistoryRequestTO.setTenantId(encashmentRequestTO.getTenantId());
        workflowHistoryRequestTO.setContextInstanceId(encashmentRequestTO.getEmployeeEncashmentId());
        workflowHistoryRequestTO.setContextEntityId(entityId);
        workflowHistoryRequestTO.setTransitionId(transitionResponse.getTransitionId());
        if( encashmentRequestTO.getWorkflowHistoryID() != null && !encashmentRequestTO.getWorkflowHistoryID().equals(0) ) {
            workflowHistoryRequestTO.setPreviousWorkflowHistoryId(encashmentRequestTO.getWorkflowHistoryID());
        }

        Boolean autoapprovalCheck = false;
        if( encashmentRequestTO != null && encashmentRequestTO.getEncashmentRequestConfigTO() != null && encashmentRequestTO.getEncashmentRequestConfigTO().getAutoApproval() != null )
            autoapprovalCheck = encashmentRequestTO.getEncashmentRequestConfigTO().getAutoApproval();
        if( autoapprovalCheck ) {
            Integer autoApprovalDays = sysWorkflowStageRepository.findAutoApprovalDaysByWorkflowStageID(transitionResponse.getNextStageId());
            if( autoApprovalDays != null && autoApprovalDays > 0 ) {
                Calendar cal = DateUtil.getCalendarWithAddedDays(autoApprovalDays);
                workflowHistoryRequestTO.setAutoApprovalDate(cal.getTime());
            }
        }

        ProtocolTO protocolTO = new ProtocolTO();
        protocolTO.setTenantID(encashmentRequestTO.getTenantId());
        protocolTO.setOrganizationID(encashmentRequestTO.getOrganizationId());
        protocolTO.setLoggedInUserId(encashmentRequestTO.getLoginUser());
        sessionID = getWebSession(encashmentRequestTO.getLoginUser(), encashmentRequestTO.getTenantId());
        if( sessionID != null ) {
            protocolTO.setSessionID(sessionID);
        }
        workflowHistoryRequestTO.setProtocolTO(protocolTO);

        workflowHistoryResponseTO = workflowService.createWorkflowHistoryTransaction(workflowHistoryRequestTO, transition);
        if( workflowHistoryResponseTO != null && workflowHistoryResponseTO.getResponseCode() != null
                && workflowHistoryResponseTO.getResponseCode().equals(LeaveConstant.MessageReponse.FAIL) ) {
            throw new AppRuntimeException(workflowHistoryResponseTO.getResponseMessage());
        }
        return workflowHistoryResponseTO;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private TmEmployeeEncashmentHistory saveTmEmployeeEncashmentHistory (TmEmployeeEncashment tmEmployeeEncashment, EncashmentRequestTO
            encashmentRequestTO, TransitionResponseTO transitionResponseTO)
    {
        TmEmployeeEncashmentHistory tmEmployeeEncashmentHistory = null;
        String uniqueKey = null;
        String SeparteStr = "~";
        try {
            tmEmployeeEncashmentHistory = new TmEmployeeEncashmentHistory();
            if( encashmentRequestTO.getWorkflowHistoryID() != null && !encashmentRequestTO.getWorkflowHistoryID().equals(0) ) {
                uniqueKey = encashmentRequestTO.getWorkflowHistoryID() + SeparteStr + transitionResponseTO.getPreviousStageType() + SeparteStr + tmEmployeeEncashment.getEncashmentID() + SeparteStr + encashmentRequestTO.getAction();
            } else {
                uniqueKey = tmEmployeeEncashment.getWorkFlowHistoryID() + SeparteStr + transitionResponseTO.getPreviousStageType() + SeparteStr + tmEmployeeEncashment.getEncashmentID() + SeparteStr + encashmentRequestTO.getAction();

            }
            tmEmployeeEncashmentHistory.setEncashmentId(tmEmployeeEncashment.getEncashmentID());
            tmEmployeeEncashmentHistory.setLeaveTypeId(tmEmployeeEncashment.getLeaveTypeID());
            tmEmployeeEncashmentHistory.setTenantId(tmEmployeeEncashment.getTenantID());
            tmEmployeeEncashmentHistory.setStatus(tmEmployeeEncashment.getStatusMessage());
            tmEmployeeEncashmentHistory.setUserId(encashmentRequestTO.getLoginUser());
            tmEmployeeEncashmentHistory.setComments(tmEmployeeEncashment.getComments());
            if( StringUtil.nonEmptyCheck(tmEmployeeEncashment.getFilePath()) ) {
                tmEmployeeEncashmentHistory.setFilePath(tmEmployeeEncashment.getFilePath());
            }
            if( StringUtil.nonEmptyCheck(tmEmployeeEncashment.getFileName()) ) {
                tmEmployeeEncashmentHistory.setFileName(tmEmployeeEncashment.getFileName());
            }
            tmEmployeeEncashmentHistory.setNextStageID(transitionResponseTO.getNextStageId());
            tmEmployeeEncashmentHistory.setWorkflowHistoryId(tmEmployeeEncashment.getWorkFlowHistoryID());
            tmEmployeeEncashmentHistory.setActionName(encashmentRequestTO.getAction());
            tmEmployeeEncashmentHistory.setNextStageType(transitionResponseTO.getNextStageType());
            tmEmployeeEncashmentHistory.setPreviousStageId(transitionResponseTO.getPreviousStageID());
            tmEmployeeEncashmentHistory.setPreviousStageName(transitionResponseTO.getPreviousStageName());
            tmEmployeeEncashmentHistory.setPreviousStageType(transitionResponseTO.getPreviousStageType());
            tmEmployeeEncashmentHistory.setCreatedDate(DateUtil.getCurrentDate());
            tmEmployeeEncashmentHistory.setCreatedBy(encashmentRequestTO.getLoginUser());
            tmEmployeeEncashmentHistory.setModifiedBy(encashmentRequestTO.getLoginUser());
            tmEmployeeEncashmentHistory.setModifiedDate(DateUtil.getCurrentDate());
            tmEmployeeEncashmentHistory.setEmployeeId(encashmentRequestTO.getEmployeeId());
            tmEmployeeEncashmentHistory.setEncashmentTypeContentId(encashmentRequestTO.getEncashmentTypeContentId());
            tmEmployeeEncashmentHistory.setOrganizationId(encashmentRequestTO.getOrganizationId());

            tmEmployeeEncashment.setPreviousStageType(transitionResponseTO.getPreviousStageType());
            if( encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(LeaveConstant.WORKFLOW_APPROVE_ACTION) ) {
                tmEmployeeEncashmentHistory.setComments(tmEmployeeEncashment.getManagerComments());
            } else if( encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(LeaveConstant.WORKFLOW_REJECT_ACTION) ) {
                tmEmployeeEncashmentHistory.setComments(tmEmployeeEncashment.getManagerComments());
            } else if (LeaveConstant.WORKFLOW_WITHDRAW_ACTION.equalsIgnoreCase(encashmentRequestTO.getAction())) {
                tmEmployeeEncashmentHistory.setComments(tmEmployeeEncashment.getEmployeeWithdrawComments());
            }
            encashmentHistoryRepository.save(tmEmployeeEncashmentHistory);

        } catch (DataIntegrityViolationException e) {
            if( e.getCause() instanceof ConstraintViolationException ) {
                LOGGER.log(Level.SEVERE, "DuplicationError: LeaveDuplication ------leaveId---" + tmEmployeeEncashment.getEncashmentID() + " ---uniqueKey---: " + uniqueKey);
                throw new AppRuntimeException("Action could not be completed due to temporary reasons, please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmEmployeeEncashmentHistory;
    }

    public Long getWebSession (Integer userId, Integer tenantId)
    {

        try {
            return leaveRedisCache.getWebSession(userId, tenantId);
        } catch (Exception e) {
            throw new AppRuntimeException(e.getMessage());
        }
    }
    private TransitionResponseTO nextStagePlatformAPI(TransitionRequestTO transitionRequestTO,
                                                      EncashmentRequestTO encashmentRequestTO, SysWorkflowStageAction currentSysWorkflowStageAction, SysWorkflowStageActionType currentSysWorkflowStageActionType, RestUtil<TransitionResponseTO> restUtil,
                                                      String url, String host)
            throws Exception, JsonProcessingException {
        String input = Utils.mapToJson(transitionRequestTO);
        LOGGER.log(Level.SEVERE, "DYNAMIC_WORKLFOW_REQUEST==> " + input);
        TransitionResponseTO transitionResponseTO = restUtil.postHttps(url, transitionRequestTO,
                TransitionResponseTO.class, host);
        String output = Utils.mapToJson(transitionResponseTO);
        LOGGER.log(Level.SEVERE, "DYNAMIC_WORKLFOW_RESPONSE==> " + output);
        if (transitionResponseTO.getResponseCode().equals(LeaveConstant.MessageReponse.SUCCESS)) {
            transitionResponseTO = checkAutoSkip(transitionRequestTO, transitionResponseTO,
                    currentSysWorkflowStageAction,currentSysWorkflowStageActionType, encashmentRequestTO, restUtil, url, host);
        }
        return transitionResponseTO;
    }

    private TransitionResponseTO checkAutoSkip(TransitionRequestTO transitionRequestTO,
                                               TransitionResponseTO transitionResponseTO, SysWorkflowStageAction currentSysWorkflowStageAction, SysWorkflowStageActionType currentSysWorkflowStageActionType,
                                               EncashmentRequestTO encashmentRequestTO, RestUtil<TransitionResponseTO> restUtil, String url, String host)
            throws JsonProcessingException, Exception {
        if (null != transitionResponseTO.getAutoSkipEnabled() && transitionResponseTO.getAutoSkipEnabled()) {
            Integer nextStageActorId = null;
            Map<String, List<RoleActorTO>> roleActorsMap = transitionResponseTO.getRoleActorsMap();
            for (Map.Entry<String, List<RoleActorTO>> entry : roleActorsMap.entrySet()) {
                for (RoleActorTO actorTO : entry.getValue()) {
                    if (actorTO.getPrimaryActor()) {
                        nextStageActorId = actorTO.getActorId();
                        break;
                    }
                }
            }
            if (null != nextStageActorId && 0 != encashmentRequestTO.getLoginUser()
                    && encashmentRequestTO.getLoginUser() == nextStageActorId.intValue()) {
                if (currentSysWorkflowStageAction == null) {
                    currentSysWorkflowStageAction = sysWorkflowStageActionRepository
                            .findByStageActionID(encashmentRequestTO.getStageActionId());

                    SysWorkflowStageActionType currentStageActionType = sysWorkflowStageActionTypeRepository.getStageActionTypeID(currentSysWorkflowStageAction.getStageActionTypeID());

                    List<Integer> stageActionTypesIds = sysWorkflowStageActionRepository
                            .getStageActionTypeIdsByWorkflowStageID(transitionResponseTO.getNextStageId());

                    List<SysWorkflowStageActionType> stageActionTypes = null;
                    if(PSCollectionUtil.isNotNullOrEmpty(stageActionTypesIds))
                        stageActionTypes = sysWorkflowStageActionTypeRepository.getWorkflowStageActionType(stageActionTypesIds);
                    if (stageActionTypes != null && !stageActionTypes.isEmpty()) {
                        for (SysWorkflowStageActionType stageActiontype : stageActionTypes) {
                            if (stageActiontype != null && stageActiontype.getStageActionType()
                                    .equalsIgnoreCase(currentStageActionType.getStageActionType())) {
                                currentSysWorkflowStageActionType = stageActiontype;
                                break;
                            }
                        }
                    }
                }
                SysWorkflowStageAction nextSysWorkflowStageAction = sysWorkflowStageActionRepository
                        .findByWorkflowStageIDAndStageActionTypeIDAndIsVisible(transitionResponseTO.getNextStageId(),
                                currentSysWorkflowStageActionType.getStageActionTypeId(), Boolean.TRUE);
                transitionRequestTO.setStageActionId(nextSysWorkflowStageAction.getStageActionID());
                transitionResponseTO = nextStagePlatformAPI(transitionRequestTO, encashmentRequestTO, currentSysWorkflowStageAction,currentSysWorkflowStageActionType, restUtil, url,host);
            } else {
                return transitionResponseTO;
            }
        }
        return transitionResponseTO;
    }

    public StageResponseTO getCurrentStageInfo (StageRequestTO request)
    {
        RestUtil<StageResponseTO> restUtil = new RestUtil<>();
        String apiUrl = AppConstant.SERVER_PROTOCOL + env.getProperty(AppConstant.HOSTNAME) + AppConstant.WORKFLOW_PRE_URL + AppConstant.WORKFLOW_CURRENT_STAGE_INFO_URL;
        try {
            return restUtil.postHttps(apiUrl, request, StageResponseTO.class, env.getProperty(AppConstant.HOSTNAME));

        } catch (AppRuntimeException ex) {
            throw ex;
        } catch (Exception e) {
            throw new AppRuntimeException(e, e.getLocalizedMessage());
        }

    }



    @Transactional(rollbackFor = Exception.class)
    public Object doBulkLeaveTransactionNew(EncashmentRequestTO encashmentRequestTO, String serverName, String approverComments,
                                            StageRequestTO stageRequestTO, String actionType, Integer entityId, String nextStageApiUrl,
                                            Map<String, String> placeHolder_method, ResourceBundle resourceBundle) {
        return doBulkEncashmentTransaction(
                encashmentRequestTO, serverName, approverComments, stageRequestTO, actionType, entityId, nextStageApiUrl, placeHolder_method,
                resourceBundle);
    }

    public Object doBulkEncashmentTransaction(EncashmentRequestTO encashmentRequestTO, String serverName, String approverComments,
                                         StageRequestTO stageRequestTO, String actionType, Integer entityId, String nextStageApiUrl,
                                         Map<String, String> placeHolder_method, ResourceBundle resourceBundle) {
        String errorMessage = null;
        
        try {
            WorkflowHistoryResponseTO responseTO = null;
            
            Object resultObject = null;
            List<TmEmployeeEncashmentDetail> tmEmployeeEncashmentDetailList = null;
            TmEmployeeEncashment tmEmployeeEncashment = new TmEmployeeEncashment();
            String ruleOutCome = null;
            Integer stageActionId = null;
            TransitionResponseTO transitionResponse = null;
            TmEmployeeLeaveQuota tmEmployeeLeaveQuota = null;
            Map<String, KeyValueTO> tmLeaveDurationMap = null;
            String ruleMethod = null;
            
            stageRequestTO.setStageId(encashmentRequestTO.getStageId());
            if(stageRequestTO.getProtocolTO()==null) {
                ProtocolTO protocolTO = new ProtocolTO();
                protocolTO.setOrganizationID(encashmentRequestTO.getOrganizationId());
                protocolTO.setTenantID(encashmentRequestTO.getTenantId());
                stageRequestTO.setProtocolTO(protocolTO);
                stageRequestTO.getProtocolTO().setEntityID(sysUserRepository.getEntityIDByUserID(encashmentRequestTO.getUserID(), encashmentRequestTO.getOrganizationId()));
            }
            StageResponseTO response = getCurrentStageInfo(stageRequestTO);
            if (response.getActions() != null && !response.getActions().isEmpty()) {
                for (ActionResponseTO action : response.getActions()) {
                    // if (action.getActionType().equalsIgnoreCase(leaveRequestTO.getAction())) {
                    if (action.getActionType().equalsIgnoreCase(actionType)) {
                        stageActionId = action.getActionId();
                        ruleMethod = action.getRuleMethod();
                        break;
                    }
                }
            }
            if (encashmentRequestTO != null) {
                if (stageActionId != null) {
                    encashmentRequestTO.setStageActionId(stageActionId);
                }
                if (ruleMethod != null) {
                    encashmentRequestTO.setRuleOutput(ruleMethod);
                }
                ruleOutCome = getDecisionTransitionForEncashment(encashmentRequestTO);
                if (ruleOutCome != null) {
                    encashmentRequestTO.setRuleOutcome(ruleOutCome);
                }
                if (actionType != null) {
                    encashmentRequestTO.setAction(actionType);
                }
            }

            transitionResponse = getNextStageInfo(encashmentRequestTO, nextStageApiUrl, serverName);
            if (encashmentRequestTO.getEmployeeEncashmentId() > 0) {
                List<Object[]> result = commonRepo.getStageTransitionList(transitionResponse.getTransitionId());

                StageTransitionTO transition = null;
                if (result != null && !result.isEmpty()) {
                    transition = LeaveUtil.convertToStageTransitionTO(result.get(0));
                }
                tmEmployeeEncashment = employeeEncashmentRepository.findByEncashmentID(encashmentRequestTO.getEmployeeEncashmentId());
                tmEmployeeEncashmentDetailList = employeeEncashmentDetailRepository
                        .getTmEmployeeEncashmentDetailByEmployeeEncashmentId(encashmentRequestTO.getEmployeeEncashmentId(),encashmentRequestTO.getOrganizationId());
                if (tmEmployeeEncashment != null) {
                    verifyMultipleTab(encashmentRequestTO, tmEmployeeEncashment);
                    tmEmployeeEncashment.setModifiedBy(encashmentRequestTO.getLoginUser());
                    tmEmployeeEncashment.setModifiedDate(DateUtil.getCurrentDate());
                    tmEmployeeEncashment.setManagerComments(approverComments);
                    tmEmployeeEncashment.setStatusMessage(transitionResponse.getToMessage());
                    tmEmployeeEncashment.setSysWorkflowStageID(transitionResponse.getNextStageId());
                    tmEmployeeEncashment.setStageType(transitionResponse.getNextStageType());
                    tmEmployeeEncashment.setPreviousStageType(transitionResponse.getPreviousStageType());

                } else {
                    throw new AppRuntimeException(
                            LeaveConstant.NO_LEAVE_RECORD_EXCEPTION + encashmentRequestTO.getEmployeeLeaveId());
                }

                if (encashmentRequestTO.getAction() != null) {
                    
                    Integer employeeEntityId = sysUserRepository.getEntityIDByUserID(encashmentRequestTO.getUserID(), encashmentRequestTO.getOrganizationId());
                    tmLeaveDurationMap = leaveDurationService.getDurationSysTypeMap(
                            encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getBundleName(), employeeEntityId);
                    HRResignationTO resignedEmpTO = new HRResignationTO();
                    if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentRequestConfigTO() != null
                            && encashmentRequestTO.getEncashmentRequestConfigTO().getIsCashable() != null
                            && encashmentRequestTO.getEncashmentRequestConfigTO().getIsCashable()) {

                        List<HrEmployeeResignation> resignedEmpList = new ArrayList<HrEmployeeResignation>();
                        resignedEmpList = resignationRepo.findByEmployeeIdAndActive(encashmentRequestTO.getEmployeeId(),
                                Boolean.TRUE);
                        if (PSCollectionUtil.isNotNullOrEmpty(resignedEmpList)) {
                            resignedEmpTO = getResignationTO(resignedEmpList);
                        }
                    }
                    if (encashmentRequestTO.getAction().equals(LeaveConstant.WORKFLOW_APPROVE_ACTION)
                            && encashmentRequestTO.getStageType() != null
                            && encashmentRequestTO.getStageType().equals(LeaveConstant.WORKFLOW_STAGE_TYPE_APPROVAL)) {
                        /**
                         * <pre>
                         * ********************************** MANAGER APPROVE PENDING LEAVE  ***********************************<br>
                         *
                         * Employee has submitted Encashment -> Manager Approving it
                         * </pre>
                         */
                        tmEmployeeLeaveQuota = getPresentQuota(encashmentRequestTO);
                        updateEncashmentBalanceInQuota(true,tmEmployeeEncashment,tmEmployeeLeaveQuota,encashmentRequestTO);
                        resultObject = approveLeaveRequestWorkFlow(encashmentRequestTO,
                                tmEmployeeEncashment, transitionResponse, serverName, entityId, transition,tmEmployeeEncashmentDetailList);
                    } else if (encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_REJECT_ACTION)
                            && encashmentRequestTO.getStageType() != null
                            && encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_APPROVAL)) {
                        /**
                         * <pre>
                         * ********************************** MANAGER REJECT PENDING LEAVE  ******************************************<br>
                         *
                         * Employee has submitted Encashment -> Manager Rejecting it
                         * </pre>
                         */
                        
                        tmEmployeeLeaveQuota = getPresentQuota(encashmentRequestTO);
                        SysWorkflow sysWorkflow = null;
                        Map<String, HrContentType> hrcontentMap = sysContentServiceImpl
                                .getHrContentTypesBySysContentType("workflow status", 1);
                        Integer entityID = sysUserRepository.getEntityIDByUserID(encashmentRequestTO.getUserID(), encashmentRequestTO.getOrganizationId());
                        if (PSCollectionUtil.isMapNotNullOrEmpty(hrcontentMap)) {
                            if (hrcontentMap.containsKey("Active")) {
                                Integer typeId = hrcontentMap.get("Active").getTypeId();
                                SysWorkflowType sysWorkflowType = sysWorkflowTypeRepository.findByWorkFlowType(EncashmentConstant.ENCASHMENT_MODULE_NAME);
                                sysWorkflow = sysWorkflowRepository.findByWorkflowTypeIDAndOrganizationIdAndStatusAndEntityID(sysWorkflowType.getWorkflowTypeId(), encashmentRequestTO.getOrganizationId(), typeId,entityID);
                                
                            }
                        }
                        boolean requestBeforeReinstate = false;
                        requestBeforeReinstate = encashmentValidationService.noBalanceCreditInReinstate(encashmentRequestTO.getEmployeeId(), encashmentRequestTO.getApplicationDateId(), encashmentRequestTO.getOrganizationId(), resourceBundle);
                        Integer formId = IformSecurityService.getSysFormIdFromForm(IEncashmentForm.ENCASHMENT_APPROVER_FORM_NAME, encashmentRequestTO.getOrganizationId());
                        SysWorkflowStageType stageType = sysWorkflowStageTypeRepository
                                .findWorkflowStageTypeIDByFormIDAndWorkflowStageTypeAndWorkflowTypeID(formId, "Completed",
                                        sysWorkflow.getWorkflowTypeID());
                        Integer stageTypeID = stageType.getWorkflowStageTypeId();
                        SysWorkflowStageTO stageTO = sysWorkflowStageRepository
                                .findStageIdByStageTypeAndWorkflowID(stageTypeID, sysWorkflow.getWorkflowId());
                        transitionResponse.setNextStageId(stageTO.getWorkflowStageID());
                        transitionResponse.setNextStageType(LeaveConstant.WORKFLOW_STAGE_TYPE_COMPLETED);
                        transition.setWorkflowNextStageType(LeaveConstant.WORKFLOW_STAGE_TYPE_COMPLETED);
                        transition.setNextStageID(stageTO.getWorkflowStageID());
                        tmEmployeeEncashment.setSysWorkflowStageID(transitionResponse.getNextStageId());
                        
                        if (transitionResponse.getNextStageType()
                                .equalsIgnoreCase(LeaveConstant.WORKFLOW_STAGE_TYPE_COMPLETED)) {
                            tmEmployeeEncashment.setIsActive(false);
                        }
                        resultObject = updateEncashmentDataRejectCase(encashmentRequestTO, serverName, tmEmployeeEncashmentDetailList,
                                tmEmployeeEncashment, tmEmployeeLeaveQuota, tmLeaveDurationMap,
                                transitionResponse, entityId, transition, resignedEmpTO, requestBeforeReinstate);
                    } else if (encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_APPROVE_ACTION)
                            && encashmentRequestTO.getStageType() != null && encashmentRequestTO.getStageType()
                            .equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_CANCEL_APPROVAL)) {
                        /**
                         * <pre>
                         * ********************************** MANAGER APPROVE ENCASHMENT CANCELLATION REQUEST  **********************************<br>
                         *
                         * Employee has submitted Encashment -> Manager approved it -> Employee withdraw it -> Manager is going to approve it
                         * </pre>
                         */
                        tmEmployeeLeaveQuota = getPresentQuota(encashmentRequestTO);
                        updateEncashmentBalanceInQuota(false,tmEmployeeEncashment,tmEmployeeLeaveQuota,encashmentRequestTO);
                        if (transitionResponse.getNextStageType()
                                .equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED)) {
                            tmEmployeeEncashment.setIsActive(false);
                        }
                        
                        boolean requestBeforeReinstate = false;
                        requestBeforeReinstate = encashmentValidationService.noBalanceCreditInReinstate(
                                encashmentRequestTO.getEmployeeId(), encashmentRequestTO.getApplicationDateId(),
                                encashmentRequestTO.getOrganizationId(), resourceBundle);
                        
                        resultObject = updateEncashmentDataApprovalCancelationApprovalCase(encashmentRequestTO, serverName,
                                tmEmployeeEncashmentDetailList, tmEmployeeEncashment, tmEmployeeLeaveQuota,
                                tmLeaveDurationMap, transitionResponse, entityId, transition, resignedEmpTO, requestBeforeReinstate);
                    } else if (encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_REJECT_ACTION)
                            && encashmentRequestTO.getStageType() != null && encashmentRequestTO.getStageType()
                            .equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_CANCEL_APPROVAL)) {
                        /**
                         * <pre>
                         * ********************************** MANAGER REJECT ENCASHMENT CANCELLATION REQUEST  ************************************<br>
                         *
                         * Employee has submitted Encashment -> Manager approved it -> Employee withdraw it -> Manager is going to Reject it
                         * </pre>
                         */
                        SysWorkflow sysWorkflow = null;
                        Integer entityID = sysUserRepository.getEntityIDByUserID(encashmentRequestTO.getUserID(), encashmentRequestTO.getOrganizationId());
                        Map<String, HrContentType> hrcontentMap = sysContentServiceImpl
                                .getHrContentTypesBySysContentType("workflow status", 1);
                        if (PSCollectionUtil.isMapNotNullOrEmpty(hrcontentMap)) {
                            if (hrcontentMap.containsKey("Active")) {
                                Integer typeId = hrcontentMap.get("Active").getTypeId();
                                SysWorkflowType sysWorkflowType = sysWorkflowTypeRepository
                                        .findByWorkFlowType(EncashmentConstant.ENCASHMENT_MODULE_NAME);
                                sysWorkflow = sysWorkflowRepository.findByWorkflowTypeIDAndOrganizationIdAndStatusAndEntityID(
                                        sysWorkflowType.getWorkflowTypeId(), encashmentRequestTO.getOrganizationId(),
                                        typeId,entityID);
                                
                            }
                        }
                        Integer formId = IformSecurityService.getSysFormIdFromForm(IEncashmentForm.ENCASHMENT_APPROVER_FORM_NAME,encashmentRequestTO.getOrganizationId());
                        SysWorkflowStageType stageType = sysWorkflowStageTypeRepository
                                .findWorkflowStageTypeIDByFormIDAndWorkflowStageTypeAndWorkflowTypeID(formId,
                                        EncashmentConstant.WORKFLOW_STAGE_TYPE_ENCASHMENT_CANCEL_REJECTED, sysWorkflow.getWorkflowTypeID());
                        Integer stageTypeID = stageType.getWorkflowStageTypeId();
                        SysWorkflowStageTO stageTO = sysWorkflowStageRepository
                                .findStageIdByStageTypeAndWorkflowID(stageTypeID, sysWorkflow.getWorkflowId());
                        transitionResponse.setNextStageId(stageTO.getWorkflowStageID());
                        transitionResponse.setNextStageType(EncashmentConstant.WORKFLOW_STAGE_TYPE_ENCASHMENT_CANCEL_REJECTED);
                        transition.setWorkflowNextStageType(EncashmentConstant.WORKFLOW_STAGE_TYPE_ENCASHMENT_CANCEL_REJECTED);
                        transition.setNextStageID(stageTO.getWorkflowStageID());
                        tmEmployeeEncashment.setSysWorkflowStageID(transitionResponse.getNextStageId());
                        
                        resultObject = rejectEncashmentRequestWorkFlow(encashmentRequestTO, tmEmployeeEncashmentDetailList,
                                tmEmployeeEncashment, transitionResponse, serverName, entityId, transition);
                    }
                    responseTO = (WorkflowHistoryResponseTO) resultObject;
//                    sendworkFlowMails(encashmentRequestTO, tmEmployeeEncashment, transitionResponse, tmEmployeeLeaveQuota,
//                            ((WorkflowHistoryResponseTO) resultObject).getSysUserWorkflowHistorys().get(0),
//                            placeHolder_method, tmLeaveDurationMap);
                } else {
                    errorMessage = resourceBundle.getString(ILeaveErrorConstants.NO_STAGE_ACTION_EXCEPTION);
                    throw new AppRuntimeException(errorMessage);
                }
            } else {
                errorMessage = resourceBundle.getString(ILeaveErrorConstants.NO_LEAVE_RECORD_EXCEPTION);
                throw new AppRuntimeException(errorMessage + encashmentRequestTO.getEmployeeLeaveId());
            }
            return responseTO;
        }
             catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            errorMessage = resourceBundle.getString(ILeaveErrorConstants.FAILED_CODE_PROBLEM_FAILURE);
            throw new AppRuntimeException(e, errorMessage);
        }

    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private Object updateEncashmentDataRejectCase(EncashmentRequestTO encashmentRequestTO, String serverName, List<TmEmployeeEncashmentDetail> tmEmployeeEncashmentDetailList,
                                             TmEmployeeEncashment tmEmployeeEncashment, TmEmployeeLeaveQuota tmEmployeeLeaveQuota, Map<String, KeyValueTO> tmLeaveDurationMap,
                                             TransitionResponseTO transitionResponse, Integer entityId, StageTransitionTO transition, HRResignationTO resignedEmpTO, boolean beforeReInstate)
    {
        Object resultObject;
        String leaveBalanceAsOnLwd = null;
        String oldLeaveBalanceAsOnLwd = null;
        if (beforeReInstate) {
        
        } else {
            leaveBalanceAsOnLwd = creditDebitLeaveQuotaFromTmEmployeeEncashment(encashmentRequestTO, tmEmployeeEncashment, tmEmployeeLeaveQuota, tmLeaveDurationMap,
                    LeaveConstant.CREDIT, resignedEmpTO);
            if (resignedEmpTO != null && leaveBalanceAsOnLwd != null) {
                Long resignationId = resignedEmpTO.getResignationId();
                HrEmployeeResignation res = resignationRepo.findByResignationId(resignationId);
                if (res != null)
                    oldLeaveBalanceAsOnLwd = res.getLeaveBalanceAsOnLwd();
                resignationRepo.updateLWDLeaveBalance(encashmentRequestTO.getLoginUser(), resignationId, new Date(), leaveBalanceAsOnLwd);
                insertResignationHistory(res, encashmentRequestTO.getLoginUser(), oldLeaveBalanceAsOnLwd);
            }
        }
        resultObject = rejectEncashmentRequestWorkFlow(encashmentRequestTO, tmEmployeeEncashmentDetailList, tmEmployeeEncashment, transitionResponse, serverName, entityId, transition);
        if(encashmentRequestTO.getCurrentStageId()!=null && encashmentRequestTO.getCurrentStageId()!=0 && encashmentRequestTO != null &&
                tmEmployeeEncashment.getSysWorkflowStageID()!=null && tmEmployeeEncashment.getSysWorkflowStageID()!=0 ) {
            if(encashmentRequestTO.getCurrentStageId().equals(tmEmployeeEncashment.getSysWorkflowStageID()) ) {
                throw new AppRuntimeException(LeaveConstant.MULTIPLE_ACTIONS_ON_LEAVE_ERROR_MESSAGE);
            }
        }
        return resultObject;
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private Object rejectEncashmentRequestWorkFlow (EncashmentRequestTO encashmentRequestTO, List < TmEmployeeEncashmentDetail > tmEmployeeEncashmentDetailList, TmEmployeeEncashment tmEmployeeEncashment, TransitionResponseTO transitionResponse, String serverName, Integer entityId, StageTransitionTO transition)
    {
        WorkflowHistoryResponseTO resultObject = null;
        employeeEncashmentRepository.save(tmEmployeeEncashment);
        updateEmployeeEncashmentDetails(encashmentRequestTO, transitionResponse, tmEmployeeEncashmentDetailList, null);
        encashmentRequestTO.setEmployeeLeaveId(tmEmployeeEncashment.getEncashmentID());
        encashmentRequestTO.setLeaveCode(tmEmployeeEncashment.getEncashmentTaskCode());
        encashmentRequestTO.setApplicationDateId(tmEmployeeEncashment.getApplicationDate());
        resultObject = saveWorkFlowHistory(serverName, transitionResponse, encashmentRequestTO, entityId, transition);
        if( resultObject.getWorkflowIDs() != null && !resultObject.getWorkflowIDs().isEmpty() ) {
            tmEmployeeEncashment.setWorkFlowHistoryID(resultObject.getWorkflowIDs().get(0));
        } else {
            throw new AppRuntimeException("Action could not be completed due to temporary reasons, please try again.");
        }
        
        TmEmployeeEncashmentHistory tmEmployeeLeaveHistory = saveTmEmployeeEncashmentHistory(tmEmployeeEncashment, encashmentRequestTO, transitionResponse);
        
        return resultObject;
        
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private Object[] populateEmployeeLeaveDetails (EncashmentRequestTO encashmentRequestTO, TmEmployeeEncashment tmEmployeeEncashment, Integer
            nextStageId, List < TmEmployeeEncashmentDetail > tmEmployeeEncashmentDetailList) {
        CalendarBlocksTO calendarBlocksTO = null;
        TmEmployeeEncashmentDetail employeeEncashmentDetail = null;
        List<CalendarBlocksTO> calenderBlockTOList = encashmentRequestTO.getCalendarBlocksTOList();
        Map<Integer, List<TmEmployeeEncashmentDetail>> tmEmployeeEncashmentDetailMap = new HashMap<Integer, List<TmEmployeeEncashmentDetail>>();
        Object[] objectArray = new Object[2];
        Map<Long,Integer[]> calendarBlockBalanceMap = processCalendarBlockBalance(tmEmployeeEncashment,encashmentRequestTO);
        List<TmEmployeeEncashmentDetail> employeeEncashmentDetailList = new ArrayList<TmEmployeeEncashmentDetail>();
            for (int i = 0; i < calenderBlockTOList.size(); i++) {
                calendarBlocksTO = calenderBlockTOList.get(i);
                employeeEncashmentDetailList = new ArrayList<TmEmployeeEncashmentDetail>();
                employeeEncashmentDetail = new TmEmployeeEncashmentDetail();
                employeeEncashmentDetail.setEncashmentID(tmEmployeeEncashment.getEncashmentID());
                employeeEncashmentDetail.setValid(true);
                employeeEncashmentDetail.setNextStageID(nextStageId);
                employeeEncashmentDetail.setCreatedDate(DateUtil.getCurrentDate());
                employeeEncashmentDetail.setCreatedBy(encashmentRequestTO.getLoginUser());
                employeeEncashmentDetail.setModifiedBy(encashmentRequestTO.getLoginUser());
                employeeEncashmentDetail.setModifiedDate(DateUtil.getCurrentDate());
                employeeEncashmentDetail.setOrganizationId(encashmentRequestTO.getOrganizationId());
                employeeEncashmentDetail.setTenantID(encashmentRequestTO.getTenantId());
                employeeEncashmentDetail.setEmployeeID(encashmentRequestTO.getEmployeeId());
                employeeEncashmentDetail.setCalenderBlockDetailID(encashmentRequestTO.getCalendarBlocksTOList().get(i).getCalendarBlockId());
                employeeEncashmentDetail.setLeaveTypeID(encashmentRequestTO.getLeaveTypeId());
                employeeEncashmentDetail.setEncashmentCalenderGroupID(encashmentRequestTO.getCalendarBlocksTOList().get(i).getCalendarGroupId());
                employeeEncashmentDetail.setEncashmentTypeId(encashmentRequestTO.getCalendarBlocksTOList().get(i).getEncashmentTypeId());
                if (calendarBlockBalanceMap != null && !calendarBlockBalanceMap.isEmpty() && calendarBlockBalanceMap.containsKey(encashmentRequestTO.getCalendarBlocksTOList().get(i).getCalendarGroupId())){
                    Integer balanceInDays = calendarBlockBalanceMap.get(encashmentRequestTO.getCalendarBlocksTOList().get(i).getCalendarGroupId())[0];
                    employeeEncashmentDetail.setEncashmentCountInBlockInDays(balanceInDays == null ? 0 : balanceInDays);
                    Integer balanceInHrs = calendarBlockBalanceMap.get(encashmentRequestTO.getCalendarBlocksTOList().get(i).getCalendarGroupId())[1];
                    employeeEncashmentDetail.setEncashmentCountInBlockInHrs(balanceInHrs == null ? 0 : balanceInHrs.shortValue());
                }
                employeeEncashmentDetailList.add(employeeEncashmentDetail);
                tmEmployeeEncashmentDetailList.add(employeeEncashmentDetail);
                if (PSCollectionUtil.isNotNullOrEmpty(employeeEncashmentDetailList)) {
                    tmEmployeeEncashmentDetailMap.put(Math.toIntExact(calendarBlocksTO.getCalendarBlockId()), employeeEncashmentDetailList);
                }
            }
            objectArray[0] = tmEmployeeEncashmentDetailMap;
            return objectArray;
        }
        
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private void saveTmEncashmentDependentDetailData (TmEmployeeEncashment tmEmployeeEncashment, EncashmentRequestTO
            encashmentRequestTO)
    {
        TmEncashmentDependentDetail tmEncashmentDependentDetail = null;
        DependentDetailTO dependentDetailTO= null;
        List<DependentDetailTO> dependentDetailTOList= encashmentRequestTO.getDependentDetailTOList();
        List<TmEncashmentDependentDetail> tmEncashmentDependentDetailList = new ArrayList<TmEncashmentDependentDetail>();
        try {
            if( encashmentRequestTO.getDependentDetailTOList()!=null   && !encashmentRequestTO.getDependentDetailTOList().isEmpty() ) {
                for (int i = 0; i < dependentDetailTOList.size(); i++) {
                    tmEncashmentDependentDetail = new TmEncashmentDependentDetail();
                    dependentDetailTO= dependentDetailTOList.get(i);
                    tmEncashmentDependentDetail.setCreatedDate(DateUtil.getCurrentDate());
                    tmEncashmentDependentDetail.setCreatedBy(encashmentRequestTO.getLoginUser());
                    tmEncashmentDependentDetail.setModifiedBy(encashmentRequestTO.getLoginUser());
                    tmEncashmentDependentDetail.setModifiedDate(DateUtil.getCurrentDate());
                    tmEncashmentDependentDetail.setOrganizationId(encashmentRequestTO.getOrganizationId());
                    tmEncashmentDependentDetail.setTenantID(encashmentRequestTO.getTenantId());
                    tmEncashmentDependentDetail.setEncashmentID(encashmentRequestTO.getEmployeeEncashmentId());
                    tmEncashmentDependentDetail.setDependentID(dependentDetailTO.getDependentId());
                    tmEncashmentDependentDetail.setAge(dependentDetailTO.getAge());
                    tmEncashmentDependentDetail.setName(dependentDetailTO.getName());
                    tmEncashmentDependentDetail.setPercentage(dependentDetailTO.getPercentage());
                    tmEncashmentDependentDetail.setRelationshipTypeId(dependentDetailTO.getRelationshipTypeId());
                    tmEncashmentDependentDetailList.add(tmEncashmentDependentDetail);
                }
            }

            encashmentDependentDetail.saveAll(tmEncashmentDependentDetailList);

        } catch (DataIntegrityViolationException e) {
            if( e.getCause() instanceof ConstraintViolationException ) {
                LOGGER.log(Level.SEVERE, "DuplicationError: LeaveDuplication ------leaveId---" + tmEmployeeEncashment.getEncashmentID());
                throw new AppRuntimeException("Action could not be completed due to temporary reasons, please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private Object updateEncashmentDataWithdrawCase(EncashmentRequestTO encashmentRequestTO, String serverName, List<TmEmployeeEncashmentDetail> tmEmployeeEncashmentDetailList,
                                                    TmEmployeeEncashment tmEmployeeEncashment, TmEmployeeLeaveQuota tmEmployeeLeaveQuota, Map<String, KeyValueTO> tmLeaveDurationMap,
                                                    TransitionResponseTO transitionResponse, Integer entityId,
                                                    StageTransitionTO transition, HRResignationTO resignedEmpTO, boolean requestBeforeReInstate)
    {
        Object resultObject;
        String oldLeaveBalanceAsOnLwd = null;
        String leaveBalanceAsOnLwd = null;
        ResourceBundle resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(encashmentRequestTO.getBundleName()));
        if (requestBeforeReInstate) {
        
        } else {
            leaveBalanceAsOnLwd = creditDebitLeaveQuotaFromTmEmployeeEncashment(encashmentRequestTO, tmEmployeeEncashment, tmEmployeeLeaveQuota, tmLeaveDurationMap,
                    EncashmentConstant.CREDIT, resignedEmpTO);
            if (resignedEmpTO != null && leaveBalanceAsOnLwd != null) {
                Long resignationId = resignedEmpTO.getResignationId();
                HrEmployeeResignation res = resignationRepo.findByResignationId(resignationId);
                if (res != null)
                    oldLeaveBalanceAsOnLwd = res.getLeaveBalanceAsOnLwd();
                resignationRepo.updateLWDLeaveBalance(encashmentRequestTO.getLoginUser(), resignationId, new Date(), leaveBalanceAsOnLwd);
                insertResignationHistory(res, encashmentRequestTO.getLoginUser(), oldLeaveBalanceAsOnLwd);
            }
        }
        resultObject = withdrawEncashmentRequestWorkFlow(encashmentRequestTO, tmEmployeeEncashmentDetailList, tmEmployeeEncashment, transitionResponse, serverName, entityId, transition);
        if (encashmentRequestTO.getCurrentStageId() != null && encashmentRequestTO.getCurrentStageId() != 0 && tmEmployeeEncashment != null &&
                tmEmployeeEncashment.getSysWorkflowStageID() != null && tmEmployeeEncashment.getSysWorkflowStageID() != 0) {
            if (encashmentRequestTO.getCurrentStageId().equals(tmEmployeeEncashment.getSysWorkflowStageID()) ){
                throw new AppRuntimeException(EncashmentConstant.MULTIPLE_ACTIONS_ON_ENCASHMENT_ERROR_MESSAGE);
            }
        }
        return resultObject;
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private Object withdrawEncashmentRequestWorkFlow (EncashmentRequestTO encashmentRequestTO, List<TmEmployeeEncashmentDetail> tmEmployeeEncashmentDetailList, TmEmployeeEncashment tmEmployeeEncashment, TransitionResponseTO transitionResponse, String serverName, Integer entityId, StageTransitionTO transition)
    {
        WorkflowHistoryResponseTO resultObject = null;
        employeeEncashmentRepository.save(tmEmployeeEncashment);
        updateEmployeeEncashmentDetails(encashmentRequestTO, transitionResponse, tmEmployeeEncashmentDetailList, null);
        encashmentRequestTO.setEmployeeLeaveId(tmEmployeeEncashment.getEncashmentID());
        encashmentRequestTO.setLeaveCode(tmEmployeeEncashment.getEncashmentTaskCode());
        encashmentRequestTO.setApplicationDateId(tmEmployeeEncashment.getApplicationDate());
        resultObject = saveWorkFlowHistory(serverName, transitionResponse, encashmentRequestTO, entityId, transition);
        if( resultObject.getWorkflowIDs() != null && !resultObject.getWorkflowIDs().isEmpty() ) {
            tmEmployeeEncashment.setWorkFlowHistoryID(resultObject.getWorkflowIDs().get(0));
        } else {
            throw new AppRuntimeException("Action could not be completed due to temporary reasons, please try again.");
        }
        TmEmployeeEncashmentHistory tmEmployeeLeaveHistory = saveTmEmployeeEncashmentHistory(tmEmployeeEncashment, encashmentRequestTO, transitionResponse);
        return resultObject;
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private void updateEmployeeEncashmentDetails (EncashmentRequestTO encashmentRequestTO, TransitionResponseTO
            transitionResponse, List <TmEmployeeEncashmentDetail> tmEmployeeEncashmentDetailList, Map <String,Object> objectMap)
    
    {
        List<TmEmployeeEncashmentDetail> updatedEmployeeEncashmentDetailList = null;
        long timeStamp = new Date().getTime();
        Long encashmentId = 0l;
        if (tmEmployeeEncashmentDetailList != null && !tmEmployeeEncashmentDetailList.isEmpty()) {
            updatedEmployeeEncashmentDetailList = new ArrayList<>();
            for (TmEmployeeEncashmentDetail employeeEncashmentDetail : tmEmployeeEncashmentDetailList) {
                if( objectMap != null && !objectMap.isEmpty() ) {
                    Map<String, KeyValueTO> tmLeaveDurationMap = objectMap.get("TmLeaveDurationTypes") == null ? null : (Map<String, KeyValueTO>) objectMap.get("TmLeaveDurationTypes");
                }
                employeeEncashmentDetail.setNextStageID(transitionResponse.getNextStageId());
                employeeEncashmentDetail.setModifiedBy(encashmentRequestTO.getLoginUser());
                employeeEncashmentDetail.setModifiedDate(DateUtil.getCurrentDate());
                encashmentId = employeeEncashmentDetail.getEncashmentID();
                if( encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_REJECT_ACTION) && encashmentRequestTO.getStageType() != null
                        && encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_APPROVAL)
                        && transitionResponse.getNextStageType().equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED) ) {
                    employeeEncashmentDetail.setValid(false);
                }
                if (encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_WITHDRAW_ACTION) && encashmentRequestTO.getStageType() != null
                        && encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_REJECT)
                        && transitionResponse.getNextStageType().equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED) ) {
                    employeeEncashmentDetail.setValid(false);
                }

                if( encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_WITHDRAW_ACTION) && encashmentRequestTO.getStageType() != null
                        && (encashmentRequestTO.getStageType() == null || encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_INITIAL))
                        && transitionResponse.getNextStageType().equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED) ) {
                    employeeEncashmentDetail.setValid(false);
                }

                if( encashmentRequestTO.getAction().equals(LeaveConstant.WORKFLOW_WITHDRAW_ACTION) && encashmentRequestTO.getStageType() != null
                        && encashmentRequestTO.getStageType().equals(LeaveConstant.WORKFLOW_STAGE_TYPE_APPROVAL)
                        && transitionResponse.getNextStageType().equalsIgnoreCase(LeaveConstant.WORKFLOW_STAGE_TYPE_COMPLETED) ) {
                    employeeEncashmentDetail.setValid(false);
                }

                if( encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_APPROVE_ACTION) && encashmentRequestTO.getStageType() != null
                        && encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_CANCEL_APPROVAL)
                        && transitionResponse.getNextStageType().equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED) ) {
                    employeeEncashmentDetail.setValid(false);
                }
                if( encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(EncashmentConstant.WORKFLOW_MANAGER_WITHDRAW_ACTION) && encashmentRequestTO.getStageType() != null
                        && (encashmentRequestTO.getStageType() == null || encashmentRequestTO.getStageType().equals(EncashmentConstant.WORKFLOW_STAGE_TYPE_INITIAL))
                        && transitionResponse.getNextStageType().equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED) ) {
                    employeeEncashmentDetail.setValid(false);
                }
                updatedEmployeeEncashmentDetailList.add(employeeEncashmentDetail);
            }
            try {
                employeeEncashmentDetailRepository.saveAll(updatedEmployeeEncashmentDetailList);
            } catch (DataIntegrityViolationException e) {
                if( e.getCause() instanceof ConstraintViolationException ) {
                    LOGGER.log(Level.SEVERE, "DuplicationError: EncashmentDuplication ------encashmentId---" + encashmentId + "---timestamp---" + timeStamp);
                    throw new AppRuntimeException("Action could not be completed due to temporary reasons, please try again.");
                }
            }
        }
    }
    
    private String creditDebitLeaveQuotaFromTmEmployeeEncashment(EncashmentRequestTO encashmentRequestTO, TmEmployeeEncashment tmEmployeeEncashment, TmEmployeeLeaveQuota tmEmployeeLeaveQuota, Map<String, KeyValueTO> tmLeaveDurationMap, String action, HRResignationTO resignedEmpTO)
    {
        String errorMessage = null;
        ResourceBundle resourceBundle = null;
        String resignBalance = null;
        
        try {
            Integer[] resultValues = null;
            int leaveApplicationDateId = 0;
            int calendarStartDateId = 0;
            Integer currentQuotaInMinutes = null;
            Integer currentQuotaInMinutesInitial = null;
            
            resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(encashmentRequestTO.getBundleName()));
            if (tmEmployeeLeaveQuota == null || encashmentRequestTO == null || tmLeaveDurationMap == null) {
                return null;
            }
            
            // Fetch Hourly Weightage at time of Leave Application
            Integer pastHourlyWeightageHH = LeaveUtil.getHour(tmEmployeeEncashment.getHourlyWeightage().toString());
            Integer pastHourlyWeightageMM = LeaveUtil.getMinutes(tmEmployeeEncashment.getHourlyWeightage().toString());
            
            // Fetch Hourly Weightage now
            Integer presentHourlyWeightage = 0;
            Integer presentHourlyWeightageHH = 0;
            Integer presentHourlyWeightageMM = 0;
            
            if (tmLeaveDurationMap != null && !tmLeaveDurationMap.isEmpty()) {
                if (tmLeaveDurationMap.get(LeaveConstant.LEAVE_DURATION_FULL) != null) {
                    KeyValueTO keyValueTO = tmLeaveDurationMap.get(LeaveConstant.LEAVE_DURATION_FULL);
                    presentHourlyWeightage = keyValueTO.getHourlyWeightage();
                    presentHourlyWeightageHH = LeaveUtil.getHour(presentHourlyWeightage.toString());
                    presentHourlyWeightageMM = LeaveUtil.getMinutes(presentHourlyWeightage.toString());
                }
            }
            Integer totalLeaveDurationInMinutes = LeaveUtil.calculate_MM_from_DDHHMM(tmEmployeeEncashment.getEncashmentCountInDays() == null ? 0 : tmEmployeeEncashment.getEncashmentCountInDays(),
                    tmEmployeeEncashment.getEncashmentCountInHrs().intValue(), pastHourlyWeightageHH, pastHourlyWeightageMM);
            
            Date applicationDate = DateUtil.getDateFromTimeDimensionId(tmEmployeeEncashment.getApplicationDate());
            
            currentQuotaInMinutes = LeaveUtil.calculate_MM_from_DDHHMM(tmEmployeeLeaveQuota.getCurrentBalanceInDays(), tmEmployeeLeaveQuota.getCurrentBalanceInHrs(),
                    presentHourlyWeightageHH, presentHourlyWeightageMM);
            
            currentQuotaInMinutesInitial = currentQuotaInMinutes;
            
            if (action.equals(EncashmentConstant.CREDIT))
                currentQuotaInMinutes = currentQuotaInMinutes + totalLeaveDurationInMinutes;
            else
                currentQuotaInMinutes = currentQuotaInMinutes - totalLeaveDurationInMinutes;
            
            leaveApplicationDateId = DateUtil.getTimeDimensionId(applicationDate);
            calendarStartDateId = DateUtil.getTimeDimensionId(encashmentRequestTO.getLeaveCalendarStartDate());
            
            resultValues = LeaveUtil.calculate_DDHHMM_from_MM(presentHourlyWeightageHH, presentHourlyWeightageMM, currentQuotaInMinutes);
            
            tmEmployeeLeaveQuota.setCurrentBalanceInDays(resultValues[0]);
            tmEmployeeLeaveQuota.setCurrentBalanceInHrs(resultValues[1] * 100 + resultValues[2]);
            
            updateEmployeeLeaveQuota(tmEmployeeLeaveQuota, encashmentRequestTO.getLoginUser());
        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            if (resourceBundle != null)
                errorMessage = resourceBundle.getString(ILeaveErrorConstants.HOURLY_LEAVE_EXCEPTION);
            else errorMessage = e.getMessage();
            throw new AppRuntimeException(errorMessage);
        }
        return resignBalance;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object doProxyLeaveTransactionNew(EncashmentRequestTO encashmentRequestTO, String serverName){
        return doProxyEncashmentTransaction(encashmentRequestTO, serverName);
    }

    public Object doProxyEncashmentTransaction(EncashmentRequestTO encashmentRequestTO, String serverName)
    {
        Object resultObject = null;
        String ruleOutCome = null;
        TransitionResponseTO transitionResponse = null;
        String nextStageApiUrl = null;
        String errorMessage = null;
        ResourceBundle resourceBundle = null;
        try {
            resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(encashmentRequestTO.getBundleName()));
            ruleOutCome = getDecisionTransitionForEncashment(encashmentRequestTO);
            encashmentRequestTO.setRuleOutcome(ruleOutCome);
            serverName = environment.getProperty(AppConstant.HOSTNAME);
            nextStageApiUrl = AppConstant.SERVER_PROTOCOL + serverName + AppConstant.WORKFLOW_PRE_URL + AppConstant.WORKFLOW_NEXT_STAGE_INFO_URL;
            transitionResponse = getNextStageInfo(encashmentRequestTO, nextStageApiUrl, serverName);
            if (encashmentRequestTO.getAction() != null && encashmentRequestTO.getAction().equals(LeaveConstant.WORKFLOW_APPROVE_ACTION)) {
                List<Object[]> result = commonRepo.getStageTransitionList(transitionResponse.getTransitionId());

                StageTransitionTO transition = null;
                if (result != null && !result.isEmpty()) {
                    transition = LeaveUtil.convertToStageTransitionTO(result.get(0));
                }
                SysEntity sysEntity = sysEntityRepository.findByName(EncashmentConstant.ENCASHMENT_ENTITY_NAME);
                Integer entityId = sysEntity.getEntityId();

                TmEmployeeLeaveQuota tmEmployeeLeaveQuota = getPresentQuota(encashmentRequestTO);
                TmLeaveLapse tmLeaveLapse = getLeaveLapse(encashmentRequestTO, null);
                TmEmployeeEncashment tmEmployeeEncashment = prepareTmEmployeeEncashmentEntity(encashmentRequestTO, transitionResponse, tmLeaveLapse);
                TmEmployeeLeaveView tmEmployeeLeaveView = prepareTmEmployeeLeaveEntity2(encashmentRequestTO, transitionResponse, tmLeaveLapse);
                List<LeaveBalanceResponseTO> leaveBalanceResponseTOs = LeaveUtil.processLeaveBalanceRules(leaveBalanceRulesRepository, encashmentRequestTO.getOrganizationId(),
                        Boolean.TRUE);
                Integer employeeEntityId = sysUserRepository.getEntityIDByUserID(encashmentRequestTO.getUserID(), encashmentRequestTO.getOrganizationId());
                Map<String, KeyValueTO> tmLeaveDurationMap = leaveDurationService.getDurationSysTypeMap(encashmentRequestTO.getOrganizationId(), encashmentRequestTO.getBundleName(), employeeEntityId);
                resultObject = submitProxyEncashmentRequest(encashmentRequestTO, tmEmployeeEncashment, tmEmployeeLeaveQuota, tmLeaveLapse, transitionResponse, tmLeaveDurationMap, serverName, entityId, transition);
//                sendworkFlowMails(encashmentRequestTO, tmEmployeeEncashment, transitionResponse, tmEmployeeLeaveQuota,
//                        ((WorkflowHistoryResponseTO) resultObject).getSysUserWorkflowHistorys().get(0), null, tmLeaveDurationMap);
            } else {
                errorMessage = resourceBundle.getString(ILeaveErrorConstants.NO_STAGE_ACTION_EXCEPTION);
                throw new AppRuntimeException(errorMessage);
            }

        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            if (resourceBundle != null)
                errorMessage = resourceBundle.getString(ILeaveErrorConstants.FAILED_CODE_PROBLEM_FAILURE);
            else errorMessage = e.getMessage();
            throw new AppRuntimeException(e, errorMessage);
        }
        return resultObject;
    }

    public Object submitProxyEncashmentRequest(EncashmentRequestTO encashmentRequestTO, TmEmployeeEncashment tmEmployeeEncashment, TmEmployeeLeaveQuota tmEmployeeLeaveQuota, TmLeaveLapse tmLeaveLapse,
                                             TransitionResponseTO transitionResponse, Map<String, KeyValueTO> tmLeaveDurationMap, String serverName, Integer entityId, StageTransitionTO transition) {
        Object result = null;
        String errorMessage = null;
        ResourceBundle resourceBundle = null;
        String leaveBalanceAsOnLwd = null;
        String oldLeaveBalanceAsOnLwd = null;
        List<TmShiftBreakDetailTO> tmShiftBreakDetailList = null;
        Map<Integer, ShiftRosterTO> shiftMap = null;
        Integer entityID = null;
        Boolean leaveDeductionBSConfig = false;
        try {
            resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(encashmentRequestTO.getBundleName()));
            List<TmEmployeeEncashmentDetail> tmEmployeeEncashementDetailList = new ArrayList<>();
            entityID = hrEmployeeRepository.findEntityIDByEmployeeID(tmEmployeeEncashment.getEmployeeID(), tmEmployeeEncashment.getOrganizationId());
            Map<Integer, String> shiftNatureMap = sysContentServiceImpl.getHrContentType(TimeManagementConstants.SHIFTNATURE,
                    encashmentRequestTO.getOrganizationId());
            Map<String, Object> objectMap = null;
            Object[] objectArray = populateEmployeeLeaveDetails(encashmentRequestTO, tmEmployeeEncashment, transitionResponse.getNextStageId(), tmEmployeeEncashementDetailList);
            Map<Integer, List<TmEmployeeEncashmentDetail>> tmEmployeeEncashmentDetailMap = (Map<Integer, List<TmEmployeeEncashmentDetail>>) objectArray[0];
            HRResignationTO resignedEmpTO = new HRResignationTO();
            if (encashmentRequestTO != null && encashmentRequestTO.getEncashmentRequestConfigTO() != null && encashmentRequestTO.getEncashmentRequestConfigTO().getIsCashable() != null
                    && encashmentRequestTO.getEncashmentRequestConfigTO().getIsCashable()) {
                List<HrEmployeeResignation> resignedEmpList = new ArrayList<HrEmployeeResignation>();
                resignedEmpList = resignationRepo.findByEmployeeIdAndActive(tmEmployeeLeaveQuota.getEmployeeId(), Boolean.TRUE);
                if (PSCollectionUtil.isNotNullOrEmpty(resignedEmpList)) {
                    resignedEmpTO = getResignationTO(resignedEmpList);
                }
            }
            leaveBalanceAsOnLwd = creditDebitLeaveQuotaFromTmEmployeeEncashment(encashmentRequestTO, tmEmployeeEncashment, tmEmployeeLeaveQuota, tmLeaveDurationMap,
                    EncashmentConstant.DEBIT, resignedEmpTO);
            if (resignedEmpTO != null && leaveBalanceAsOnLwd != null) {
                Long resignationId = resignedEmpTO.getResignationId();
                HrEmployeeResignation res = resignationRepo.findByResignationId(resignationId);
                if (res != null)
                    oldLeaveBalanceAsOnLwd = res.getLeaveBalanceAsOnLwd();
                resignationRepo.updateLWDLeaveBalance(encashmentRequestTO.getLoginUser(), resignationId, new Date(), leaveBalanceAsOnLwd);
                insertResignationHistory(res, encashmentRequestTO.getLoginUser(), oldLeaveBalanceAsOnLwd);
            }

            Integer employeeEntityId = sysUserRepository.getEntityIDByUserID(encashmentRequestTO.getUserID(), encashmentRequestTO.getOrganizationId());
            List<TmEmployeeLeaveDetail> tmEmployeeLeaveDetailList = new ArrayList<>();
            if (EncashmentUtil.checkIfFromUpload(encashmentRequestTO)) {
                    boolean revertCase = Boolean.FALSE;
                    UserInfo userInfo = encashmentRequestTO.getUserInfo().getOldUserInfo();
                    userInfo.setOrganizationId(encashmentRequestTO.getOrganizationId());
                    userInfo.setTenantId(encashmentRequestTO.getTenantId());

                    TimeOffWorkFlowModel timeOffWorkFlowModelRequest = new TimeOffWorkFlowModel();
                    TimeOffWorkFlowModel timeOffWorkFlowModelResponse = null;
                    timeOffWorkFlowModelRequest.setStageType(IEncashmentForm.ENCASHMENT_APPROVER_FORM_NAME);
                    timeOffWorkFlowModelRequest.setEntityID(employeeEntityId);
                    List<TimeOffWorkFlowModel> actionJsonList = leaveServiceImpl
                            .getStageActionList(timeOffWorkFlowModelRequest, userInfo);
                    for (TimeOffWorkFlowModel timeOffWorkFlowModel21 : actionJsonList) {
                        if (timeOffWorkFlowModel21.getActionType()
                                .equals(LeaveConstant.WORKFLOW_MANAGER_WITHDRAW_ACTION)) {
                            timeOffWorkFlowModelResponse = timeOffWorkFlowModel21;
                            break;
                        }
                    }

                    if (null == timeOffWorkFlowModelResponse) {
                        throw new AppRuntimeException(
                                "You do not have proxy leave withdraw permission for this employee, Please try again from application");
                    }
                    Integer formId = IformSecurityService.getSysFormIdFromForm(IEncashmentForm.ENCASHMENT_APPROVER_FORM_NAME, encashmentRequestTO.getOrganizationId());
                    List<SysWorkflowStageType> sysWorkflowStageTypes = sysWorkflowStageTypeRepository.findByFormID(formId);
                    Map<Integer, String> workflowStageTypeId_workflowStageType = new HashMap<Integer, String>();
                    if (PSCollectionUtil.isNotNullOrEmpty(sysWorkflowStageTypes)) {
                        for (SysWorkflowStageType sysWorkflowStageType : sysWorkflowStageTypes) {
                            workflowStageTypeId_workflowStageType.put(sysWorkflowStageType.getWorkflowStageTypeId(), sysWorkflowStageType.getWorkflowStageType());
                        }

                    }
            }
            result = saveProxyEncashmentDetails(tmEmployeeEncashment, tmEmployeeLeaveQuota, tmEmployeeEncashmentDetailMap,  tmLeaveLapse, encashmentRequestTO, transitionResponse, tmLeaveDurationMap, serverName, entityId, transition, resignedEmpTO);
        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            if (resourceBundle != null)
                errorMessage = resourceBundle.getString(ILeaveErrorConstants.FAILED_APPLY_PROXYLEAVE_SUBMITLEAVETRANSACTION);
            else errorMessage = e.getMessage();
            throw new AppRuntimeException(e, errorMessage);
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object saveProxyEncashmentDetails(TmEmployeeEncashment tmEmployeeEncashment, TmEmployeeLeaveQuota tmEmployeeLeaveQuota,Map<Integer, List<TmEmployeeEncashmentDetail>> tmEmployeeEncashmentDetailMap,
                                        TmLeaveLapse tmLeaveLapse, EncashmentRequestTO encashmentRequestTO, TransitionResponseTO transitionResponse, Map<String, KeyValueTO> tmLeaveDurationMap, String serverName, Integer entityId,
                                        StageTransitionTO transition, HRResignationTO resignedEmpTO)
            throws Exception {

        {
            String errorMessage = null;
            ResourceBundle resourceBundle = null;
            WorkflowHistoryResponseTO objectResult = null;
            List<String> dateIdHalfTypeList = new ArrayList<>();
            try {
                resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(encashmentRequestTO.getBundleName()));
                saveEncashmentData(tmEmployeeEncashment, tmEmployeeEncashmentDetailMap, tmEmployeeLeaveQuota, tmLeaveLapse, encashmentRequestTO, resourceBundle, dateIdHalfTypeList);
                updateEncashmentBalanceInQuota(true, tmEmployeeEncashment, tmEmployeeLeaveQuota, encashmentRequestTO);
                encashmentRequestTO.setEmployeeEncashmentId(tmEmployeeEncashment.getEncashmentID());
                encashmentRequestTO.setEncashmentCode(tmEmployeeEncashment.getEncashmentTaskCode());
                objectResult = saveWorkFlowHistory(serverName, transitionResponse, encashmentRequestTO, entityId, transition);
                if (objectResult.getWorkflowIDs() != null && !objectResult.getWorkflowIDs().isEmpty()) {
                    tmEmployeeEncashment.setWorkFlowHistoryID(objectResult.getWorkflowIDs().get(0));
                } else {
                    throw new AppRuntimeException(
                            "Action could not be completed due to temporary reasons, please try again.");
                }
                Date currentDate = DateUtil.getCurrentDate();
                Map<String, Integer> lAEEExemptMap = null;
                TmEmployeeEncashmentHistory tmEmployeeEncashmentHistory = saveTmEmployeeEncashmentHistory(tmEmployeeEncashment, encashmentRequestTO, transitionResponse);
                /**
                 * PostGre view Save NOTE : Please dont put any code below this
                 * function as SQL and PostGre transaction run differently, hence
                 * any code below might cause reversal of SQL transaction but commit
                 * in PostGre which is not right.
                 */
                saveTmEncashmentDependentDetailData(tmEmployeeEncashment, encashmentRequestTO);
            } catch (AppRuntimeException e) {
                throw e;
            } catch (Exception e) {
                if (resourceBundle != null)
                    errorMessage = resourceBundle.getString(ILeaveErrorConstants.FAILED_APPLY_PROXYLEAVE_SUBMITLEAVETRANSACTION);
                else errorMessage = e.getMessage();
                throw new AppRuntimeException(e, errorMessage);
            }
            return objectResult;

        }
    }
    
    private void sendworkFlowMails(EncashmentRequestTO encashmentRequestTO, TmEmployeeEncashment tmEmployeeEncashment, TransitionResponseTO transitionResponse,
                                   TmEmployeeLeaveQuota tmEmployeeLeaveQuota, SysUserWorkflowHistory sysUserWorkflowHistory, Map<String, String> placeHolder_method,
                                   Map<String, KeyValueTO> tmLeaveDurationMap)
    {
        communicationService.sendEncashmentWorkFlowMails(new ArrayList<Integer>(Arrays.asList(transitionResponse.getTransitionId())), tmEmployeeEncashment, tmEmployeeLeaveQuota,
                encashmentRequestTO, LeaveConstant.LEAVE_MODULE_NAME, sysUserWorkflowHistory, placeHolder_method, tmLeaveDurationMap, true, true);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private void updateEncashmentBalanceInQuota(boolean flag, TmEmployeeEncashment tmEmployeeEncashment, TmEmployeeLeaveQuota tmEmployeeLeaveQuota, EncashmentRequestTO encashmentRequestTO) {
        try{
            if (tmEmployeeLeaveQuota != null && tmEmployeeLeaveQuota.getLeaveQuotaId() != null && tmEmployeeLeaveQuota.getLeaveQuotaId() > 0) {
                Integer encashmentBalanceInDays = null;
                Integer encashmentBalanceInHrs = null;
                Integer loggedInUserId = encashmentRequestTO.getLoginUser();
                Integer hourlyWeightage = tmEmployeeEncashment.getHourlyWeightage() != null ? tmEmployeeEncashment.getHourlyWeightage().intValue() : 0;
                Integer hourMinute = tmEmployeeEncashment.getEncashmentCountInHrs() != null ? tmEmployeeEncashment.getEncashmentCountInHrs().intValue() : 0;
                Integer days = tmEmployeeEncashment.getEncashmentCountInDays() != null ? tmEmployeeEncashment.getEncashmentCountInDays() : 0;
                Float encashmentQuota = LeaveUtil.quotaCalculation(days, hourMinute, hourlyWeightage);
                Float currentEncashmentQuota = LeaveUtil.quotaCalculation(
                        tmEmployeeLeaveQuota.getEncachmentBalanceInDays() == null ? 0 : tmEmployeeLeaveQuota.getEncachmentBalanceInDays(),
                        tmEmployeeLeaveQuota.getEncachmentBalanceInHrs() == null ? 0 : tmEmployeeLeaveQuota.getEncachmentBalanceInHrs(),
                        hourlyWeightage);
                Float updatedBalance = 0.0f;
                // for approval flag will be true and for withdrawl, flag will be false
                if (flag)
                    updatedBalance = encashmentQuota + currentEncashmentQuota;
                else
                    updatedBalance = currentEncashmentQuota - encashmentQuota;
                Integer[] updatedQuota = LeaveUtil.getDaysAndHours(LeaveUtil.getTotalDaysFromQuota(updatedBalance,hourlyWeightage), hourlyWeightage);
                encashmentBalanceInDays = updatedQuota[0];
                encashmentBalanceInHrs = updatedQuota[1];
                Date currentDate = DateUtil.getCurrentDate();
                tmEmployeeLeaveQuota.setEncachmentBalanceInDays(encashmentBalanceInDays);
                tmEmployeeLeaveQuota.setEncachmentBalanceInHrs(encashmentBalanceInHrs);
                tmEmployeeEncashment.setModifiedBy(loggedInUserId);
                tmEmployeeEncashment.setModifiedDate(currentDate);
                leaveQuotaRepository.updateEncashmentBalanceInLeaveQuota(loggedInUserId,tmEmployeeLeaveQuota.getLeaveQuotaId(),encashmentBalanceInDays,encashmentBalanceInHrs,currentDate,encashmentRequestTO.getOrganizationId());
            }
        } catch (Exception e){
            throw new AppRuntimeException("Failed to update encashment balance in quota");
        }
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppRuntimeException.class)
    private Object updateEncashmentDataApprovalCancelationApprovalCase(EncashmentRequestTO encashmentRequestTO, String serverName, List<TmEmployeeEncashmentDetail> tmEmployeeEncashmentDetailList,
                                                                  TmEmployeeEncashment tmEmployeeEncashment, TmEmployeeLeaveQuota tmEmployeeLeaveQuota, Map<String, KeyValueTO> tmLeaveDurationMap,
                                                                  TransitionResponseTO transitionResponse, Integer entityId, StageTransitionTO transition,
                                                                  HRResignationTO resignedEmpTO, boolean requestBeforeReInstate) throws Exception
    {
        Object resultObject;
        String leaveBalanceAsOnLwd = null;
        String oldLeaveBalanceAsOnLwd = null;
        ResourceBundle resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(encashmentRequestTO.getBundleName()));
        if (requestBeforeReInstate) {
        
        } else {
            if (transitionResponse != null && transitionResponse.getNextStageType().equalsIgnoreCase(EncashmentConstant.WORKFLOW_STAGE_TYPE_COMPLETED)) {
                leaveBalanceAsOnLwd = creditDebitLeaveQuotaFromTmEmployeeEncashment(encashmentRequestTO, tmEmployeeEncashment, tmEmployeeLeaveQuota, tmLeaveDurationMap,
                        EncashmentConstant.CREDIT, resignedEmpTO);
            }
            if (resignedEmpTO != null && leaveBalanceAsOnLwd != null) {
                Long resignationId = resignedEmpTO.getResignationId();
                HrEmployeeResignation res = resignationRepo.findByResignationId(resignationId);
                if (res != null)
                    oldLeaveBalanceAsOnLwd = res.getLeaveBalanceAsOnLwd();
                resignationRepo.updateLWDLeaveBalance(encashmentRequestTO.getLoginUser(), resignationId, new Date(), leaveBalanceAsOnLwd);
                insertResignationHistory(res, encashmentRequestTO.getLoginUser(), oldLeaveBalanceAsOnLwd);
            }
        }
        resultObject = approveLeaveRequestWorkFlow(encashmentRequestTO, tmEmployeeEncashment, transitionResponse, serverName, entityId, transition,tmEmployeeEncashmentDetailList);
        
        if(encashmentRequestTO.getCurrentStageId()!=null && encashmentRequestTO.getCurrentStageId()!=0 && tmEmployeeEncashment!=null &&
                tmEmployeeEncashment.getSysWorkflowStageID()!=null && tmEmployeeEncashment.getSysWorkflowStageID()!=0 ) {
            if(encashmentRequestTO.getCurrentStageId().equals(tmEmployeeEncashment.getSysWorkflowStageID()) ) {
                throw new AppRuntimeException(EncashmentConstant.MULTIPLE_ACTIONS_ON_ENCASHMENT_ERROR_MESSAGE);
            }
        }
        return resultObject;
    }
    
    private Map<Long,Integer[]> processCalendarBlockBalance(TmEmployeeEncashment tmEmployeeEncashment, EncashmentRequestTO encashmentRequestTO){
        try {
            Long altGroupId = groupServiceImpl.getGroupIdByPolicyCode(encashmentRequestTO.getEmployeeId(), EncashmentConstant.ENCASHMENT_GROUP_POLICY);
            if (altGroupId == null || altGroupId <= 0)
                return null;
            List<Long> calendarGroupIds = encashmentRequestTO.getCalendarBlocksTOList().stream().map(CalendarBlocksTO::getCalendarGroupId).collect(Collectors.toList());
            if (calendarGroupIds == null || calendarGroupIds.isEmpty())
                return null;
            List<Object[]> maximumLimits = tmEncashmentCalenderGroupRepository.findMaximumEncashmentLimit(calendarGroupIds,true,altGroupId,encashmentRequestTO.getLeaveTypeId(),encashmentRequestTO.getOrganizationId(),encashmentRequestTO.getTenantId());
            Map<Long,Float> limitMap = new HashMap<>();
            if (maximumLimits != null && !maximumLimits.isEmpty()){
                maximumLimits.forEach(limit->{
                    limitMap.put(((Long) limit[1]),((Float) limit[0]));
                });
            }
            if (limitMap.isEmpty())
                return null;
            Short hourlyWeightage = tmEmployeeEncashment.getHourlyWeightage() != null ? tmEmployeeEncashment.getHourlyWeightage() : 0;
            Integer[] daysAndHours = LeaveUtil.getDaysAndHours(encashmentRequestTO.getEncashmentNumberOfDays(), hourlyWeightage.intValue());
            Map<Long,Integer[]> calendarBlockBalanceMap = new HashMap<>();
            if (encashmentRequestTO.getCalendarBlocksTOList() != null && !encashmentRequestTO.getCalendarBlocksTOList().isEmpty()){
                if (encashmentRequestTO.getCalendarBlocksTOList().size() == 1){
                    Long calendarGroupId = encashmentRequestTO.getCalendarBlocksTOList().get(0).getCalendarGroupId();
                    calendarBlockBalanceMap.put(calendarGroupId,daysAndHours);
                } else {
                    Long initialCalendarGroupId = encashmentRequestTO.getCalendarBlocksTOList().get(0).getCalendarGroupId();
                    Long secondCalendarGroupId = encashmentRequestTO.getCalendarBlocksTOList().get(1).getCalendarGroupId();
                    List<Object[]> appliedEncashmentsByGroup = employeeEncashmentDetailRepository.getEncashmentTakenCountByGroup(encashmentRequestTO.getOrganizationId(),true,Arrays.asList(initialCalendarGroupId,secondCalendarGroupId),encashmentRequestTO.getEmployeeId());
                    Map<Long,Float> appliedEncashmentMap = new HashMap<>();
                    if (appliedEncashmentsByGroup != null && !appliedEncashmentsByGroup.isEmpty()){
                        appliedEncashmentsByGroup.forEach(appliedEncashment->{
                            Integer daysBalance = appliedEncashment[0] == null ? 0 : ((Integer) appliedEncashment[0]);
                            Integer hrsBalance = appliedEncashment[1] == null ? 0 : ((Short) appliedEncashment[1]).intValue();
                            Float appliedDays = LeaveUtil.getTotalDaysFromQuota(LeaveUtil.quotaCalculation(daysBalance,hrsBalance,hourlyWeightage.intValue()),hourlyWeightage.intValue());
                            if (appliedEncashmentMap.containsKey(((Long) appliedEncashment[2]))){
                                appliedEncashmentMap.put(((Long) appliedEncashment[2]),appliedDays+appliedEncashmentMap.get(((Long) appliedEncashment[2])));
                            } else {
                                appliedEncashmentMap.put(((Long) appliedEncashment[2]),appliedDays);
                            }
                        });
                    }
                    if (limitMap.get(initialCalendarGroupId) >= appliedEncashmentMap.get(initialCalendarGroupId) + encashmentRequestTO.getEncashmentNumberOfDays()){
                        calendarBlockBalanceMap.put(initialCalendarGroupId,LeaveUtil.getDaysAndHours(encashmentRequestTO.getEncashmentNumberOfDays(),hourlyWeightage.intValue()));
                    } else {
                        Float balanceForFirstGroup = limitMap.get(initialCalendarGroupId) - appliedEncashmentMap.get(initialCalendarGroupId);
                        Float balanceLeft = encashmentRequestTO.getEncashmentNumberOfDays() - balanceForFirstGroup;
                        calendarBlockBalanceMap.put(initialCalendarGroupId,LeaveUtil.getDaysAndHours(balanceForFirstGroup,hourlyWeightage.intValue()));
                        calendarBlockBalanceMap.put(secondCalendarGroupId,LeaveUtil.getDaysAndHours(balanceLeft,hourlyWeightage.intValue()));
                    }
                }
                
            }
            return calendarBlockBalanceMap;
        } catch (Exception e){
            throw new AppRuntimeException(e.getMessage());
        }
    }
}
