package com.jscompany.tp15hugpet2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.jscompany.tp15hugpet2.Model.BoardService;
import com.jscompany.tp15hugpet2.Model.NoticeService;
import com.jscompany.tp15hugpet2.Model.RetrofitBaseUrl;

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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    //공지사항
    ArrayList<NoticeItem> noticeItems = new ArrayList<>();
    RecyclerView noticeRycview;
    NoticeMainAdapter noticeAdapter;

    //베스트 입양후기
    ArrayList<YouMeItem> youMeItems = new ArrayList<>();
    RecyclerView bestYouMeRcyView;
    MainBestYoyMeAdapter bestYouMeAdapter;

    //가족이되어주세요
    ArrayList<ShelterItem> shelterItems = new ArrayList<>();
    RecyclerView shelterRcyView;
    MainShelterAdapter shelterAdapter;

    //허그펫 소식

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //youMeItems.add(new YouMeItem(1,"안녕","3"));
        //입양 후기
        bestYouMeRcyView = view.findViewById(R.id.best_you_me_rcyview);


        //가족이되어주세요
        shelterRcyView = view.findViewById(R.id.shelter_rycview);
        shelterAdapter = new MainShelterAdapter(getActivity(),shelterItems);
        shelterRcyView.setAdapter(shelterAdapter);

        //공지사항
        noticeRycview = view.findViewById(R.id.notice_rycview);


        //더보기
        view.findViewById(R.id.shelter_more).setOnClickListener(v->moreBtn(1));
        view.findViewById(R.id.review_more).setOnClickListener(v->moreBtn(2));
        view.findViewById(R.id.notice_more).setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), NoticeActivity.class);
            startActivity(intent);
            intent = null;
        });

    }

    //화면 만들어지기 전에 여기부터 들여다 봄
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bestReview();
        shelterData();
        noticeData();
    }

    private void moreBtn(int i) {
        activity.onFragmentChange(i);
    }

    SQLiteDatabase sqLiteDatabase;
    //데이터) 베스트 입양후기
    void bestReview() {

        RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(BoardService.class).boardBest().enqueue(new Callback<List<YouMeItem>>() {
            @Override
            public void onResponse(Call<List<YouMeItem>> call, Response<List<YouMeItem>> response) {
                List<YouMeItem> items = response.body();

                for(int i=0; i< items.size();i++) {
                    youMeItems.add(new YouMeItem(items.get(i).bNo,items.get(i).title,items.get(i).viewCnt));
                }

                bestYouMeAdapter = new MainBestYoyMeAdapter(getActivity(),youMeItems);
                bestYouMeRcyView.setAdapter(bestYouMeAdapter);
            }

            @Override
            public void onFailure(Call<List<YouMeItem>> call, Throwable t) {

            }
        });

    }
//    void bestReview() {
//        sqLiteDatabase = getActivity().openOrCreateDatabase("hugpet_db",getActivity().MODE_PRIVATE,null);
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT boardNum,title,view_cnt FROM board ORDER BY view_cnt DESC LIMIT 3",null);
//        //Cursor cursor = sqLiteDatabase.rawQuery("SELECT boardNum,title,view_cnt FROM board ORDER BY view_cnt",null);
//        //Cursor cursor = sqLiteDatabase.rawQuery("SELECT boardNum,title,view_cnt FROM board",null);
//        if(cursor == null) return;
//        int rowCnt = cursor.getCount();
//
//        cursor.moveToFirst();
//
//        for(int i=0; i< rowCnt;i++) {
//            int boardNum = cursor.getInt(0);
//            String title = cursor.getString(1);
//            String view_cnt = cursor.getString(2);
//
//            youMeItems.add(new YouMeItem(boardNum,title,view_cnt));
//
//            cursor.moveToNext();
//        }
//    }

    //데이터) 가족이되어주세요
    void shelterData() {

        new Thread() {

            @Override
            public void run() {

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); //오늘
                String today = sdf.format(date);
                Log.i("MyTag","today = " + today);
                date.setTime(date.getTime() - (1000*60*60*24));
                String yesterDay = sdf.format(date);
                Log.i("MyTag","yesterDay = " + yesterDay);

                String address = Common.SERVER_URI
                        + "?bgnde="+ yesterDay
                        + "&endde=" + today
                        + "&pageNo=1"
                        + "&numOfRows=6"
                        + "&upkind=" + Common.UPKIND_DOG
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
                                } else if (tagName.equals("popfile")) {
                                    xpp.next();
                                    //String img = xpp.getText();
                                    //Bitmap imgbit = Common.imgToBitmap(img);
                                    shelterItem.popfile = xpp.getText();
                                }
                                break;

                            case XmlPullParser.END_TAG:
                                String eadTagName = xpp.getName();
                                if(eadTagName.equals("item")){
                                    shelterItems.add(shelterItem);
                                }
                                break;

                            case XmlPullParser.TEXT:
                                break;
                        }
                        eventType = xpp.next();
                    }

                    //어답터한테 알려주기
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            shelterAdapter.notifyDataSetChanged();
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

    NoticeItem noticeItem;
    //허그펫 소식
    void noticeData(){

        RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(NoticeService.class).NoticeBest().enqueue(new Callback<List<NoticeItem>>() {
            @Override
            public void onResponse(Call<List<NoticeItem>> call, Response<List<NoticeItem>> response) {

                List<NoticeItem> resultItem = response.body();

                for(int i=0; i < resultItem.size(); i++) {
                    noticeItems.add(new NoticeItem(resultItem.get(i).ntcNo,resultItem.get(i).title,resultItem.get(i).date));
                }

                noticeAdapter = new NoticeMainAdapter(getActivity(),noticeItems);
                noticeRycview.setAdapter(noticeAdapter);

            }

            @Override
            public void onFailure(Call<List<NoticeItem>> call, Throwable t) {

            }
        });


//        sqLiteDatabase = getActivity().openOrCreateDatabase("hugpet_db",getActivity().MODE_PRIVATE,null);
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT niti_num,title,date FROM notice ORDER BY niti_num DESC LIMIT 3",null);
//        if(cursor == null) return;
//        int rowCnt = cursor.getCount();
//
//        cursor.moveToFirst();
//
//        for(int i=0; i< rowCnt;i++) {
//            int niti_num = cursor.getInt(0);
//            String title = cursor.getString(1);
//            String date = cursor.getString(2);
//
//            noticeItems.add(new NoticeItem(niti_num,title,date));
//
//            cursor.moveToNext();
//        }

    }


    //플래그먼트에서 플래그먼트로 전환
    MainActivity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activity = (MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        activity = null;
    }
}
