package org.fjzzy.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
	Attachment attch;
	// 初始化附件
	public MessageContent() {
		attch = new Attachment();
	}

	// 获得附件内容
	public void getAttach(String dirpath) throws Exception {
		for (int i = 0; i < attch.in.size(); i++) {
			String path = dirpath+attch.fileName.get(i);
			File file = new File(path);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);
			FileInputStream in = (FileInputStream) attch.in.get(i);
			int count;
			while ((count = in.read()) != -1) {
				out.write(count);
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
