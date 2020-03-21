package com.leisure.myproject.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "打个招呼")
@RestController
@RequestMapping("/api/hello")
public class HelloController {

	@ApiOperation(value = "你好呀")
	@GetMapping("/addHello")
	public void addHello(){
		System.out.println("你好哈好啊好哦嗷嗷哈后!");
	}
}
