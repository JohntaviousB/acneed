package com.abusement.park.acneed.model;


import java.util.Objects;

public class FaceRectangle {

    private int width;
    private int height;
    private int top;
    private int left;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        FaceRectangle that = (FaceRectangle) obj;
        return left == that.left && top == that.top && height == that.height && width == that.width;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, top, height, width);
    }
}
