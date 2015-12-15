package org.fjzzy.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import org.fjzzy.model.*;
import org.fjzzy.tool.MyTools;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Insets;
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
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MsgListView window = new MsgListView();
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
				boolean b = false;
				try {
					b = folder.getFolder().hasNewMessages();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
				if(b){
					System.out.println("+++++++++++++++++++++");
				}else{
					System.out.println("------------------------");
				}
				
			}
		});
		btn_receve.setMargin(new Insets(0, 0, 0, 0));
		btn_receve.setBounds(21, 45, 57, 23);
		panel.add(btn_receve);
		
		JButton btn_write = new JButton("写信");
		btn_write.setBounds(21, 115, 57, 23);
		btn_write.setMargin(new Insets(0, 0, 0, 0));
		panel.add(btn_write);
		
		JButton btn_open = new JButton("打开");
		btn_open.setBounds(21, 188, 57, 23);
		btn_open.setMargin(new Insets(0, 0, 0, 0));
		panel.add(btn_open);
		
		JButton button = new JButton("退出");
		button.setBounds(21, 259, 57, 23);
		panel.add(button);
		
		panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		Thread t = new Thread(this);
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
		tbl = new JTable(folder);
		tbl.setRowHeight(25);
		tbl.setFont(MyTools.f4);
		jsp_list = new JScrollPane(tbl);
		panel_1.add(jsp_list, BorderLayout.CENTER);
		frame.setVisible(true);
	}
}
