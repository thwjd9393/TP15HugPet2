package com.jscompany.tp15hugpet2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jscompany.tp15hugpet2.Model.NoticeService;
import com.jscompany.tp15hugpet2.Model.RetrofitBaseUrl;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeWrite extends AppCompatActivity {

    EditText title, content;
    TextView textLen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_write);

        init();
    }

    void init(){
        //
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        textLen=findViewById(R.id.text_len);

        focuce(content);
        
        //취소
        findViewById(R.id.btn_cancel).setOnClickListener(view -> clickCancel());

        //등록
        findViewById(R.id.btn_insert).setOnClickListener(view -> clicIinsert());

    }

    void focuce(EditText et) {

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textLen.setText(et.getText().length()+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void clicIinsert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("등록하시겠습니까?");

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String titleGetText = title.getText().toString();
                String contentGetText = content.getText().toString();
                int userNo = Common.getUserNum(NoticeWrite.this);

                if(contentGetText.length() > 300 ) {
                    Common.makeToast(NoticeWrite.this,"300자 이상 등록 불가");
                    return;
                }

//                SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("hugpet_db",MODE_PRIVATE,null);
//                sqLiteDatabase.execSQL("INSERT INTO notice(title,content,user_num) VALUES ('"+titleGetText+"','"+contentGetText+"','"+userNo+"')");

                RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(NoticeService.class).NoticeInsert(titleGetText,contentGetText,userNo).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if(response.body().equals("true")) {
                            Common.makeToast(NoticeWrite.this,"등록 완료");

                            Intent intent = new Intent(NoticeWrite.this, NoticeActivity.class);
                            startActivity(intent);
                        } else {
                            Common.makeToast(NoticeWrite.this,"등록 실패");
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Common.makeToast(NoticeWrite.this,"네트워크 문제");
                    }
                });

            }
        });

        AlertDialog dialog =builder.create();
        dialog.setCancelable(false);
        dialog.show();

    }

//    private void clicIinsert() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setMessage("등록하시겠습니까?");
//
//        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                String titleGetText = title.getText().toString();
//                String contentGetText = content.getText().toString();
//                int userNo = Common.getUserNum(NoticeWrite.this);
//
//                if(contentGetText.length() > 300 ) {
//                    Common.makeToast(NoticeWrite.this,"300자 이상 등록 불가");
//                    return;
//                }
//
//                SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("hugpet_db",MODE_PRIVATE,null);
//                sqLiteDatabase.execSQL("INSERT INTO notice(title,content,user_num) VALUES ('"+titleGetText+"','"+contentGetText+"','"+userNo+"')");
//
//                Common.makeToast(NoticeWrite.this,"등록 완료");
//
//                Intent intent = new Intent(NoticeWrite.this, NoticeActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        AlertDialog dialog =builder.create();
//        dialog.setCancelable(false);
//        dialog.show();
//
//    }


    private void clickCancel() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("글쓰기를 완료하지 않고 나가시겠습니까?");

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });

        AlertDialog dialog =builder.create();
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}