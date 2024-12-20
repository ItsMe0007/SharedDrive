package com.peoplestrong.timeoff.encashment.controller;


import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.encashment.pojo.DropdownResponseTO;
import com.peoplestrong.timeoff.encashment.pojo.EncashmentTypeRequestTO;
import com.peoplestrong.timeoff.encashment.pojo.EncashmentTypeResponseTO;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = AppConstant.PRE_URL + AppConstant.ENCASHMENT_PRE_URL)
@Api(tags = {AppConstant.TagName.ENCASHMENT})
public interface EncashmentTypeController {

    @ApiOperation("Get list of encashment types")
    @GetMapping(path = "/encashmenttype/list")
    AppResponse<List<EncashmentTypeResponseTO>> listEncashmentTypes();


    @ApiOperation("Get dropdown values for encashment type")
    @PostMapping(path = "/encashmenttype/dropdowns")
    AppResponse<DropdownResponseTO> getDropdowns(@RequestBody EncashmentTypeRequestTO request);

    @ApiOperation("View detail of encashment type")
    @PostMapping(path = "/encashmenttype/viewdetail")
    AppResponse<EncashmentTypeResponseTO> viewDetail(@RequestBody Long id);

    @ApiOperation("Save new encashment type")
    @PostMapping(path = "/encashmenttype/save")
    AppResponse<Void> saveEncashmentType(@RequestBody EncashmentTypeRequestTO request);

    @ApiOperation("Update existing encashment type")
    @PostMapping(path = "/encashmenttype/update")
    AppResponse<Object> updateEncashmentType(@RequestBody EncashmentTypeRequestTO request);
}
