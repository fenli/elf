package com.fenlisproject.elf.core.annotation;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Binder {

    public static void bindView(Object receiver, View parentView) {
        Field[] fields = receiver.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ViewId viewId = field.getAnnotation(ViewId.class);
            if (viewId != null) {
                try {
                    View view = null;
                    if (receiver instanceof Activity) {
                        view = ((Activity) receiver).findViewById(viewId.value());
                    } else if (receiver instanceof Fragment) {
                        view = parentView.findViewById(viewId.value());
                    } else if (receiver instanceof Dialog) {
                        view = ((Dialog) receiver).findViewById(viewId.value());
                    }
                    if (view != null) {
                        field.set(receiver, view);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void bindEventListener(Object receiver, View parentView) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                View view = null;
                if (receiver instanceof Activity) {
                    view = ((Activity) receiver).findViewById(onClick.value());
                } else if (receiver instanceof Fragment) {
                    view = parentView.findViewById(onClick.value());
                } else if (receiver instanceof Dialog) {
                    view = ((Dialog) receiver).findViewById(onClick.value());
                }
                if (view != null && receiver instanceof View.OnClickListener) {
                    view.setOnClickListener((View.OnClickListener) receiver);
                }
            }

            OnItemClick onItemClick = method.getAnnotation(OnItemClick.class);
            if (onItemClick != null) {
                View view = null;
                if (receiver instanceof Activity) {
                    view = ((Activity) receiver).findViewById(onClick.value());
                } else if (receiver instanceof Fragment) {
                    view = parentView.findViewById(onClick.value());
                } else if (receiver instanceof Dialog) {
                    view = ((Dialog) receiver).findViewById(onClick.value());
                }
                if (view instanceof AdapterView &&
                        receiver instanceof AdapterView.OnItemClickListener) {
                    ((AdapterView) view).setOnItemClickListener(
                            (AdapterView.OnItemClickListener) receiver);
                }
            }
        }
    }

}
