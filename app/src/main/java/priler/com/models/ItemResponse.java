package priler.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemResponse {

    @SerializedName("results")
    @Expose
    private List<Item> results;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Item> getResults() {
        return results;
    }

    public String getMessage() {
        return message;
    }

}
