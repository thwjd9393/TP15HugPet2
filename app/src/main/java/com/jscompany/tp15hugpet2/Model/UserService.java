package com.jscompany.tp15hugpet2.Model;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

    //아이디 체크
    @GET("Hugpet/selectCheckId.php")
    Call<String> checkId(@Query("userId") String userId);

    //닉네임 체크
    @GET("Hugpet/selectCheckNic.php")
    Call<String> checkNic(@Query("userNic") String userNic);

    //회원가입
    @FormUrlEncoded
    @POST("Hugpet/insertUser.php")
    Call<String> insertUser(@Field("userId") String userId ,@Field("userPasswd") String userPasswd, @Field("userNic") String userNic);

    //로그인
    @FormUrlEncoded
    @POST("Hugpet/selectUser.php")
    Call<String> loginUser(@Field("userId") String userId ,@Field("userPasswd") String userPasswd);

    //비밀번호 변경
    @FormUrlEncoded
    @POST("Hugpet/updateUser.php")
    Call<String> updatePassWd(@Field("userNo") int userNo, @Field("currentPasswd") String currentPasswd, @Field("userPasswd") String userPasswd);

}
