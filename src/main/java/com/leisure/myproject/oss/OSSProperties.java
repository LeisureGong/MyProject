package com.leisure.myproject.oss;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OSSProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private String endpoint;

    private String bucketName;

    private String bucket;

    @PostConstruct
    public void init() {
        // OSS工具类配置初始化
        OSSUtil.initConfig(this);
    }

    // 过期时间
    private int expiration;

    @Override
    public String toString() {
        return "OSSProperties{" +
                "accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", bucket='" + bucket + '\'' +
                ", expiration=" + expiration +
                '}';
    }
}
