package com.peoplestrong.timeoff.encashment.mss.controller;

import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.common.controller.AbstractController;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.exception.CountryMappingException;
import com.peoplestrong.timeoff.common.impl.pojo.SelectItemForLeaveStage;
import com.peoplestrong.timeoff.common.impl.pojo.SelectItemForLeaveType;
import com.peoplestrong.timeoff.encashment.mss.pojo.DayViewDTOin;
import com.peoplestrong.timeoff.encashment.mss.pojo.DayViewPageDTO;
import com.peoplestrong.timeoff.encashment.mss.service.EncashmentMssService;
import com.peoplestrong.timeoff.encashment.mss.service.impl.EncashmentMssServiceImpl;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import com.peoplestrong.timeoff.encashment.service.common.SessionService;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.translation.service.TranslationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = AppConstant.PRE_URL + AppConstant.ENCASHMENT_MSS_URL)
@Api(tags = { AppConstant.TagName.ENCASHMENT_MSS_PAGE })

public class EncashmentMssController extends AbstractController {
    
    @Autowired
    EncashmentMssService encashmentMssService;
    
    @Autowired
    SessionService sessionService;
    
    @Autowired
    TranslationService translationService;

    @ApiOperation("Mss List")
    @RequestMapping(path = "/fetchMssData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppResponse<?> msslist(@RequestBody DayViewDTOin dayViewDTOin){
        DayViewPageDTO dayViewPageDTO = null;
        try{
            UserInfo userInfo =  sessionService.getUserInfo();
            dayViewPageDTO = encashmentMssService.dayViewMssPage(userInfo,dayViewDTOin);
            if (dayViewPageDTO != null &&  dayViewPageDTO.getDayViewList() != null){
                Integer orgId = userInfo.getOrganizationId();
                int bundleId = userInfo.getBundleId();
            }
            return AppResponse.success("dayViewPage",dayViewPageDTO);

        } catch (Exception e){
            return AppResponse.error("Failure");
        }
    }

    @RequestMapping(path = "/fetchMssPageFilters", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppResponse<Object> mssPageFilters() throws Exception {
        Map<String, Object> apiResponseObj = null;
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            apiResponseObj = encashmentMssService.getMssFilters(userInfo);

            int orgId = userInfo.getOrganizationId();
            int bundleId = userInfo.getBundleId();

            apiResponseObj.put(EncashmentMssServiceImpl.LEAVE_TYPE_KEY,
                    translationService.translateLabels(
                            (List<SelectItemForLeaveType>) apiResponseObj.get(EncashmentMssServiceImpl.LEAVE_TYPE_KEY),
                            bundleId, orgId));

            apiResponseObj.put(EncashmentMssServiceImpl.LEAVE_STAGE_KEY,
                    translationService.translateLabels(
                            (List<SelectItemForLeaveStage>) apiResponseObj.get(EncashmentMssServiceImpl.LEAVE_STAGE_KEY),
                            bundleId, orgId));

        } catch (CountryMappingException | AppRuntimeException e) {
            return AppResponse.error("Request failed");
        }
        
        return AppResponse.success("success",apiResponseObj);
    }
}
