/*
* Copyright (C) 2015 Steven Lewi
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.fenlisproject.elf.core.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.annotation.OptionMenu;
import com.fenlisproject.elf.core.config.AppEnvironment;
import com.fenlisproject.elf.core.data.PreferencesStorage;
import com.fenlisproject.elf.core.data.SessionStorage;
import com.fenlisproject.elf.core.event.CommonFragmentEventListener;
import com.fenlisproject.elf.core.framework.ElfBinder;
import com.fenlisproject.elf.core.framework.ElfCaller;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseActivity extends AppCompatActivity implements BaseEventListener, BaseTask {

    private ConcurrentHashMap<Integer, Fragment> mActiveFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActiveFragments = new ConcurrentHashMap<>();
        ContentView contentView = getClass().getAnnotation(ContentView.class);
        if (contentView != null) {
            setContentView(contentView.value());
            ElfBinder.bindView(this, null);
            ElfBinder.bindEventListener(this, null);
        }
        ElfBinder.bindAnimation(this);
        ElfBinder.bindIntentExtra(this);
        onContentViewCreated();
    }

    protected abstract void onContentViewCreated();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        OptionMenu optionMenu = getClass().getAnnotation(OptionMenu.class);
        if (optionMenu != null) {
            getMenuInflater().inflate(optionMenu.value(), menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ElfCaller.callOnMenuItemSelectedListener(this, item.getItemId())) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        ElfCaller.callOnClickListener(this, v.getId());
    }

    @Override
    public boolean onLongClick(View v) {
        return ElfCaller.callOnLongClickListener(this, v.getId());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ElfCaller.callOnFocusChangeListener(this, v.getId(), hasFocus);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return ElfCaller.callOnTouchListener(this, v.getId(), event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ElfCaller.callOnItemClickListener(this, parent.getId(), position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return ElfCaller.callOnItemLongClickListener(this, parent.getId(), position);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ElfCaller.callOnItemSelectedListener(this, parent.getId(), position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ElfCaller.callOnCheckedChangedListener(this, buttonView.getId(), isChecked);
    }

    @Override
    public void onBackPressed() {
        boolean intercept = false;
        Iterator it = mActiveFragments.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            Fragment fragment = (Fragment) pairs.getValue();
            it.remove();
            if (fragment instanceof CommonFragmentEventListener) {
                intercept = intercept || ((CommonFragmentEventListener) fragment).onBackPressed();
            }
        }
        if (!intercept) {
            super.onBackPressed();
        }
    }

    @Override
    public void runDelayed(final String tag, long delayMilis, final Object... args) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ElfCaller.callMethodByTag(BaseActivity.this, tag, args);
            }
        }, delayMilis);
    }

    @Override
    public void runOnUiThread(final String tag, final Object... args) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ElfCaller.callMethodByTag(BaseActivity.this, tag, args);
            }
        });
    }

    public void replaceFragment(int layoutId, Fragment fragment) {
        mActiveFragments.put(layoutId, fragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(layoutId, fragment)
                .commit();
    }

    public void replaceFragment(int layoutId, Fragment fragment, int enterAnim, int exitAnim) {
        mActiveFragments.put(layoutId, fragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(enterAnim, exitAnim)
                .replace(layoutId, fragment)
                .commit();
    }

    public void replaceFragment(int layoutId, Class FragmentClass) {
        try {
            Fragment fragment = (Fragment) FragmentClass.newInstance();
            replaceFragment(layoutId, fragment);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void replaceFragment(int layoutId, Class FragmentClass, int enterAnim, int exitAnim) {
        try {
            Fragment fragment = (Fragment) FragmentClass.newInstance();
            replaceFragment(layoutId, fragment, enterAnim, exitAnim);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Fragment getActiveFragment(int layoutId) {
        return mActiveFragments.get(layoutId);
    }

    public AppEnvironment getAppEnvironment() {
        return getApplicationContext() instanceof BaseApplication ?
                ((BaseApplication) getApplicationContext()).getAppEnvironment() : null;
    }

    public PreferencesStorage getDefaultPreferencesStorage() {
        return getApplicationContext() instanceof BaseApplication ?
                ((BaseApplication) getApplicationContext()).getDefaultPreferencesStorage() : null;
    }

    public SessionStorage getDefaultSessionStorage() {
        return getApplicationContext() instanceof BaseApplication ?
                ((BaseApplication) getApplicationContext()).getDefaultSessionStorage() : null;
    }
}
