package com.example.androidtask.response;

import com.example.androidtask.CustomDateAdapter;
import com.google.gson.annotations.JsonAdapter;

import java.util.Date;

public class Comment {
    private String id;
    private String appKey;
    private String pUserId;
    private String userName;
    private String shareId;
    private String parentCommentId;
    private String parentCommentUserId;
    private String replyCommentId;
    private String replyCommentUserId;
    private int commentLevel;
    private String content;
    private int status;
    private int praiseNum;
    private int topStatus;
    @JsonAdapter(CustomDateAdapter.CustomDateTypeAdapter.class)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getpUserId() {
        return pUserId;
    }

    public void setpUserId(String pUserId) {
        this.pUserId = pUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getParentCommentUserId() {
        return parentCommentUserId;
    }

    public void setParentCommentUserId(String parentCommentUserId) {
        this.parentCommentUserId = parentCommentUserId;
    }

    public String getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(String replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public String getReplyCommentUserId() {
        return replyCommentUserId;
    }

    public void setReplyCommentUserId(String replyCommentUserId) {
        this.replyCommentUserId = replyCommentUserId;
    }

    public int getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(int commentLevel) {
        this.commentLevel = commentLevel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getTopStatus() {
        return topStatus;
    }

    public void setTopStatus(int topStatus) {
        this.topStatus = topStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", appKey='" + appKey + '\'' +
                ", pUserId='" + pUserId + '\'' +
                ", userName='" + userName + '\'' +
                ", shareId='" + shareId + '\'' +
                ", parentCommentId='" + parentCommentId + '\'' +
                ", parentCommentUserId='" + parentCommentUserId + '\'' +
                ", replyCommentId='" + replyCommentId + '\'' +
                ", replyCommentUserId='" + replyCommentUserId + '\'' +
                ", commentLevel=" + commentLevel +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", praiseNum=" + praiseNum +
                ", topStatus=" + topStatus +
                ", createTime=" + createTime +
                '}';
    }
}
