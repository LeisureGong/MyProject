package com.leisure.myproject.oss;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
      * 判断url是否存在
      * @param url 阿里云OSS文件存储链接
      * @return
      * @date 2020/11/12
      */
     public static boolean isUrlExists(String url) {
         if (StringUtils.isBlank(url)) {
             log.error("文件url不能为空");
         }
         if (url.contains(ossProperties.getBucket())) {
             // 获取OSS文件名(key)
             url = getObjectNameByUrl(url);
         }
         boolean found = true;
         OSS ossClient = new OSSClientBuilder()
                 .build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
         try {
             found = ossClient.doesObjectExist(ossProperties.getBucketName(), url);
         } catch (Exception e) {
             log.error("查询url是否存在异常");
         }
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
     * 下载文件到指定目录， 文件名称为OSS对象名称
     * @param url 待下载对象url
     * @param dir 下载到本地目录
     * @return
     * @date 2020/11/12
     */
    public static File download2Dir(String url, String dir) {
        if (url.contains(ossProperties.getBucket())) {
            // 根据url获取对象名称
            url = getObjectNameByUrl(url);
        }
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        try {
            File file = new File(dir + File.separator + url);
            // 如果指定的本地文件存在则会被覆盖，不存在则新建
            ossClient.getObject(new GetObjectRequest(ossProperties.getBucketName(), url), file);
            log.info("阿里云OSS下载文件结束:{}",url);
            return file;
        } catch (Exception e) {
            log.error("阿里云下载文件异常：{}", e.getMessage());
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
         return url.substring(url.indexOf(ossProperties.getBucket()) + ossProperties.getBucket().length() + 1, url.indexOf("?"));
     }

     /**
      * 调用浏览器下载
      * @para
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

    public static String uploadVod(MultipartFile file){
         try {
             String id = ossProperties.getAccessKeyId();
             String screct = ossProperties.getAccessKeySecret();
             // 本地文件的路径和名称
             String fileName = file.getOriginalFilename();
             // 上传到阿里云的显示标题
             String title = fileName.substring(0,fileName.lastIndexOf("."));
             InputStream inputStream= file.getInputStream();
             String videoId = testUploadVideo(id,screct,title,fileName,inputStream);
            log.info("videoId={}", videoId);
         } catch (Exception e) {

         }
        return null;
    }

    public static String testUploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName, InputStream inputStream) {
        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        String videoId = null;
        if (response.isSuccess()) {
            log.info("VideoId=" + response.getVideoId() + "\n");
            videoId = response.getVideoId();
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            log.error("VideoId=" + response.getVideoId() + "\n");
            videoId = response.getVideoId();
        }
        return videoId;
    }

     /**
      * 上传视频文件到阿里云OSS
      * @param key OSS文件存储名
      * @param url 视频文件路径
      * @return
      * @date 2020/11/12
      */
     public static String uploadVideo(String key, String url) {

         try {
             DefaultAcsClient vodClient = initVodClient();
             CreateUploadVideoResponse createUploadVideoResponse = createUploadVideo(vodClient);

             // 执行成功会返回VideoId、UploadAddress和UploadAuth
             String videoId = createUploadVideoResponse.getVideoId();


             JSONObject uploadAuth = JSONObject.parseObject(decodeBase64(createUploadVideoResponse.getUploadAuth()));
             JSONObject uploadAddress = JSONObject.parseObject(decodeBase64(createUploadVideoResponse.getUploadAddress()));

             // 使用UploadAuth和UploadAddress初始化OSS客户端
             OSSClient ossClient = initOssClient(uploadAuth, uploadAddress);
             // 上传文件，注意是同步上传会阻塞等待，耗时与文件大小和网络上行带宽有关
             uploadLocalFile(ossClient, uploadAddress, url);
             log.info("上传视频成功：{}", videoId);
             return videoId;

         } catch (Exception e) {
             log.error("");
         }
        return null;
     }

    public static void uploadLocalFile(OSSClient ossClient, JSONObject uploadAddress, String localFile) {
        String bucketName = uploadAddress.getString("Bucket");
        String objectName = uploadAddress.getString("FileName");
        File file = new File(localFile);
        ossClient.putObject(bucketName, objectName, file);
    }

     // 初始化VOD客户端
     public static DefaultAcsClient initVodClient() throws ClientException {
         // 点播服务接入区域，国内请填cn-shanghai，其他区域请参考文档 https://help.aliyun.com/document_detail/98194.html
         String regionId = "cn-shanghai";
         DefaultProfile profile = DefaultProfile.getProfile(regionId, ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
         DefaultAcsClient client = new DefaultAcsClient(profile);
         return client;
     }

    public static CreateUploadVideoResponse createUploadVideo(DefaultAcsClient vodClient) throws com.aliyuncs.exceptions.ClientException {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setFileName("vod_test.mp4");
        request.setTitle("this is title");
        //request.setDescription("this is desc");
        //request.setTags("tag1,tag2");
        //request.setCoverURL("http://vod.aliyun.com/test_cover_url.jpg");
        //request.setCateId(-1L);
        //request.setTemplateGroupId("");
        //request.setWorkflowId("");
        //request.setStorageLocation("");
        //request.setAppId("app-1000000");

        //设置请求超时时间
        request.setSysReadTimeout(1000);
        request.setSysConnectTimeout(1000);
        return vodClient.getAcsResponse(request);
    }

    public static OSSClient initOssClient(JSONObject uploadAuth, JSONObject uploadAddress) {
        String endpoint = uploadAddress.getString("Endpoint");
        String accessKeyId = uploadAuth.getString("AccessKeyId");
        String accessKeySecret = uploadAuth.getString("AccessKeySecret");
        String securityToken = uploadAuth.getString("SecurityToken");
        return (OSSClient) new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, securityToken);
    }

    public static String decodeBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            Base64 decoder = new Base64();
            try {
                b = decoder.decode(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
            }
        }
        return result;
    }


}
