package org.fjzzy.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.filechooser.*;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import org.fjzzy.model.*;
import org.fjzzy.tool.MyTools;

import javax.swing.JScrollPane;

public class WriteMsg {

	private JFrame frame;
	private JTextField txt_getter;
	private JTextField txt_subject;
	private JEditorPane edt_content;
	private boolean isAttch = false;
	private boolean isImg = false;
	//账号
	private String account;
	//密码
	private String pwd;
	//图片id
	private int imgId = 0;
	//图片路径
	private ArrayList<String> Img = null;
	//文本内容
	 private String content = "";
	 //附件路径
	 private ArrayList<String> attchment = null;
	 //邮件
	 MyMessage msg = null;
	 //验证器
	 MyAuthenticator auth = null;
	 //会话
	 Session session = null;
	 Transport transport = null;
	 private boolean istransmit = false;
	 private JButton btn_send;
	 private JButton btn_attchment;
	 private JButton btn_img;
	 //转发邮件
	 private MyMessage transmit = null;
	 private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WriteMsg window = new WriteMsg();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	
	public void init(){
		this.account = AccountInfo.Account;
		this.pwd = AccountInfo.Password;
		Img = new ArrayList<String>();
		attchment = new ArrayList<String>();
		frame.setVisible(true);
	 }
	 //正常写信
	public WriteMsg() {
		initialize();
		init();
		frame.setVisible(true);
	}
	//回复
	public WriteMsg(String getter) {
		initialize();
		init();
		txt_getter.setText(getter);
		txt_getter.setEditable(false);
		frame.setVisible(true);
	}
	//转发
	public WriteMsg(MessageContent content){
		initialize();
		init();
		istransmit = true;
		edt_content.setText(content.getContent());
		edt_content.setEditable(false);
		btn_img.setEnabled(false);
		btn_attchment.setEnabled(false);
		String s= content.getSubject() ;
		txt_subject.setText(s);
		txt_subject.setEditable(false);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 588, 471);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("写信");
		frame.getContentPane().setLayout(null);
		
		JLabel lbl_getter = new JLabel("收件人：");
		lbl_getter.setFont(MyTools.f2);
		lbl_getter.setBounds(56, 46, 54, 15);
		frame.getContentPane().add(lbl_getter);
		
		txt_getter = new JTextField();
		txt_getter.setBounds(112, 43, 396, 21);
		frame.getContentPane().add(txt_getter);
		txt_getter.setColumns(10);
		
		JLabel label = new JLabel("主  题：");
		label.setFont(MyTools.f2);
		label.setBounds(56, 91, 54, 15);
		frame.getContentPane().add(label);
		
		txt_subject = new JTextField();
		txt_subject.setBounds(112, 88, 396, 21);
		frame.getContentPane().add(txt_subject);
		txt_subject.setColumns(10);
		//添加附件到信件中
		btn_attchment = new JButton("附件");
		btn_attchment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(frame);
				chooser.setDialogTitle("选择一个文件");
				if(returnVal == JFileChooser.APPROVE_OPTION){
					String path = chooser.getSelectedFile().getAbsolutePath();
					attchment.add(path);
					isAttch = true;
				}
			}
		});
		btn_attchment.setFont(MyTools.f2);
		btn_attchment.setMargin(new Insets(0, 0, 0, 0));
		btn_attchment.setBounds(84, 117, 54, 23);
		frame.getContentPane().add(btn_attchment);
		
		//添加图片到文本中
		btn_img = new JButton("图片");
		btn_img.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//打开文件对话框
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "JPG & GIF Images", "jpg", "gif");
			    chooser.setFileFilter(filter);
			    chooser.setDialogTitle("选择一张图片");
			    String img ;
			    int returnVal = chooser.showOpenDialog(frame);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	//把图片放入到图片资源中
			    	Img.add(chooser.getSelectedFile().getAbsolutePath());
			    	//图片html格式
			    	img = "<img src='cid:"+imgId+"'/>";
			    	//获取文本框中的内容
			    	content = edt_content.getText();
			    	System.out.println(content);
			    	content += img;
			    	edt_content.setText(content);
			    	//图片id号
			    	imgId++;
			    	isImg = true;
			    	
			    }
				
				
			}
		});
		btn_img.setFont(MyTools.f2);
		btn_img.setMargin(new Insets(0, 0, 0, 0));
		btn_img.setBounds(143, 117, 54, 23);
		frame.getContentPane().add(btn_img);
		
		//发送邮件
		btn_send = new JButton("发送");
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("+++++++++++++++++++++++++"+AccountInfo.Account);
				if(!istransmit){					
					send();
				}else{
					transmit();
				}
			}
		});
		btn_send.setFont(MyTools.f2);
		btn_send.setMargin(new Insets(0, 0, 0, 0));
		btn_send.setBounds(460, 404, 54, 23);
		frame.getContentPane().add(btn_send);
		
		JButton btn_back = new JButton("返回");
		btn_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btn_back.setFont(MyTools.f2);
		btn_back.setMargin(new Insets(0, 0, 0, 0));
		btn_back.setBounds(329, 404, 66, 23);
		frame.getContentPane().add(btn_back);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(34, 155, 509, 221);
		frame.getContentPane().add(scrollPane);
		
		edt_content = new JEditorPane();
		scrollPane.setViewportView(edt_content);
	}
	public void send(){
		try {
			content  = edt_content.getText();
			auth = new MyAuthenticator(account, pwd, "smtp");
			session = auth.getSendSession();
			msg = new MyMessage(session);
			String[] id = new String[imgId];
			String subject = txt_subject.getText();
			String getter = txt_getter.getText().trim();
			msg.setSubject(subject);
			//设置图片id
			for(int i = 0;i < imgId; i++){
				id[i] = i + "";
			}
			//添加附件
			if(isAttch){
				msg.addAttach(attchment);
			}
			//添加图片和文字
			if(isImg){
				msg.ImgText(content, Img, id);
			}else{
				msg.addContent(content);
			}
			//设置收件人
			msg.setRecipient(RecipientType.TO, new InternetAddress(getter));
			msg.setFrom(new InternetAddress(account));
			transport=session.getTransport();
			transport.connect(account , pwd);
			transport.sendMessage(msg, new Address[]{new InternetAddress(getter)});
			JOptionPane.showMessageDialog(frame, "发送成功!");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if(transport != null){					
					transport.close();
				}
			} catch (MessagingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	public void transmit(){
		String getter;
		try {
			auth = new MyAuthenticator(account, pwd, "smtp");
			session = auth.getSendSession();
			session.setDebug(true);
			getter = txt_getter.getText().trim();
			//新建转发邮件
			this.transmit= new MyMessage(session);
			
			//设置收件人
			transmit.setRecipient(RecipientType.TO, new InternetAddress(getter));
			transmit.setFrom(new InternetAddress(account));
			transmit.setSubject(txt_subject.getText());
			transmit.addContent(edt_content.getText().toString());
			
			transport=session.getTransport();
			transport.connect(account , pwd);
			transport.sendMessage(transmit, new Address[]{new InternetAddress(getter)});
		} catch (AddressException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(frame, "发送成功!");
	}
	public void isEmpty(){
		
	}
}
