package com.peoplestrong.timeoff.encashment.controller.impl;


import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.controller.AbstractController;
import com.peoplestrong.timeoff.encashment.controller.EncashmentTypeController;
import com.peoplestrong.timeoff.encashment.pojo.DropdownResponseTO;
import com.peoplestrong.timeoff.encashment.pojo.EncashmentTypeRequestTO;
import com.peoplestrong.timeoff.encashment.pojo.EncashmentTypeResponseTO;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import com.peoplestrong.timeoff.encashment.service.EncashmentTypeService;
import com.peoplestrong.timeoff.encashment.service.common.SessionService;
import com.peoplestrong.timeoff.encashment.utils.AppLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class EncashmentTypeControllerImpl extends AbstractController implements EncashmentTypeController {

    private static final AppLogger logger = AppLogger.get(EncashmentTypeControllerImpl.class);

    final EncashmentTypeService encashmentTypeService;

    @Autowired
    public EncashmentTypeControllerImpl(EncashmentTypeService encashmentTypeService, SessionService sessionService) {
        this.encashmentTypeService = encashmentTypeService;
    }

    @Override
    public AppResponse<List<EncashmentTypeResponseTO>> listEncashmentTypes() {
        try {
            List<EncashmentTypeResponseTO> response = encashmentTypeService.listEncashmentTypes();
            return AppResponse.success("Successfully fetched encashment types", response);
        } catch (AppRuntimeException e) {
            logger.error("Error fetching encashment types", e);
            return AppResponse.error(e.getMessage());
        }
    }


    @Override
    public AppResponse<DropdownResponseTO> getDropdowns(@RequestBody EncashmentTypeRequestTO request) {
        AppResponse<DropdownResponseTO> appResponse;
        try {
            DropdownResponseTO dropdowns = encashmentTypeService.getDropdowns(request);
            appResponse = AppResponse.success("Dropdowns retrieved successfully", dropdowns);
        } catch (Exception e) {
            logger.error("Error fetching dropdowns", e);
            appResponse = AppResponse.unknownError();
        }
        return appResponse;
    }

    @Override
    public AppResponse<EncashmentTypeResponseTO> viewDetail(@RequestBody Long id) {
        try {
            EncashmentTypeResponseTO response = encashmentTypeService.viewDetail(id);
            return AppResponse.success("Successfully fetched encashment type detail", response);
        } catch (AppRuntimeException e) {
            logger.error("Error fetching encashment type detail", e);
            return AppResponse.error(e.getMessage());
        }
    }

    @Override
    public AppResponse<Void> saveEncashmentType(@RequestBody EncashmentTypeRequestTO request) {
        try {
            encashmentTypeService.saveEncashmentType(request);
            return AppResponse.success("Encashment type saved successfully", null);
        } catch (AppRuntimeException e) {
            logger.error("Error saving encashment type", e);
            return AppResponse.error(e.getMessage());
        }
    }

    @Override
    public AppResponse<Object> updateEncashmentType(@RequestBody EncashmentTypeRequestTO request) {
        try {
            encashmentTypeService.updateEncashmentType(request);
            return AppResponse.success("Encashment type updated successfully", null);
        } catch (AppRuntimeException e) {
            logger.error("Error updating encashment type", e);
            return AppResponse.error(e.getMessage());
        }
    }
}

