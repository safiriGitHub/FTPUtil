package com.core.util.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;


public class ZSFTPClient {
        

		public static class FTPConfig {
			private String ftpHost;
			private String port;
			private String username;
			private String password;
			/**
			 * 上传操作目录， 受用户被配置的工作目录限制
			 */
			private String uploadpath;
			/**
			 * 下载操作目录， 受用户被配置的工作目录限制
			 */
			private String downloadpath;
			/**
			 * 下载操作目录， 受用户被配置的工作目录限制
			 */
			private String deletepath;
			/**
			 * 文件访问地址http前缀
			 */
			private String fileHttpUtlPrefix;
			
			public String getFileHttpUtlPrefix() {
				return fileHttpUtlPrefix;
			}

			public void setFileHttpUtlPrefix(String fileHttpUtlPrefix) {
				this.fileHttpUtlPrefix = fileHttpUtlPrefix;
			}

			public FTPConfig(String ftpHost, String port, String username, String password) {
				this.ftpHost = ftpHost;
				this.port = port;
				this.username = username;
				this.password = password;
			}

			public String getFtpHost() {
				return ftpHost;
			}

			public void setFtpHost(String ftpHost) {
				this.ftpHost = ftpHost;
			}

			public String getPort() {
				return port;
			}

			public void setPort(String port) {
				this.port = port;
			}

			public String getUsername() {
				return username;
			}

			public void setUsername(String username) {
				this.username = username;
			}

			public String getPassword() {
				return password;
			}

			public void setPassword(String password) {
				this.password = password;
			}

			public String getUploadpath() {
				return uploadpath;
			}

			public void setUploadpath(String uploadpath) {
				this.uploadpath = uploadpath;
			}

			public String getDownloadpath() {
				return downloadpath;
			}

			public void setDownloadpath(String downloadpath) {
				this.downloadpath = downloadpath;
			}

			public String getDeletepath() {
				return deletepath;
			}

			public void setDeletepath(String deletepath) {
				this.deletepath = deletepath;
			}
			
		}
		
		private FTPConfig ftpConfig;
		public FTPConfig getFtpConfig() throws Exception {
			if (null == ftpConfig) {
				throw new Exception("未设置FTPConfig对象");
			}
			return ftpConfig;
		}
		public void setFtpConfig(FTPConfig ftpConfig) {
			this.ftpConfig = ftpConfig;
		}
		
		public ZSFTPClient(FTPConfig config) {
			setFtpConfig(config);
		}
		public ZSFTPClient() {
			
		}
		
