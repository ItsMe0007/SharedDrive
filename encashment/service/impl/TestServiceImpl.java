package com.peoplestrong.timeoff.encashment.service.impl;

import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.impl.pojo.SelectItem;
import com.peoplestrong.timeoff.encashment.pojo.TestRequestTO;
import com.peoplestrong.timeoff.encashment.service.TestService;
import com.peoplestrong.timeoff.encashment.utils.AppLogger;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TestServiceImpl implements TestService {

    private static final AppLogger logger = AppLogger.get(TestServiceImpl.class);
    private static final Random random = new Random();

    @Override
    public SelectItem testDetails(TestRequestTO request, UserInfo userInfo) throws AppRuntimeException, IllegalStateException {
        logger.info("Reached Service");

        if (random.nextBoolean()) {
            if (random.nextBoolean()) {
                throw new AppRuntimeException("Just Kidding");
            } else {
                throw new IllegalStateException("Aborting");
            }
        }
        return new SelectItem("Hello World!", 2000);
    }
}
