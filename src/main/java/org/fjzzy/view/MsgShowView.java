package org.fjzzy.view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.fjzzy.model.MailParser;
import org.fjzzy.model.*;
import org.fjzzy.model.MyMessage;
import org.eclipse.swt.browser.Browser;

import java.util.ArrayList;

import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MsgShowView {

	protected Shell shell;
	private Message msg = null;
	private static MessageContent content;
	private Label lbl_subject;
	private Label lbl_data;
	private Browser browser;
	private MailParser parser;
	private Label lbl_attch;
	private Button btn_extract;

	/**
	 * Launch the application.
	 * @param args
	 */
//	public static void main(String[] args) {
//		try {
//			MsgShowView window = new MsgShowView();
//			window.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public MsgShowView(Message msg){
		this.msg = msg;
	}
	public MsgShowView(){
		
	}
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		showMsg();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(948, 630);
		shell.setText("信件内容");
		
		browser = new Browser(shell, SWT.NONE);
		browser.setBounds(10, 62, 909, 428);
		//删除邮件
		Button btn_del = new Button(shell, SWT.NONE);
		btn_del.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					msg.setFlag(Flag.DELETED,false);
					msg.saveChanges();
				} catch (MessagingException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					MessageBox box = new MessageBox(shell);
					box.setMessage("删除失败！POP3协议\\IMAP协议的邮件为只读");
					box.open();
				}
			}
		});
		btn_del.setBounds(43, 512, 56, 27);
		btn_del.setText("删除");
		//回复邮件
		Button btn_reply = new Button(shell, SWT.NONE);
		btn_reply.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String from ="";
				try {
					from = msg.getFrom()[0].toString();
				} catch (MessagingException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				WriteMsg w = new WriteMsg(from);
				shell.dispose();
				
			}
		});
		btn_reply.setBounds(136, 512, 56, 27);
		btn_reply.setText("回复");
		//转发邮件
		Button btn_transmit = new Button(shell, SWT.NONE);
		btn_transmit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			
				WriteMsg w = new WriteMsg(content);
				shell.dispose();
			}
		});
		btn_transmit.setBounds(232, 512, 56, 27);
		btn_transmit.setText("转发");
		//提取附件
		btn_extract = new Button(shell, SWT.NONE);
		btn_extract.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog dir = new DirectoryDialog(shell);
				dir.open();
				String path = dir.getFilterPath();
				if(path != null){
					try {
						content.getAttach(path+"/");
						MessageBox box = new MessageBox(shell);
						box.setMessage("接收成功!");
						box.open();
						btn_extract.setEnabled(false);
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						MessageBox box = new MessageBox(shell);
						box.setMessage("接收失败!");
						box.open();
					}
				}
			}
		});
		btn_extract.setBounds(43, 554, 72, 27);
		btn_extract.setText("提取附件");
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(23, 10, 38, 17);
		lblNewLabel.setText("主题:");
		
		lbl_subject = new Label(shell, SWT.NONE);
		lbl_subject.setBounds(67, 10, 535, 17);
		lbl_subject.setText("-");
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(23, 33, 61, 17);
		label.setText("时间:");
		
		lbl_data = new Label(shell, SWT.NONE);
		lbl_data.setBounds(67, 33, 535, 17);
		lbl_data.setText("-");
		
		lbl_attch = new Label(shell, SWT.NONE);
		lbl_attch.setBounds(131, 564, 61, 17);
		lbl_attch.setText("--");

	}
	//将邮件内容显示在页面控件上
	public void showMsg(){
		try {
			parser = new MailParser(msg);
			parser.parsing(msg);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		content = parser.getMsgContent();
		//判断邮件内容是否为空
		if(content.getContent() != null){			
			browser.setText(content.getContent());
		}
		if(content.getSubject() != null){			
			lbl_subject.setText(content.getSubject());
		}
		if(content.getSendDate() != null){			
			lbl_data.setText(content.getSendDate());
		}
		//是否有附件
		if(content.hasAttch){
			ArrayList<String > attch = content.attch.fileName;
			String s = "";
			for(String name : attch){
				s += name+" ";
			}
			lbl_attch.setText(s);
		}else{
			lbl_attch.setEnabled(false);
			btn_extract.setEnabled(false);
		}
	}
}
