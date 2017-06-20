package com.truthso.ip360.bean;

/**
 * Created by summer on 2017/6/6.
 */
public class File{

    private String fileSize;
    private String fileTitle;
    private String fileDate;
    private String fileUrl;
    private int pkValue;
    private int type;
    private String mobileType;
    private String fileFormatType;//1-文本 2-图片 3-音视频

    public String getFileFormatType() {
        return fileFormatType;
    }

    public void setFileFormatType(String fileFormatType) {
        this.fileFormatType = fileFormatType;
    }

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

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }
}
