package com.peoplestrong.timeoff.encashment.controller.impl;

import com.peoplestrong.timeoff.dataservice.model.common.AltGroup;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmCalenderBlock;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmCalenderBlockDetail;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmEncashmentCalenderGroup;
import com.peoplestrong.timeoff.dataservice.model.leave.TmLeaveType;
import com.peoplestrong.timeoff.dataservice.repo.common.AltGroupPolicyRepository;
import com.peoplestrong.timeoff.dataservice.repo.common.AltGroupRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmCalenderBlockDetailRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmCalenderBlockRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmEncashmentCalenderGroupRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.LeaveDurationTypeRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.LeaveTypeRepository;
import com.peoplestrong.timeoff.encashment.controller.EncashmentCalendargroupController;
import com.peoplestrong.timeoff.encashment.pojo.*;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import com.peoplestrong.timeoff.encashment.service.CalendarBlockService;
import com.peoplestrong.timeoff.encashment.service.EncashmentCalenderGroupService;
import com.peoplestrong.timeoff.encashment.service.TestService;
import com.peoplestrong.timeoff.encashment.service.common.SessionService;
import com.peoplestrong.timeoff.leave.helper.LeaveUtil;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.leave.service.Impl.GroupServiceImpl;
import com.peoplestrong.timeoff.multilingual.MultiLingualMessageRemoteFacade;
import org.springframework.beans.factory.annotation.Autowired;
import com.peoplestrong.timeoff.encashment.utils.AppLogger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class EncashmentCalendargroupControllerImpl implements EncashmentCalendargroupController {

    private static final AppLogger logger = AppLogger.get(EncashmentCalendargroupController.class);

    @Autowired
    LeaveTypeRepository leaveTyperepo;

    @Autowired
    AltGroupPolicyRepository altGroupPolicyRepository;

    @Autowired
    AltGroupRepository altGroupRepository;

    @Autowired
    private TmCalenderBlockRepository blockRepository;

    @Autowired
    private TmCalenderBlockDetailRepository blockDetailRepository;

    @Autowired
    private TmEncashmentCalenderGroupRepository encashmentCalenderGroupRepository;

    @Autowired
    private LeaveDurationTypeRepository leaveDurationTypeRepository;

    final MultiLingualMessageRemoteFacade multiLingualMessageRemoteFacade;
    final SessionService sessionService;
    final EncashmentCalenderGroupService calenderGroupService;

    @Autowired
    public EncashmentCalendargroupControllerImpl(MultiLingualMessageRemoteFacade multiLingualMessageRemoteFacade, SessionService sessionService, EncashmentCalenderGroupService calenderGroupService) {
        this.multiLingualMessageRemoteFacade = multiLingualMessageRemoteFacade;
        this.sessionService = sessionService;
        this.calenderGroupService = calenderGroupService;
    }
    
    @Override
    public AppResponse<Object> getleavetypedetails() {
        AppResponse<Object> appResponse;

        try {
            UserInfo userInfo = sessionService.getUserInfo();
            List<TmLeaveType> getleavetypedetails = leaveTyperepo.getLeavetypeidandcode(userInfo.getOrganizationId());

            if (!getleavetypedetails.isEmpty()) {
                appResponse = AppResponse.success("Data fetch successfully", getleavetypedetails);
            } else {
                appResponse = AppResponse.error("No data in the database");
            }

        } catch (Exception e) {
            logger.error("Error getting list", e);
            appResponse = AppResponse.unknownError();
        }
        return appResponse;
    }

    @Override
    public AppResponse<Object> getAltGroupdetails() {
        AppResponse<Object> appResponse;

        try {
            UserInfo userInfo = sessionService.getUserInfo();
            List<Long> altgroupId = altGroupPolicyRepository.getAltGroupIdsForPolicywithOrgId("Leave Encashment Policy",userInfo.getOrganizationId());

            List<AltGroupTo> altList=new ArrayList<>();
            for(Long id:altgroupId){

                String altgroupname=altGroupRepository.getAltgroupname_forgroupid(id,userInfo.getOrganizationId());

                AltGroupTo altgrpto=new AltGroupTo();

                altgrpto.setAltGroupID(id);

                altgrpto.setAltGroupName(altgroupname);

                altList.add(altgrpto);
            }

            if (!altList.isEmpty()) {
                appResponse = AppResponse.success("Data fetch successfully", altList);
            } else {
                appResponse = AppResponse.error("No data in the database");
            }

        } catch (Exception e) {
            logger.error("Error getting list", e);
            appResponse = AppResponse.unknownError();
        }
        return appResponse;
    }

    @Override
    public AppResponse<Object> getAllBlockNames() {
        AppResponse<Object> appResponse;

        try {
            UserInfo userInfo = sessionService.getUserInfo();
            List<TmCalenderBlock> activeBlockInfos = blockRepository.getCalenderidandName(userInfo.getOrganizationId());
            if (!activeBlockInfos.isEmpty()) {
                appResponse = AppResponse.success("Data fetch successfully", activeBlockInfos);
            } else {
                appResponse = AppResponse.error("No data in the database");
            }

        } catch (Exception e) {
            logger.error("Error getting list", e);
            appResponse = AppResponse.unknownError();
        }
        return appResponse;
    }

    @Override
    public AppResponse<Object> getBlockDetails(@RequestBody CalenderBlockTo calenderBlockTo) {
        AppResponse<Object> appResponse;

        try {
            UserInfo userInfo = sessionService.getUserInfo();
            List<TmCalenderBlockDetail> calenderBlockDetailName = blockDetailRepository.getCalenderdetaildName(calenderBlockTo.getCalenderBlockID(), userInfo.getOrganizationId());

            if (!calenderBlockDetailName.isEmpty()) {
                appResponse = AppResponse.success("Data fetch successfully", calenderBlockDetailName);
            } else {
                appResponse = AppResponse.error("No data in the database");
            }

        } catch (Exception e) {
            logger.error("Error getting list", e);
            appResponse = AppResponse.unknownError();
        }
        return appResponse;
    }

    @Override
    public AppResponse<Object> saveEcg(@RequestBody TmEncashmentCalendarGroupTo encashmentCalendarGroupTo) {
        AppResponse<Object> appResponse;
        try {

            UserInfo userInfo = sessionService.getUserInfo();
            String validate=calenderGroupService.validateEcgDetails(encashmentCalendarGroupTo,userInfo.getOrganizationId());
            if(validate.equals("Validation Successfull")) {
                Boolean saveSuccess = calenderGroupService.saveData(encashmentCalendarGroupTo,userInfo);


                if (saveSuccess) {
                    appResponse = AppResponse.success("Data saved successfully", encashmentCalendarGroupTo);
                } else {
                    appResponse = AppResponse.error("Failed to save data");
                }
            }else{
                appResponse=AppResponse.error(validate);
            }

        } catch (Exception e) {
            logger.error("Error validating details", e);
            appResponse = AppResponse.unknownError();
        }
        return appResponse;
    }

    @Override
    public AppResponse<Object> getEcg() {
        AppResponse<Object> appResponse;

        try {
                    UserInfo userInfo = sessionService.getUserInfo();
            List<TmEncashmentCalenderGroup> ecgData = encashmentCalenderGroupRepository.findAllByOrgIdandTenantId(userInfo.getOrganizationId(), userInfo.getTenantId());

            List<TmEncashmentCalendarGroupTo> ecglist = ecgData.stream()
                    .map(ecg -> {
                        if (ecg == null) {
                            return null;
                        }

                        TmLeaveType leaveType = leaveTyperepo.findByLeaveTypeId(ecg.getLeaveTypeID());
                        String altGroupName = altGroupRepository.getAltgroupname_forgroupid(ecg.getAltGroupID(),userInfo.getOrganizationId());

                        TmEncashmentCalendarGroupTo encashmentCalendarGroupTo = new TmEncashmentCalendarGroupTo();
                        encashmentCalendarGroupTo.setEncashmentCalenderGroupID(ecg.getEncashmentCalenderGroupID());
                        encashmentCalendarGroupTo.setConfigurationName(ecg.getConfigurationName());
                        encashmentCalendarGroupTo.setLeaveTypeCode(leaveType != null ? leaveType.getLeaveTypeCode() : null);
                        encashmentCalendarGroupTo.setAltGroupName(altGroupName);
                        encashmentCalendarGroupTo.setActive(ecg.getActive());
                        encashmentCalendarGroupTo.setMaxEncashmentLimit(ecg.getMaxEncashmentLimit());
                        encashmentCalendarGroupTo.setMaxEncashmentAllowedInMonth(ecg.getMaxEncashmentAllowedInMonth());
                        encashmentCalendarGroupTo.setMaxEncashmentAllowedInQuarter(ecg.getMaxEncashmentAllowedInQuarter());
                        encashmentCalendarGroupTo.setMaxEncashmentAllowedInYear(ecg.getMaxEncashmentAllowedInYear());
                        encashmentCalendarGroupTo.setMinimumBalanceForEncashment(ecg.getMinimumBalanceForEncashment());
                        if(ecg.getMinimumBalanceForEncashment()!=null) {
                        encashmentCalendarGroupTo.setMinLeaveBalanceAfterEncashment(ecg.getMinLeaveBalanceAfterEncashment());
                        }else{
                        encashmentCalendarGroupTo.setMinLeaveBalanceAfterEncashment(null);
                        }
                        return encashmentCalendarGroupTo;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());


            if (!ecglist.isEmpty()) {
                appResponse = AppResponse.success("Data fetch successfully", ecglist);
            } else {
                appResponse = AppResponse.error("No data in the database");
            }

        } catch (Exception e) {
            logger.error("Error getting list", e);
            appResponse = AppResponse.unknownError();
        }
        return appResponse;
    }

    @Override
    public AppResponse<Object> getEcgListDetails(@RequestBody TmEncashmentCalendarGroupTo encashmentCalendarGroupTo) {
        AppResponse<Object> appResponse;

        try {
            UserInfo userInfo = sessionService.getUserInfo();
            Long id = encashmentCalendarGroupTo.getEncashmentCalenderGroupID();
            Optional<TmEncashmentCalenderGroup> ecgListDetails = encashmentCalenderGroupRepository.findById(id);

            TmLeaveType leaveType = leaveTyperepo.findByLeaveTypeId(ecgListDetails.get().getLeaveTypeID());
            Optional<TmCalenderBlockDetail> calenderBlockDetail = blockDetailRepository.findById(ecgListDetails.get().getCalenderBlockDetailID());
            String altGroupName=altGroupRepository.getAltgroupname_forgroupid(ecgListDetails.get().getAltGroupID(),userInfo.getOrganizationId());


            TmEncashmentCalendarGroupTo ecgto = new TmEncashmentCalendarGroupTo();
            ecgto.setEncashmentCalenderGroupID(ecgListDetails.get().getEncashmentCalenderGroupID());
            ecgto.setConfigurationName(ecgListDetails.get().getConfigurationName());
            ecgto.setLeaveTypeCode(leaveType.getLeaveTypeCode());
            ecgto.setAltGroupName(altGroupName);
            ecgto.setBlockDetailName(calenderBlockDetail.get().getBlockDetailName());
            ecgto.setActive(ecgListDetails.get().getActive());
            ecgto.setMaxEncashmentLimit(ecgListDetails.get().getMaxEncashmentLimit());
            ecgto.setMaxEncashmentAllowedInMonth(ecgListDetails.get().getMaxEncashmentAllowedInMonth());
            ecgto.setMaxEncashmentAllowedInQuarter(ecgListDetails.get().getMaxEncashmentAllowedInQuarter());
            ecgto.setMaxEncashmentAllowedInYear(ecgListDetails.get().getMaxEncashmentAllowedInYear());
            ecgto.setMinimumBalanceForEncashment(ecgListDetails.get().getMinimumBalanceForEncashment());
            ecgto.setMinLeaveBalanceAfterEncashment(ecgListDetails.get().getMinLeaveBalanceAfterEncashment());
            if (encashmentCalendarGroupTo!=null) {
                appResponse = AppResponse.success("Data  found successfully", ecgto);
            } else {
                appResponse = AppResponse.error("No data in the database");
            }
        } catch (Exception e) {
            logger.error("Error getting list", e);
            appResponse = AppResponse.unknownError();
        }
        return appResponse;
    }

    @Override
    public AppResponse<Object> updateECGgroup(@RequestBody TmEncashmentCalendarGroupTo encashmentCalendarGroupTo) {
        AppResponse<Object> appResponse;

        try{
            Optional<TmEncashmentCalenderGroup> existingEcg=encashmentCalenderGroupRepository.findById(encashmentCalendarGroupTo.getEncashmentCalenderGroupID());
            UserInfo userInfo = sessionService.getUserInfo();

            if(existingEcg.isPresent()){
                TmEncashmentCalenderGroup updateEcg=existingEcg.get();
                updateEcg.setActive(encashmentCalendarGroupTo.getActive());
                updateEcg.setModifiedBy(userInfo.getUserId());
                updateEcg.setModifiedDate(new Date());
                encashmentCalenderGroupRepository.save(updateEcg);
                appResponse = AppResponse.success("Data updated succesfully", existingEcg);
            }else{
                appResponse = AppResponse.error("No data in the database")  ;
            }
        }catch (Exception e){
            logger.error("Error updating Data.", e);
            appResponse = AppResponse.unknownError();
        }
        return  appResponse;
    }
}
