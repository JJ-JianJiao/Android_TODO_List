package com.example.jj.todoornot;

public class CloudItem {
    private String postDate;
    private String listTitle;
    private String content;
    private String completedFlag;

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCompletedFlag() {
        return completedFlag;
    }

    public void setCompletedFlag(String completedFlag) {
        this.completedFlag = completedFlag;
    }
}
