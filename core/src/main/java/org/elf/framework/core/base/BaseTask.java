package org.elf.framework.core.base;

import android.view.View;
import android.widget.AdapterView;

public interface BaseTask extends View.OnClickListener, AdapterView.OnItemClickListener {

    public void runDelayed(final String tag, long delayMilis, final Object... args);

    public void runOnUiThread(final String tag, final Object... args);

    @Override
    public void onClick(View v);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id);
}
