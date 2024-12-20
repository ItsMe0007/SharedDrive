package com.peoplestrong.timeoff.encashment.service;

import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.encashment.pojo.DropdownResponseTO;
import com.peoplestrong.timeoff.encashment.pojo.EncashmentTypeRequestTO;
import com.peoplestrong.timeoff.encashment.pojo.EncashmentTypeResponseTO;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public interface EncashmentTypeService {

    List<EncashmentTypeResponseTO> listEncashmentTypes() throws AppRuntimeException;

    DropdownResponseTO getDropdowns(EncashmentTypeRequestTO request) throws AppRuntimeException;

    EncashmentTypeResponseTO viewDetail(Long id) throws AppRuntimeException;

    void saveEncashmentType(EncashmentTypeRequestTO request) throws AppRuntimeException;

    AppResponse<Object> updateEncashmentType(EncashmentTypeRequestTO request) throws AppRuntimeException;
}
