package com.truthso.ip360.bean;

/**
 * Created by Administrator on 2017/5/4.
 */

public class WaituploadBean {
    private int id;
    private String filePath;
    private String fileTitle;
    private int fileType;
    private String fileSize;
    private String hashCode;
    private String fileDate;
    private String fileLocation;
    private String fileTime;
    private String latitudeLongitude;
    private String priKey;
    private int rsaId;

    public WaituploadBean() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public String getLatitudeLongitude() {
        return latitudeLongitude;
    }

    public void setLatitudeLongitude(String latitudeLongitude) {
        this.latitudeLongitude = latitudeLongitude;
    }

    public String getPriKey() {
        return priKey;
    }

    public void setPriKey(String priKey) {
        this.priKey = priKey;
    }

    public int getRsaId() {
        return rsaId;
    }

    public void setRsaId(int rsaId) {
        this.rsaId = rsaId;
    }

    @Override
    public String toString() {
        return "WaituploadBean{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileTitle='" + fileTitle + '\'' +
                ", fileType=" + fileType +
                ", fileSize='" + fileSize + '\'' +
                ", hashCode='" + hashCode + '\'' +
                ", fileDate='" + fileDate + '\'' +
                ", fileLocation='" + fileLocation + '\'' +
                ", fileTime='" + fileTime + '\'' +
                ", latitudeLongitude='" + latitudeLongitude + '\'' +
                ", priKey='" + priKey + '\'' +
                ", rsaId=" + rsaId +
                '}';
    }
}
