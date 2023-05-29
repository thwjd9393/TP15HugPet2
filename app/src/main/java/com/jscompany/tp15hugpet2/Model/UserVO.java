package com.jscompany.tp15hugpet2.Model;

public class UserVO {

    private int userNo;
    private String userId;
    private String userPasswd;
    private String userNic;

    public UserVO() {

    }

    public UserVO(String userId, String userPasswd, String userNic) {
        this.userId = userId;
        this.userPasswd = userPasswd;
        this.userNic = userNic;
    }

    public UserVO(int userNo, String userId, String userPasswd, String userNic) {
        this.userNo = userNo;
        this.userId = userId;
        this.userPasswd = userPasswd;
        this.userNic = userNic;
    }
}