		//private final static Log logger = LogFactory.getLog(FtpUtil.class);  
		public FTPClient getFTPClient() {
			FTPConfig config = null;
			try {
				config = getFtpConfig();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			return getFTPClient(config);
		}
			  
		public FTPClient getFTPClient(FTPConfig config) {
			FTPClient ftpClient = new FTPClient();
	        try {  
	            ftpClient = new FTPClient();  
	            ftpClient.connect(config.ftpHost, Integer.parseInt(config.port));// 连接FTP服务器  
	            ftpClient.login(config.username, config.password); // 登陆FTP服务器  
	            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {  
	                //logger.info("未连接到FTP，用户名或密码错误");  
	            	System.out.println("未连接到FTP，用户名或密码错误");
	                ftpClient.disconnect();  
	            } else {  
	                //logger.info("FTP连接成功");  
	            	System.out.println("FTP连接成功");
	            }  
	        } catch (SocketException e) {  
	            e.printStackTrace();  
	            //logger.info("FTP的IP地址可能错误，请正确配置");  
	            System.out.println("FTP的IP地址可能错误，请正确配置");
	        } catch (IOException e) {  
	            e.printStackTrace();  
	            //logger.info("FTP的端口错误,请正确配置");  
	            System.out.println("FTP的端口错误,请正确配置");
	        }  
	        return ftpClient; 
		}
				
		/**
		 * 1.1  上传文件 -- 默认连接配置
		 * @param fileName	上传到ftp的文件名
		 * @param originfilename	待上传文件的名称（绝对地址）
		 * @return
		 */
		public boolean uploadFile(String fileName, String originfilename) {
			FTPConfig config = null;
			try {
				config = getFtpConfig();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			
			return uploadFile(config, fileName, originfilename);
		}
		/**
		 * 1.1  上传文件 -- 默认连接配置
		 * @param fileName 上传到ftp的文件名
		 * @param inputStream 上传文件流
		 * @return
		 */
		public boolean uploadFile(String fileName, InputStream inputStream) {
			FTPConfig config = null;
			try {
				config = getFtpConfig();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			
			return uploadFile(config, fileName, inputStream);
		}
	    /**
	    * 1.1 上传文件
	    * @param config ftp链接配置
	    * @param fileName 上传到ftp的文件名
	    * @param originfilename 待上传文件的名称（绝对地址）
	    * @return
	    */
	    public boolean uploadFile(FTPConfig config, String fileName, String originfilename) {
	    	boolean flag = false;
	        InputStream inputStream = null;
	        
	        try{
	            //把文件转化为流
	            inputStream = new FileInputStream(new File(originfilename));
	            flag = uploadFile(config , fileName, inputStream);
	        }catch (FileNotFoundException e) {
	            System.out.println("未找到文件"+originfilename);
	            e.printStackTrace();
	        }
	        return flag;
	    }
	    /**
	     * 1.1 上传文件
	     * @param config ftp链接配置
	     * @param fileName 上传到ftp的文件名
	     * @param inputStream 上传文件流
	     * @return
	     */
	    public boolean uploadFile(FTPConfig config, String fileName, InputStream inputStream){
	    	FTPClient ftpClient = null; 
	        boolean flag = false;
	        try{
	            ftpClient = getFTPClient(config);
	            String uploadpath = config.getUploadpath();
	            if (null != uploadpath && !uploadpath.isEmpty()) 
	            	makeAndChangeWorkingDirectoryFinally(ftpClient, uploadpath); //创建文件目录
	         
	            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	            //设置文件名字的编码格式为iso-8859-1，因为FTP上传的时候默认编码为iso-8859-1，解决文件名字乱码的问题
	            fileName = new String(fileName.getBytes("GBK"), "iso-8859-1");
	            System.out.println("开始上传文件");
	            flag = ftpClient.storeFile(fileName, inputStream);
	            ftpClient.logout(); //断开和ftp服务器之间的连接
	        }catch (Exception e) {
	            System.out.println("上传文件失败");
	            e.printStackTrace();
	        }finally{
	        	if(ftpClient.isConnected()){ 
		            try{
		                ftpClient.disconnect();
		                System.out.println("disconnect done.");
		            }catch(IOException e){
		                e.printStackTrace();
		            }
		        }
	            if(null != inputStream){
	                try {
	                    inputStream.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                } 
	            } 
	        }
	        if (flag) {
            	System.out.println("上传文件成功");
            }else {
            	System.out.println("上传文件失败");
            }
	        return flag;
	    }
	    
	     /** 2 下载文件 
	     * @param filename 文件名称 
	     * @param localpath 下载后的文件路径 
	     * @return 
	     */
		public boolean downloadFile(String filename, String localpath) {
	
			FTPConfig config = null;
			try {
				config = getFtpConfig();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
	
			return downloadFile(config, filename, localpath);
		}
	     
	     /** 2 下载文件 
	      * @param config ftp链接配置
	      * @param filename 需下载的文件
	      * @param localpath 下载后文件路径
	      * @return
	      */
	     public boolean downloadFile(FTPConfig config, String filename, String localpath) { 
	    	 FTPClient ftpClient = null;
	         boolean flag = false; 
	         OutputStream os=null;
	         
	         try {
	        	 ftpClient = getFTPClient(config);
	        	 ftpClient.setControlEncoding("UTF-8"); // 中文支持  
	             ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
	             ftpClient.enterLocalPassiveMode(); //启动被动模式
	             String downloadpath = config.getDownloadpath();
	             if (null != downloadpath && !downloadpath.isEmpty()) {
	            	 String ftpPath = new String(downloadpath.getBytes("GBK"),"iso-8859-1");
	            	 boolean flag1 = ftpClient.changeWorkingDirectory(ftpPath); 
		             if (!flag1) {
		                 //logger.error("没有找到" + ftpPath + "---该路径");
		            	 System.out.println("没有找到\" + ftpPath + \"---该路径");
		                 return flag1;
		             }
	             }
	             
	             System.out.println("开始下载文件");
	             FTPFile[] ftpFiles = ftpClient.listFiles(); 
	             for(FTPFile file : ftpFiles){ 
	                 if(filename.equalsIgnoreCase(file.getName())){ 
	                     File localFile = new File(localpath + "/" + file.getName()); 
	                     os = new FileOutputStream(localFile); 
	                     flag = ftpClient.retrieveFile(file.getName(), os); 
	                     os.close(); 
	                 } 
	             } 
	             ftpClient.logout(); 
	         } catch (Exception e) { 
	             System.out.println("下载文件失败");
	             e.printStackTrace(); 
	         } finally{ 
	        	 if(ftpClient.isConnected()){ 
			            try{
			                ftpClient.disconnect();
			                System.out.println("disconnect done.");
			            }catch(IOException e){
			                e.printStackTrace();
			            }
			     }
	             if(null != os){
	                 try {
	                     os.close();
	                 } catch (IOException e) {
	                     e.printStackTrace();
	                 } 
	             } 
	         } 
	         if (flag) {
            	 System.out.println("下载文件成功");
             } else {
            	 System.out.println("下载文件失败");
             }
	         return flag; 
	     }
	     
	     /** 
	      *  3删除文件 
	      * @param filename 要删除的文件名称 
	      * @return 
	      */ 
	     public boolean deleteFile(String filename) { 
	    	 FTPConfig config = null;
				try {
					config = getFtpConfig();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
		
				return deleteFile(config, filename);
	     }
	     
	     /**
	      *3 删除文件 在当前文件夹内
	      * @param filename  要删除的文件名称
	      * @return
	      */
	     public boolean deleteFile(FTPConfig config, String filename) { 
	    	 FTPClient ftpClient = null;
	         boolean flag = false; 
	         
	         try { 
	        	 ftpClient = getFTPClient(config);
	        	 ftpClient.setControlEncoding("UTF-8"); // 中文支持  
	             ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
	             ftpClient.enterLocalPassiveMode(); //启动被动模式
	             String deletePath = config.getDeletepath();
	             if (null != deletePath && !deletePath.isEmpty()) {
	            	 String ftpPath = new String(deletePath.getBytes("GBK"),"iso-8859-1");
	            	 flag = ftpClient.changeWorkingDirectory(ftpPath); 
		             if (!flag) {
		                 //logger.error("没有找到" + ftpPath + "---该路径");
		            	 System.out.println("没有找到\" + ftpPath + \"---该路径");
		                 return flag;
		             }
	             }
	             System.out.println("开始删除文件");
	             //切换FTP目录 
	             int replyCode = ftpClient.dele(filename); 
	             flag = FTPReply.isPositiveCompletion(replyCode);
	             ftpClient.logout();
	         } catch (Exception e) { 
	             System.out.println("删除文件失败");
	             e.printStackTrace(); 
	         } finally {
	        	 if(ftpClient.isConnected()){ 
			            try{
			                ftpClient.disconnect();
			                System.out.println("disconnect done.");
			            }catch(IOException e){
			                e.printStackTrace();
			            }
			        }
	         }
	         if (flag) {
            	 System.out.println("删除文件成功");
             }else {
            	 System.out.println("删除文件失败");
             }
	         return flag; 
	     }
	     //MARK: util
	     // 最终成功切换工作目录 注意连接ftp的用户被配置了目录A，则此处工作目录受到限制，只能在A下创建目录
	     private void makeAndChangeWorkingDirectoryFinally(FTPClient ftpClient, String pathname) throws IOException {
	    	//文件需要保存的路径
	        CreateDirecroty(ftpClient, pathname);
	        ftpClient.makeDirectory(pathname);
	        ftpClient.changeWorkingDirectory(pathname);
	     }
	     
	     // 改变ftpClient工作目录路径
	     private boolean changeWorkingDirectory(FTPClient ftpClient, String directory) {
	            boolean flag = true;
	            try {
	                flag = ftpClient.changeWorkingDirectory(directory);
	                if (flag) {
	                   System.out.println("进入文件夹" + directory + " 成功！");
	                } else {
	                   System.out.println("进入文件夹" + directory + " 失败！开始创建文件夹");
	                }
	            } catch (IOException ioe) {
	                ioe.printStackTrace();
	            }
	            return flag;
	        }

	     //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
	     private boolean CreateDirecroty(FTPClient ftpClient, String remote) throws IOException {
	        boolean success = true;
	        String directory = remote + "/";
	        // 如果远程目录不存在，则递归创建远程服务器目录
	        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(ftpClient, new String(directory))) {
	            int start = 0;
	            int end = 0;
	            if (directory.startsWith("/")) {
	                start = 1;
	            } else {
	                start = 0;
	            }
	            end = directory.indexOf("/", start);
	            String path = "";
	            String paths = "";
	            while (true) {
	                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
	                path = path + "/" + subDirectory;
	                if (!existFile(ftpClient, path)) {
	                    if (makeDirectory(ftpClient, subDirectory)) {
	                        changeWorkingDirectory(ftpClient, subDirectory);
	                    } else {
	                        System.out.println("创建目录[" + subDirectory + "]失败");
	                        changeWorkingDirectory(ftpClient, subDirectory);
	                    }
	                } else {
	                    changeWorkingDirectory(ftpClient, subDirectory);
	                }

	                paths = paths + "/" + subDirectory;
	                start = end + 1;
	                end = directory.indexOf("/", start);
	                // 检查所有目录是否创建完毕
	                if (end <= start) {
	                    break;
	                }
	            } 
	        }
	        return success;
	    }

	    //判断ftp服务器文件是否存在    
	    public boolean existFile(FTPClient ftpClient, String path) throws IOException {
	            boolean flag = false;
	            FTPFile[] ftpFileArr = ftpClient.listFiles(path);
	            if (ftpFileArr.length > 0) {
	                flag = true;
	            }
	            return flag;
	        }
	     //创建目录
	     public boolean makeDirectory(FTPClient ftpClient, String dir) {
	        boolean flag = true;
	        try {
	            flag = ftpClient.makeDirectory(dir);
	            if (flag) {
	                System.out.println("创建文件夹" + dir + " 成功！");

	            } else {
	                System.out.println("创建文件夹" + dir + " 失败！");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return flag;
	     }
	    
	    
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
			
//			Properties  ftpProperties = PropertiesUtil.readProperties(propertiesFileName);
//	      	String ftpip = (String)ftpProperties.get("ftpip");
//	      	String port = (String)ftpProperties.get("port");
//	      	String username = (String)ftpProperties.get("username");
//	      	String password = (String)ftpProperties.get("password");
//	      	String workpath = (String)ftpProperties.get("workpath");
//	      	
	      	
			if (b) {
				System.out.println("success");
			} else {
				System.out.println("failed");
			}
		}
	     
	     
	     /**
		     * 0.2 根据配置连接ftp
		     * example:uplaodimgftp.properties
		     * 	ftpip=
				port=
				username=
				password=
				workpath=
		     * @return
		     
		    public boolean defaultFtpClient(String propertiesFileName) {
		        Properties  ftpProperties = PropertiesUtil.readProperties(propertiesFileName);
		      	String ftpip = (String)ftpProperties.get("ftpip");
		      	String port = (String)ftpProperties.get("port");
		      	String username = (String)ftpProperties.get("username");
		      	String password = (String)ftpProperties.get("password");
		      	String workpath = (String)ftpProperties.get("workpath");
		      	boolean b = connect(ftpip, port, username, password);
		      	try {
		      		if (null != workpath && workpath.length() > 0)
		      			makeAndChangeWorkingDirectoryFinally(workpath);
				} catch (IOException e) {
					e.printStackTrace();
					b = false;
				}
		      	return b;
		    }
		    */
}
