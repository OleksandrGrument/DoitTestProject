package com.grument.doittestproject.retrofit;


import com.grument.doittestproject.dto.GifDTO;
import com.grument.doittestproject.dto.ImageResponseListDTO;
import com.grument.doittestproject.dto.SignInResponseDTO;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitRequestResponseService {


    @FormUrlEncoded
    @POST("login")
    Observable<Response<SignInResponseDTO>> loginUser(@Field("email") String email,
                                                      @Field("password") String password);


    @Multipart
    @POST("create")
    Observable<Response<SignInResponseDTO>> createAndSignInUser(@Part("username") RequestBody userName,
                                                                @Part("email") RequestBody email,
                                                                @Part("password") RequestBody password,
                                                                @Part MultipartBody.Part file);


    @GET("all")
    Observable<Response<ImageResponseListDTO>> loadAllImages(@Header("token") String token);


    @Multipart
    @POST("image")
    Observable<Response<ResponseBody>> uploadImage(@Header("token") String token,
                                                   @Part MultipartBody.Part file,
                                                   @Part("description") RequestBody description,
                                                   @Part("hashtag") RequestBody hashTag,
                                                   @Part("longitude") RequestBody longitude,
                                                   @Part("latitude") RequestBody latitude);

    @GET("gif")
    Observable<Response<GifDTO>> loadGif(@Header("token") String token);


}