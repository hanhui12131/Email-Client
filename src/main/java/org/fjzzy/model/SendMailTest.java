package org.fjzzy.model;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import java.io.*;
import java.util.*;
public class SendMailTest {

	public static void main(String[] args) throws Exception {
		Session session = null;
		try {
//			Properties props=System.getProperties();
//			props.setProperty("mail.smtp.auth", "true");
//			props.setProperty("mail.transport.protocol", "smtp");
//			props.setProperty("mail.host", "smtp.qq.com");
//			session=Session.getInstance(props);
			
			MyAuthenticator auth = new MyAuthenticator("1593423661@qq.com", "a9908651251", "smtp");
			session = auth.getSendSession();
			session.setDebug(true);
			
			MyMessage msg=new MyMessage(session);
			msg.setSubject("javamail发送邮件测试");
			
			ArrayList<String> attach1 = new ArrayList<String>();
			attach1.add("D:/bg1.jpg");
			attach1.add("D:/bg3.jpg");
			
			String[] id = new String[2];
			id[0] = "d1";
			id[1] = "test";
			
			
			msg.tst("美女<img src='cid:"+id[0]+"'/><img src='cid:"+id[1]+"'/>",attach1,id);
			msg.addAttach(attach1);
			
			
			msg.setFrom(new InternetAddress("1593423661@qq.com"));
			msg.setReplyTo(new Address[]{new InternetAddress("lili@163.com")});
			msg.setRecipient(RecipientType.TO, new InternetAddress("1593423661@qq.com"));
			
			
			
			Transport transport=session.getTransport();
			transport.connect("smtp.qq.com","1593423661@qq.com" , "a9908651251");
			transport.sendMessage(msg, new Address[]{new InternetAddress("1593423661@qq.com")});
			transport.close();

//			MyAuthenticator auth1 = new MyAuthenticator("1593423661@qq.com", "a9908651251", "pop3");
//			session = auth1.getStoreSession();
//			session.setDebug(true);
//			Store store = session.getStore();
//			store.connect();
//			
//			
//			System.out.println(store.isConnected());
//			
//			Folder folder = store.getFolder("INBOX");
//			folder.open(Folder.READ_WRITE);
//			folder.hasNewMessages();
//			MyFolder folder = new MyFolder(auth1);
//			
//			Message[] msg1 = folder.getMessages();
//			
//			System.out.println(msg1[3].getMessageNumber());
//			msg1[0].setFlag(Flags.Flag.DRAFT, true);
//			System.out.println(msg1[0].isExpunged()+"++++++++++++++++++++++++++++");
			
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally{
		}
		
		
	}

}
