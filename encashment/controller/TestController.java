package com.peoplestrong.timeoff.encashment.controller;

import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.encashment.pojo.TestRequestTO;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = AppConstant.PRE_URL + AppConstant.ENCASHMENT_PRE_URL)
@Api(tags = {AppConstant.TagName.ENCASHMENT})
public interface TestController {

    @ApiOperation("Prepare Test details")
    @PostMapping(path = "/test")
    AppResponse<Object> testDetails(@RequestBody TestRequestTO request);

}
