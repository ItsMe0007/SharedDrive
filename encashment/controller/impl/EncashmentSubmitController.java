package com.peoplestrong.timeoff.encashment.controller.impl;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import com.peoplestrong.timeoff.encashment.constant.IEncashmentErrorConstants;
import com.peoplestrong.timeoff.encashment.service.EncashmentNotificationService;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentTaskActionRequestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.common.controller.AbstractController;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import com.peoplestrong.timeoff.encashment.service.EncashmentService;
import com.peoplestrong.timeoff.encashment.service.common.SessionService;
import com.peoplestrong.timeoff.encashment.transport.input.EncashmentRequestRestTO;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = AppConstant.PRE_URL + AppConstant.ENCASHMENT_PRE_URL)
@Api(tags = {AppConstant.TagName.ENCASHMENT})
public class EncashmentSubmitController extends AbstractController {

    @Autowired
    EncashmentService encashmentService;

    @Autowired
    SessionService sessionService;

    @Autowired
    EncashmentNotificationService encashmentNotificationService;

//    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @ApiOperation("submitEncashmentTransaction")
    @RequestMapping(path = "/submitEncashment-transaction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppResponse<Object> submitEncashmentTransaction(HttpServletRequest httpServletRequest, @RequestBody EncashmentRequestRestTO restRequest)
            throws Exception
    {
        Object resultObject = null;
        try {
           UserInfo userInfo = sessionService.getUserInfo();
            ResourceBundle resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(userInfo.getBundleName()));
            if(userInfo==null || userInfo.getOrganizationId()==0 || userInfo.getEmployeeId()==0 || userInfo.getUserId()==0) {
                return AppResponse.error("Invalid Request");
            }
            resultObject = encashmentService.doEncashmentTransaction(restRequest, userInfo);
            Object finalResultObject = resultObject;
//            executorService.submit(()->encashmentNotificationService.sendEncMobileNotification(restRequest, finalResultObject, userInfo.getOldUserInfo(), resourceBundle));
        } catch (AppRuntimeException e) {
            return AppResponse.error(e.getDisplayMsg());
        } catch (Exception e) {
            return AppResponse.unknownError();
        }
        return AppResponse.success("Success", resultObject);
    }
    
    @ApiOperation("taskAction")
    @RequestMapping(path = "/encashmentTaskAction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppResponse<Object> doTaskAction(HttpServletRequest httpServletRequest, @RequestBody EncashmentTaskActionRequestTO request) throws Exception {
        Object resultObject = null;
        String errorMessage = null;
        ResourceBundle resourceBundle = null;
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            if (userInfo == null || userInfo.getOrganizationId() == 0 || userInfo.getEmployeeId() == 0 || userInfo.getUserId() == 0)
                return AppResponse.error("Invalid Request");
            resourceBundle = ResourceBundle.getBundle(AppConstant.TALENTPACT_MESSAGE_BUNDLE_NAME, new Locale(userInfo.getBundleName()));
            resultObject = encashmentService.doEncashmentTaskAction(request, userInfo);
            Object finalResultObject = resultObject;
            final ResourceBundle finalResourceBundle = resourceBundle;
//            executorService.submit(()->encashmentNotificationService.sendEncMobileNotificationForLeaveAction(finalResultObject, userInfo.getOldUserInfo(), finalResourceBundle, request));
        } catch (AppRuntimeException e) {
            return AppResponse.error(e.getDisplayMsg());
        } catch (Exception e) {
            if (resourceBundle != null)
                errorMessage = resourceBundle.getString(IEncashmentErrorConstants.TASK_ACTION_API_NOT_RESPONDING);
            else
                errorMessage = e.getMessage();
            return AppResponse.error(errorMessage);
        }
        return AppResponse.success("Success Message", resultObject);
    }
}
