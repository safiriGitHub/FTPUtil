package com.core.util.upload;

import java.io.Serializable;
import java.util.Date;

public class UploadFileResult implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// 上传过来的文件名 MultipartFile-getOriginalFilename()
	private String originalFilename;
	// 上传参数名 MultipartFile-getName()
	private String name;
	// MultipartFile-getContentType()
	private String contentType;
	// MultipartFile-getSize()
	private long sizeInBytes;
	// 是否成功
	private boolean uploadFlag;
	// 上传结果描述
	private String message;
	// 上传日期时间
	private Date uploadDateTime;
	// 互联网访问地址
	private String httpURL;
	
	public boolean isUploadFlag() {
		return uploadFlag;
	}
	public void setUploadFlag(boolean uploadFlag) {
		this.uploadFlag = uploadFlag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getOriginalFilename() {
		return originalFilename;
	}
	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public long getSizeInBytes() {
		return sizeInBytes;
	}
	public void setSizeInBytes(long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}
	public Date getUploadDateTime() {
		return uploadDateTime;
	}
	public void setUploadDateTime(Date uploadDateTime) {
		this.uploadDateTime = uploadDateTime;
	}
	public String getHttpURL() {
		return httpURL;
	}
	public void setHttpURL(String httpURL) {
		this.httpURL = httpURL;
	}
	
	
}
