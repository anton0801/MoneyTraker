package priler.com.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import priler.com.R;
import priler.com.activities.MainActivity;
import priler.com.adapters.ItemsAdapter;
import priler.com.api.Api;
import priler.com.api.App;
import priler.com.interfices.ItemsAdapterListener;
import priler.com.models.Item;
import priler.com.models.ItemResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsFragment extends Fragment implements ItemsAdapterListener {

    public static final int ADD_ITEM_REQUEST_CODE = 123;

    private static final String TYPE_KEY = "type";
    private static final String TAG = "ItemsFragment";

    private String type;
    private int page = 1;

    RecyclerView recyclerView;
    ItemsAdapter adapter;

    private SwipeRefreshLayout refresh;

    private Api api;

    public static ItemsFragment createItemsFragment(String type) {
        Fragment fragment = new ItemsFragment();
        Bundle bundle = new Bundle(); // Bundle он служит для того чтобы через него передать данные
        bundle.putString(ItemsFragment.TYPE_KEY, type);
        fragment.setArguments(bundle);
        return (ItemsFragment) fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null)
            type = bundle.getString(TYPE_KEY, Item.TYPE_INCOMES);

        if (type.equals(Item.TYPE_UNKNOWN))
            throw new IllegalArgumentException("Unknown type");

        api = ((App) getActivity().getApplication()).getApi();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ItemsAdapter(getContext());
        adapter.setListener(this);

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        refresh = view.findViewById(R.id.refresh);
        refresh.setColorSchemeColors(Color.BLUE, Color.CYAN, Color.GREEN);
        refresh.setOnRefreshListener(() -> {
            loadItems();
            refresh.setRefreshing(false);
        });

        loadItems();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 4) {
                    page++;
                    loadItems();
                }
            });
        }
    }

    public void loadItems() {
        refresh.setRefreshing(true);
        api.getItems(Integer.parseInt(MainActivity.userId), type, page).enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        refresh.setRefreshing(false);
                        adapter.setData(response.body().getResults());
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemResponse> call, Throwable t) {
                refresh.setRefreshing(false);
                Toast.makeText(getContext(), getString(R.string.some_went_wrong_internet), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* start ActionMode */

    private ActionMode actionMode = null;

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            actionMode = mode;
            MenuInflater inflater = new MenuInflater(getContext());
            inflater.inflate(R.menu.items_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.remove:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                            .setTitle("Удаление")
                            .setMessage("Вы увереный что хотите удалить?")
                            .setPositiveButton("Да", (dialog, v) -> {
                                refresh.setRefreshing(true);
                                ArrayList<Item> selections = adapter.getSelections();
                                boolean isSuccess = false;
                                for (Item selection : selections) {
                                    api.delete(selection.id).enqueue(new Callback<ItemResponse>() {
                                        @Override
                                        public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                                            if (response.isSuccessful() && response.code() != 404) {
                                                ItemResponse itemResponse = response.body();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ItemResponse> call, Throwable t) {

                                        }
                                    });
                                }
                                isSuccess = true;
                                refresh.setRefreshing(false);
                            })
                            .setNegativeButton("Нет", null)
                            .setCancelable(false);
                    builder.show();
                    break;
            }
            Toast.makeText(getContext(), "Вы удачно удалили зап.", Toast.LENGTH_SHORT).show();
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapter.removeToggleSelection();
        }
    };

    /* end ActionMode */

    @Override
    public void onItemClick(Item item, int position) {
        if (isActionMode()) {
            toggleSelection(position);
        }
    }

    @Override
    public void onItemLongClick(Item item, int position) {
        if (isActionMode()) {
            return;
        }
        actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        toggleSelection(position);
    }

    private boolean isActionMode() {
        return actionMode != null;
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
    }

}
