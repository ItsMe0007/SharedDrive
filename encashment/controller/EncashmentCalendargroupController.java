package com.peoplestrong.timeoff.encashment.controller;

import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.encashment.pojo.CalenderBlockTo;
import com.peoplestrong.timeoff.encashment.pojo.TmEncashmentCalendarGroupTo;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = AppConstant.PRE_URL + AppConstant.ENCASHMENT_PRE_URL)
@Api(tags = {AppConstant.TagName.ENCASHMENT})
public interface EncashmentCalendargroupController {

    @ApiOperation("Prepare Test details")
    @GetMapping(path = "/getleavetypedetails")
    AppResponse<Object> getleavetypedetails();

    @ApiOperation("Prepare Test details")
    @GetMapping(path = "/getAltGroup")
    AppResponse<Object> getAltGroupdetails();


    @ApiOperation("Prepare Test details")
    @GetMapping("/blocks")
    AppResponse<Object> getAllBlockNames();


    @PostMapping("/block-details")
    AppResponse<Object>  getBlockDetails(@RequestBody CalenderBlockTo  calenderBlockTo);

    @PostMapping("/saveEcg")
    AppResponse<Object>  saveEcg(@RequestBody TmEncashmentCalendarGroupTo encashmentCalendarGroupTo);

    @GetMapping("/getEcgList")
    AppResponse<Object>  getEcg();


    @ApiOperation("Prepare Test details")
    @PostMapping(path = "/getEcgListDetails")
    AppResponse<Object> getEcgListDetails(@RequestBody TmEncashmentCalendarGroupTo encashmentCalendarGroupTo);

    @ApiOperation("Prepare Test details")
    @PostMapping(path = "/updateECGgroup")
    AppResponse<Object> updateECGgroup(@RequestBody TmEncashmentCalendarGroupTo encashmentCalendarGroupTo);
}
