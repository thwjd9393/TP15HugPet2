package com.jscompany.tp15hugpet2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.jscompany.tp15hugpet2.Model.RetrofitBaseUrl;
import com.jscompany.tp15hugpet2.Model.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordChgActivity extends AppCompatActivity {

    EditText curPw, newPw,newPwCof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_chg);

        init();

    }

    void init(){
        //탑
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //기본 타이틀 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //백버튼 생성

        curPw = findViewById(R.id.cur_pw);
        newPw = findViewById(R.id.new_pw);
        newPwCof = findViewById(R.id.new_pw_cof);

        findViewById(R.id.pw_chg_btn).setOnClickListener(v-> clickPawChg());
    }

    private void clickPawChg() {

        String cpassWdRex = curPw.getText().toString();
        String npassWdRex = newPw.getText().toString();
        String etPasswdConf = newPwCof.getText().toString();
//        String pwdb = dbPawd();
//        Toast.makeText(this, "pwdb="+pwdb, Toast.LENGTH_SHORT).show();

        if(curPw.getText().length() <=0 || newPw.getText().length() <=0 || newPwCof.getText().length() <=0) {
            Common.makeToast(this, "항목을 입력하지 않았습니다");
            return;
        } else if(!Common.isValidPasswd(npassWdRex)){
            Common.makeToast(this,"비밀번호가 형식에 맞지 않습니다");
            return;
        } else if(!npassWdRex.equals(etPasswdConf)){
            Common.makeToast(this,"변경하시려는 비밀번호와 같지않습니다");
            return;
        } else {
            //비밀번호 업데이트
            updatePasWd(cpassWdRex, npassWdRex);
        }

    }

    //비밀번호 변경
    private void updatePasWd(String currentPasswd, String userPasswd) {

        //유저번호
        int userNo = Common.getUserNum(this);

        RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(UserService.class).updatePassWd(userNo,currentPasswd,userPasswd).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.i("TAG",response.body());
                if(response.body().equals("true")) {

                    curPw.setText("");
                    newPw.setText("");
                    newPwCof.setText("");

                    Common.makeToast(PasswordChgActivity.this,"비밀번호 변경 완료");
                }else if(response.body().equals("fail")){
                    Common.makeToast(PasswordChgActivity.this,"기존 비밀번호가 틀립니다");
                } else {
                    Common.makeToast(PasswordChgActivity.this,"비밀번호 변경 실패");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(PasswordChgActivity.this, "네트워크 문제", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //디비에 저장된 비밀번호 얻어오기
//    SQLiteDatabase sqLiteDatabase;
//    String passwdDb="";
//    private String dbPawd(){
//
//        sqLiteDatabase = openOrCreateDatabase("hugpet_db",MODE_PRIVATE,null);
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT password FROM member WHERE num=?",new String[]{Common.getUserNum(this)+""});
//
//        cursor.moveToFirst();
//        String passwdDb = cursor.getString(0);
//
//        return passwdDb;
//    }
//
//    private void updatePasWd() {
//        String npasswd = newPw.getText().toString();
//
//        sqLiteDatabase.execSQL("UPDATE member SET password = '" + npasswd +"' WHERE num=?",new String[]{Common.getUserNum(this)+""});
//
//        curPw.setText("");
//        newPw.setText("");
//        newPwCof.setText("");
//
//        Common.makeToast(this,"비밀번호 변경 완료");
//    }

    // 백버튼
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("비밀번호를 변경하지않고 나가겠습니까?");

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(item.getItemId() == android.R.id.home) {
                    onBackPressed();
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