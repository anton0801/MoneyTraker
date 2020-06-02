package priler.com;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemsAdapter();
        recyclerView.setAdapter(adapter);
        // adapter.notifyDataSetChanged(); // обновляет если есть изменения и если есть то на экране вы увидите новые даные
    }
}