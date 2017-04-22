package com.abusement.park.acneed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties({"image_id", "request_id", "time_used", } )
public class FacialDetectionResult {

    private Face[] faces;

    public Face[] getFaces() {
        if (faces != null) {
            return faces.clone();
        } else {
            return new Face[0];
        }
    }

    public void setFaces(Face[] faces) {
        if (faces != null) {
            this.faces = faces.clone();
        } else {
            this.faces = null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return false;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        FacialDetectionResult that = (FacialDetectionResult) obj;
        return Arrays.equals(faces, that.faces);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(faces);
    }
}
