package org.fjzzy.model;

import java.awt.EventQueue;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.apache.geronimo.javamail.store.pop3.POP3Message;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class SetTab {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SetTab window = new SetTab();
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
	public SetTab() {
		try {
			initialize();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	MyFolder folder = null;
	JTable tbl = null;
	private void initialize() throws Exception {
		frame = new JFrame();
		frame.setBounds(100, 100, 552, 387);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MyAuthenticator auth1 = new MyAuthenticator("1593423661@qq.com", "a9908651251", "pop3");
		folder = new MyFolder(auth1);
		tbl = new JTable(folder);
		
		JScrollPane scrollPane = new JScrollPane(tbl);
		
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					for(Message msg : folder.getMessages()){
						
						Message pmsg =  msg;
					}
					
				} catch (MessagingException e1) {
					e1.printStackTrace();
				}
				FileOutputStream fileOutputStream = null;
				BufferedOutputStream bufferouput = null;
				try {
					Message msg = folder.getMessage(16);
					if(msg.getContent() instanceof Multipart){
						
						MimeMultipart multipart = (MimeMultipart) msg.getContent();
						System.out.println(multipart.getBodyPart(0).getFileName()+"+++++++++++");
					}
				} catch (Exception e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				} 
			}
		});
		
		scrollPane.setRowHeaderView(btnNewButton);
		
	}
}
