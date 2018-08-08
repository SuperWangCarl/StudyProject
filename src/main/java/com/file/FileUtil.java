package com.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class FileUtil {

	public static String upLoad(HttpServletRequest request, String savePath) {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					String fileName = file.getOriginalFilename();
					// savePath =
					// request.getSession().getServletContext().getRealPath("/zip");
					savePath = savePath + "\\" + fileName;
					File localFile = new File(savePath);
					try {
						file.transferTo(localFile);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
		return savePath;
	}

	public static InputStream getInputStream(HttpServletRequest request) {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		InputStream is = null;
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {

					try {
						is = file.getInputStream();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		}
		return is;
	}

	public static void UnCompressZip(String destPath, String zipPath) {
		zipUpLoad(destPath, zipPath);
	}

	public static void deleteFile(String filePath) {
		java.io.File deleteFile = new java.io.File(filePath);
		deleteFile.delete();
	}

	@SuppressWarnings("unchecked")
	public static void zipUpLoad(String destPath, String zipPath) {
		if (StringUtils.isBlank(zipPath)) {
			throw new RuntimeException("压缩文件路径不可为空！");
		}
		if (!zipPath.endsWith(".zip")) {
			throw new RuntimeException("保存文件名应以.zip结尾！");
		}
		File file = new File(zipPath);
		if (StringUtils.isBlank(destPath)) {
			destPath = getUnzipBasePath(file);
		}
		if (!file.exists()) {
			throw new RuntimeException("路径'" + zipPath + "'下未找到文件");
		}
		ZipFile zipFile;
		int len;
		byte[] buff = new byte[1024];
		try {
			zipFile = new ZipFile(zipPath, "gbk");
			// 获取压缩文件中所有条目
			Enumeration<ZipEntry> entries = zipFile.getEntries();
			if (entries != null) {
				while (entries.hasMoreElements()) {
					// 压缩文件条目转为文件或文件夹
					ZipEntry zipEntry = entries.nextElement();
					// 获取输入流
					InputStream ins = zipFile.getInputStream(zipEntry);
					BufferedInputStream bis = new BufferedInputStream(ins);
					File unzipFile = new File(destPath + File.separator + zipEntry.getName());
					if (zipEntry.isDirectory()) {
						unzipFile.mkdirs();
						continue;
					}

					File pf = unzipFile.getParentFile();
					if (pf != null && !pf.exists()) {
						pf.mkdirs();
					}
					unzipFile.createNewFile();
					FileOutputStream fos = new FileOutputStream(unzipFile);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					// 输出流写文件
					while ((len = bis.read(buff, 0, 1024)) != -1) {
						bos.write(buff, 0, len);
					}

					bos.flush();
					bis.close();
					ins.close();
					fos.close();
					bos.close();

				}
			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

	}

	/**
	 * 获取解压缩文件基路径
	 * 
	 * @param zipPath
	 * @return
	 */
	private static String getUnzipBasePath(File zipPath) {
		return zipPath.getParentFile().getAbsolutePath();
	}
}
