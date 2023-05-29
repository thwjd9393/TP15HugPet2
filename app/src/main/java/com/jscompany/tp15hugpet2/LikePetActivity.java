package com.jscompany.tp15hugpet2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LikePetActivity extends AppCompatActivity {

    //리사이클뷰 셋팅
    RecyclerView recyview;
    ArrayList<ShelterItem> items = new ArrayList<>();
    ShelterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_pet);

        data();
        init();

    }

    void init(){
        //탑
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //기본 타이틀 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //백버튼 생성

        //리사이클러뷰
        recyview= findViewById(R.id.recyview);
        adapter= new ShelterAdapter(this,items);
        recyview.setAdapter(adapter);
    }

    //사진 &

    void data(){

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("hugpet_db",MODE_PRIVATE,null);;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT like_num,desertionNo,petKind,user_num FROM my_like WHERE user_num=? ORDER BY like_num DESC",new String[]{Common.getUserNum(this)+""});

        if(cursor == null) return;

        int rowCnt = cursor.getCount();

        cursor.moveToFirst();

        ArrayList<String> desertionNoArr = new ArrayList<>();
        ArrayList<String> petKindArr = new ArrayList<>();
        for(int i=0; i< rowCnt; i++) {
            int likeNum = cursor.getInt(0);
            String desertionNo = cursor.getString(1);
            String petKind = cursor.getString(2);
            int userNum = cursor.getInt(3);

            desertionNoArr.add(desertionNo);
            petKindArr.add(petKind);

            Log.i("TAG", "종 = " + petKind);

            cursor.moveToNext();
        }

        recycleSet(desertionNoArr, petKindArr);
        Log.i("TAG",desertionNoArr.size() + "");

    }

    private void recycleSet(ArrayList<String> desertionNoArr, ArrayList<String> petKindArr) {
        new Thread() {

            @Override
            public void run() {

                //https://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic?bgnde=20230201&endde=20230305&pageNo=1&numOfRows=30&upkind=422400&serviceKey=UMQ0EyOjBkKxNwpgH4XOuue0lb0Eak0woNiW%2FizAQ%2FwfLanqe3u%2FeLn5%2FYoSwFYIoyNLkIrmtSsu94jCjOgljQ%3D%3D

                //날짜
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); //오늘
                String today = sdf.format(date);

                Date dateAgo = Common.addMonth(date, -1);
                String mothAgo = sdf.format(dateAgo);

                String address = Common.SERVER_URI
                        + "?bgnde="+ mothAgo
                        + "&endde=" + today
                        + "&pageNo=" + 1
                        + "&numOfRows="+ 150
                        + "&serviceKey=" + Common.APIKEY;

                //api 연결
                try {
                    URL url = new URL(address);

                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(isr);

                    int eventType = xpp.getEventType();

                    ShelterItem shelterItem = null;

                    while (eventType != XmlPullParser.END_DOCUMENT) {

                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:

                                String tagName = xpp.getName();

                                if(tagName.equals("item")){
                                    shelterItem = new ShelterItem();

                                } else if(tagName.equals("desertionNo")){
                                    xpp.next();
                                    shelterItem.desertionNo = xpp.getText();
                                } else if (tagName.equals("kindCd")) {
                                    xpp.next();
                                    shelterItem.kindCd = xpp.getText();
                                } else if (tagName.equals("noticeNo")) {
                                    xpp.next();
                                    shelterItem.noticeNo = xpp.getText();
                                } else if (tagName.equals("popfile")) {
                                    xpp.next();
                                    shelterItem.popfile = xpp.getText();
                                } else if (tagName.equals("processState")) {
                                    xpp.next();
                                    shelterItem.processState = xpp.getText();
                                } else if (tagName.equals("sexCd")) {
                                    xpp.next();
                                    shelterItem.sexCd = xpp.getText();
                                }
                                break;

                            case XmlPullParser.END_TAG:
                                String eadTagName = xpp.getName();
                                if(eadTagName.equals("item")){
                                    for(int i=0; i<desertionNoArr.size(); i++){
                                        if(shelterItem.desertionNo.equals(desertionNoArr.get(i))) {
                                            shelterItem.petKind = petKindArr.get(i);
                                            items.add(shelterItem);
                                        }
                                    }
                                }
                                break;

                            case XmlPullParser.TEXT:
                                break;
                        }
                        eventType = xpp.next();
                    }

                    //어답터한테 알려주기
                    LikePetActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

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




    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}