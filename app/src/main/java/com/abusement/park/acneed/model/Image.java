package com.abusement.park.acneed.model;

import java.util.Date;
import java.util.Objects;

public class Image {

    private String uri;
    private Date uploadDate;

    public Image() {
    }

    public Image(String uri, Date uploadDate) {
        this.uri = uri;
        this.uploadDate = uploadDate;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Image that = (Image)obj;
        return Objects.equals(uri, that.uri)
                && Objects.equals(uploadDate, that.uploadDate);
    }

    @Override
    public int hashCode(){
        return Objects.hash(uploadDate, uri);
    }
}
