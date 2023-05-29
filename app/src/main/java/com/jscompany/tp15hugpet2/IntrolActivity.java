package com.jscompany.tp15hugpet2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.WindowManager;

public class IntrolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introl);

        //레이아웃 리밋 - no
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        handler.sendEmptyMessageDelayed(0,3000); //3초뒤에 보내기
    }

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {

            int userNum = Common.getUserNum(IntrolActivity.this);
            if(userNum != 0) startActivity(new Intent(IntrolActivity.this, MainActivity.class));
            else startActivity(new Intent(IntrolActivity.this, LoginActivity.class));

            finish(); //화면 닫아
        }
    };

}
