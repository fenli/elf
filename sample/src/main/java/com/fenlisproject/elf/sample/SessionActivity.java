package com.fenlisproject.elf.sample;

import android.widget.Toast;

import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.annotation.OnClick;
import com.fenlisproject.elf.core.annotation.ViewId;
import com.fenlisproject.elf.core.base.BaseActivity;
import com.fenlisproject.elf.core.data.SecureSessionStorage;
import com.fenlisproject.elf.core.data.SessionDataWrapper;
import com.fenlisproject.elf.core.widget.ExtendedEditText;

import java.io.Serializable;

@ContentView(R.layout.activity_session)
public class SessionActivity extends BaseActivity {

    @ViewId(R.id.edittext1)
    ExtendedEditText edittext1;

    @ViewId(R.id.edittext2)
    ExtendedEditText edittext2;

    SecureSessionStorage secureStorage;

    @Override
    protected void onContentViewCreated() {
        secureStorage = ((SampleApplication) getApplication()).getSecureSessionStorage();
    }

    @OnClick(R.id.button1)
    public void writeSessionData() {
        Contact c = new Contact();
        c.setName(edittext1.getValue());
        secureStorage.putSessionData("contact", c, 4000);
    }

    @OnClick(R.id.button2)
    public void readSessionData() {
        SessionDataWrapper<Contact> c = secureStorage.getSessionData("contact", Contact.class);
        edittext2.setText(c.getData().getName());
        Toast.makeText(this, "Expired : " + c.isExpired(), Toast.LENGTH_SHORT).show();
    }

    static class Contact implements Serializable {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
