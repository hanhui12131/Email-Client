package org.fjzzy.model;
import java.util.Properties;

import  javax.mail.*;
public class MyAuthenticator extends Authenticator {
	//账号
	private String account;
	//密码
	private String pwd;
	//主机服务器
	private String host;
	//协议类型
	private String ProtocolType;
	private Properties props;
	
	public MyAuthenticator(String account, String pwd, String ProtocolType){
		this.account = account;
		this.pwd = pwd;
		this.ProtocolType = ProtocolType;
	}
	
	//获取服务
	private String gethost(){
		String[] part = account.split("@");
		if(ProtocolType.equals("smtp")){
			return ProtocolType+"."+part[1];
		}else{
			return "pop."+part[1];
		}
	}
	//获取发送Session
	public Session getSendSession(){
		host = gethost();
		props=System.getProperties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", ProtocolType);
		props.setProperty("mail.host", host);
		return Session.getDefaultInstance(props);
		
	}
	//获取接收邮件Session
	public Session getStoreSession(){
		host = gethost();
		props=System.getProperties();
		props.setProperty("mail.store.protocol", "pop3");
		props.setProperty("mail.pop3.port", "110");
		props.setProperty("mail.pop3.host", host);
		return Session.getInstance(props,this);
	}
	
	public String getAccount(){
		return account;
	}
	
	public String getPwd(){
		return pwd;
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		// TODO 自动生成的方法存根
		return new PasswordAuthentication(account, pwd);
	}

}
