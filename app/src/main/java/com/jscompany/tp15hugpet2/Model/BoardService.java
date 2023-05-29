package com.jscompany.tp15hugpet2.Model;

import com.jscompany.tp15hugpet2.YouMeItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BoardService {

    //글쓰기
    @GET("Hugpet/boardInsert.php")
    Call<String> boardInsert(@Query("title") String titleGetText, @Query("content") String contentGetText,@Query("userNo") int userNo);
    
    //전체 글
    @GET("Hugpet/boardSelectAll.php")
    Call<List<YouMeItem>> boardSelectAll();
    
    //선택 글
    @GET("Hugpet/boardSelect.php")
    Call<List<YouMeItem>> boardSelect(@Query("bNo") String bNo);

    //좋아요 카운트
    @GET("Hugpet/boardViewCnt.php")
    Call<String> boardViewCnt(@Query("bNo") String bNo);

    //베스트 카운트
    @GET("Hugpet/boardBest.php")
    Call<List<YouMeItem>> boardBest();

}
