package com.jscompany.tp15hugpet2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jscompany.tp15hugpet2.Model.BoardService;
import com.jscompany.tp15hugpet2.Model.RetrofitBaseUrl;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YouMeDetail extends AppCompatActivity {

    TextView boardnum, title, content, nicName, viewCnt, date;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_me_detail);

        init();

        data();
    }

    void init(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //기본 타이틀 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //백버튼 생성

        boardnum = findViewById(R.id.boardnum);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        nicName = findViewById(R.id.nic);
        viewCnt = findViewById(R.id.view_cnt);
        date = findViewById(R.id.date);

        img = findViewById(R.id.img);
    }

    private void data() {
        
        //디테일 페이지
        Intent intent = getIntent();
        int bNo = intent.getIntExtra("boardN",0);

        //디테일 조회
        RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(BoardService.class)
                .boardSelect(bNo+"").enqueue(new Callback<List<YouMeItem>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<YouMeItem>> call, Response<List<YouMeItem>> response) {

                List<YouMeItem> items = response.body();

                boardnum.setText(items.get(0).bNo + "");
                title.setText(items.get(0).title);
                content.setText(items.get(0).content);
                nicName.setText(items.get(0).nicName);
                viewCnt.setText(items.get(0).viewCnt);
                date.setText(items.get(0).date);

            }

            @Override
            public void onFailure(Call<List<YouMeItem>> call, Throwable t) {
                Toast.makeText(YouMeDetail.this, "네트워크 문제", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

//    void data(){
//
//        Intent intent = getIntent();
//        int bNum = intent.getIntExtra("boardN",0);
//
//        SQLiteDatabase board_db = openOrCreateDatabase("hugpet_db",MODE_PRIVATE,null);
//        Cursor cursor
//                = board_db.rawQuery("SELECT B.*, (SELECT nicname FROM member WHERE num=B.userNum) AS nicname FROM board B WHERE B.boardNum=?",new String[]{bNum+""});
//
//        if(cursor == null) return;
//
//        int rowCnt = cursor.getCount();
//
//        cursor.moveToFirst();
//
//        int boardNumDB=0, userNumDB=0;
//        String titleDB="",contentDB="",view_cntDB="";
//        String dateDB ="", nicnameDB="";
//        for(int i=0; i< rowCnt; i++) {
//            boardNumDB = cursor.getInt(0);
//            titleDB = cursor.getString(1);
//            contentDB = cursor.getString(2);
//            view_cntDB = cursor.getString(3);
//            userNumDB = cursor.getInt(4);
//            dateDB = cursor.getString(5);
//            nicnameDB = cursor.getString(6);
//
//            cursor.moveToNext();
//        }
//
//        boardnum.setText(boardNumDB + "");
//        title.setText(titleDB);
//        content.setText(contentDB);
//        nicName.setText(nicnameDB);
//        viewCnt.setText(view_cntDB);
//        date.setText(dateDB);
//
//    }

    //백버튼
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
