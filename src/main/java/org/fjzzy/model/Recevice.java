package org.fjzzy.model;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import java.io.*;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import org.eclipse.swt.widgets.Text;
public class Recevice {

	protected Shell shell;
	private Browser browser;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Recevice window = new Recevice();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	static int i=0;
	private Text text;
	protected void createContents() {
		shell = new Shell();
		shell.setSize(915, 526);
		shell.setText("SWT Application");
		
		browser = new Browser(shell, SWT.NONE);
		browser.setBounds(10, 43, 880, 404);
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
	    		MyAuthenticator auth1 = new MyAuthenticator("1593423661@qq.com", "a9908651251", "pop3");
	    		MyFolder folder1;
				try {
					folder1 = new MyFolder(auth1);
					Folder folder = folder1.getFolder();
					int j = Integer.parseInt(text.getText());
					Message msg = folder.getMessage(j);
					MailParser parser = new MailParser(msg);
					parser.parsing(msg);
					MessageContent content = parser.getMsgContent();
					browser.setText(content.getContent());
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
				
				
			}
		});
		btnNewButton.setBounds(59, 10, 80, 27);
		btnNewButton.setText("New Button");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(172, 10, 73, 23);

	}
}
