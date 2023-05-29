package com.jscompany.tp15hugpet2.Model;

import com.jscompany.tp15hugpet2.NoticeItem;
import com.jscompany.tp15hugpet2.YouMeItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NoticeService {

    //글쓰기
    @GET("Hugpet/noticeInsert.php")
    Call<String> NoticeInsert(@Query("title") String titleGetText, @Query("content") String contentGetText, @Query("userNo") int userNo);

    //전체 글
    @GET("Hugpet/noticeSelectAll.php")
    Call<List<NoticeItem>> NoticeSelectAll();

    //베스트 글
    @GET("Hugpet/noticeBest.php")
    Call<List<NoticeItem>> NoticeBest();


}
