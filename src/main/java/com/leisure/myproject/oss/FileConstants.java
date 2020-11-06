package com.leisure.myproject.oss;

import org.springframework.util.ClassUtils;

import java.io.File;

/**
 * 上传文件常量
 * @author gonglei
 * @date 2020/11/6
 */
public class FileConstants {

    // 文件存储临时文件夹
    public final static String TEMP_ROOT = "temp";

    // 下载暂存目录
    public final static String DOWNLOAD = TEMP_ROOT + File.separator + "download";

    // 二维码暂存路径
    public static final String QRCODE = "qrcode";
    public static final String QRCODE_PATH = ClassUtils.getDefaultClassLoader().getResource("static") + File.separator + QRCODE;

    /**
     * 文件的后缀名
     */
    public static final String FILE_TYPE_AVI  = "avi";
    public static final String FILE_TYPE_CSV = "csv";
    public static final String FILE_TYPE_DOC  = "doc";
    public static final String FILE_TYPE_DOCX  = "docx";
    public static final String FILE_TYPE_MP3 = "mp3";
    public static final String FILE_TYPE_PDF  = "pdf";
    public static final String FILE_TYPE_PPT  = "ppt";
    public static final String FILE_TYPE_PPTX  = "pptx";
    public static final String FILE_TYPE_RAR  = "rar";
    public static final String FILE_TYPE_TXT  = "txt";
    public static final String FILE_TYPE_XLS = "xls";
    public static final String FILE_TYPE_ZIP  = "zip";
}
