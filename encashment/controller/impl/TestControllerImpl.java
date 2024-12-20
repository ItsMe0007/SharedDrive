package com.peoplestrong.timeoff.encashment.controller.impl;

import com.peoplestrong.timeoff.common.controller.AbstractController;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.impl.pojo.SelectItem;
import com.peoplestrong.timeoff.encashment.controller.TestController;
import com.peoplestrong.timeoff.encashment.pojo.TestRequestTO;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import com.peoplestrong.timeoff.encashment.service.TestService;
import com.peoplestrong.timeoff.encashment.service.common.SessionService;
import com.peoplestrong.timeoff.encashment.utils.AppLogger;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.multilingual.MultiLingualMessageRemoteFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControllerImpl extends AbstractController implements TestController {

    private static final AppLogger logger = AppLogger.get(TestControllerImpl.class);


    final TestService testService;
    final MultiLingualMessageRemoteFacade multiLingualMessageRemoteFacade;
    final SessionService sessionService;

    @Autowired
    public TestControllerImpl(TestService testService, MultiLingualMessageRemoteFacade multiLingualMessageRemoteFacade, SessionService sessionService) {
        this.testService = testService;
        this.multiLingualMessageRemoteFacade = multiLingualMessageRemoteFacade;
        this.sessionService = sessionService;
    }


    @Override
    public AppResponse<Object> testDetails(TestRequestTO request) {
        AppResponse<Object> appResponse;
        try {
            UserInfo userInfo = sessionService.getUserInfo();
            SelectItem response = testService.testDetails(request, userInfo);
            appResponse = AppResponse.success("Success Message", response);
        } catch (AppRuntimeException e) {
            logger.error(logger.getShortLog(e), e);
            appResponse = AppResponse.error(e.getDisplayMsg());
        } catch (Exception e) {
            logger.error("Something went wrong again!!!", e);

            appResponse = AppResponse.unknownError();
        }
        return appResponse;
    }
}
