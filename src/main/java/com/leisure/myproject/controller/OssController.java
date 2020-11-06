package com.leisure.myproject.controller;


import com.leisure.myproject.oss.ObjectStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "OSS")
@RestController
@RequestMapping("/oss")
public class OssController {

    @Autowired
    private ObjectStorageService objectStorageService;

    @ApiOperation("上传文件至aliyun")
    @PostMapping(value="/upload",headers="content-type=multipart/form-data ")
    public String upload(@RequestParam(value = "file") MultipartFile uploadFile) throws Exception {
        return objectStorageService.uploadSingleFile(uploadFile);
    }
}
