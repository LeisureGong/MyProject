package com.leisure.myproject.oss;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageService {


    // 上传文件
    String uploadSingleFile(MultipartFile file);


}
