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

package com.fenlisproject.elf.core.framework;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import com.fenlisproject.elf.core.annotation.AnimationResourceId;
import com.fenlisproject.elf.core.annotation.OnClick;
import com.fenlisproject.elf.core.annotation.OnItemClick;
import com.fenlisproject.elf.core.annotation.ViewId;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ElfBinder {

    private ElfBinder() {
    }

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

    public static void bindAnimation(Object receiver) {
        Field[] fields = receiver.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            AnimationResourceId animResId = field.getAnnotation(AnimationResourceId.class);
            if (animResId != null) {
                try {
                    Context context = null;
                    if (receiver instanceof Activity) {
                        context = (Activity) receiver;
                    } else if (receiver instanceof Fragment) {
                        context = ((Fragment) receiver).getActivity();
                    } else if (receiver instanceof Dialog) {
                        context = ((Dialog) receiver).getContext();
                    }
                    field.set(receiver, AnimationUtils.loadAnimation(context, animResId.value()));
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
                    view = ((Activity) receiver).findViewById(onItemClick.value());
                } else if (receiver instanceof Fragment) {
                    view = parentView.findViewById(onItemClick.value());
                } else if (receiver instanceof Dialog) {
                    view = ((Dialog) receiver).findViewById(onItemClick.value());
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
