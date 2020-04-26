package com.leisure.myproject.service;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Store;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * fdfs client
 * @author gonglei
 * @date 2020/4/26 15:19
 */

@Component
@Slf4j
public class FastDFSClient {

	@Autowired
	private FastFileStorageClient storageClient;

	@Autowired
	private FdfsWebServer fdfsWebServer;

	/**
	* 上传文件
	*
	* @param file 文件对象
	* @return
	* @date 2020/4/26
	*/
	public String uploadFile(MultipartFile file) throws IOException{
		StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(),
				FilenameUtils.getExtension(file.getOriginalFilename()),null);
		return getResAccessUrl(storePath);
	}

	/**
	* 上传文件
	*
	* @param
	* @return
	* @date 2020/4/26
	*/
	public String uploadFile(File file) throws IOException{
		FileInputStream inputStream = new FileInputStream(file);
		StorePath storePath = storageClient.uploadFile(inputStream,file.length(),
				FilenameUtils.getExtension(file.getName()),null);
		return getResAccessUrl(storePath);
	}

	/**
	* 将一段字符串生成一个文件上传
	*
	* @param content 文件内容
	* @param fileExtension 文件后缀名
	* @return 路径
	* @date 2020/4/26
	*/
	public String uploadFile(String content,String fileExtension){
		byte[] buff = content.getBytes((Charset.forName("UTF-8")));
		ByteArrayInputStream stream = new ByteArrayInputStream(buff);
		StorePath storePath = storageClient.uploadFile(stream,buff.length,fileExtension,null);
		return getResAccessUrl(storePath);
	}

	//封装图片完整URL地址
	private String getResAccessUrl(StorePath storePath){
		String fileUrl = fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
		return fileUrl;
	}

	/**
	* 删除文件
	*
	* @param fileUrl 文件访问地址
	* @return
	* @date 2020/4/26
	*/
	public void deleteFile(String fileUrl){
		if(StringUtils.isEmpty(fileUrl)){
			return;
		}
		try{
			StorePath storePath = StorePath.parseFromUrl(fileUrl);
			storageClient.deleteFile(storePath.getGroup(),storePath.getPath());
		}catch (FdfsUnsupportStorePathException e){
			log.warn("=======[当前文件访问路径不存在][{}]=======",e.getMessage());
		}
	}
}
