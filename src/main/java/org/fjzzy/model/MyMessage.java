package org.fjzzy.model;

import java.io.*;
import java.util.*;
import java.io.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.Flags.*;
import javax.mail.Message.*;
import javax.mail.internet.*;

public class MyMessage extends MimeMessage {

	// 主题
	private String subject;
	// 文本内容
	private String contentText;
	// 图片资源路径
	private ArrayList<String> resourceFile;
	// 附件路径
	private ArrayList<String> attachFile;

	private MimeMultipart AllPart = null;

	public MyMessage(Session session) {
		super(session);
		//所有邮件体的整合
		AllPart = new MimeMultipart();
		try {
			this.setContent(AllPart);
		} catch (MessagingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	// 添加附件
	public void addAttach(ArrayList<String> attach) throws Exception {
		//设置整体邮件类型为mixed
		AllPart.setSubType("mixed");
		for (String path : attach) {
			
			MimeBodyPart Part = new MimeBodyPart();
			AllPart.addBodyPart(Part);
			//附件
			File file = new File(path);
			DataSource ds = new FileDataSource(file);
			//获取附件Datahandler
			DataHandler handler = new DataHandler(ds);
			Part.setDataHandler(handler);
			String name = "";
			//对附件名进行编码
			try {
				name = MimeUtility.encodeText(file.getName());
			} catch (Exception e) {
				// TODO: handle exception
				name = file.getName();
			}
			Part.setFileName(name);
		}
	}

	//添加文字
	public void addContent(String text) throws MessagingException {
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setText(text);
		AllPart.addBodyPart(textPart);
	}
	//
	public void ImgText(String text, ArrayList<String> resource,String[] id) throws MessagingException {
		//存储图片与文本内容的邮件体
		MimeMultipart part = new MimeMultipart();
		//存储文本内容的邮件体
		MimeBodyPart htmlPart = new MimeBodyPart();
		//设置文本内容和格式
		htmlPart.setContent(text, "text/html;charset=UTF-8");
		//循环加载文本内容图片
		for (int i=0;i<resource.size();i++) {
			MimeBodyPart imagePart = new MimeBodyPart();
			File file = new File(resource.get(i));
			//获得图片资源的handler
			DataHandler handler = new DataHandler(new FileDataSource(file));
			imagePart.setDataHandler(handler);
			//设置内嵌图片id
			imagePart.setContentID(id[i]);
			
			part.addBodyPart(imagePart);
		}

		part.addBodyPart(htmlPart);
		//设置
		MimeBodyPart textImagePart = new MimeBodyPart();
		textImagePart.setContent(part);
		part.setSubType("related");

		MimeMultipart mpart2 = new MimeMultipart();
		mpart2.addBodyPart(textImagePart);
		this.setContent(mpart2);
	}

}
