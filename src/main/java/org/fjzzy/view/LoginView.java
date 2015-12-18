package org.fjzzy.view;

import java.awt.EventQueue;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.mail.*;
import javax.swing.JButton;
import org.fjzzy.tool.*;
import java.awt.Font;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.fjzzy.model.*;

public class LoginView {

	private JFrame frame;
	private JTextField txt_account;
	private JPasswordField txt_pwd;
	private String Account = "";
	private String Pwd = "";
	private JCheckBox chk_pwd;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView window = new LoginView();
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
	public LoginView() {
		initialize();
		String[] info = getLastAP();
		if(info != null){
			System.out.println("++++++++++++++");
			txt_account.setText(info[0]);
			txt_pwd.setText(info[1]);
			chk_pwd.setSelected(true);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 427, 351);
		frame.setTitle("登录");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lb1_title = new JLabel("邮件客户端");
		lb1_title.setBounds(160, 21, 112, 38);
		lb1_title.setFont(MyTools.f1);
		frame.getContentPane().add(lb1_title);
		
		JLabel lbl_accunt = new JLabel("账号：");
		lbl_accunt.setBounds(99, 84, 52, 15);
		lbl_accunt.setFont(MyTools.f2);
		frame.getContentPane().add(lbl_accunt);
		
		JLabel lbl_pwd = new JLabel("密码：");
		lbl_pwd.setBounds(97, 130, 54, 15);
		lbl_pwd.setFont(MyTools.f2);
		frame.getContentPane().add(lbl_pwd);
		
		txt_account = new JTextField();
		txt_account.setBounds(145, 81, 171, 21);
		frame.getContentPane().add(txt_account);
		txt_account.setColumns(10);
		
		txt_pwd = new JPasswordField();
		txt_pwd.setBounds(145, 127, 171, 21);
		frame.getContentPane().add(txt_pwd);
		
		chk_pwd = new JCheckBox("记住密码");
		chk_pwd.setFont(MyTools.f3);
		chk_pwd.setBounds(94, 166, 103, 23);
		frame.getContentPane().add(chk_pwd);
		//登录按钮
		JButton btn_login = new JButton("登录");
		btn_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean isconnect = false;
				Account = txt_account.getText().trim().toString();
				Pwd = new String(txt_pwd.getPassword());
				MyAuthenticator auth1 = null;
				Session session = null;
				Store store = null;
				Folder folder = null;
				try {
					auth1 = new MyAuthenticator(Account, Pwd, "pop3");
					session = auth1.getStoreSession();
					session.setDebug(true);
					store = session.getStore("pop3");
					store.connect();
					isconnect = store.isConnected();
					
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}finally{
					try {
						if(folder != null){
							folder.close(true);
						}
					} catch (MessagingException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					if(!isconnect){
						JOptionPane.showMessageDialog(frame, "账号密码错误\n请检查是否已将邮件的SMTP/POP3协议打开!","连接失败",JOptionPane.INFORMATION_MESSAGE);
						
					}else{
						//保存密码
						AccountInfo.Account = Account;
						AccountInfo.Password = Pwd;
						saveInfo();
						frame.dispose();
						MsgListView msgListView = new MsgListView(Account,Pwd);
						
					}
				}
				
			}
			
		});
		btn_login.setBounds(99, 219, 72, 23);
		frame.getContentPane().add(btn_login);
		//取消按钮
		JButton btn_cancle = new JButton("取消");
		btn_cancle.setBounds(244, 219, 72, 23);
		btn_cancle.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				System.exit(1);
				
			}
		});
		
		frame.getContentPane().add(btn_cancle);
	}
	
	//获得最后一次登录的密码账号
	public String[] getLastAP(){
		File file = new File("resource/LoginMember.txt");
		FileReader filereader = null;
		BufferedReader reader = null;
		String Line = "";
		try {
			filereader = new FileReader(file);
			reader = new BufferedReader(filereader);
			Line = reader.readLine().trim();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(Line.equals("")){
			return null;
		}else {
			return Line.split(" ");
		}
	}
	//存储密码与账号
	public void saveInfo(){
		File file = new File("resource/LoginMember.txt");
		FileWriter fileWriter = null;
		BufferedWriter writer = null;
		if(chk_pwd.isSelected()){
			
			try {
				fileWriter = new FileWriter(file);
				writer = new BufferedWriter(fileWriter);
				writer.write(txt_account.getText().trim()+" "+
						new String(txt_pwd.getPassword()).trim());
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				try {
					writer.close();
					fileWriter.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
		}
		
	}
}
