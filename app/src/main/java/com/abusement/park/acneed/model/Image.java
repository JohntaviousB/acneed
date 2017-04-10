package com.abusement.park.acneed.model;

import java.util.Date;
import java.util.Objects;

public class Image {

    private Date uploadDate;
    private String uri;

    public Image() {
    }

    public Image(String uri, Date uploadDate) {
        this.uploadDate = new Date(uploadDate.getTime());
        this.uri = uri;
    }


    public Date getUploadDate() {
        return new Date(uploadDate.getTime());
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = new Date(uploadDate.getTime());
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
