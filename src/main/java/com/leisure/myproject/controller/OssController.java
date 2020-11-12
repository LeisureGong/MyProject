package com.leisure.myproject.controller;


import com.leisure.myproject.oss.OSSUtil;
import com.leisure.myproject.oss.OssFileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Api(tags = "OSS")
@RestController
@RequestMapping("/oss")
public class OssController {

    @ApiOperation("上传文件至aliyun")
    @PostMapping(value="/upload",headers="content-type=multipart/form-data")
    public String upload(@RequestParam(value = "file") MultipartFile uploadFile) throws Exception {
        return OssFileUtils.uploadSingleFile(uploadFile);
    }

    @ApiOperation("上传视频")
    @PostMapping(value = "uploadVideo")
    public String uploadVideo(@RequestParam(value = "file") MultipartFile uploadFile) {
        return OSSUtil.uploadVod(uploadFile);
    }

    @ApiOperation("删除文件")
    @GetMapping(value = "/delete")
    public boolean delete(@RequestParam(value = "url") String url) {
        return OSSUtil.delete(url);
    }

    @ApiOperation("查询url是否存在")
    @GetMapping(value = "/urlExists")
    public boolean isUrlExists(@RequestParam(value = "url") String url) {
        boolean flag = OSSUtil.isUrlExists(url);
        return flag;
    }

    @ApiOperation("下载文件到指定目录")
    @GetMapping(value = "/download2Dir")
    public File download2Fir(@RequestParam(value = "url") String url,
                             @RequestParam(value = "dir") String dir) {
        File file = OSSUtil.download2Dir(url, dir);
        return file;
    }

}
