package com.jscompany.tp15hugpet2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jscompany.tp15hugpet2.Model.RetrofitBaseUrl;
import com.jscompany.tp15hugpet2.Model.UserService;
import com.jscompany.tp15hugpet2.Model.UserVO;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {

    TextInputLayout passwdcoflay, passwdlay,etEmailLay;
    TextInputEditText etEmail, etPasswd, etPasswdConf, etNicname;

    //아이디 닉네임 체크
    Boolean boolEmailChek = false;
    Boolean boolNicChek = false;

    //Common common;

    //member테이블 생성 & 저장
//    SQLiteDatabase memdb;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//        memdb = openOrCreateDatabase("hugpet_db",MODE_PRIVATE,null);
//        memdb.execSQL("CREATE TABLE IF NOT EXISTS member(num INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "email VARCHAR(150) NOT NULL,password VARCHAR(20), nicname VARCHAR(10))");

        //탑
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //기본 타이틀 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //백버튼 생성

        etEmailLay = findViewById(R.id.et_email_lay);
        passwdcoflay = findViewById(R.id.passwdcoflay);
        passwdlay = findViewById(R.id.passwdlay);

        etEmail = findViewById(R.id.et_email);
        etPasswd = findViewById(R.id.et_passwd);
        etPasswdConf = findViewById(R.id.et_passwd_conf);
        etNicname = findViewById(R.id.et_nicname);

        etEmail.addTextChangedListener(watcher);
        etPasswd.addTextChangedListener(watcher);
        etPasswdConf.addTextChangedListener(watcher);

        findViewById(R.id.btn_sign).setOnClickListener(v->clickSinUp());

    }

    //회원가입 
    private void clickSinUp() {
        String userId= etEmail.getText().toString();
        String userPasswd= etPasswd.getText().toString();
        String userNic= etNicname.getText().toString();
        String pswf= etPasswdConf.getText().toString();

        if(userPasswd.length()!=0 && pswf.equals(userPasswd) && userNic.length() != 0){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("회원가입 하겠습니까?");

            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //이메일 & 닉네임 체크
//                    Boolean isCheck =checkDb();
//                    Log.i("TAG","isCheck :"+isCheck);



                    Boolean isIdCheck = checkId();
                    Boolean isNicCheck = checkNic();

                    Log.i("TAG","boolEmailChek1" + boolEmailChek);
                    Log.i("TAG","boolNicChek1" + boolNicChek);


                    if(isIdCheck == false) return;
                    else if(isNicCheck == false) return;

                    //member insert
                    RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(UserService.class)
                            .insertUser(userId,userPasswd,userNic).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                    String result = response.body();

                                    if (result.equals("true")) {
                                        Toast.makeText(SignUpActivity.this, "회원가입 됐습니다", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        intent = null;
                                        finish();
                                    } else Toast.makeText(SignUpActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();

                                    //return result;
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(SignUpActivity.this, "네트워크 문제", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });

            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();


        } else {
            Toast.makeText(this, "입력사항을 체크하세요", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkId() {
        String userId= etEmail.getText().toString();

        //아이디
        RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(UserService.class).checkId(userId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(!(response.body().equals("0"))) {
                    Toast.makeText(SignUpActivity.this, "이미 가입된 이메일 입니다", Toast.LENGTH_SHORT).show();
                } else {
                    boolEmailChek = true;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "네트워크 문제", Toast.LENGTH_SHORT).show();
            }
        });

        return boolEmailChek;

    }//

    private boolean checkNic() {
        String userNic= etNicname.getText().toString();

        //닉네임
        RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(UserService.class).checkNic(userNic).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.body().equals("0")) {
                    Toast.makeText(SignUpActivity.this, "이미 존재하는 닉네임 입니다", Toast.LENGTH_SHORT).show();
                    etNicname.setFocusable(true);
                    Log.i("TAG","boolNicChek" + boolNicChek);
                } else {
                    boolNicChek = true;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "네트워크 문제", Toast.LENGTH_SHORT).show();
            }
        });

        return boolNicChek;
    }//

    //중복체크
