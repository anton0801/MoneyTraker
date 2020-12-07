package priler.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BalanceResult {

    @SerializedName("total_expense")
    @Expose
    public int totalExpense;
    @SerializedName("total_income")
    @Expose
    public int totalIncome;

}
