package priler.com.api;

import priler.com.models.AuthResponse;
import priler.com.models.BalanceResult;
import priler.com.models.ItemBody;
import priler.com.models.ItemResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("auth")
    Call<AuthResponse> auth(@Query("name") String name, @Query("email") String email,
                            @Query("social_user_id") String token);

    @GET("get/{id}")
    Call<ItemResponse> getItems(@Path("id") int id, @Query("type") String type,
                                @Query("page") int page);

    @POST("add")
    Call<ItemResponse> addItem(@Body ItemBody itemBody);

    @DELETE("delete/{id}")
    Call<ItemResponse> delete(@Path("id") int id);

    @GET("balance/{id}")
    Call<BalanceResult> getBalance(@Path("id") int id);

}
