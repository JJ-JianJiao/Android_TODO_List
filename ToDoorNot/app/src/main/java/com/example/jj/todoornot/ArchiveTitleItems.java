package com.example.jj.todoornot;

public class ArchiveTitleItems {
    private String itemId;
    private String titleName;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getCompletedFlag() {
        return completedFlag;
    }

    public void setCompletedFlag(String completedFlag) {
        this.completedFlag = completedFlag;
    }

    private String itemDescription;
    private String completedFlag;
}
