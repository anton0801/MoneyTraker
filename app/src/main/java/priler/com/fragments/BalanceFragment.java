package priler.com.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import priler.com.DiagramView;
import priler.com.R;
import priler.com.activities.MainActivity;
import priler.com.api.Api;
import priler.com.api.App;
import priler.com.models.BalanceResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceFragment extends Fragment {

    private static final String TAG = "BalanceFragment";

    private TextView total;
    private TextView expense;
    private TextView income;

    private DiagramView diagramView;

    private ProgressBar progressBar;

    private Api api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        api = ((App) getActivity().getApplication()).getApi();

        total = view.findViewById(R.id.income_total_value);
        expense = view.findViewById(R.id.expense_value);
        income = view.findViewById(R.id.income_value);
        diagramView = view.findViewById(R.id.diagram);
        progressBar = view.findViewById(R.id.balance_progress_bar);
        progressBar.setVisibility(View.GONE);

        updateData();
    }

//    @SuppressWarnings("deprecation")
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && isResumed()) {
//            updateData();
//        }
//    }

    private void updateData() {
        progressBar.setVisibility(View.VISIBLE);
        api.getBalance(Integer.parseInt(MainActivity.userId))
                .enqueue(new Callback<BalanceResult>() {
                    @Override
                    public void onResponse(Call<BalanceResult> call, Response<BalanceResult> response) {
                        if (response.isSuccessful()) {
                            BalanceResult balanceResult = response.body();
                            if (balanceResult != null) {
                                expense.setText(getString(R.string.price, String.valueOf(balanceResult.totalExpense)));
                                income.setText(getString(R.string.price, String.valueOf(balanceResult.totalIncome)));
                                total.setText(getString(R.string.price, String.valueOf(balanceResult.totalIncome - balanceResult.totalExpense)));

                                diagramView.update(balanceResult.totalIncome, balanceResult.totalExpense);
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<BalanceResult> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), getString(R.string.some_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
