package com.jscompany.tp15hugpet2;

import java.util.ArrayList;

public class NoticeItem {

    int ntcNo;
    String title;
    String content;
    int userNo;
    String date;

    public NoticeItem() {
    }

    public NoticeItem(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public NoticeItem(int ntcNo, String title, String date) {
        this.ntcNo = ntcNo;
        this.title = title;
        this.date = date;
    }
}
