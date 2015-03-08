package com.fenlisproject.elf.core.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;

import com.fenlisproject.elf.core.annotation.Binder;
import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.config.AppEnvironment;
import com.fenlisproject.elf.core.data.PersistentStorage;
import com.fenlisproject.elf.core.handler.BaseTaskExecutor;
import com.fenlisproject.elf.core.listener.CommonFragmentEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BaseActivity extends ActionBarActivity implements BaseEventListener {

    private HashMap<Integer, Fragment> mActiveFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActiveFragments = new HashMap<>();
        ContentView contentView = getClass().getAnnotation(ContentView.class);
        if (contentView != null) {
            setContentView(contentView.value());
        }
        Binder.bindView(this, null);
        Binder.bindEventListener(this, null);
        onContentViewCreated();
    }

    protected abstract void onContentViewCreated();

    @Override
    public void onClick(View v) {
        BaseTaskExecutor.executeOnClickListener(this, v.getId());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaseTaskExecutor.executeOnItemClickListener(this, parent.getId(), position);
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
                BaseTaskExecutor.executeMethodByTag(BaseActivity.this, tag, args);
            }
        }, delayMilis);
    }

    @Override
    public void runOnUiThread(final String tag, final Object... args) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseTaskExecutor.executeMethodByTag(BaseActivity.this, tag, args);
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

    public PersistentStorage getDefaultSessionStorage() {
        return getApplicationContext() instanceof BaseApplication ?
                ((BaseApplication) getApplicationContext()).getDefaultSessionStorage() : null;
    }
}
