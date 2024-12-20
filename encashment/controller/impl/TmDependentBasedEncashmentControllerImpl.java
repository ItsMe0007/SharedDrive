package com.peoplestrong.timeoff.encashment.controller.impl;

import com.peoplestrong.timeoff.common.controller.AbstractController;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmDependentBasedEncashment;
import com.peoplestrong.timeoff.encashment.controller.TmDependentBasedEncashmentController;
import com.peoplestrong.timeoff.encashment.pojo.*;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import com.peoplestrong.timeoff.encashment.service.TmDependentBasedEncashmentService;
import com.peoplestrong.timeoff.encashment.service.common.SessionService;
import com.peoplestrong.timeoff.encashment.utils.AppLogger;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.multilingual.MultiLingualMessageRemoteFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TmDependentBasedEncashmentControllerImpl extends AbstractController implements TmDependentBasedEncashmentController {

    private static final AppLogger logger = AppLogger.get(TmDependentBasedEncashmentControllerImpl.class);

    private final TmDependentBasedEncashmentService tmDependentBasedEncashmentService;
    private final MultiLingualMessageRemoteFacade multiLingualMessageRemoteFacade;
    private final SessionService sessionService;
    private final TmDependentBasedEncashmentService service;

    @Autowired
    public TmDependentBasedEncashmentControllerImpl(
            TmDependentBasedEncashmentService tmDependentBasedEncashmentService,
            MultiLingualMessageRemoteFacade multiLingualMessageRemoteFacade,
            SessionService sessionService, TmDependentBasedEncashmentService service) {
        this.tmDependentBasedEncashmentService = tmDependentBasedEncashmentService;
        this.multiLingualMessageRemoteFacade = multiLingualMessageRemoteFacade;
        this.sessionService = sessionService;
        this.service = service;
    }

    @Override
    public AppResponse<DependentBasedDetailResponseTo> createDependentBasedEncashment(@RequestBody DependentBasedDetailRequestAllTo request) {
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            DependentBasedDetailResponseTo response = service.save(request, userInfo);
            return AppResponse.success("Dependent-based encashment record created successfully", response);
        } catch (AppRuntimeException e) {
            logger.error(logger.getShortLog(e), e);
            return AppResponse.error(e.getDisplayMsg());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating the dependent-based encashment record.", e);
            return AppResponse.unknownError();
        }
    }

    public AppResponse<List<TmDependentBasedEncashment>> getEncashment() {
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            List<TmDependentBasedEncashment> encashment = service.findDependentBasedDetail(userInfo.getTenantId());
            if (encashment != null) {
                return AppResponse.success("Encashment retrieved successfully", encashment);
            } else {
                return AppResponse.error("Encashment not found");
            }
        } catch (AppRuntimeException e) {
            logger.error(logger.getShortLog(e), e);
            return AppResponse.error(e.getDisplayMsg());
        } catch (Exception e) {
            logger.error("Error retrieving encashment", e);
            return AppResponse.unknownError();
        }
    }

    @Override
    public AppResponse<DependentBasedEncashmentResponseTo> getDependentBasedEncashmentDetails(@RequestBody DependentBasedRequestTo dependentBasedRequestTo) {
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            DependentBasedEncashmentResponseTo encashmentDetails = service.findEncashmentDetails(userInfo.getTenantId(), userInfo.getOrganizationId(), dependentBasedRequestTo.getDependentBasedEncashmentID());
            if (encashmentDetails != null && !encashmentDetails.getDependentBasedEncashmentDetailResponseTos().isEmpty()) {
                return AppResponse.success("Encashment details retrieved successfully", encashmentDetails);
            } else {
                return AppResponse.error("No encashment details found");
            }
        } catch (AppRuntimeException e) {
            logger.error("Error retrieving age-based encashment details", e);
            return AppResponse.error(e.getDisplayMsg());
        } catch (Exception e) {
            logger.error("Unexpected error retrieving age-based encashment details", e);
            return AppResponse.unknownError();
        }
    }

    @Override
    public AppResponse<TmDependentBasedEncashment> updateEncashment(@RequestBody DependentBasedRequestTo request) {
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            TmDependentBasedEncashment updatedEncashment = service.updateEncashment(request, userInfo);
            if (updatedEncashment != null) {
                return AppResponse.success("Encashment updated successfully", updatedEncashment);
            } else {
                return AppResponse.error("Encashment not found");
            }
        } catch (AppRuntimeException e) {
            logger.error(logger.getShortLog(e), e);
            return AppResponse.error(e.getDisplayMsg());
        } catch (Exception e) {
            logger.error("Error updating encashment", e);
            return AppResponse.unknownError();
        }
    }
}
