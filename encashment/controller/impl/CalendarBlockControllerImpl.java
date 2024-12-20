package com.peoplestrong.timeoff.encashment.controller.impl;

import com.peoplestrong.timeoff.common.controller.AbstractController;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmCalenderBlock;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmCalenderBlockDetail;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmCalenderBlockDetailRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmCalenderBlockRepository;
import com.peoplestrong.timeoff.encashment.controller.CalendarBlockController;
import com.peoplestrong.timeoff.encashment.pojo.*;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import com.peoplestrong.timeoff.encashment.service.CalendarBlockService;
import com.peoplestrong.timeoff.encashment.service.TestService;
import com.peoplestrong.timeoff.encashment.service.common.SessionService;
import com.peoplestrong.timeoff.encashment.utils.AppLogger;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.multilingual.MultiLingualMessageRemoteFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CalendarBlockControllerImpl extends AbstractController implements CalendarBlockController {

    private static final AppLogger logger = AppLogger.get(CalendarBlockControllerImpl.class);

    @Autowired
    TmCalenderBlockRepository calenderBlockRepository;

    @Autowired
    TmCalenderBlockDetailRepository calenderBlockDetailRepository;

    final TestService testService;
    final MultiLingualMessageRemoteFacade multiLingualMessageRemoteFacade;
    final SessionService sessionService;
    final CalendarBlockService calendarBlockService;

    @Autowired
    public CalendarBlockControllerImpl(TestService testService, MultiLingualMessageRemoteFacade multiLingualMessageRemoteFacade, SessionService sessionService, CalendarBlockService calendarBlockService) {
        this.testService = testService;
        this.multiLingualMessageRemoteFacade = multiLingualMessageRemoteFacade;
        this.sessionService = sessionService;
        this.calendarBlockService=calendarBlockService;
    }


    @Override
    public AppResponse<Object> validateDetails(@RequestBody EncashmentRequestTo request) {
        AppResponse<Object> appResponse;
        try {
            // Extract the calendar and ranges from the request
            CalenderBlockTo calenderTo = request.getCalenderTo();
            List<DateRange> ranges = request.getRanges();
            UserInfo userInfo = sessionService.getUserInfo();

                String validationMessage = calendarBlockService.validateRanges(calenderTo, ranges,userInfo.getOrganizationId());

                if ("Validation successful".equals(validationMessage)) {
                    Boolean saveSuccess = calendarBlockService.saveData(calenderTo, ranges,userInfo);
                    if (saveSuccess) {
                        appResponse = AppResponse.success("Data saved successfully", null);
                    } else {
                        appResponse = AppResponse.error("Failed to save data");
                    }
                } else {
                    appResponse = AppResponse.error(validationMessage);
                }

        } catch (Exception e) {
            logger.error("Error validating details", e);
            appResponse = AppResponse.unknownError();
        }
        return appResponse;
    }

    @Override
    public AppResponse<Object> getcalenderList() {
        AppResponse<Object> appResponse;

        try{
            UserInfo userInfo = sessionService.getUserInfo();
            List<TmCalenderBlock> calenderBlocks=calenderBlockRepository.findAllByOrgIdandTenantId(userInfo.getOrganizationId(), userInfo.getTenantId());

            if(!calenderBlocks.isEmpty()){
                appResponse = AppResponse.success("Data fetch successfully", calenderBlocks);
            }else{
                appResponse = AppResponse.error("No data in the database");
            }

        }catch (Exception e){
            logger.error("Error getting list", e);
            appResponse = AppResponse.unknownError();
        }
        return  appResponse;
    }

    @Override
    public AppResponse<Object> getcalenderListDetails(@RequestBody CalenderBlockTo calenderBlockTo) {
        AppResponse<Object> appResponse;

        try{
            Long id = calenderBlockTo.getCalenderBlockID();
            List<TmCalenderBlockDetail> calenderBlocklistdetails=calenderBlockDetailRepository.findbyCalenderBlockId(id);
            Optional<TmCalenderBlock> calenderBlock=calenderBlockRepository.findById(id);
            if(!calenderBlocklistdetails.isEmpty()){
                CalenderDetailsResponse response = new CalenderDetailsResponse(calenderBlocklistdetails, calenderBlock);
                appResponse = AppResponse.success("Data  found successfully", response);
            }else{
                appResponse = AppResponse.error("No data in the database");
            }
        }catch (Exception e){
            logger.error("Error getting list", e);
            appResponse = AppResponse.unknownError();
        }
        return  appResponse;
    }

    @Override
    public AppResponse<Object> updatecalenderlistdetails(@RequestBody CalenderBlockTo calenderBlockTo) {
        AppResponse<Object> appResponse;

        try{
            Optional<TmCalenderBlock> existingtmCalenderBlock=calenderBlockRepository.findById(calenderBlockTo.getCalenderBlockID());
            UserInfo userInfo = sessionService.getUserInfo();
            if(existingtmCalenderBlock.isPresent()){
                TmCalenderBlock updatetmCalenderBlock=existingtmCalenderBlock.get();
                updatetmCalenderBlock.setActive(calenderBlockTo.getActive());
                updatetmCalenderBlock.setModifiedBy(userInfo.getUserId());
                updatetmCalenderBlock.setModifiedDate(new Date());
                calenderBlockRepository.save(updatetmCalenderBlock);
                appResponse = AppResponse.success("Data updated succesfully", existingtmCalenderBlock);
            }else{
                appResponse = AppResponse.error("No data in the database")  ;
            }
        }catch (Exception e){
            logger.error("Error getting list", e);
            appResponse = AppResponse.unknownError();
        }
        return  appResponse;
    }
}
