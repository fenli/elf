package org.elf.framework.core.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.elf.framework.core.annotation.ContentView;
import org.elf.framework.core.annotation.OnClick;
import org.elf.framework.core.annotation.OnItemClick;
import org.elf.framework.core.annotation.ViewId;
import org.elf.framework.core.config.AppEnvironment;
import org.elf.framework.core.data.FileSessionManager;
import org.elf.framework.core.data.SimpleSessionManager;
import org.elf.framework.core.handler.BaseTaskExecutor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class BaseFragment extends Fragment implements BaseTask {

    private View mContentView;

    public View getContentView() {
        return mContentView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        Annotation classAnnotation = getClass().getAnnotation(ContentView.class);
        if (classAnnotation instanceof ContentView) {
            mContentView = inflater.inflate(((ContentView) classAnnotation).value(), container, false);
            bindAllView();
            bindAllEventListener();
            onContentViewCreated();
        }
        return mContentView;
    }

    private void bindAllView() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ViewId viewId = field.getAnnotation(ViewId.class);
            if (viewId != null) {
                try {
                    field.set(this, getContentView().findViewById(viewId.value()));
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
                View view = getContentView().findViewById(onClick.value());
                view.setOnClickListener(this);
            }

            OnItemClick onItemClick = method.getAnnotation(OnItemClick.class);
            if (onItemClick != null) {
                View view = getContentView().findViewById(onItemClick.value());
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
    public void runDelayed(final String tag, long delayMilis, final Object... args) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BaseTaskExecutor.executeMethodByTag(BaseFragment.this, tag, args);
            }
        }, delayMilis);
    }

    @Override
    public void runOnUiThread(final String tag, final Object... args) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseTaskExecutor.executeMethodByTag(BaseFragment.this, tag, args);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivityListener) {
            ((BaseActivityListener) activity).onFragmentAttached(this);
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

    public SimpleSessionManager getDefaultSessionManager() {
        return getActivity() instanceof BaseActivity ?
                ((BaseActivity) getActivity()).getDefaultSessionManager() : null;
    }

    public FileSessionManager getFileSessionManager() {
        return getActivity() instanceof BaseActivity ?
                ((BaseActivity) getActivity()).getFileSessionManager() : null;
    }
}
