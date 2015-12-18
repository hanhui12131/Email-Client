package org.fjzzy.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import org.fjzzy.model.*;
import org.fjzzy.tool.MyTools;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Insets;
import java.awt.JobAttributes;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MsgListView implements Runnable{

	private JFrame frame;
	private String Account = null;
	private String Pwd = null;
	private MyFolder folder = null;
	private JTable tbl = null;
	private JScrollPane jsp_list;
	private MyAuthenticator auth1 = null;
	private JPanel panel_1 = null;
	private Thread t = null;
	private static boolean isFrist = true;
	private static boolean isRece = false;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MsgListView window = new MsgListView();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public MsgListView(String Account, String Pwd) {
		this.Account = Account;
		this.Pwd = Pwd;
		initialize();
		
	}
	public MsgListView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setTitle("信件列表");
		frame.setBounds(100, 100, 776, 526);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("账户管理");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("重新登录");
		menu.add(menuItem);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		splitPane.setDividerLocation(100);
		splitPane.setDividerSize(0);
		panel.setLayout(null);
		
		JButton btn_receve = new JButton("收信");
		btn_receve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "正在收信,请稍后!");
				if(!isRece){					
					thread();
					isRece = true;
				}else{					
					JOptionPane.showMessageDialog(frame, "正在收信,请稍后!");
				}
			}
		});
		btn_receve.setFont(MyTools.f2);
		btn_receve.setMargin(new Insets(0, 0, 0, 0));
		btn_receve.setBounds(21, 45, 57, 23);
		panel.add(btn_receve);
		
		JButton btn_write = new JButton("写信");
		btn_write.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WriteMsg writemsg = new WriteMsg();
			}
		});
		btn_write.setBounds(21, 115, 57, 23);
		btn_write.setFont(MyTools.f2);
		btn_write.setMargin(new Insets(0, 0, 0, 0));
		panel.add(btn_write);
		
		JButton btn_open = new JButton("打开");
		btn_open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = tbl.getSelectedRow()+1;
				Message msg = null;
				if(index <1){
					JOptionPane.showMessageDialog(frame, "请选择一行");
				}else{
					try {
						msg = folder.getMessage(index);
						MsgShowView showView  = new MsgShowView(msg);
						showView.open();
					} catch (MessagingException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}
			}
		});
		btn_open.setBounds(21, 188, 57, 23);
		btn_open.setFont(MyTools.f2);
		btn_open.setMargin(new Insets(0, 0, 0, 0));
		panel.add(btn_open);
		
		JButton button = new JButton("退出");
		button.setMargin(new Insets(0, 0, 0, 0));
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				System.exit(1);
			}
		});
		button.setFont(MyTools.f2);
		button.setBounds(21, 259, 57, 23);
		panel.add(button);
		
		panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		thread();
		
	}
	public void thread(){
		t = new Thread(this);
		t.start();
	}
	
	public void run() {
		// TODO 自动生成的方法存根
		auth1 = new MyAuthenticator(Account, Pwd, "pop3");
		try {
			folder = new MyFolder(auth1);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if(isFrist){
			isFrist = false;
			tbl = new JTable(folder);
			tbl.setRowHeight(25);
			tbl.setFont(MyTools.f4);
			jsp_list = new JScrollPane(tbl);
			panel_1.add(jsp_list, BorderLayout.CENTER);
		}else{
			try {
				folder = new MyFolder(auth1);
				JOptionPane.showMessageDialog(frame, "收信成功!");
				isRece = false;
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				JOptionPane.showMessageDialog(frame, "收信失败!");
			}
			tbl.setModel(folder);
		}
		frame.setVisible(true);
	}
	public void folderClose(){
		folder.close();
	}
	
}
