package com.example.androidtask.response;

import java.util.List;


public class Records {

    private String id;
    private String pUserId;
    private String imageCode;
    private String title;
    private String content;
    private String createTime;
    private List<String> imageUrlList;
    private String likeId;
    private int likeNum;
    private boolean hasLike;
    private String collectId;
    private int collectNum;
    private boolean hasCollect;
    private boolean hasFocus;
    private String username;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setPUserId(String pUserId) {
        this.pUserId = pUserId;
    }
    public String getPUserId() {
        return pUserId;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }
    public String getImageCode() {
        return imageCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }
    public String getLikeId() {
        return likeId;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
    public int getLikeNum() {
        return likeNum;
    }

    public void setHasLike(boolean hasLike) {
        this.hasLike = hasLike;
    }
    public boolean getHasLike() {
        return hasLike;
    }

    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }
    public String getCollectId() {
        return collectId;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }
    public int getCollectNum() {
        return collectNum;
    }

    public void setHasCollect(boolean hasCollect) {
        this.hasCollect = hasCollect;
    }
    public boolean getHasCollect() {
        return hasCollect;
    }

    public void setHasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
    public boolean getHasFocus() {
        return hasFocus;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }


    @Override
    public String toString() {
        return "Records{" +
                "id='" + id + '\'' +
                ", pUserId='" + pUserId + '\'' +
                ", imageCode='" + imageCode + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", imageUrlList=" + imageUrlList +
                ", likeId='" + likeId + '\'' +
                ", likeNum=" + likeNum +
                ", hasLike=" + hasLike +
                ", collectId='" + collectId + '\'' +
                ", collectNum=" + collectNum +
                ", hasCollect=" + hasCollect +
                ", hasFocus=" + hasFocus +
                ", username='" + username + '\'' +
                '}';
    }
}
