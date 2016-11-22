package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;
/**
 * 备注页获取备注的Bean
 * @author summer
 *
 */
public class FileRemarkBean extends BaseHttpResponse{
		private FileRemark datas;
		
		public FileRemark getDatas() {
			return datas;
		}

		public void setDatas(FileRemark datas) {
			this.datas = datas;
		}

		public class FileRemark {
			private String remarkText;

			public String getRemarkText() {
				return remarkText;
			}

			public void setRemarkText(String remarkText) {
				this.remarkText = remarkText;
			}
			
			
			
		}
}
