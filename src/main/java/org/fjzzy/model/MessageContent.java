package org.fjzzy.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.eclipse.swt.widgets.MessageBox;

public class MessageContent {
	// 主题
	private String subject;
	// 发送日期
	private String sendDate;
	// 发件人
	private String From;
	// 文本内容
	private String content = "";
	// 附件内容
	public Attachment attch;
	// 初始化附件
	public MessageContent() {
		attch = new Attachment();
	}
	public boolean hasAttch = false;

	// 获得附件内容
	public void getAttach(String dirpath) throws Exception {
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream out = null;
		InputStream in = null;
		for (int i = 0; i < attch.in.size(); i++) {
			String path = dirpath+attch.fileName.get(i);
			File file = new File(path);
			try {
				fileOutputStream = new FileOutputStream(file);
				//文件输出流
				out = new BufferedOutputStream(fileOutputStream);
				in = attch.in.get(i);
				int count = -1;
				//字节流缓存
				byte[] buffer = new byte[512];
				//循环从输入流中读取到输出流中
				while ((count = in.read(buffer)) != -1) {
					out.write(buffer);
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw e;
			}finally{
				//关闭文件流
				if(in != null){
					in.close();
				}
				if(out != null){
					out.close();
				}
				if(fileOutputStream != null){
					fileOutputStream.close();
				}
			}
		}
	}

	// 添加文本内容
	public String appentContent(String text) {
		return content += text;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getFrom() {
		return From;
	}

	public void setFrom(String from) {
		From = from;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
