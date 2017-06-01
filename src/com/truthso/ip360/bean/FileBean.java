package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;

/**
 * @despriction :我的公证 中的证据信息
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/5/27 17:56
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class FileBean extends BaseHttpResponse{
//    fileSize	文件大小	String(10)	文件大小	非空	28K
//    fileTitle	文件名称	String(30)	文件名称	非空	W20160913.jpg
//    fileDate	时间	文件取证的时间。格式：
//    yyyy-MM-dd HH:mm:ss	非空	2016-09-13 10:44:06	文件取证的时间。格式：
//    yyyy-MM-dd HH:mm:ss
//    fileUrl	文件路径	String(255)	云URL。用于在线播放场景	非空
//    pkValue	证据记录主键值	Integer	证据记录唯一标识，查看证书时候传	非空
//    type	请求展示类别	Integer	1-确权  2-现场取证 3-pc取证	非空	2
//    mobileType	取证类型	Integer	现场取证 （拍照（50001）、录像（50003）、录音（50002）	可空	现场取证传递此值
  private File datas;

    public File getDatas() {
        return datas;
    }

    public void setDatas(File datas) {
        this.datas = datas;
    }

    public class File{

        private String fileSize;
        private String fileTitle;
        private String fileDate;
        private String fileUrl;
        private int pkValue;
        private int type;
        private int mobileType;

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public String getFileTitle() {
            return fileTitle;
        }

        public void setFileTitle(String fileTitle) {
            this.fileTitle = fileTitle;
        }

        public String getFileDate() {
            return fileDate;
        }

        public void setFileDate(String fileDate) {
            this.fileDate = fileDate;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPkValue() {
            return pkValue;
        }

        public void setPkValue(int pkValue) {
            this.pkValue = pkValue;
        }

        public int getMobileType() {
            return mobileType;
        }

        public void setMobileType(int mobileType) {
            this.mobileType = mobileType;
        }
    }

}
