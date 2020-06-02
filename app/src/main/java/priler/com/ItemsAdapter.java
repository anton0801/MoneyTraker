package priler.com;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> data = new ArrayList<>();

    public ItemsAdapter() {
        createData();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView price;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
        }

        public void applyData(Item record) {
            // Log.d(TAG, "applyData " + mRecyclerView.getChildLayoutPosition(itemView) + record.getTitle());
            title.setText(record.getTitle());
            price.setText(String.format("%s₽", record.getPrice()));
        }

        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Log.d(ItemListActivity.TAG, "onCreateFolder " + parent.getChildCount());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new ItemsAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.ItemViewHolder holder, int position) {
        // Log.d(ItemListActivity.TAG, "onBindViewHolder " + itemListActivity.mRecyclerView.getChildCount() + " " + position);
        Item record = data.get(position);
        holder.applyData(record);
    }

    private void createData() {
        data.add(new Item("Молоко", 100));
        data.add(new Item("Курсы", 40000));
        data.add(new Item("macBook pro 2019 16", 350000));
        data.add(new Item("за дом", 120000));
        data.add(new Item("еда", 35000));
        data.add(new Item("компьютер", 150000));
        data.add(new Item("iphone", 120000));
        data.add(new Item("книги", 5000));
        data.add(new Item("мебель", 70000));
        data.add(new Item("кошка", 10000));
    }

    @Override
    public int getItemCount() { return data.size(); }
}
