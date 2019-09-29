package com.core.util.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.core.util.upload.ZSFTPClient.FTPConfig;

public class FTPUploadService {
	/**
	 * 1解析并上传文件到配置的ftp路径
	 * @param ftpConfig ftp配置
	 * @param request 上传文件request
	 * @param urlCreate 请求地址如何创建回调
	 * @return 上传成功后的结果集
	 * @throws Exception
	 */
	public static List<UploadFileResult> parseRequestAnduploadFileToFTPService(FTPConfig ftpConfig, HttpServletRequest request, HttpURLCreate urlCreate) throws Exception {
		
		if (isEmpty(ftpConfig.getFtpHost())) {
			throw new IllegalArgumentException("ftphost can't be null");
		}
		if (isEmpty(ftpConfig.getPort())) {
			throw new IllegalArgumentException("port can't be null");
		}

		List<UploadFileResult> upList = new ArrayList<UploadFileResult>();
		List<MultipartFile> fileList = FTPUploadService.parseUploadRequest(request);
		for (MultipartFile mf : fileList) {
			if (!mf.isEmpty()) {
				if (urlCreate == null) {
					urlCreate = (String name) -> {
						if (isEmpty(ftpConfig.getFileHttpUtlPrefix())) {
							return name;
						}else {
							return ftpConfig.getFileHttpUtlPrefix() + name;
						}
					};
				}
				UploadFileResult uploadFileResult = FTPUploadService.uploadToFTPService(mf, ftpConfig, urlCreate);
				upList.add(uploadFileResult);
			}
		}

		return upList;
	}
	/**
	 * 2解析上传文件request
	 * @param request
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static List<MultipartFile> parseUploadRequest(HttpServletRequest request) throws IllegalArgumentException{
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) throw new IllegalArgumentException("上传内容不是有效的multipart/form-data类型.");
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
        // 获取multiRequest 中所有的文件名
        Iterator<String> iter = multipartRequest.getFileNames();
        List<MultipartFile> allFileList = new ArrayList<MultipartFile>();
        while (iter.hasNext()) {
        	List<MultipartFile> fileList = multipartRequest.getFiles(iter.next().toString());
        	allFileList.addAll(fileList);
        }
        return allFileList;
	}
	/**
	 * 3上传文件到指定ftp服务
	 * @param mf 文件
	 * @param config 指定的ftp服务
	 * @param urlCreate 请求地址如何创建回调
	 * @return
	 */
	public static UploadFileResult uploadToFTPService(MultipartFile mf, FTPConfig config, HttpURLCreate urlCreate) {
		boolean flag = false;
		String message = "";
		UploadFileResult result = new UploadFileResult();
		
		if(!mf.isEmpty()) {    
			String originalFilename = mf.getOriginalFilename();// 文件名
			String name = mf.getName(); // 参数名
			String contentType = mf.getContentType();
			long sizeInBytes = mf.getSize();
			
			InputStream inputStream = null;
			try {
				inputStream = mf.getInputStream();
				ZSFTPClient client = new ZSFTPClient(config);
				flag = client.uploadFile(originalFilename, inputStream);
			} catch (IOException e) {
				e.printStackTrace();
				message = "上传失败，获取不到上传文件";
			} finally {
				if (flag) {
					message = "上传成功";
				}else {
					if (message.length() == 0) message = "上传失败";
				}
			}
			
			result.setOriginalFilename(originalFilename);
			result.setName(name);
			result.setContentType(contentType);
			result.setSizeInBytes(sizeInBytes);
			result.setHttpURL(urlCreate.create(originalFilename));
        } else {
        	message = "上传失败，获取不到上传文件";
        }
		result.setUploadDateTime(new Date());
		result.setMessage(message);
		result.setUploadFlag(flag);
		return result;
	}
	
	public interface HttpURLCreate {
		String create(String originalFilename);
	}
	
	private static boolean isEmpty(CharSequence value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	} 
}
