package com.leisure.myproject.controller;


import com.leisure.myproject.dto.SiteQueryReqDTO;
import com.leisure.myproject.entity.SiteInfoEntity;
import com.leisure.myproject.service.SiteManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "打个招呼")
@RestController
@RequestMapping("/api/hello")
public class HelloController {

	@Autowired
	SiteManageService siteManageService;

	@ApiOperation(value = "你好呀")
	@GetMapping("/addHello")
	public String addHello(){
		return "你好你好你好你好你好";
	}

	@ApiOperation(value = "查询场站信息")
	@PostMapping("/querySite")
	public List<SiteInfoEntity> querySite(@RequestBody SiteQueryReqDTO siteQueryReqDTO){
		return(siteManageService.querySite(siteQueryReqDTO));
	}
}
