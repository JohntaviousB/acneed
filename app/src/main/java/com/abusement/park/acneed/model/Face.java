package com.abusement.park.acneed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(value = "attributes")
public class Face {

    @JsonProperty(value = "face_token")
    private String faceToken;
    @JsonProperty(value = "face_rectangle")
    private FaceRectangle faceRectangle;

    public String getFaceToken() {
        return faceToken;
    }

    public void setFaceToken(String faceToken) {
        this.faceToken = faceToken;
    }

    public FaceRectangle getFaceRectangle() {
        return faceRectangle;
    }

    public void setFaceRectangle(FaceRectangle faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Face that = (Face) obj;
        return Objects.equals(faceRectangle, that.faceRectangle)
                && Objects.equals(faceToken, that.faceToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(faceRectangle, faceToken);
    }
}
