package com.java.scm.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.service.LicenseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author hujunhui
 * @date 2020/7/8
 */
@RestController
@RequestMapping("/license")
public class LicenseController {

    @Resource
    private LicenseService licenseService;

    @PostMapping("/init")
    public BaseResult init(@RequestBody JSONObject params){
        String license = params.getString("license");
        return licenseService.init(license);
    }

    @GetMapping("/date")
    public BaseResult date(){
        return licenseService.getLicenseDate();
    }
}
