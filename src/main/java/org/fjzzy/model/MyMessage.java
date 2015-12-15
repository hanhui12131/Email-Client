package org.fjzzy.model;

import java.io.*;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import com.sun.xml.internal.ws.encoding.DataHandlerDataSource;

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
		AllPart.setSubType("mixed");
		for (String path : attach) {

			MimeBodyPart Part = new MimeBodyPart();
			AllPart.addBodyPart(Part);

			File file = new File(path);
			DataSource ds = new FileDataSource(file);
			DataHandler handler = new DataHandler(ds);
			Part.setDataHandler(handler);
			String name = MimeUtility.encodeText(file.getName());
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
	public void tst(String text, ArrayList<String> resource,String[] id) throws MessagingException {
		MimeBodyPart htmlPart = new MimeBodyPart();
		MimeMultipart part = new MimeMultipart();
		//设置文本内容和格式
		htmlPart.setContent(text, "text/html;charset=UTF-8");
		//循环加载文本内容图片
		for (int i=0;i<resource.size();i++) {
			MimeBodyPart imagePart = new MimeBodyPart();
			File file = new File(resource.get(i));
			DataHandler handler = new DataHandler(new FileDataSource(file));
			imagePart.setDataHandler(handler);
			imagePart.setContentID(id[i]);
			//
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
