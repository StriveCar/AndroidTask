
package com.example.androidtask.response;

public class ImageText {

    private String content;
    private String imageCode;
    private String pUserId;
    private String title;
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ImageText{" +
                "content='" + content + '\'' +
                ", imageCode='" + imageCode + '\'' +
                ", pUserId='" + pUserId + '\'' +
                ", title='" + title + '\'' +
                ", id='" + id + id + '\'' +
                '}';
    }
}
