package com.peoplestrong.timeoff.encashment.controller.impl;

import com.peoplestrong.timeoff.encashment.pojo.AgeBasedEncashmentRequestTo;
import com.peoplestrong.timeoff.encashment.pojo.AgeBasedEncashmentResponseTo;
import com.peoplestrong.timeoff.encashment.pojo.TmAgeBasedEncashmentDetailResponseTo;
import com.peoplestrong.timeoff.encashment.pojo.TmAgeBasedEncashmentRequestTo;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.encashment.service.common.SessionService;
import com.peoplestrong.timeoff.common.controller.AbstractController;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.encashment.controller.TmAgeBasedEncashmentController;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmAgeBasedEncashment;
import com.peoplestrong.timeoff.encashment.service.TmAgeBasedEncashmentService;
import com.peoplestrong.timeoff.encashment.utils.AppLogger;
import com.peoplestrong.timeoff.multilingual.MultiLingualMessageRemoteFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TmAgeBasedEncashmentControllerImpl extends AbstractController implements TmAgeBasedEncashmentController {

    private static final AppLogger logger = AppLogger.get(TmAgeBasedEncashmentControllerImpl.class);

    private final TmAgeBasedEncashmentService service;

    final MultiLingualMessageRemoteFacade multiLingualMessageRemoteFacade;
    final SessionService sessionService;

    @Autowired
    public TmAgeBasedEncashmentControllerImpl(TmAgeBasedEncashmentService service, MultiLingualMessageRemoteFacade multiLingualMessageRemoteFacade, SessionService sessionService) {
        this.service = service;
        this.multiLingualMessageRemoteFacade = multiLingualMessageRemoteFacade;
        this.sessionService = sessionService;
    }

    @Override
    public AppResponse<AgeBasedEncashmentResponseTo> createEncashment(@RequestBody AgeBasedEncashmentRequestTo request) {
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            AgeBasedEncashmentResponseTo savedEncashment = service.save(request, userInfo);
            return AppResponse.success("Encashment created successfully", savedEncashment);
        } catch (AppRuntimeException e) {
            logger.error(logger.getShortLog(e), e);
            return AppResponse.error(e.getDisplayMsg());
        } catch (Exception e) {
            logger.error("Error creating encashment", e);
            return AppResponse.unknownError();
        }
    }

    @Override
    public AppResponse<List<TmAgeBasedEncashment>> getEncashment() {
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            List<TmAgeBasedEncashment> encashment = service.findAll(userInfo.getTenantId());
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
    public AppResponse<TmAgeBasedEncashmentDetailResponseTo> getAgeBasedEncashmentDetails(
            @RequestBody TmAgeBasedEncashmentRequestTo tmAgeBasedEncashmentRequestTo) {
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            TmAgeBasedEncashmentDetailResponseTo encashmentDetails = service.findAgeBasedEncashmentDetails(userInfo.getTenantId(), userInfo.getOrganizationId(), tmAgeBasedEncashmentRequestTo.getAgeBasedEncashmentId());
            if (encashmentDetails != null && !encashmentDetails.getAgeBasedEncashmentDetailsResponse().isEmpty()) {
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
    public AppResponse<TmAgeBasedEncashment> updateEncashment(@RequestBody TmAgeBasedEncashmentRequestTo request) {
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            TmAgeBasedEncashment updatedEncashment = service.update(request, userInfo);
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