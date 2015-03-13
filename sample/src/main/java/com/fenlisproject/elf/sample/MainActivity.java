package com.fenlisproject.elf.sample;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.annotation.OnItemClick;
import com.fenlisproject.elf.core.annotation.ViewId;
import com.fenlisproject.elf.core.base.BaseActivity;

import org.fenlisproject.elf.sample.R;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewId(R.id.menu)
    private ListView menuListView;

    @Override
    protected void onContentViewCreated() {
        List<String> menu = new ArrayList<>();
        menu.add("Form Validation");
        menuListView.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu));
    }

    @OnItemClick(R.id.menu)
    public void menuItemClicked(int position) {
        Log.d("TAG", "Clicked item position : " + position);
        switch (position) {
            case 0:

                break;
        }
    }
}
