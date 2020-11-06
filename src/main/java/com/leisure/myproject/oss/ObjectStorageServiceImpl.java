package com.leisure.myproject.oss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ObjectStorageServiceImpl implements ObjectStorageService{

    @Override
    public String uploadSingleFile(MultipartFile file) {
        String path = OssFileUtils.uploadSingleFile(file);
        return path;
    }
}
