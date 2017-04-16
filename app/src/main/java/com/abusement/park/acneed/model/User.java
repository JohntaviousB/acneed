package com.abusement.park.acneed.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private String uid;
    private String email;
    private String password;
    private List<Image> images;
    private List<Video> videos;
    private Settings settings;

    public User() {
        images = new ArrayList<>();
        videos = new ArrayList<>();
        settings = Settings.DEFAULT_SETTINGS;
    }

    public User(String email, String password, String uid) {
        this.email = email;
        this.password = password;
        this.uid = uid;
        images = new ArrayList<>();
        settings = Settings.DEFAULT_SETTINGS;
    }

    public void addImage(Image image) {
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(image);
    }

    public void removeImage(Image image) {
        if (images != null) {
            images.remove(image);
        }
    }

    public void removeImage(int index) {
        if (images != null) {
            images.remove(index);
        }
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public void addVideo(Video toAdd) {
        if (videos == null) {
            videos = new ArrayList<>();
        }
        videos.add(toAdd);
    }

    public void removeVideo(int index) {
        if (videos != null) {
            videos.remove(index);
        }
    }

    public void removeVideo(Video toRemove) {
        if (videos != null){
            videos.remove(toRemove);
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        User that = (User)obj;
        return Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

}
