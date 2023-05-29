package com.jscompany.tp15hugpet2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.jscompany.tp15hugpet2.Model.RetrofitBaseUrl;
import com.jscompany.tp15hugpet2.Model.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Intent intent;

    TextInputEditText etEmail, etPasswd;

    TextView user_no;

//    SQLiteDatabase memdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        memdb = openOrCreateDatabase("hugpet_db",MODE_PRIVATE,null);

        etEmail = findViewById(R.id.et_email);
        etPasswd = findViewById(R.id.et_passwd);
        user_no = findViewById(R.id.user_no);

        //로그인버튼
        findViewById(R.id.btn_login).setOnClickListener(v -> clickLogin());

        //회원가입
        findViewById(R.id.tv_signup).setOnClickListener(v -> clickSingUp());
    }

    private void clickLogin() {
        String userId = etEmail.getText().toString();
        String userPasswd = etPasswd.getText().toString();

        RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(UserService.class)
                .loginUser(userId,userPasswd).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(!(response.body().equals("false"))){

                    Log.i("TAG", response.body());
                    //1,master@naver.com,master

                    String result[] = response.body().split(",");

                    Log.i("TAG",result[0]);
                    Log.i("TAG",result[1]);
                    Log.i("TAG",result[2]);

                    //사용자 정보 저장(쿠키)
                    SharedPreferences pref = getSharedPreferences("UserInfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("userNum",Integer.parseInt(result[0]));
                    editor.putString("email",result[1]);
                    editor.putString("nicName",result[2]);

                    editor.commit();

                    Common.makeToast(LoginActivity.this,"로그인");
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    intent = null;
                    finish();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "네트워크 문제", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void clickLogin() {
//        String emi = etEmail.getText().toString();
//        String pw = etPasswd.getText().toString();
//
//        Cursor cursor = memdb.rawQuery("SELECT num,email,password,nicname FROM member WHERE email=?", new String[]{emi});
//
//        if(cursor == null) return;
//
//        int rowCnt = cursor.getCount();
//
//        if(rowCnt == 0) {
//            Common.makeToast(this,"아이디가 존재하지 않습니다");
//            return;
//        }
//
//        cursor.moveToFirst();
//
//        int num = cursor.getInt(0);
//        String emidb = cursor.getString(1);
//        String pwdb = cursor.getString(2);
//        String nicdb = cursor.getString(3);
//        if(!pw.equals(pwdb)) {
//            Common.makeToast(this,"비밀번호가 맞지 않습니다");
//            return;
//        }
//
//        //사용자 정보 저장(쿠키)
//        SharedPreferences pref = getSharedPreferences("UserInfo",MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("email",emidb);
//        editor.putString("nicName",nicdb);
//        editor.putInt("userNum",num);
//        editor.commit();
//
//        Common.makeToast(this,"로그인");
//        intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        intent = null;
//        finish();
//
//    }

    private void clickSingUp() {
        intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
