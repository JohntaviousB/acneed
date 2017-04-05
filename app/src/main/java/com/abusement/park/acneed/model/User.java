package com.abusement.park.acneed.model;


import java.util.List;
import java.util.Objects;

public class User {

    private String uid;
    private String email;
    private String password;
    private List<Image> images;
    private Settings settings;

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public User() {
    }

    public User(String email, String password, String uid) {
        this.email = email;
        this.password = password;
        this.uid = uid;
    }

//    public static User.Builder builder() {
//        return new User.Builder();
//    }

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

//    public static class Builder {
//        private User user;
//
//        public Builder() {
//            user = new User();
//        }
//
//        public Builder email(String email) {
//            user.email = email;
//            return this;
//        }
//
//        public Builder uid(String uid) {
//            user.uid = uid;
//            return this;
//        }
//
//        public Builder password(String password) {
//            user.password = password;
//            return this;
//        }
//
//        public Builder images(List<Image> images) {
//            user.images = images;
//            return  this;
//        }
//
//        public Builder settings(Settings settings) {
//            user.settings = settings;
//            return this;
//        }
//
//        public User build() {
//            return user;
//        }
//
//    }
}
