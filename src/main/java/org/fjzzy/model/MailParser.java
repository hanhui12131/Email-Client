package org.fjzzy.model;
import java.io.*;
import java.util.ArrayList;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;

public class MailParser {
	MimeMessage msg;
	MessageContent msgContent;

	public MessageContent getMsgContent() {
		return msgContent;
	}

	public MailParser(Message msg) {
		// TODO 自动生成的构造函数存根
		this.msg = (MimeMessage) msg;
		msgContent = new MessageContent();
	}

	//邮件基本信息
	public void getSimpleContent() throws MessagingException {
		msgContent.setSubject(msg.getSubject());
		msgContent.setFrom(msg.getFrom()[0].toString());
		msgContent.setSendDate(msg.getSentDate().toLocaleString());
	}
	
	
	// 单个邮件内容
	public void rePart(Part part) throws Exception {
		//part为带有附件内容，将邮件附件名与输入流存储到msgContent
		if (part.getDisposition() != null) {
			if (part.getFileName() != null) {
				//对可能带有中文的文件名进行解码
				String name = "";
				try {
					name = MimeUtility.decodeText(part.getFileName());
					
				} catch (Exception e) {
					name = part.getFileName();
					// TODO: handle exception
				}
				msgContent.attch.add(name, part.getInputStream());
			}
		}
		//part如果为文本类型则直接放入msgContent
		if (part.isMimeType("text/*")) {
			msgContent.appentContent((String) part.getContent());
		}
	}
	// 分解包含由多个part组成的内容
	public void reMultipart(Multipart multipart) throws Exception {
		int count = multipart.getCount();
		Part part;
		for (int i = 0; i < count; i++) {
			part = multipart.getBodyPart(i);
			// 分类解析
			// part是不是Multipart
			if (part instanceof Multipart) {
				reMultipart((Multipart) part);
			} else {
				rePart(part);
			}
		}
	}
	
	//邮件整体解析
	public void parsing(Message msg) throws Exception{
		//获取邮件基本信息
		getSimpleContent();
		//解析邮件体
		if(msg.getContent() instanceof Multipart) {
			Multipart multipart = (Multipart) msg.getContent() ;
			reMultipart(multipart);
		} else if (msg.getContent() instanceof Part){
			Part part = (Part) msg.getContent(); 
			rePart(part);
		} else {
			msgContent.setContent((String)msg.getContent());
		}
	}
	

}

class Attachment {
	// 附件名
	ArrayList<String> fileName;
	// 附件输入流
	ArrayList<InputStream> in;
	public Attachment() {
		fileName = new ArrayList<String>();
		in = new ArrayList<InputStream>();
	}

	public void add(String name, InputStream input) {
		fileName.add(name);
		this.in.add(input);
	}

}