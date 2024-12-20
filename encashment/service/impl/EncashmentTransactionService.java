package com.peoplestrong.timeoff.encashment.service.impl;
import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.util.RestUtil;
import com.peoplestrong.timeoff.workflow.to.StageRequestTO;
import com.peoplestrong.timeoff.workflow.to.StageResponseTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service
public class EncashmentTransactionService {
    
    @Autowired
    Environment env;
    
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

}
