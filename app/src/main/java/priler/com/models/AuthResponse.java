package priler.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("social_user_id")
    @Expose
    public String token;
}
