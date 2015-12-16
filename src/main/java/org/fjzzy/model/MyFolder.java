package org.fjzzy.model;
import javax.mail.*;
import javax.mail.internet.MimeUtility;

import java.util.*;
import org.apache.geronimo.javamail.store.pop3.POP3Folder;
import org.apache.geronimo.javamail.store.pop3.POP3Store;
import javax.swing.table.*;;

public class MyFolder extends AbstractTableModel{
	//信件文件箱
	Folder folder = null;
	//INBOX信箱中的邮件
	Message[] msgs = null;
	//表列
	Vector<String> Cols;
	//表行
	Vector<Vector> Rows;
	//表行数据
	Vector<String> RowData;
	
	public Folder getFolder() {
		return folder;
	}
	
	public MyFolder(MyAuthenticator auth) throws Exception{
		Session session = auth.getStoreSession();
		session.setDebug(true);
		Store store = session.getStore();
		//连接信箱
		store.connect();
		System.out.println(store.isConnected());
		//初始化Folder
		folder = store.getFolder("INBOX");
		//以读写方式打开
		folder.open(Folder.READ_WRITE);
		//获取信箱邮件
		msgs = folder.getMessages();
		Message[] msgs = folder.getMessages();

//		FetchProfile fp = new FetchProfile();
//		fp.add(FetchProfile.Item.ENVELOPE);
//		fp.add("X-mailer");
//		folder.fetch(msgs, fp);
		intialTab();
	}
	//初始化表行列
	public void intialTab() throws Exception{
		Cols = new Vector<String>();
		Cols.add("主题");
		Cols.add("发信人");
		Cols.add("时间");
		Rows = new Vector<Vector>();
		String subject;
		String From;
		for(Message msg : msgs){
			
			
			RowData = new Vector<String>();
			subject =msg.getSubject();
			From = msg.getFrom()[0].toString();
			try{
				RowData.add(MimeUtility.decodeText(subject));
				
			}catch(Exception ex){
				RowData.add(subject);
			}
			try {
				RowData.add(MimeUtility.decodeText(From));
			} catch (Exception e) {
				// TODO: handle exception
				RowData.add(From);
			}
				
			RowData.add(msg.getSentDate().toLocaleString());
			Rows.add(RowData);
		}
	}
	
	
	public Message[] getMessages() throws MessagingException{
		return folder.getMessages();
	}

	public Message getMessage(int i) throws MessagingException{
		return folder.getMessage(i);
	}
	
	@Override
	public String getColumnName(int column) {
		// TODO 自动生成的方法存根
		return Cols.get(column);
	}

	public int getRowCount() {
		// TODO 自动生成的方法存根
		return Rows.size();
	}

	public int getColumnCount() {
		// TODO 自动生成的方法存根
		return Cols.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO 自动生成的方法存根
		return Rows.get(rowIndex).get(columnIndex);
	}
	public void close(){
		try {
			if(folder != null){
				folder.close(true);				
			}
		} catch (MessagingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	

}
