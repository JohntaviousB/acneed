package com.abusement.park.acneed.model;

import java.util.Date;
import java.util.Objects;

public class Video {

    private String uri;
    private Date uploadDate;
    private String filePath;

    public Video() {
    }

    public Video(String uri, Date uploadDate, String filePath) {
        this.uri = uri;
        this.uploadDate = uploadDate;
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getUploadDate() {
        return new Date(uploadDate.getTime());
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = new Date(uploadDate.getTime());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Video that = (Video) obj;
        return Objects.equals(uri, that.uri) &&
                Objects.equals(uploadDate, that.uploadDate);
    }
}
