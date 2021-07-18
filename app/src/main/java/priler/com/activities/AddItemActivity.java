package priler.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import priler.com.R;
import priler.com.api.Api;
import priler.com.api.App;
import priler.com.models.ItemBody;
import priler.com.models.ItemResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemActivity extends AppCompatActivity {

    private static final String TAG = "AddItemActivity";
    public static final String KEY_TYPE = "type";

    private String type;

    private EditText name;
    private EditText price;
    private Button addBtn;

    private Toolbar toolbar;

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        api = ((App) getApplication()).getApi();

        if (getIntent() != null) {
            type = getIntent().getStringExtra(KEY_TYPE);
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true); // небудет заголовка еслм false
        getSupportActionBar().setTitle(R.string.add_item_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        addBtn = findViewById(R.id.add_btn);

        price.setText(getString(R.string.price, price.getText()));

        addBtn.setOnClickListener(v -> {
            String nameS = name.getText().toString();
            String priceS = price.getText().toString().replace(getString(R.string.current_valute), "");

            if (nameS.length() == 0 || priceS.length() == 0) {
                Toast.makeText(this, getString(R.string.enter_all_dates_text), Toast.LENGTH_SHORT).show();
            } else {
                ItemBody itemBody = new ItemBody(Integer.parseInt(MainActivity.userId), nameS, priceS, "", type);
                api.addItem(itemBody).enqueue(new Callback<ItemResponse>() {
                    @Override
                    public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AddItemActivity.this, getString(R.string.create_post_success_text), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ItemResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(AddItemActivity.this, getString(R.string.some_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
