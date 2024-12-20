package com.peoplestrong.timeoff.encashment.controller;

import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmAgeBasedEncashment;
import com.peoplestrong.timeoff.encashment.pojo.AgeBasedEncashmentRequestTo;
import com.peoplestrong.timeoff.encashment.pojo.AgeBasedEncashmentResponseTo;
import com.peoplestrong.timeoff.encashment.pojo.TmAgeBasedEncashmentDetailResponseTo;
import com.peoplestrong.timeoff.encashment.pojo.TmAgeBasedEncashmentRequestTo;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = AppConstant.PRE_URL + AppConstant.ENCASHMENT_PRE_URL)
@Api(tags = {AppConstant.TagName.ENCASHMENT})

public interface TmAgeBasedEncashmentController {

    @ApiOperation("Create a new Age Based Encashment record")
    @PostMapping("/age-based-encashment/add-details")
    AppResponse<AgeBasedEncashmentResponseTo> createEncashment(@RequestBody AgeBasedEncashmentRequestTo request);

    @ApiOperation("Get details of Age Based Encashment records")
    @GetMapping("/age-based-encashment/get-details")
    AppResponse<List<TmAgeBasedEncashment>> getEncashment();

    @ApiOperation("Get Age Based Encashment Detail By Using Configuration Name")
    @PostMapping("/age-based-encashment-details/get-details")
    AppResponse<TmAgeBasedEncashmentDetailResponseTo> getAgeBasedEncashmentDetails(@RequestBody TmAgeBasedEncashmentRequestTo tmAgeBasedEncashmentRequestTo);

    @ApiOperation("Update an existing Age Based Encashment record")
    @PostMapping("/age-based-encashment/update-details")
    AppResponse<TmAgeBasedEncashment> updateEncashment(@RequestBody TmAgeBasedEncashmentRequestTo request);

}