package com.jscompany.tp15hugpet2;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URI;

public class ShelterItem {

    String desertionNo; //유기견 고유번호
    String kindCd; //종
    String colorCd; //색
    String age;
    String weight;
    String noticeNo; //공고번호
    String happenPlace; //발견장소
    String noticeSdt; //공고시작
    String noticeEdt; //공고끝
    //Bitmap popfile; //사진 주소
    String popfile;
    String sexCd; //성별
    String neuterYn; //중성화
    String processState; //보호상태
    String specialMark; //특징
    String careNm; //보호소
    String careTel; //보호소번호
    String careAddr; //보호소주소
    String chargeNm; //담당자
    String officetel; //보호담당자

    //축종코드
    String petKind;

    public ShelterItem() {
    }

    public ShelterItem(String desertionNo, String kindCd, String noticeNo, String popfile, String sexCd, String processState) {
        this.desertionNo = desertionNo;
        this.kindCd = kindCd;
        this.noticeNo = noticeNo;
        this.popfile = popfile;
        this.sexCd = sexCd;
        this.processState = processState;
    }

    public ShelterItem(String desertionNo, String kindCd, String noticeNo, String popfile, String sexCd, String processState, String petKind) {
        this.desertionNo = desertionNo;
        this.kindCd = kindCd;
        this.noticeNo = noticeNo;
        this.popfile = popfile;
        this.sexCd = sexCd;
        this.processState = processState;
        this.petKind = petKind;
    }

}
