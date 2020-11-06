package com.leisure.myproject.oss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作工具类
 * @author gonglei
 * @date 2020/11/6
 */
@Slf4j
public class OssFileUtils {

    /**
     * 单个文件上传
     * @param
     * @return
     * @date 2020/11/6
     */
    public static String uploadSingleFile(MultipartFile file) {
        if (file == null) {
            log.error("单个文件上传失败，文件为空");
            return null;
        }

        try {
            String key = OSSUtil.generateKey(file.getOriginalFilename());
            return OSSUtil.upload(key, file.getBytes());
        } catch (Exception e) {
            log.error("单个文件上传异常：{}", e.getMessage());
        }
        return null;
    }
}
