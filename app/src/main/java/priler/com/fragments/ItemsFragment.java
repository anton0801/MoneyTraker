package priler.com.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;

import priler.com.R;
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
            type = bundle.getString(TYPE_KEY, Item.TYPE_EXPENSES);

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
        recyclerView.setLayoutManager(layoutManager); // getActivity это и есть context или getContext()

        refresh = view.findViewById(R.id.refresh);
        refresh.setColorSchemeColors(Color.BLUE, Color.CYAN, Color.GREEN);
        refresh.setOnRefreshListener(() -> {
            loadItems(page);
            refresh.setRefreshing(false);
        });

        loadItems(page);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            recyclerView.setOnScrollChangeListener((view1, i, i1, i2, i3) -> {
//                if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 4) {
//                    page++;
//                    loadItems(page);
//                }
//            });
//        }
    }

    public void loadItems(int page) {
        refresh.setRefreshing(true);
        Call<ItemResponse> call = api.getItems(1, type, page);

        call.enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                if (response.isSuccessful()) {
                    List<Item> items = response.body().getResults();
                    if (items != null) {
                        refresh.setRefreshing(false);
                        adapter.setData(items);
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemResponse> call, Throwable t) {
                refresh.setRefreshing(false);
                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void addItem(Item item) {
//        api.addItem(1, item.name, item.price,
//                item.comment, type).enqueue(new Callback<ItemResponse>() {
//            @Override
//            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
//                if (response.body() != null) {
//                    item.id = response.body().getResults().get(0).id;
//                    adapter.addItem(item);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ItemResponse> call, Throwable t) {
//                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Item item = (Item) data.getSerializableExtra("item");
            if (item != null && item.type.equals(type)) {
                adapter.addItem(item);
            }
        }
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
//                                refresh.setRefreshing(true);
//                                SparseBooleanArray selections = adapter.getSelections();
//                                for (int i = 0; i < selections.size(); i++) {
//                                    api.delete(selections);
//                                }
//                                refresh.setRefreshing(false);
                            })
                            .setNegativeButton("Нет", null)
                            .setCancelable(false);
                    builder.show();
                    break;
            }
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
        } else {
            toggleSelection(position);
        }
        Log.d(TAG, "onItemClick: name = " + item.name + " position = " + position);
    }

    @Override
    public void onItemLongClick(Item item, int position) {
        if (isActionMode()) {
            return;
        }
        actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        toggleSelection(position);
        Log.d(TAG, "onItemLongClick: name = " + item.name + " position = " + position);
    }

    private boolean isActionMode() {
        return actionMode != null;
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
    }

}
