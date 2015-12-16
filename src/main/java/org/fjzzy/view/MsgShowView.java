package org.fjzzy.view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.fjzzy.model.MailParser;
import org.fjzzy.model.*;
import org.fjzzy.model.MyMessage;
import org.eclipse.swt.browser.Browser;

import javax.mail.Message;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

public class MsgShowView {

	protected Shell shell;
	private Message msg = null;
	private MessageContent content;
	private Label lbl_subject;
	private Label lbl_data;
	private Browser browser;
	private MailParser parser;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MsgShowView window = new MsgShowView();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
		shell.setSize(945, 588);
		shell.setText("信件内容");
		
		browser = new Browser(shell, SWT.NONE);
		browser.setBounds(10, 62, 909, 428);
		
		Button btn_del = new Button(shell, SWT.NONE);
		btn_del.setBounds(43, 512, 56, 27);
		btn_del.setText("删除");
		
		Button btn_reply = new Button(shell, SWT.NONE);
		btn_reply.setBounds(136, 512, 56, 27);
		btn_reply.setText("回复");
		
		Button btn_transmit = new Button(shell, SWT.NONE);
		btn_transmit.setBounds(232, 512, 56, 27);
		btn_transmit.setText("转发");
		
		Button btn_extract = new Button(shell, SWT.NONE);
		btn_extract.setBounds(328, 512, 72, 27);
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

	}
	public void showMsg(){
		try {
			parser = new MailParser(msg);
			parser.parsing(msg);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		content = parser.getMsgContent();
		browser.setText(content.getContent());
		lbl_subject.setText(content.getSubject());
		lbl_data.setText(content.getSendDate());
		
	}
}
