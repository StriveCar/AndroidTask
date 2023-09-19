package com.example.androidtask.response;

import java.io.Serializable;
import java.util.List;

public class sharelist_item implements Serializable {

    private Records record;
    private String profileUrl;

    public Records getRecord() {
        return record;
    }

    public void setRecord(Records record) {
        this.record = record;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
