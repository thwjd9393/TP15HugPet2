package com.jscompany.tp15hugpet2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShelterFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shelter,container,false);

        return view;
    }

    ImageView gridBtn;
    Boolean imgFlag=true;

    Spinner spinnerSelect;
    ArrayAdapter arrayAdapterPet;
    RecyclerView recyview;
    ArrayList<ShelterItem> items = new ArrayList<>();
    ShelterAdapter adapter;

    String petKind = Common.UPKIND_DOG;

    //스크롤 탑
    NestedScrollView scroll;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridBtn = view.findViewById(R.id.grid_btn);
        gridBtn.setOnClickListener(v-> layoutChg(imgFlag));

        spinnerSelect = view.findViewById(R.id.spinner_select);

        arrayAdapterPet = ArrayAdapter.createFromResource(getActivity(),R.array.pet,android.R.layout.simple_spinner_dropdown_item);
        spinnerSelect.setAdapter(arrayAdapterPet);
        spinnerSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String[] pet = getActivity().getResources().getStringArray(R.array.pet);

                if(pet[i].equals("강아지")) petKind  = Common.UPKIND_DOG;
                else if (pet[i].equals("고양이")) petKind  = Common.UPKIND_CAT;
                else petKind  = Common.UPKIND_ETC;

                data(petKind);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //up 버튼
        scroll = view.findViewById(R.id.nested_scroll);
        view.findViewById(R.id.top_btn).setOnClickListener(v->clickScrollTop());

        //리사이클뷰
        recyview = view.findViewById(R.id.recyview);
        adapter = new ShelterAdapter(getActivity(),items);
        recyview.setAdapter(adapter);

    }

    //맨 위로
    private void clickScrollTop() {
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    //데이터 파싱
    void data(String petKind){

        if(adapter!=null) {
            items.clear();
            adapter.notifyDataSetChanged();
        }

        new Thread() {

            @Override
            public void run() {

                //https://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic?bgnde=20230201&endde=20230305&pageNo=1&numOfRows=30&upkind=422400&serviceKey=UMQ0EyOjBkKxNwpgH4XOuue0lb0Eak0woNiW%2FizAQ%2FwfLanqe3u%2FeLn5%2FYoSwFYIoyNLkIrmtSsu94jCjOgljQ%3D%3D

                //날짜
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); //오늘
                String today = sdf.format(date);

                Date dateAgo = Common.addMonth(date, -3);
                String mothAgo = sdf.format(dateAgo);
                Log.i("TAG",mothAgo);

                String address = Common.SERVER_URI
                        + "?bgnde="+ mothAgo
                        + "&endde=" + today
                        + "&pageNo=" + 1
                        + "&numOfRows="+ 100
                        + "&upkind=" + petKind
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
//                                    String img = xpp.getText();
//                                    Bitmap imgbit = Common.imgToBitmap(img);
//                                    shelterItem.popfile = imgbit;
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
                                    shelterItem.petKind = petKind;
                                    items.add(shelterItem);
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

    //
    private void layoutChg(Boolean flag) {

        if(flag==true){
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            recyview.setLayoutManager(layoutManager);
            gridBtn.setImageResource(R.drawable.squares);

            imgFlag = false;
        } else {
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
            recyview.setLayoutManager(layoutManager);
            gridBtn.setImageResource(R.drawable.list);

            imgFlag = true;
        }

    }

}
