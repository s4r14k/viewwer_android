package com.priscilla.viewwer.api;

import com.priscilla.viewwer.model.BaseResponse;
import com.priscilla.viewwer.model.ChangeSettingsRequest;
import com.priscilla.viewwer.model.Employe;
import com.priscilla.viewwer.model.Favorites;
import com.priscilla.viewwer.model.Likes;
import com.priscilla.viewwer.model.RegisterUser;
import com.priscilla.viewwer.model.SearchRequest;
import com.priscilla.viewwer.model.StartTaskRequest;
import com.priscilla.viewwer.model.Tour;
import com.priscilla.viewwer.model.TourBuildRequest;
import com.priscilla.viewwer.model.TourSettings;
import com.priscilla.viewwer.model.UpvoteModel;
import com.priscilla.viewwer.model.User;
import com.priscilla.viewwer.model.responseEmploye;
import com.priscilla.viewwer.model.responseFavorite;
import com.priscilla.viewwer.model.responseProfil;
import com.priscilla.viewwer.model.responseProfilEdit;
import com.priscilla.viewwer.model.responseSearch;
import com.priscilla.viewwer.model.responseTour;
import com.priscilla.viewwer.model.responseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {


    @FormUrlEncoded
    @POST("/api/user/auth")
    Call<responseUser> loginUser(@Field("email") String email,
                                 @Field("password") String password
    );

    @GET("/api/user/register-and-auth-with-access-token")
    Call<responseUser>ConnectToFacebook(@Query("accessToken") String accessToken,
                                        @Query("loginProvider") String loginProvider
    );

    @GET("/api/user/register-with-google")
    Call<responseUser>ConnectToGoogle(@Query("email") String email,
                                      @Query("id") String id,
                                      @Query("name") String name,
                                      @Query("loginProvider") String loginProvider
    );

    @FormUrlEncoded
    @POST("/api/user/signup")
    Call<BaseResponse> registerUser(@Nullable @Field("companyName") String companyName,
                                    @Field("activity") String activity,
                                    @Field("firstName") String firstName,
                                    @Field("lastName") String lastName,
                                    @Field("email") String email,
                                    @Nullable @Field("phone") String phone,
                                    @Field("password") String password,
                                    @Field("isPro") boolean IsPro,
                                    @Nullable @Field("partenerCode") String partenerCode
    );

    @FormUrlEncoded
    @POST("/api/user/signup")
    Call<BaseResponse> registerUserParticular(@Field("email") String email,
                                              @Field("password") String password,
                                              @Field("firstName") String firstName,
                                              @Field("lastName") String lastName,
                                              @Nullable @Field("phone") String phone,
                                              @Field("isPro") boolean IsPro,
                                              @Nullable @Field("partenerCode") String partnerCode

    );

    @GET("/api/tours/list")
    Call<ArrayList<responseSearch>> GetMyTours();


    @POST("/api/search")
    Call<ArrayList<responseSearch>> GetMyVisites();

    @GET("/api/user/profile")
    Call<responseProfil> GetMyProfil();

    @FormUrlEncoded
    @POST("/api/user/profile")
    Call<responseProfilEdit> UpdateProfil(@Field("firstName") String firsName,
                                          @Field("lastName") String lastName,
                                          @Nullable @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("/api/user/change-password")
    Call<responseProfilEdit> ChangePassword(@Field("oldPassword") String oldPassword,
                                            @Field("newPassword") String newPassword
    );

    @FormUrlEncoded
    @POST("/api/favorites")
    Call<responseFavorite> CreateFavorite(@Field("entityType") String entityType,
                                          @Field("entityId") String entityId
    );

    @GET("/api/favorites/list")
    Call<ArrayList<Favorites>> GetListFavorites();


    @POST("/api/favorites/{favoriteApi}/delete")
    Call<responseFavorite> DeleteFavorite(@Path("favoriteApi") String favoriteApiId
    );

    @GET("/api/likes/list")
    Call<ArrayList<UpvoteModel>> GetListLikes();

    @FormUrlEncoded
    @POST("/api/likes")
    Call<BaseResponse> CreateLike(
            @Field("entityId") String entityId
    );

    @POST("/api/likes/{likeApiId}/delete")
    Call<BaseResponse> Dislike(@Path("likeApiId") String likeApiId
    );

    @POST("/api/rating")
    Call<BaseResponse> LikeTour(@Body UpvoteModel params
    );

    @GET("/api/tours/{tourApi}")
    Call<responseSearch> GetTour(@Path("tourApi") String tourApi);

    @FormUrlEncoded
    @POST("/api/tours/{tourApi}")
    Call<Tour> UpdateTour(@Path("tourApi") String tourApi,
                          @Field("address") String address,
                          @Field("saleType") String saleType,
                          @Field("house") Boolean house,
                          @Field("area") Double area,
                          @Field("energyClass") String energyClass,
                          @Field("gasClass") String gasClass,
                          @Field("description") String description,
                          @Field("price") Double price,
                          @Field("type") String type
    );

    @FormUrlEncoded
    @POST("/api/tours/{tourApiId}")
    Call<responseProfilEdit> PublishedTour(@Path("tourApiId") String tourApiId,
                                           @Field("isPublished") boolean isPublished
    );

    @POST("/api/tours/{tourApiId}/delete")
    Call<responseProfilEdit> DeleteTour(@Path("tourApiId") String favoriteApiId
    );

    @FormUrlEncoded
    @POST("/api/search?locale=fr")
    Call<ArrayList<responseSearch>> SearchTours(@Field("saleType") String saleType
    );

    @POST("/api/search?locale=fr")
    Call<ArrayList<responseSearch>> SearchTours2(@Body SearchRequest params
    );



    @POST("/api/task/{taskId}/start")
    Call<BaseResponse> StartBuildingTour(@Path("taskId") String taskId,
                                         @Body TourBuildRequest params
    );


    @POST("/api/task/{taskId}/start")
    Call<BaseResponse> UploadHotspots(@Path("taskId") String taskId,
                                      @Body ChangeSettingsRequest params
    );


    @POST("/api/task/{taskId}/start")
    Call<BaseResponse> SaveTour(@Path("taskId") String taskId,
                                @Body TourSettings params
    );

    @POST("/api/task")
    Call<BaseResponse> CreateSaveTourTask(
            @Body StartTaskRequest params
    );

    @POST("/api/task")
    Call<BaseResponse> StartTourTask(
            @Body StartTaskRequest params
    );

//    @POST(".")
//    Call<BaseResponse> PostMultipart(
//            @Header("apikey") String headers,
//            @Body byte[] rawBytes
//           // @Field("name") String address
//    );

    @Multipart
    @POST(".")
    Call<BaseResponse> PostMultipart(
            @Header("apikey") String headers,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part thumb
    );




}
