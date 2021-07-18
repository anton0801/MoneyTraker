package priler.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import priler.com.R;
import priler.com.adapters.MainPagesAdapter;
import priler.com.api.App;
import priler.com.fragments.ItemsFragment;
import priler.com.models.Item;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static String userId;

    private ViewPager pager;
    private TabLayout tabLayout;

    private Toolbar toolbar;

    private FloatingActionButton fab;

    private ActionMode actionMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!((App) getApplication()).getBoolean(App.IS_AUTH_KEY)) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            return;
        }

        MainActivity.userId = ((App) getApplication()).getString("user_id");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true); // небудет заголовка если false

        pager = findViewById(R.id.viewPager);
        MainPagesAdapter adapter = new MainPagesAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(this);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            int currentPage = pager.getCurrentItem(); // текущая страница
            String type = null;
            if (currentPage == MainPagesAdapter.PAGE_INCOMES) {
                type = Item.TYPE_INCOMES;
            } else if (currentPage == MainPagesAdapter.PAGE_EXPENSES) {
                type = Item.TYPE_EXPENSES;
            } else {
                type = Item.TYPE_BALANCE;
            }
            Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
            intent.putExtra(AddItemActivity.KEY_TYPE, type);
            startActivityForResult(intent, ItemsFragment.ADD_ITEM_REQUEST_CODE);
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case MainPagesAdapter.PAGE_INCOMES:
            case MainPagesAdapter.PAGE_EXPENSES:
                fab.show(); // у метода show() усть еще анимация показывания
                break;
            case MainPagesAdapter.PAGE_BALANCE:
                fab.hide();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        /*
         *   у state есть три разновидности state:
         *       1. ViewPager.SCROLL_STATE_IDLE
         *           это когда просто страница задана и все она стоит на месте
         *       2. ViewPager.SCROLL_STATE_DRAGGING
         *           это когда мы начинаем тянуть
         *       3. ViewPager.SCROLL_STATE_SETTLING
         *           это уже когда мы палец отпустили и страница уже сама доводится до положения нужного
         * */
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                fab.setEnabled(true);
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
            case ViewPager.SCROLL_STATE_SETTLING:
                if (actionMode != null)
                    actionMode.finish();
                fab.setEnabled(false);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        fab.hide();
        actionMode = mode;
    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        fab.show();
        actionMode = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sign_out) {
            ((App) getApplication()).putBoolean(App.IS_AUTH_KEY, false);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return true;
    }
}
