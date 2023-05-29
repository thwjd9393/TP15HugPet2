package com.jscompany.tp15hugpet2;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

    Context context;

    // 닷홈 베이스 Url
    static String dotHomeUrl = "http://mrhisj23.dothome.co.kr/";

    static int getUserNum(Context context){

        SharedPreferences pref = context.getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        int userNn = pref.getInt("userNum",0);

        return  userNn;
    }

    static String getUserEmil(Context context){
        SharedPreferences pref = context.getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        String userEmial = pref.getString("email","-");

        return userEmial;
    }

    static String getUserNic(Context context){
        SharedPreferences pref = context.getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        String userNic = pref.getString("nicName","user");

        return userNic;
    }

    static final int RESULT_YES = 2;
    static final int RESULT_NO = 1;

    static final String SERVER_URI = "https://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic";
    static final String APIKEY = "UMQ0EyOjBkKxNwpgH4XOuue0lb0Eak0woNiW%2FizAQ%2FwfLanqe3u%2FeLn5%2FYoSwFYIoyNLkIrmtSsu94jCjOgljQ%3D%3D";

    //축종코드
    //개 417000
    static final String UPKIND_DOG = "417000"; //디폴트
    //고양이 422400
    static final String UPKIND_CAT = "422400";
    //기타 429900
    static final String UPKIND_ETC = "429900";


    //이미지 파싱 함수
    static Bitmap imgToBitmap(String img) {

        Bitmap imgBitmap = null;

        try {
            URL url = new URL(img);
            URLConnection conn = url.openConnection();
            conn.connect();

            int nSize = conn.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
            bis.close();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return imgBitmap;
    }

    //달 계산
    public static Date addMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public static String dateFormat(String date) {

        String y = date.substring(0,4);
        String m = date.substring(4,6);
        String d = date.substring(6);

        return y + "-" + m+"-"+d;
    }

    //정규식
    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) {
            err = true;
        }
        return err;
    }

    public static boolean isValidPasswd(String passwd) {
        boolean err = false;
        String regex = "^[A-Za-z0-9]{6,12}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(passwd);
        if(m.matches()) {
            err = true;
        }
        return err;
    }

    static void makeToast(Context context, String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    //좋아요
    static void clickLike(Context context2, ArrayList<String> datas) {

        String state =  Environment.getExternalStorageState();

        if (!(state.equals(Environment.MEDIA_MOUNTED))) {
            Common.makeToast(context2,"SD card is not mounted");

            return;
        }


        File[] dirs = context2.getExternalFilesDirs("MyLike");

        File file = new File(dirs[0],"Like.txt");

        try {
            FileWriter fw = new FileWriter(file, true);

            PrintWriter writer = new PrintWriter(fw);

            for(int i=0; i<datas.size();i++){
                writer.println(datas.get(i));
            }

            writer.flush();
            writer.close();

            Common.makeToast(context2,"관심동물에 추가하였습니다");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
