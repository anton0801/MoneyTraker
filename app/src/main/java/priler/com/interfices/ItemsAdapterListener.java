package priler.com.interfices;

import priler.com.models.Item;

public interface ItemsAdapterListener {

    void onItemClick(Item item, int position);
    void onItemLongClick(Item item, int position);

}
