package org.elf.framework.core.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;

import org.elf.framework.core.annotation.ContentView;
import org.elf.framework.core.annotation.OnClick;
import org.elf.framework.core.annotation.OnItemClick;
import org.elf.framework.core.annotation.ViewId;
import org.elf.framework.core.config.AppEnvironment;
import org.elf.framework.core.data.FileSessionManager;
import org.elf.framework.core.data.SimpleSessionManager;
import org.elf.framework.core.handler.BaseTaskExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BaseActivity extends ActionBarActivity implements BaseTask {

    private HashMap<Integer, Fragment> mActiveFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActiveFragments = new HashMap<>();
        ContentView contentView = getClass().getAnnotation(ContentView.class);
        if (contentView != null) {
            setContentView(contentView.value());
        }
        bindAllView();
        bindAllEventListener();
        onContentViewCreated();
    }

    private void bindAllView() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ViewId viewId = field.getAnnotation(ViewId.class);
            if (viewId != null) {
                try {
                    View view = findViewById(viewId.value());
                    field.set(this, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void bindAllEventListener() {
        Method[] methods = getClass().getDeclaredMethods();
        for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                View view = findViewById(onClick.value());
                view.setOnClickListener(this);
            }

            OnItemClick onItemClick = method.getAnnotation(OnItemClick.class);
            if (onItemClick != null) {
                View view = findViewById(onItemClick.value());
                if (view instanceof AdapterView) {
                    ((AdapterView) view).setOnItemClickListener(this);
                }
            }
        }
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
            if (fragment instanceof BaseFragmentListener) {
                intercept = intercept || ((BaseFragmentListener) fragment).onBackPressed();
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

    public SimpleSessionManager getDefaultSessionManager() {
        return getApplicationContext() instanceof BaseApplication ?
                ((BaseApplication) getApplicationContext()).getDefaultSessionManager() : null;
    }

    public FileSessionManager getFileSessionManager() {
        return getApplicationContext() instanceof BaseApplication ?
                ((BaseApplication) getApplicationContext()).getFileSessionManager() : null;
    }
}
