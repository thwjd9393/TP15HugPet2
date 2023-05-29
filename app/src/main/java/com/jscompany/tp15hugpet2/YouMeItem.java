package com.jscompany.tp15hugpet2;

import java.util.Date;

public class YouMeItem {
    int bNo;
    String title;
    String content;
    String nicName;
    String viewCnt;
    int userNo;
    String date;

    public YouMeItem() {
    }

    public YouMeItem(int bNo, String title, String viewCnt) {
        this.bNo = bNo;
        this.title = title;
        this.viewCnt = viewCnt;
    }

    public YouMeItem(int bNo, String title, String content, String nicName, String viewCnt, int userNo, String date) {
        this.bNo = bNo;
        this.title = title;
        this.content = content;
        this.nicName = nicName;
        this.viewCnt = viewCnt;
        this.userNo = userNo;
        this.date = date;
    }
}
