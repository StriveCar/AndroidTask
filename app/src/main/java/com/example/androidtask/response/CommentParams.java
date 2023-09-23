package com.example.androidtask.response;

public class CommentParams {

    private String content;
    private String shareId;
    private String userId;
    private String userName;
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }
    public String getShareId() {
        return shareId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

}