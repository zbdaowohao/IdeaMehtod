package com.ads.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class FileUtils {

	private static String reportFilePath = "D://temp";

	public static String saveTempDownloadFile(HttpServletRequest request, String fileName, String content) {
		BufferedWriter bufferWriter = null;
		String tmpFileName = "";
		try {

			File tmpDir = new File(reportFilePath);
			// 判断上传文件的保存目录是否存在
			if (!tmpDir.exists() && !tmpDir.isDirectory()) {
				// 创建目录
				tmpDir.mkdir();
			}
			// 得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
			tmpFileName = reportFilePath + File.separator + fileName + ".xls";
			File file = new File(tmpFileName);
			// 判断上传文件的保存目录是否存在
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file, true);
			// 流的方式
			bufferWriter = new BufferedWriter(fileWriter);
			bufferWriter.write(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != bufferWriter) {
				try {
					bufferWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return tmpFileName;
	}

	public static void downLodadReport(HttpServletResponse response, String filePath, String fileName) {
		BufferedInputStream bis = null;
		OutputStream output = null;
		try {
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-disposition",
					"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
			bis = new BufferedInputStream(new FileInputStream(filePath));
			output = new BufferedOutputStream(response.getOutputStream());
			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = bis.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);// 字节写入，避免乱码
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != output) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != bis) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 删除临时文件
			File tempFile = new File(filePath);
			tempFile.delete();
		}
	}

}
