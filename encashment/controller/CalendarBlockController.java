package com.peoplestrong.timeoff.encashment.controller;

import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.encashment.pojo.CalenderBlockTo;
import com.peoplestrong.timeoff.encashment.pojo.EncashmentRequestTo;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = AppConstant.PRE_URL + AppConstant.ENCASHMENT_PRE_URL)
@Api(tags = {AppConstant.TagName.ENCASHMENT})
public interface CalendarBlockController {


    @ApiOperation("Prepare Test details")
    @PostMapping(path = "/validate")
    AppResponse<Object> validateDetails(@RequestBody EncashmentRequestTo request);


    @ApiOperation("Prepare Test details")
    @GetMapping(path = "/getCalenderList")
    AppResponse<Object> getcalenderList();


    @ApiOperation("Prepare Test details")
    @PostMapping(path = "/getcalenderListDetails")
    AppResponse<Object> getcalenderListDetails(@RequestBody CalenderBlockTo calenderBlockTo);

    @ApiOperation("Prepare Test details")
    @PostMapping(path = "/updateCalenderlistdetails")
    AppResponse<Object> updatecalenderlistdetails(@RequestBody CalenderBlockTo calenderBlockTo);
}

