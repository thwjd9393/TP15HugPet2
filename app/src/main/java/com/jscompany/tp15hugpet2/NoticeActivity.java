package com.jscompany.tp15hugpet2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jscompany.tp15hugpet2.Model.NoticeService;
import com.jscompany.tp15hugpet2.Model.RetrofitBaseUrl;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeActivity extends AppCompatActivity {

    FloatingActionButton btn;
    ExpandableListView expend_view;
    ArrayList<NoticeItem> items = new ArrayList<>();
    NoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);


        init();
        seletData();
    }

    void init(){

        //탭
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //기본 타이틀 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //백버튼 생성

        btn = findViewById(R.id.btn_insert);
        //공지사항 마스터만 보이게
        if(Common.getUserNum(this)==1){
            btn.setVisibility(View.VISIBLE);

            btn.setOnClickListener(v->{
                Intent intent = new Intent(this, NoticeWrite.class);
                startActivity(intent);
                intent=null;
            });
        }

        expend_view = findViewById(R.id.expend_view);


    }

    private void seletData() {

        RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(NoticeService.class).NoticeSelectAll().enqueue(new Callback<List<NoticeItem>>() {
            @Override
            public void onResponse(Call<List<NoticeItem>> call, Response<List<NoticeItem>> response) {
                List<NoticeItem> resultItems = response.body();

                for (int i=0; i < resultItems.size(); i++) {

                    items.add(new NoticeItem(resultItems.get(i).title,resultItems.get(i).content));

                }

                adapter = new NoticeAdapter(NoticeActivity.this,R.layout.item_expend_parent,R.layout.item_expend_child, items);
                expend_view.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<NoticeItem>> call, Throwable t) {
                Common.makeToast(NoticeActivity.this,"네트워크 문제 :"+t.toString());
                Log.i("TAG",t.toString());
            }
        });

//        RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(NoticeService.class)
//                .NoticeSelectAll().enqueue(new Callback<List<NoticeItem>>() {
//            @Override
//            public void onResponse(Call<List<NoticeItem>> call, Response<List<NoticeItem>> response) {
//                List<NoticeItem> resultItems = response.body();
//
//                for (int i=0; i < resultItems.size(); i++) {
//
//                    items.add(new NoticeItem(resultItems.get(i).title,resultItems.get(i).content));
//
//                }
//
//                adapter = new NoticeAdapter(NoticeActivity.this,R.layout.item_expend_parent,R.layout.item_expend_child,items);
//                expend_view.setAdapter(adapter);
//            }
//
//            @Override
//            public void onFailure(Call<List<NoticeItem>> call, Throwable t) {
//                Common.makeToast(NoticeActivity.this,"네트워크 문제 :"+t.toString());
//                Log.i("TAG",t.toString());
//            }
//        });


    }

//    SQLiteDatabase sqLiteDatabase;

//    void seletData() {
//        sqLiteDatabase = openOrCreateDatabase("hugpet_db", MODE_PRIVATE, null);
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * from notice ORDER BY niti_num DESC",null);
//
//        if(cursor == null) return;
//
//        int rowNum = cursor.getCount();
//
//        cursor.moveToFirst();
//
//        for(int i=0;i<rowNum;i++){
//            int numDb = cursor.getInt(0);
//            String titleDb = cursor.getString(1);
//            String contentDb = cursor.getString(2);
//            int userNumDb = cursor.getInt(3);
//            String date = cursor.getString(4);
//
//            items.add(new NoticeItem(titleDb,contentDb));
//
//            cursor.moveToNext();
//        }
//
//    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
