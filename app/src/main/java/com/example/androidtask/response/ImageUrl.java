
package com.example.androidtask.response;


import java.util.List;


public class ImageUrl {

    private String imageCode;
    private List<String> imageUrlList;
    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }
    public String getImageCode() {
        return imageCode;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    @Override
    public String toString() {
        return "ImageUrl{" +
                "imageCode='" + imageCode + '\'' +
                ", imageUrlList=" + imageUrlList +
                '}';
    }
}
