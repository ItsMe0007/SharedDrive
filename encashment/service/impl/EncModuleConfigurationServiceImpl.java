package com.peoplestrong.timeoff.encashment.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.peoplestrong.timeoff.encashment.constant.EncashmentConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peoplestrong.timeoff.common.constant.ModuleConfigConstant;
import com.peoplestrong.timeoff.common.util.PSCollectionUtil;
import com.peoplestrong.timeoff.dataservice.model.leave.SysModuleConfiguration;
import com.peoplestrong.timeoff.dataservice.model.leave.SysParameter;
import com.peoplestrong.timeoff.dataservice.model.leave.SysSubModule;
import com.peoplestrong.timeoff.dataservice.repo.leave.SysModuleConfigurationRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.SysParameterRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.SysSubModuleRepository;
import com.peoplestrong.timeoff.encashment.pojo.EncModuleConfigurationTO;
import com.peoplestrong.timeoff.encashment.service.EncModuleConfigurationService;


@Service
public class EncModuleConfigurationServiceImpl implements EncModuleConfigurationService {

    @Autowired
    SysParameterRepository sysParameterRepository;

    @Autowired
    SysSubModuleRepository sysSubModuleRepository;

    @Autowired
    SysModuleConfigurationRepository sysModuleConfigurationRepository;
    @Override
    public Map<String, EncModuleConfigurationTO> getModuleConfigMapForEntity(Integer organizationId, List<String> params, Integer entityId) throws Exception {
        Map<Integer, String> paramIdToparamNameMap = null;
        Map<String, EncModuleConfigurationTO> moduleConfigMap = null;

        List<SysParameter> sysParameters = sysParameterRepository.findByParamLabelIn(params);

        if (PSCollectionUtil.isNotNullOrEmpty(sysParameters)) {

            paramIdToparamNameMap = new HashMap<Integer, String>();
            for (SysParameter sysParameter : sysParameters) {
                paramIdToparamNameMap.put(sysParameter.getParamId(), sysParameter.getParamLabel());

            }

            SysSubModule sysSubModule = sysSubModuleRepository.findByModuleName(ModuleConfigConstant.LEAVE_MODULE_NAME);

            if (PSCollectionUtil.isMapNotNullOrEmpty(paramIdToparamNameMap) && sysSubModule.getModuleId() != null && sysSubModule.getModuleId() != 0) {
                List<SysModuleConfiguration> sysModuleConfiguration = sysModuleConfigurationRepository.findByOrganizationIdAndModuleIdAndParamIdInAndEntityId(organizationId,
                        sysSubModule.getModuleId(), paramIdToparamNameMap.keySet(), entityId);
                if (PSCollectionUtil.isNotNullOrEmpty(sysModuleConfiguration)) {
                    moduleConfigMap = new HashMap<String, EncModuleConfigurationTO>();
                    for (SysModuleConfiguration sysModuleConfiguration1 : sysModuleConfiguration) {
                        EncModuleConfigurationTO moduleConfigurationTO = new EncModuleConfigurationTO();
                        moduleConfigurationTO.setValue(sysModuleConfiguration1.getValue());
                        moduleConfigMap.put(paramIdToparamNameMap.get(sysModuleConfiguration1.getParamId()), moduleConfigurationTO);
                    }
                }

            }

        }
        return moduleConfigMap;
    }
    
    @Override
    public Boolean getIsMultiCalendarEnabledModuleConfig(Integer organizationId) {
        List<SysParameter> multiCalendarConfigParameters = sysParameterRepository.findByParamLabelIn(Collections.singletonList(EncashmentConstant.MULTI_CALENDAR_PARAMETER));
        if (PSCollectionUtil.isNotNullOrEmpty(multiCalendarConfigParameters)){
            Set<Integer> paramIds = multiCalendarConfigParameters.stream().map(SysParameter::getParamId).collect(Collectors.toSet());
            if (PSCollectionUtil.isNotNullOrEmpty(paramIds)){
                SysSubModule subModule = sysSubModuleRepository.findByModuleName(EncashmentConstant.ENCASHMENT_MODULE_NAME);
                if (subModule != null){
                    Integer moduleId = subModule.getParentModuleId();
                    List<SysModuleConfiguration> moduleConf = sysModuleConfigurationRepository.findByOrganizationIdAndModuleIdAndParamIdIn(organizationId,moduleId,paramIds);
                    if (PSCollectionUtil.isNotNullOrEmpty(moduleConf) && moduleConf.get(0) != null && moduleConf.get(0).getValue() != null){
                        return Boolean.valueOf(moduleConf.get(0).getValue());
                    } else return false;
                } else return false;
            } else return false;
        } else return false;
    }
}
