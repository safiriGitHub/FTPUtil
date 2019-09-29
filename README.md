# FTPUtil

**java代码解析文件，并将文件上传至ftp服务路径下**

## Tag
### 0.1.0

**FTPUploadService:** 解析`HttpServletRequest`中的文件并调用`ZSFTPClient`相关方法完成上传

**UploadFileResult:** 上传文件成功后的结果类，提供了文件的信息供使用

**ZSFTPClient:** 实现`FTP`上传、下载、删除、修改文件名等操作

## Example

**FTPUploadService:**
```

public void uploadFileExample(HttpServletRequest req, HttpServletResponse resp) throws Exception {
	
	...
	try {
		String ftphost = "";
		String port = "";
		String username = "";
		String password = "";
		String fileHttpUtlPrefix = "";
		String uploadpath = "";
		FTPConfig config = new ZSFTPClient.FTPConfig(ftphost, port, username, password);
		config.setUploadpath(uploadpath);
		config.setFileHttpUtlPrefix(fileHttpUtlPrefix);
		
		//开始上传文件
	  	List<UploadFileResult> upList = FTPUploadService.parseRequestAnduploadFileToFTPService(config, req, null);
	  	if (!upList.isEmpty()) {
	  		
	  		UploadFileResult result = upList.get(0);
			//上传成功后 后续操作
	  		...
		}
	} catch (Exception e) {
		
	}
	......
		
}
	
```

**ZSFTPClient:**
```
public static void main(String[] args) {

	boolean b = true;
			
			
	FTPConfig config = new ZSFTPClient.FTPConfig("192.168.1.179", "21", "safiri", "daka123");
	// 1
	ZSFTPClient client = new ZSFTPClient(config);
	// 1.1
	b = client.uploadFile("wahaha.jpg", "F:\\downloads\\hy1.jpg");// 会使用默认的用户限定根目录
	//b = client.downloadFile("wahaha.jpg", "F:\\downloads\\sa1");
	//b = client.deleteFile("wahaha.jpg");
	
	
	
	/*
	FTPConfig config = new ZSFTPClient.FTPConfig("192.168.1.179", "21", "safiri", "daka123");
	config.downloadpath = "sa1/test";
	config.deletepath = "sa1/test";
	ZSFTPClient client = new ZSFTPClient(config);
	b = client.downloadFile("test01.jpg", "F:\\\\downloads\\\\sa1");
	b = client.deleteFile("test01.jpg");
	*/
	
	/*
	FTPConfig config = new ZSFTPClient.FTPConfig("192.168.1.179", "21", "safiri", "daka123");
	config.downloadpath = "sa1/test";
	ZSFTPClient client = new ZSFTPClient();
	b = client.downloadFile(config, "test02.jpg", "F:\\\\downloads\\\\sa1");
	config.downloadpath = "/";
	b = client.downloadFile(config, "user_list", "F:\\\\downloads\\\\sa1");
	config.deletepath = "sa1/test";
	b = client.deleteFile(config, "test02.jpg");
	 */
	
	if (b) {
		System.out.println("success");
	} else {
		System.out.println("failed");
	}
}

```
