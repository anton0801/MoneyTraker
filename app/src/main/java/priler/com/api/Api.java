package priler.com.api;

import priler.com.models.BalanceResult;
import priler.com.models.ItemResponse;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("get/{id}")
    Call<ItemResponse> getItems(@Path("id") int id, @Query("type") String type,
                                @Query("page") int page);

    @GET("add")
    Call<ItemResponse> addItem(
            @Query("user_id") int id,
            @Query("name") String name,
            @Query("price") String price,
            @Query("comment") String comment,
            @Query("type") String type
    );

    @FormUrlEncoded
    @POST("delete")
    Call<ItemResponse> delete(@Query("id") int id);

    @GET("balance/{id}")
    Call<BalanceResult> getBalance(@Path("id") int id);

}
