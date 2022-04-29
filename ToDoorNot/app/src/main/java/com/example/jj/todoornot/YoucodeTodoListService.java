package com.example.jj.todoornot;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YoucodeTodoListService {

    @GET("Lab02Get.jsp")
    Call<String> itemListJSONServlet(@Query("ALIAS") String username, @Query("PASSWORD") String password);

    @FormUrlEncoded
    @POST("Lab02Post.jsp")
    Call<String> postToDoItem(@Field("LIST_TITLE") String listTitle, @Field("CONTENT") String description,
                            @Field("COMPLETED_FLAG") String isCompleted, @Field("ALIAS") String userName,
                            @Field("PASSWORD") String password, @Field("CREATED_DATE") String date);
}
