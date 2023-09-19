package com.example.androidtask.response;

public class ImageText {

    private String content;
    private String id;
    private String imageCode;
    private String pUserId;
    private String title;
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ImageText{" +
                "content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", imageCode='" + imageCode + '\'' +
                ", pUserId='" + pUserId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
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

}
