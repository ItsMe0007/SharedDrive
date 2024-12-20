package com.peoplestrong.timeoff.encashment.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.peoplestrong.timeoff.encashment.pojo.EncModuleConfigurationTO;

@Service
public interface EncModuleConfigurationService extends Serializable {


    public Map<String, EncModuleConfigurationTO> getModuleConfigMapForEntity(Integer organizationId, List<String> params, Integer entityId)
            throws Exception;
    
    Boolean getIsMultiCalendarEnabledModuleConfig(Integer organizationId);
}
