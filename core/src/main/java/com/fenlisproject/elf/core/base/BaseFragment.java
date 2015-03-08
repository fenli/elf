package com.fenlisproject.elf.core.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.fenlisproject.elf.core.annotation.Binder;
import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.config.AppEnvironment;
import com.fenlisproject.elf.core.data.PersistentStorage;
import com.fenlisproject.elf.core.handler.BaseTaskExecutor;
import com.fenlisproject.elf.core.listener.CommonFragmentActivityListener;

import java.lang.annotation.Annotation;

public abstract class BaseFragment extends Fragment implements BaseEventListener {

    private View mContentView;

    public View getContentView() {
        return mContentView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        Annotation classAnnotation = getClass().getAnnotation(ContentView.class);
        if (classAnnotation instanceof ContentView) {
            mContentView = inflater.inflate(((ContentView) classAnnotation).value(), container, false);
            Binder.bindView(this, mContentView);
            Binder.bindEventListener(this, mContentView);
            onContentViewCreated();
        }
        return mContentView;
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
        if (activity instanceof CommonFragmentActivityListener) {
            ((CommonFragmentActivityListener) activity).onFragmentAttached(this);
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

    public PersistentStorage getDefaultSessionStorage() {
        return getActivity() instanceof BaseActivity ?
                ((BaseActivity) getActivity()).getDefaultSessionStorage() : null;
    }
}
