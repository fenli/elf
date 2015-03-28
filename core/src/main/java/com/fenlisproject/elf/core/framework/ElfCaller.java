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

import android.view.MotionEvent;

import com.fenlisproject.elf.core.annotation.OnCheckedChanged;
import com.fenlisproject.elf.core.annotation.OnClick;
import com.fenlisproject.elf.core.annotation.OnFocusChange;
import com.fenlisproject.elf.core.annotation.OnItemClick;
import com.fenlisproject.elf.core.annotation.OnItemLongClick;
import com.fenlisproject.elf.core.annotation.OnItemSelected;
import com.fenlisproject.elf.core.annotation.OnLongClick;
import com.fenlisproject.elf.core.annotation.OnMenuItemSelected;
import com.fenlisproject.elf.core.annotation.OnTouch;
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

    public static boolean callOnLongClickListener(Object receiver, int id) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            OnLongClick onLongClick = method.getAnnotation(OnLongClick.class);
            if (onLongClick != null) {
                try {
                    if (onLongClick.value() == id) {
                        method.invoke(receiver);
                        return true;
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static Object callOnFocusChangeListener(Object receiver, int id, boolean hasFocus) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            OnFocusChange onFocusChange = method.getAnnotation(OnFocusChange.class);
            if (onFocusChange != null) {
                try {
                    if (onFocusChange.value() == id) {
                        return method.invoke(receiver, hasFocus);
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

    public static boolean callOnTouchListener(Object receiver, int id, MotionEvent event) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            OnTouch onTouch = method.getAnnotation(OnTouch.class);
            if (onTouch != null) {
                try {
                    if (onTouch.value() == id) {
                        method.invoke(receiver, event);
                        return true;
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
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

    public static boolean callOnItemLongClickListener(Object receiver, int id, int position) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            OnItemLongClick onItemLongClick = method.getAnnotation(OnItemLongClick.class);
            if (onItemLongClick != null) {
                try {
                    if (onItemLongClick.value() == id) {
                        method.invoke(receiver, position);
                        return true;
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static Object callOnItemSelectedListener(Object receiver, int id, int position) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            OnItemSelected onItemSelected = method.getAnnotation(OnItemSelected.class);
            if (onItemSelected != null) {
                try {
                    if (onItemSelected.value() == id) {
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

    public static Object callOnCheckedChangedListener(Object receiver, int id, boolean isChecked) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            OnCheckedChanged onCheckedChanged = method.getAnnotation(OnCheckedChanged.class);
            if (onCheckedChanged != null) {
                try {
                    if (onCheckedChanged.value() == id) {
                        return method.invoke(receiver, isChecked);
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

    public static boolean callOnMenuItemSelectedListener(Object receiver, int id) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            OnMenuItemSelected onMenuItemSelected = method.getAnnotation(OnMenuItemSelected.class);
            if (onMenuItemSelected != null) {
                try {
                    if (onMenuItemSelected.value() == id) {
                        method.invoke(receiver);
                        return true;
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
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
