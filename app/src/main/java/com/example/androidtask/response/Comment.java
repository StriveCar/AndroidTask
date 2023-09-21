package com.example.androidtask.response;

/**
 * Copyright 2023 bejson.com
 */
import java.util.Date;

/**
 * Auto-generated: 2023-09-21 22:26:17
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
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
    private Date createTime;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    public String getAppKey() {
        return appKey;
    }

    public void setPUserId(String pUserId) {
        this.pUserId = pUserId;
    }
    public String getPUserId() {
        return pUserId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }
    public String getShareId() {
        return shareId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentUserId(String parentCommentUserId) {
        this.parentCommentUserId = parentCommentUserId;
    }
    public String getParentCommentUserId() {
        return parentCommentUserId;
    }

    public void setReplyCommentId(String replyCommentId) {
        this.replyCommentId = replyCommentId;
    }
    public String getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentUserId(String replyCommentUserId) {
        this.replyCommentUserId = replyCommentUserId;
    }
    public String getReplyCommentUserId() {
        return replyCommentUserId;
    }

    public void setCommentLevel(int commentLevel) {
        this.commentLevel = commentLevel;
    }
    public int getCommentLevel() {
        return commentLevel;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }
    public int getPraiseNum() {
        return praiseNum;
    }

    public void setTopStatus(int topStatus) {
        this.topStatus = topStatus;
    }
    public int getTopStatus() {
        return topStatus;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getCreateTime() {
        return createTime;
    }

}
