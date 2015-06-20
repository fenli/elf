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

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.annotation.OptionMenu;
import com.fenlisproject.elf.core.config.AppEnvironment;
import com.fenlisproject.elf.core.data.PreferencesStorage;
import com.fenlisproject.elf.core.data.SessionStorage;
import com.fenlisproject.elf.core.event.CommonActivityEventListener;
import com.fenlisproject.elf.core.framework.ElfBinder;
import com.fenlisproject.elf.core.framework.ElfCaller;

public abstract class BaseFragment extends Fragment implements BaseEventListener, BaseTask {

    private View mContentView;

    public View getContentView() {
        return mContentView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OptionMenu optionMenu = getClass().getAnnotation(OptionMenu.class);
        if (optionMenu != null) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        ContentView contentView = getClass().getAnnotation(ContentView.class);
        if (contentView != null) {
            mContentView = inflater.inflate(contentView.value(), container, false);
            ElfBinder.bindView(this, mContentView);
            onBeforeBindEventListener();
            ElfBinder.bindEventListener(this, mContentView);
        }
        ElfBinder.bindAnimation(this);
        ElfBinder.bindIntentExtra(this);
        ElfBinder.bindFragmentArgument(this);
        onContentViewCreated();
        return mContentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        OptionMenu optionMenu = getClass().getAnnotation(OptionMenu.class);
        if (optionMenu != null) {
            inflater.inflate(optionMenu.value(), menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return ElfCaller.callOnMenuItemSelectedListener(this, item.getItemId());
    }

    protected void onBeforeBindEventListener() {
    }

    protected abstract void onContentViewCreated();

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
    public void runDelayed(final String tag, long delayMilis, final Object... args) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ElfCaller.callMethodByTag(BaseFragment.this, tag, args);
            }
        }, delayMilis);
    }

    @Override
    public void runOnUiThread(final String tag, final Object... args) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ElfCaller.callMethodByTag(BaseFragment.this, tag, args);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CommonActivityEventListener) {
            ((CommonActivityEventListener) activity).onFragmentAttached(this);
        }
    }

    protected BaseActivity getBaseActivity() {
        return getActivity() instanceof BaseActivity ? (BaseActivity) getActivity() : null;
    }

    public void replaceChildFragment(int layoutId, Fragment fragment) {
        FragmentTransaction t = getChildFragmentManager().beginTransaction();
        t.replace(layoutId, fragment);
        t.commit();
    }

    public void replaceChildFragment(int layoutId, Fragment fragment, int enterAnimation,
                                     int exitAnimation) {
        FragmentTransaction t = getChildFragmentManager().beginTransaction();
        t.setCustomAnimations(enterAnimation, exitAnimation);
        t.replace(layoutId, fragment);
        t.commit();
    }

    public AppEnvironment getAppEnvironment() {
        return getActivity() instanceof BaseActivity ?
                ((BaseActivity) getActivity()).getAppEnvironment() : null;
    }

    public PreferencesStorage getDefaultPreferencesStorage() {
        return getActivity() instanceof BaseActivity ?
                ((BaseActivity) getActivity()).getDefaultPreferencesStorage() : null;
    }

    public SessionStorage getDefaultSessionStorage() {
        return getActivity() instanceof BaseActivity ?
                ((BaseActivity) getActivity()).getDefaultSessionStorage() : null;
    }
}
