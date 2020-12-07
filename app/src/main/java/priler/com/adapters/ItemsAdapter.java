package priler.com.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import priler.com.R;
import priler.com.interfices.ItemsAdapterListener;
import priler.com.models.Item;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> data = new ArrayList<>();
    private Context context;

    public ItemsAdapter(Context context) {
        this.context = context;
    }

    private ItemsAdapterListener listener = null;

    public void setListener(ItemsAdapterListener listener) {
        this.listener = listener;
    }

    public void setData(List<Item> data) {
        this.data = data;
        notifyItemRangeInserted(this.data.size(), data.size());
    }

    public void deleteItem(Item item) {
        int index = data.indexOf(item);
        data.remove(item);
        notifyItemRemoved(index);
    }

    public void addItem(Item item) {
        data.add(item);
        notifyItemInserted(data.size());
    }

    private SparseBooleanArray selections = new SparseBooleanArray(); // это типо HashMap, но в android лучше использовать SparseBooleanArray

    public void toggleSelection(int position) {
        if (selections.get(position, false)) {
            selections.delete(position);
        } else {
            selections.put(position, true);
        }
        notifyItemChanged(position);
    }

    public void removeToggleSelection() {
        if (selections.size() > 0) {
            selections.clear();
        }
        // notifyItemRangeChanged(0, selections.size());
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView price;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
        }

        public void bindData(final Item record, final ItemsAdapterListener listener, boolean selected) {
            title.setText(record.name);
            price.setText(context.getResources().getString(R.string.price, record.price));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(record, getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onItemLongClick(record, getAdapterPosition());
                    }
                    return true;
                }
            });
            itemView.setActivated(selected);
        }

    }

    public SparseBooleanArray getSelections() {
        return selections;
    }

    @NonNull
    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new ItemsAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.ItemViewHolder holder, int position) {
        Item record = data.get(position);
        holder.bindData(record, listener, selections.get(position, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
