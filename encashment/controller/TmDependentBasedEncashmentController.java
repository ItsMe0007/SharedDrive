package com.peoplestrong.timeoff.encashment.controller;

import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmDependentBasedEncashment;
import com.peoplestrong.timeoff.encashment.pojo.*;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = AppConstant.PRE_URL + AppConstant.ENCASHMENT_PRE_URL)
@Api(tags = {AppConstant.TagName.ENCASHMENT})
public interface TmDependentBasedEncashmentController {

    @ApiOperation("Dependent-based encashment")
    @PostMapping("/dependent-based-encashment/add-details")
    AppResponse<DependentBasedDetailResponseTo> createDependentBasedEncashment(@RequestBody DependentBasedDetailRequestAllTo request);

    @ApiOperation("Get details of dependent-based encashment ")
    @GetMapping("/dependent-based-encashment/get-details")
    AppResponse<List<TmDependentBasedEncashment>> getEncashment();

    @ApiOperation("Get Dependent Based Encashment Detail By Using Configuration Name")
    @PostMapping("/dependent-based-encashment-details/get-details")
    AppResponse<DependentBasedEncashmentResponseTo> getDependentBasedEncashmentDetails(@RequestBody DependentBasedRequestTo dependentBasedRequestTo);

    @ApiOperation("Update an existing Age Based Encashment record")
    @PostMapping("/dependent-based-encashment/update-details")
    AppResponse<TmDependentBasedEncashment> updateEncashment(@RequestBody DependentBasedRequestTo request);

}
