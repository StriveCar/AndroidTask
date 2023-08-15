package com.example.androidtask.response;

public class LoginData {

    private String id;
    private String appKey;
    private String username;
    private String password;
    private String sex;
    private String introduce;
    private String avatar;
    private String createTime;
    private String lastUpdateTime;
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

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getSex() {
        return sex;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
    public String getIntroduce() {
        return introduce;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "id='" + id + '\'' +
                ", appKey='" + appKey + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", introduce='" + introduce + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime='" + createTime + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                '}';
    }
}
