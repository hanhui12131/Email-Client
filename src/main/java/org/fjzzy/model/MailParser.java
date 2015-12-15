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

	
	public void getSimpleContent() throws MessagingException {
		msgContent.setSubject(msg.getSubject());
		msgContent.setFrom(msg.getFrom()[0].toString());
		msgContent.setSendDate(msg.getSentDate().toLocaleString());
	}
	
	
	// 单个邮件内容
	public void rePart(Part part) throws Exception {
		if (part.getDisposition() != null) {
			if (part.getFileName() != null) {
				String name = MimeUtility.decodeText(part.getFileName());
				msgContent.attch.add(name, part.getInputStream());
			}
		}
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

class MessageContent {
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
	public void getAttach(String filepath) throws Exception {
		for (int i = 0; i < attch.in.size(); i++) {
			File file = new File(attch.fileName.get(i));
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