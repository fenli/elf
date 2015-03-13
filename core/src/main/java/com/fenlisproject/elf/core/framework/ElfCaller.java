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

import com.fenlisproject.elf.core.annotation.OnClick;
import com.fenlisproject.elf.core.annotation.OnItemClick;
import com.fenlisproject.elf.core.annotation.Tag;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ElfCaller {

    private ElfCaller() {
    }

    public static Object callOnClickListener(Object receiver, int id) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                try {
                    if (onClick.value() == id) {
                        return method.invoke(receiver);
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Object callOnItemClickListener(Object receiver, int id, int position) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            OnItemClick onItemClick = method.getAnnotation(OnItemClick.class);
            if (onItemClick != null) {
                try {
                    if (onItemClick.value() == id) {
                        return method.invoke(receiver, position);
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Object callMethodByTag(final Object receiver, String tag, Object... args) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (final Method method : methods) {
            method.setAccessible(true);
            Tag methodAnnotation = method.getAnnotation(Tag.class);
            if (methodAnnotation != null) {
                if (methodAnnotation.value().equalsIgnoreCase(tag)) {
                    try {
                        return method.invoke(receiver, args);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
