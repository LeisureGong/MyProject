package com.leisure.myproject.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;
import java.util.Random;

/**
 * 阿里云OSS工具类
 * @author gonglei
 * @date 2020/11/5
 */
@Slf4j
public class OSSUtil {

    // 注入配置
    private static OSSProperties ossProperties;


    /**
     * 配置初始化
     * @param
     * @return
     * @date 2020/11/5
     */
    public static void initConfig(OSSProperties aliyunProperties) {
        ossProperties = aliyunProperties;
    }

    /**
     * 生成OSS对象名称
     * @param fileName
     * @return String
     * @date 2020/11/5
     */
    public static String generateKey(String fileName) {
        // 对象名称格式： yyyymmss-dddd
        return new StringBuilder(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"))
                .append("-").append(String.format("%04d", new Random().nextInt(9999)))
                .append(".").append(FilenameUtils.getExtension(fileName))
                .toString();
    }

    public static void main(String[] args) {
        System.out.println(generateKey("hah.jpg"));
    }

//    public static String getHttpsAddress(String url) {
//        return url.replaceAll("http://" + ossProperties.getBucket())
//    }

    /**
     * 上传文件
     * @param key 对象名称
     * @parma file 待上传的文件
     * @return
     * @date 2020/11/5
     */
    public static String upload(String key, File file) {
        if (file == null || !file.exists()) {
            log.error("Aliyun文件上传失败,{}不存在", file);
            return null;
        }

        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        try {
            ossClient.putObject(ossProperties.getBucketName(), key, file);
            // 设置url过期时间
            Date expirationDate = DateUtils.addYears(new Date(), ossProperties.getExpiration());
            String url = ossClient.generatePresignedUrl(ossProperties.getBucketName(), key, expirationDate).toString();
            log.info("Aliyun上传文件结束：文件{} ====> url: {}", file, url);
            return url;
        } catch (Exception e) {
            log.error("Aliyun上传文件异常【{}】", e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return null;
    }

    // todo 多文件，开启5个线程
     /**
      * 上传文件
      * @param
      * @return
      * @date 2020/11/5
      */
     public static String upload(String key, byte[] bytes) {
         if (bytes == null || StringUtils.isBlank(key)) {
             log.error("Aliyun文件上传失败,{}不存在", key);
             return null;
         }

         OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
         try {
             ossClient.putObject(ossProperties.getBucketName(), key, new ByteArrayInputStream(bytes));
             // 设置url过期时间
             Date expirationDate = DateUtils.addYears(new Date(), ossProperties.getExpiration());
             String url = ossClient.generatePresignedUrl(ossProperties.getBucketName(), key, expirationDate).toString();
             log.info("Aliyun上传文件结束：文件{} ====> url: {}", key, url);
             return url;
         } catch (Exception e) {
             log.error("Aliyun上传文件异常【"+key+"】", e);
         } finally {
             ossClient.shutdown();
         }
         return null;
     }

     /**
      * 删除文件
      * @param url 待删除对象的url
      * @return
      * @date 2020/11/5
      */
     public static void delete(String url) {
         if (StringUtils.isBlank(url)) {
             log.error("阿里云删除文件失败，url为空");
             return;
         }
         if (url.contains(ossProperties.getBucket())) {

         }
     }

}
