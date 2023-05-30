package com.jscompany.tp15hugpet2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jscompany.tp15hugpet2.Model.BoardService;
import com.jscompany.tp15hugpet2.Model.RetrofitBaseUrl;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YouMeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_you_me,container,false);
    }

    ArrayList<YouMeItem> items = new ArrayList<>();
    RecyclerView review_rycview;
    YouMeAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //items.add(new YouMeItem(100,"안녕","text","user","6",1,"2023-03-05"));
        review_rycview = view.findViewById(R.id.review_rycview);

        // 플로팅 버튼
        view.findViewById(R.id.fabtn).setOnClickListener(v -> goToWriter());

    }


    //글쓰기 화면
    private void goToWriter() {

        Intent intent = new Intent(getActivity(), YouMeWrite.class);
        startActivity(intent);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(BoardService.class).boardSelectAll().enqueue(new Callback<List<YouMeItem>>() {
            @Override
            public void onResponse(Call<List<YouMeItem>> call, Response<List<YouMeItem>> response) {

                List<YouMeItem> youMeItems = response.body();

                Log.i("TAG", "youMeItems :"+ youMeItems.size());

                for(int i=0; i < youMeItems.size(); i++) {
                    items.add(new YouMeItem(youMeItems.get(i).bNo,youMeItems.get(i).title,youMeItems.get(i).content,youMeItems.get(i).userNic,youMeItems.get(i).viewCnt,youMeItems.get(i).userNo,youMeItems.get(i).date));
                }

                adapter = new YouMeAdapter(requireActivity(),items);
                review_rycview.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<YouMeItem>> call, Throwable t) {

            }
        });
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        board_db = getActivity().openOrCreateDatabase("hugpet_db",getActivity().MODE_PRIVATE,null);
//        //데이터 불러오기
//        //Cursor cursor = board_db.rawQuery("SELECT * FROM board", null);
//        Cursor cursor = board_db.rawQuery("SELECT B.boardNum AS boardNum, B.title AS title,"
//                + "B.content AS content,B.view_cnt AS view_cnt,"
//                + "B.userNum AS userNum,B.date AS date,(SELECT nicname FROM member WHERE num=B.userNum) AS nicname FROM board B ORDER BY B.boardNum DESC", null);
//
//
//        if(cursor == null) return;
//
//        int rowCnt = cursor.getCount();
//
//        cursor.moveToFirst();
//
//        for(int i=0; i< rowCnt; i++){
//            int boardNum = cursor.getInt(0);
//            String title = cursor.getString(1);
//            String content = cursor.getString(2);
//            String view_cnt = cursor.getString(3);
//            int userNum = cursor.getInt(4);
//            String date = cursor.getString(5);
//            String nicname = cursor.getString(6);
//            //String img = cursor.get(7);
//
//            items.add(new YouMeItem(boardNum,title,content,nicname,view_cnt,userNum,date));
//
//            cursor.moveToNext();
//        }
//
//    }
}
