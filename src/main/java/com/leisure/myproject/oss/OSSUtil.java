package com.leisure.myproject.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
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
      * @param key 对象名称
      * @param bytes 待上传文件字节流
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
     public static boolean delete(String url) {
         if (StringUtils.isBlank(url)) {
             log.error("阿里云删除文件失败，url为空");
             return false;
         }
         if (url.contains(ossProperties.getBucketName())) {
             // 根据url获取对象名称
             url = getObjectNameByUrl(url);
         }
         OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
         try {
             // 删除文件
             ossClient.deleteObject(ossProperties.getBucketName(), url);
             log.info("阿里云删除文件结束：{}", url);
         } catch (Exception e) {
             log.error("阿里云删除文件异常");
         } finally {
             ossClient.shutdown();
         }
         return true;
     }

     /**
      * 判断文件是否存在
      * @param fileName
      * @return
      * @date 2020/11/11
      */
     public static boolean isFileExists(String fileName) {

         OSS ossClient = new OSSClientBuilder()
                 .build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());

         // 判断文件是否存在
         boolean found = ossClient.doesObjectExist(ossProperties.getBucketName(), fileName);

         return found;
     }

     /**
      * 下载OSS对象到本地文件
      * @param url 待下载对象url
      * @param filePath
      * @return
      * @date 2020/11/10
      */
     public static File download2File(String url, String filePath) {
         if (url.contains(ossProperties.getBucket())) {
             url = getObjectNameByUrl(url);
         }
         OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
         try {
             File file = new File(filePath);
             // 下载OSS文件到本地文件，如果指定的本地文件存在会被覆盖，不存在则新建
             ossClient.getObject(new GetObjectRequest(ossProperties.getBucketName(), url), file);
             return file;
         } catch (Exception e) {
             log.error("阿里云下载文件异常");
         } finally {
             ossClient.shutdown();
         }
         return null;
     }


     /**
      * 通过对象url获取对象名称
      * @param url oss存储返回的url
      * @return String
      * @date 2020/11/10
      */
     public static String getObjectNameByUrl(String url) {
         if (StringUtils.isBlank(url)) {
             return null;
         }
         return url.substring(url.indexOf(ossProperties.getBucketName()) + ossProperties.getBucketName().length() + 1, url.indexOf("?"));
     }

     /**
      * 调用浏览器下载
      * @param
      * @return
      * @date 2020/11/10
      */
     public static void download2FileByStream(String url, HttpServletResponse response, String name) {

         File file = new File(url);
         String fileName = file.getName();
         fileName = StringUtils.substringBefore(fileName, "?");
         String fileLast = StringUtils.substringAfterLast(fileName, ".");
         // 拼接成新的文件名
         fileName = name + "." + fileLast;

         BufferedInputStream inputStream = null;
         OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());

         try (
             BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());) {
            // 配置文件下载
             response.setHeader("content-type", "application/octet-stream");
             response.setContentType("application/octet-stream");
             if (url.contains(ossProperties.getBucket())) {
                 // 根据url获取对象名称
                 url = getObjectNameByUrl(url);
             }
             // 下载文件能正常显示中文
             response.setHeader("Content-Disposition", "attachment;filename=" +
                     URLEncoder.encode(StringUtils.isBlank(fileName) ? url : fileName, "UTF-8"));

             log.info("阿里云下载文件开始:{}", url);
             OSSObject ossObject = ossClient.getObject(ossProperties.getBucketName(), url);
             inputStream = new BufferedInputStream(ossObject.getObjectContent());
             byte[] buf = new byte[2048];
             int bytesRead;
             while (-1 != (bytesRead = inputStream.read(buf, 0, buf.length))) {
                 outputStream.write(buf, 0 , bytesRead);
             }
             outputStream.flush();
         } catch (Exception e) {
             log.error("下载异常！",e);
         } finally {
             log.info("阿里云OSS下载文件结束");
             ossClient.shutdown();
             if (inputStream != null ) {
                 try {
                     inputStream.close();
                 } catch (IOException ioe) {
                     ioe.printStackTrace();
                 }
             }
         }
     }
}
