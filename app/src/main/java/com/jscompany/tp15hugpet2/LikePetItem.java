package com.jscompany.tp15hugpet2;

import java.sql.Timestamp;

public class LikePetItem {

    int likeNum;
    int desertionNo;
    int userNum;
    String date;

    public LikePetItem() {
    }

    public LikePetItem(int likeNum, int desertionNo, int userNum, String date) {
        this.likeNum = likeNum;
        this.desertionNo = desertionNo;
        this.userNum = userNum;
        this.date = date;
    }
}
