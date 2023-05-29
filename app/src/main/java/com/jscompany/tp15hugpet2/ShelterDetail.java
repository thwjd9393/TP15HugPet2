package com.jscompany.tp15hugpet2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShelterDetail extends AppCompatActivity {

    ImageView popfile, processState; //사진, 보호상태, 좋아요 버튼(내 관심 동물)
    TextView kindCd, sexCd, neuterYn, colorCd, age, weight;
    TextView notice_no, notice_sdt, notice_edt, happen_place;
    TextView care_nm, care_tel, care_addr, charge_nm, officetel, special_mark;

    ToggleButton toggle_like;

    String desertionNoExtra = null;
    String petKindExtra = null;

    ShelterItem finalShelterItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail);

        Intent intent = getIntent();
        desertionNoExtra = intent.getStringExtra("desertionNo");
        petKindExtra = intent.getStringExtra("petKind");

        dataSet(petKindExtra);

        init();
    }

    private void init(){
        //탑레이아웃
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //기본 타이틀 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //백버튼 생성

        //화면 만들기
        popfile = findViewById(R.id.popfile);
        processState = findViewById(R.id.processState);

        kindCd = findViewById(R.id.kindCd);
        sexCd = findViewById(R.id.sexCd);
        neuterYn = findViewById(R.id.neuterYn);
        colorCd = findViewById(R.id.colorCd);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);

        notice_no = findViewById(R.id.notice_no);
        notice_sdt = findViewById(R.id.notice_sdt);
        notice_edt = findViewById(R.id.notice_edt);
        happen_place = findViewById(R.id.happen_place);

        care_nm = findViewById(R.id.care_nm);
        care_tel = findViewById(R.id.care_tel);
        care_addr = findViewById(R.id.care_addr);
        charge_nm = findViewById(R.id.charge_nm);
        officetel = findViewById(R.id.officetel);

        special_mark = findViewById(R.id.special_mark); //특징

        toggle_like = findViewById(R.id.toggle_like);

        toggle_like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String desertionNoSet = desertionNoExtra.toString();
                int userNo = Common.getUserNum(ShelterDetail.this);
                if(b){//눌렸으면
                    insertLike(desertionNoSet,userNo);
                } else { //체크 해제
                    deleteLike(desertionNoSet,userNo);
                }
            }
        });

        findViewById(R.id.btn_tel).setOnClickListener(view -> clickTel());

    }


    SQLiteDatabase sqLiteDatabase;
    private void insertLike(String desertionNo, int userNo) {
        sqLiteDatabase = this.openOrCreateDatabase("hugpet_db",this.MODE_PRIVATE,null);

        sqLiteDatabase.execSQL("INSERT INTO my_like(desertionNo, user_num, petKind) VALUES ('"+desertionNo+"' , '"+userNo+"','"+petKindExtra+"')");
        Log.i("TAG","누름" + desertionNo);
        Common.makeToast(this, "관심동물 추가");
    }

    private void deleteLike(String desertionNo, int userNo) {
        sqLiteDatabase = this.openOrCreateDatabase("hugpet_db",this.MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("DELETE from my_like where desertionNo=? AND user_num=?",new String[]{desertionNo, userNo+""});
        Common.makeToast(this, "관심동물 삭제");
    }

    private void clickTel() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);

        intent.setData(Uri.parse("tel:"+finalShelterItem.officetel));

        startActivity(intent);
    }


    private void dataSet(String upkind) {

        new Thread(){

            @Override
            public void run() {

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); //오늘
                String today = sdf.format(date);

                Date dateAgo = Common.addMonth(date, -2);
                String mothAgo = sdf.format(dateAgo);

                //주소
                String address = Common.SERVER_URI
                        + "?bgnde="+ mothAgo
                        + "&endde=" + today
                        + "&pageNo=1"
                        + "&numOfRows=100"
                        + "&upkind=" + upkind
                        + "&serviceKey=" + Common.APIKEY;


                //연결
                try {
                    URL url = new URL(address);

                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(isr);

                    int evenType = xpp.getEventType();

                    //참조변수
                    ShelterItem shelterItem = null;

                    String desertionNo = "";

                    while (evenType != XmlPullParser.END_DOCUMENT) {

                        switch (evenType) {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:

                                String tagName = xpp.getName();

                                if(tagName.equals("item")){
                                    shelterItem = new ShelterItem();
                                } else if(tagName.equals("desertionNo")){
                                    xpp.next();
                                    shelterItem.desertionNo = xpp.getText();
                                    desertionNo = xpp.getText();
                                } else if(tagName.equals("happenPlace")){
                                    xpp.next();
                                    shelterItem.happenPlace = xpp.getText();
                                } else if (tagName.equals("kindCd")) {
                                    xpp.next();
                                    shelterItem.kindCd = xpp.getText();
                                } else if (tagName.equals("colorCd")) {
                                    xpp.next();
                                    shelterItem.colorCd = xpp.getText();
                                } else if (tagName.equals("age")) {
                                    xpp.next();
                                    shelterItem.age = xpp.getText();
                                } else if (tagName.equals("weight")) {
                                    xpp.next();
                                    shelterItem.weight = xpp.getText();
                                } else if (tagName.equals("noticeNo")) {
                                    xpp.next();
                                    shelterItem.noticeNo = xpp.getText();
                                } else if (tagName.equals("noticeSdt")) {
                                    xpp.next();
                                    shelterItem.noticeSdt = xpp.getText();
                                } else if (tagName.equals("noticeEdt")) {
                                    xpp.next();
                                    shelterItem.noticeEdt = xpp.getText();
                                } else if (tagName.equals("popfile")) {
                                    xpp.next();
//                                    Bitmap bitmap = Common.imgToBitmap(xpp.getText());
//                                    shelterItem.popfile = bitmap;

                                    shelterItem.popfile = xpp.getText();
                                } else if (tagName.equals("processState")) {
                                    xpp.next();
                                    shelterItem.processState = xpp.getText();
                                } else if (tagName.equals("sexCd")) {
                                    xpp.next();
                                    shelterItem.sexCd = xpp.getText();
                                } else if (tagName.equals("neuterYn")) {
                                    xpp.next();
                                    shelterItem.neuterYn = xpp.getText();
                                } else if (tagName.equals("specialMark")) {
                                    xpp.next();
                                    shelterItem.specialMark = xpp.getText();
                                } else if (tagName.equals("careNm")) {
                                    xpp.next();
                                    shelterItem.careNm = xpp.getText();
                                } else if (tagName.equals("careTel")) {
                                    xpp.next();
                                    shelterItem.careTel = xpp.getText();
                                } else if (tagName.equals("careAddr")) {
                                    xpp.next();
                                    shelterItem.careAddr = xpp.getText();
                                } else if (tagName.equals("chargeNm")) {
                                    xpp.next();
                                    shelterItem.chargeNm = xpp.getText();
                                } else if (tagName.equals("officetel")) {
                                    xpp.next();
                                    shelterItem.officetel = xpp.getText();
                                }
                                break;
                            case XmlPullParser.TEXT:
                                break;
                            case XmlPullParser.END_TAG:

                                String eadTagName = xpp.getName();
                                if (eadTagName.equals("item")) {
                                    if (desertionNoExtra.equals(desertionNo)) {
                                        finalShelterItem = shelterItem;

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String imgUri = finalShelterItem.popfile.toString();
                                                Glide.with(ShelterDetail.this).load(Uri.parse(imgUri)).into(popfile);

                                                int processStateImg = 0;
                                                String processStateS =  finalShelterItem.processState;
                                                if(processStateS.equals("보호중")) processStateImg = R.drawable.status_ok;
                                                else processStateImg = R.drawable.status_end;
                                                processState.setImageResource(processStateImg);

                                                kindCd.setText(finalShelterItem.kindCd);
                                                colorCd.setText(finalShelterItem.colorCd);

                                                String neuterYnResult = "";
                                                if(finalShelterItem.neuterYn.equals("Y")) neuterYnResult = "o";
                                                else if (finalShelterItem.neuterYn.equals("N"))  neuterYnResult = "x";
                                                else  neuterYnResult = "확인불가";
                                                neuterYn.setText(neuterYnResult);
                                                age.setText(finalShelterItem.age);
                                                weight.setText(finalShelterItem.weight);
                                                notice_no.setText(finalShelterItem.noticeNo);

                                                notice_sdt.setText(Common.dateFormat(finalShelterItem.noticeSdt));
                                                notice_edt.setText(Common.dateFormat(finalShelterItem.noticeEdt));

                                                happen_place.setText(finalShelterItem.happenPlace);
                                                care_nm.setText(finalShelterItem.careNm);
                                                care_tel.setText(finalShelterItem.careTel);
                                                care_addr.setText(finalShelterItem.careAddr);
                                                charge_nm.setText(finalShelterItem.chargeNm);
                                                officetel.setText(finalShelterItem.officetel);
                                                special_mark.setText(finalShelterItem.specialMark);


                                            }
                                        });

                                    }
                                }
                                break;
                        }
                        evenType = xpp.next();
                    }

                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (XmlPullParserException e) {
                    throw new RuntimeException(e);
                }

            }
        }.start();

    }


    //백버튼
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
