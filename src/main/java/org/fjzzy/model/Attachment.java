package org.fjzzy.model;

import java.io.InputStream;
import java.util.ArrayList;

public class Attachment {
	// 附件名
		public ArrayList<String> fileName;
		// 附件输入流
		ArrayList<InputStream> in;
		//初始化
		public Attachment() {
			fileName = new ArrayList<String>();
			in = new ArrayList<InputStream>();
		}
		//添加附件信息
		public void add(String name, InputStream input) {
			fileName.add(name);
			this.in.add(input);
		}
}
