package com.fenlisproject.elf.sample;

import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.annotation.OnCheckedChanged;
import com.fenlisproject.elf.core.annotation.OnClick;
import com.fenlisproject.elf.core.annotation.OnFocusChange;
import com.fenlisproject.elf.core.annotation.OnLongClick;
import com.fenlisproject.elf.core.annotation.OnTouch;
import com.fenlisproject.elf.core.annotation.ViewId;
import com.fenlisproject.elf.core.base.BaseActivity;

import com.fenlisproject.elf.sample.R;

@ContentView(R.layout.activity_view_binding)
public class ViewBindingActivity extends BaseActivity {

    @ViewId(R.id.info)
    TextView info;

    @Override
    protected void onContentViewCreated() {
        info.setText("Please touch area above to show touch position");
    }

    @OnFocusChange(R.id.edittext1)
    public void displayEditText1Focus(boolean hasFocus) {
        Toast.makeText(this, "EditText1 Focus : " + hasFocus, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button1)
    public void button1Click() {
        Toast.makeText(this, "Button 1 clicked", Toast.LENGTH_SHORT).show();
    }

    @OnLongClick(R.id.button1)
    public void button1LongClick() {
        Toast.makeText(this, "Button 1 long clicked", Toast.LENGTH_SHORT).show();
    }

    @OnCheckedChanged(R.id.checkbox1)
    public void onCheckBox1CheckedChange(boolean checked) {
        if (checked) {
            Toast.makeText(this, "CheckBox 1 checked", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Checkbox 1 unchecked", Toast.LENGTH_SHORT).show();
        }
    }

    @OnTouch(R.id.touchpad1)
    public void showTouchCoordinate(MotionEvent event) {
        info.setText(String.format("Touched at %f,%f", event.getX(), event.getY()));
    }
}