//    private boolean checkDb() {
//        Boolean aBoolean = true;
//        String userId= etEmail.getText().toString();
//        String userNic= etNicname.getText().toString();
//
//        final String[] checkId = {""};
//        final String[] checkNic = {""};
//
//        //아이디
//        RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(UserService.class).checkId(userId).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                checkId[0] = response.body().toString();
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(SignUpActivity.this, "네트워크 문제", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //닉네임
//        RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(UserService.class).checkNic(userNic).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                checkNic[0] = response.body().toString();
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(SignUpActivity.this, "네트워크 문제", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//        if(!(checkId[0].equals("0"))){
//            Toast.makeText(this, "이미 가입된 이메일 입니다", Toast.LENGTH_SHORT).show();
//            aBoolean=false;
//            Log.i("TAG","aBoolean ema" + aBoolean);
//            return aBoolean;
//        } else {
//            aBoolean=true;
//            return aBoolean;
//        }
//
//        if (!(checkNic[0].equals("0"))) {
//            Toast.makeText(this, "이미 존재하는 닉네임 입니다", Toast.LENGTH_SHORT).show();
//            etNicname.setFocusable(true);
//            Log.i("TAG","aBoolean nic" + aBoolean);
//            aBoolean=false;
//            return aBoolean;
//        } else {
//            aBoolean=true;
//            return aBoolean;
//        }
//
//        Log.i("TAG","aBoolean" + aBoolean);
//
////        return aBoolean;
//    }//



    //가입
//    private void clickSinUp() {
//        String emi= etEmail.getText().toString();
//        String psw= etPasswd.getText().toString();
//        String nic= etNicname.getText().toString();
//        String pswf= etPasswdConf.getText().toString();
//
//        //모든게 통과
//        if(psw.length()!=0 && pswf.equals(psw) && nic.length() != 0){
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            builder.setMessage("회원가입 하겠습니까?");
//
//            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    //이메일 & 닉네임 체크
//                    boolean isbool = checkDb();
//
//                    if(isbool == false) return;
//
//                    //member insert
//                    memdb.execSQL("INSERT INTO member(email,password,nicname) " +
//                            "VALUES('"+emi+"','"+psw+"','"+nic+"')");
//
//                    Toast.makeText(SignUpActivity.this, "회원가입 됐습니다", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    intent = null;
//                    finish();
//                }
//            });
//
//            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//
//            builder.show();
//
//
//        } else {
//            Toast.makeText(this, "입력사항을 체크하세요", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    //이메일 닉네임 중복 체크
    //Boolean aBoolean;
//    private boolean checkDb() {
//        Boolean aBoolean = true;
//        String emi= etEmail.getText().toString();
//        String nic= etNicname.getText().toString();
//
//        Cursor cursor = memdb.rawQuery("SELECT email,nicname FROM member", null);
//
//        if(cursor == null) {
//            aBoolean=false;
//            return aBoolean;
//        }
//        int rowCnt = cursor.getCount();
//        cursor.moveToFirst();
//
//        for(int i=0; i< rowCnt; i++) {
//            String emick = cursor.getString(0);
//            String nicik = cursor.getString(1);
//
//            if(emi.equals(emick)){
//                Toast.makeText(this, "이미 가입된 이메일 입니다", Toast.LENGTH_SHORT).show();
//                aBoolean=false;
//                return aBoolean;
//            } else if (nic.equals(nicik)) {
//                Toast.makeText(this, "이미 존재하는 닉네임 입니다", Toast.LENGTH_SHORT).show();
//                etNicname.setFocusable(true);
//                aBoolean=false;
//                return aBoolean;
//            }
//
//            cursor.moveToNext();
//        }//for
//
//        return aBoolean;
//    }//

    //텍스트 체인지 리스너
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String emailRex = etEmail.getText().toString();
            String passWdRex = etPasswd.getText().toString();
            String passWdCfRex = etPasswdConf.getText().toString();

            //정규식
            if(s == etEmail.getText()){
                Boolean boolEmail = Common.isValidEmail(emailRex);
                Log.i("MyLog","bool=" +boolEmail);
                if(boolEmail==false) etEmailLay.setHelperText("이메일 형식이 맞질 않습니다");
                else {
                    etEmailLay.setHelperText(null);
                }
            }

            if(s == etPasswd.getText()){
                Boolean bool = Common.isValidPasswd(passWdRex);
                Log.i("MyLog","bool=" +bool);
                if(bool==false) {
                    passwdlay.setHelperText("비밀번호 형식이 맞질 않습니다");
                } else passwdlay.setHelperText(null);
            }

            if(s == etPasswdConf.getText()){
                if(!passWdRex.equals(passWdCfRex)){
                    passwdcoflay.setHelperText("비밀번호가 틀립니다");
                } else {
                    passwdcoflay.setHelperText(null);
                }
            }
        }
    };

    // 백버튼
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //common.alertShow("회원가입을 하지않고 나가겠습니까?", item, LoginActivity.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("회원가입을 하지않고 나가겠습니까?");

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(item.getItemId() == android.R.id.home) {
                    finish();
                }
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create();
        builder.show();

        return super.onOptionsItemSelected(item);
    }
}
