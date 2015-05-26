package com.fenlisproject.elf.sample;

import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.annotation.OnItemClick;
import com.fenlisproject.elf.core.annotation.OnMenuItemSelected;
import com.fenlisproject.elf.core.annotation.OptionMenu;
import com.fenlisproject.elf.core.annotation.ViewId;
import com.fenlisproject.elf.core.base.BaseActivity;

import com.fenlisproject.elf.sample.R;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
@OptionMenu(R.menu.menu_main)
public class MainActivity extends BaseActivity {

    private final String TAG = getClass().getName();

    @ViewId(R.id.menu)
    private ListView mainMenu;

    @Override
    protected void onContentViewCreated() {
        List<String> menu = new ArrayList<>();
        menu.add("View Binding");
        menu.add("Intent Extra");
        menu.add("Fragment Argument");
        menu.add("Extended View (Widget)");
        menu.add("Form Validation");
        menu.add("Http Request");
        menu.add("Session Manager");
        mainMenu.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu));
    }

    @OnItemClick(R.id.menu)
    public void selectMenu(int position) {
        Log.d(TAG, "Clicked item position : " + position);
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, ViewBindingActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(this, FormValidationActivity.class);
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(this, SessionActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    @OnMenuItemSelected(R.id.action_exit)
    public void onBackPressed() {
        super.onBackPressed();
    }
}
