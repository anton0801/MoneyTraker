package priler.com;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private static final String TAG = "AddItemActivity";

    private EditText name;
    private EditText price;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        // setTitle(R.string.add_item_title);
        name = findViewById(R.id.name); // находит наш элемент
        price = findViewById(R.id.price);
        addBtn = findViewById(R.id.add_btn);

//        name.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // до то го как текст изменится
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // когда текст изменится
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // после того как текст изменится
//                if (price.getText() .toString() != "" & name.getText().toString() != "") {
//                    addBtn.setEnabled(true);
//                    Log.i(TAG, "enable");
//                } else {
//                    addBtn.setEnabled(false);
//                }
//            }
//        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(price.getText()) && !TextUtils.isEmpty(name.getText())) {
                    addBtn.setEnabled(true);
                    Log.i(TAG, "enable");
                } else {
                    Log.e(TAG, "disabled");
                }
            }
        });

    }
}
