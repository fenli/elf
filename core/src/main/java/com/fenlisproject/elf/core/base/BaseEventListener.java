package com.fenlisproject.elf.core.base;

import android.view.View;
import android.widget.AdapterView;

public interface BaseEventListener extends View.OnClickListener, AdapterView.OnItemClickListener {

    @Override
    public void onClick(View v);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id);
}
