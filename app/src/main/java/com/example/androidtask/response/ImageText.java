package com.example.androidtask.response;

public class ImageText {

    private String content;
    private String imageCode;
    private String pUserId;
    private String title;
    public void setContent(String content) {
        this.content = content;
    }



    public String getContent() {
        return content;
    }


    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }
    public String getImageCode() {
        return imageCode;
    }

    public void setPUserId(String pUserId) {
        this.pUserId = pUserId;
    }
    public String getPUserId() {
        return pUserId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return "ImageText{" +
                "content='" + content + '\'' +
                ", imageCode='" + imageCode + '\'' +
                ", pUserId='" + pUserId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
