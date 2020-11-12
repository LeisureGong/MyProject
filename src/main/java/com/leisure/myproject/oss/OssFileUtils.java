package com.leisure.myproject.oss;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sun.plugin.javascript.navig.JSType.URL;

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
            // bucket key
            String key = OSSUtil.generateKey(file.getOriginalFilename());
            return OSSUtil.upload(key, file.getBytes());
        } catch (Exception e) {
            log.error("单个文件上传异常：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 上传视频
     * @param
     * @return
     * @date 2020/11/6
     */
    public static String uploadVideoFile(String vodUrl) {
        if (StringUtils.isBlank(vodUrl)) {
            log.error("上传视频失败，文件为空");
            return null;
        }

        try {
            // bucket key
            String key = vodUrl.substring(vodUrl.lastIndexOf("/") + 1);
            return OSSUtil.uploadVideo(key,vodUrl);
        } catch (Exception e) {
            log.error("单个文件上传异常：{}", e.getMessage());
        }
        return null;
    }






    /**
     * 多个文件上传
     * @param fileList 文件参数
     * @return
     * @date 2020/11/10
     */
    public static List<String> uploadMultipartFile(List<MultipartFile> fileList) {

        List<String> filePaths = new ArrayList<>();
        Optional.ofNullable(fileList).ifPresent(fl -> {
            fl.stream().forEach(f -> {
                try {
                    filePaths.add(OSSUtil.upload(OSSUtil.generateKey(f.getOriginalFilename()), f.getBytes()));
                } catch (IOException e) {
                    log.error("多文件上传异常:{}", e.getMessage());
                }
            });
        });
        return filePaths;
    }

    /**
     * 下载OSS文件到本地
     * @param url 阿里云链接
     * @param filePath 下载目录
     * @return
     * @date 2020/11/10
     */
    public static File downloadSingleFile(String url, String filePath) {
        try {
            return OSSUtil.download2File(url, filePath);
        } catch (Exception e) {
            log.error("下载异常");
        }
        return null;
    }

    /**
     * 下载网络文件
     * @param urlPath url网络路径
     * @param saveDir 文件保存本地路径
     * @param fileName 文件自定义冥冥
     * @return
     * @date 2020/11/11
     */
    public static File downloadByUrlPath(String urlPath, String saveDir, String fileName) {
        if (StringUtils.isBlank(urlPath)) {
            log.error("下载文件失败，链接为空");
            return null;
        }
        try (InputStream ins = new URL(urlPath).openStream()) {
            Path target = Paths.get(saveDir, fileName);
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
            return new File(saveDir + File.separator + fileName);
        } catch (IOException e) {
            log.error("下载网络文件异常：{}", e.getMessage());
        }
        return null;
    }
}
