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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.fenlisproject.elf.core.annotation.AnimationResourceId;
import com.fenlisproject.elf.core.annotation.FragmentArgument;
import com.fenlisproject.elf.core.annotation.IntentExtra;
import com.fenlisproject.elf.core.annotation.OnCheckedChanged;
import com.fenlisproject.elf.core.annotation.OnClick;
import com.fenlisproject.elf.core.annotation.OnFocusChange;
import com.fenlisproject.elf.core.annotation.OnItemClick;
import com.fenlisproject.elf.core.annotation.OnItemLongClick;
import com.fenlisproject.elf.core.annotation.OnItemSelected;
import com.fenlisproject.elf.core.annotation.OnLongClick;
import com.fenlisproject.elf.core.annotation.OnTouch;
import com.fenlisproject.elf.core.annotation.ViewId;

import java.io.Serializable;
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
                    } else if (receiver instanceof Dialog) {
                        view = ((Dialog) receiver).findViewById(viewId.value());
                    } else if (parentView != null) {
                        view = parentView.findViewById(viewId.value());
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
                    if (context != null) {
                        Animation anim = AnimationUtils.loadAnimation(context, animResId.value());
                        field.set(receiver, anim);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void bindIntentExtra(Object receiver) {
        Field[] fields = receiver.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            IntentExtra intentExtra = field.getAnnotation(IntentExtra.class);
            if (intentExtra != null) {
                try {
                    Intent intent = null;
                    if (receiver instanceof Activity) {
                        intent = ((Activity) receiver).getIntent();
                    } else if (receiver instanceof Fragment) {
                        intent = ((Fragment) receiver).getActivity().getIntent();
                    }
                    if (intent != null) {
                        if (field.getType() == Boolean.class) {
                            field.set(receiver, intent.getBooleanExtra(intentExtra.value(), false));
                        } else if (field.getType() == Byte.class) {
                            field.set(receiver, intent.getByteExtra(intentExtra.value(), (byte) 0));
                        } else if (field.getType() == Character.class) {
                            field.set(receiver, intent.getCharExtra(intentExtra.value(), '\u0000'));
                        } else if (field.getType() == Double.class) {
                            field.set(receiver, intent.getDoubleExtra(intentExtra.value(), 0.0d));
                        } else if (field.getType() == Float.class) {
                            field.set(receiver, intent.getFloatExtra(intentExtra.value(), 0.0f));
                        } else if (field.getType() == Integer.class) {
                            field.set(receiver, intent.getIntExtra(intentExtra.value(), 0));
                        } else if (field.getType() == Long.class) {
                            field.set(receiver, intent.getLongExtra(intentExtra.value(), 0L));
                        } else if (field.getType() == Short.class) {
                            field.set(receiver, intent.getShortExtra(intentExtra.value(), (short) 0));
                        } else if (field.getType() == String.class) {
                            field.set(receiver, intent.getStringExtra(intentExtra.value()));
                        } else if (field.getType() == Boolean[].class) {
                            field.set(receiver, intent.getBooleanArrayExtra(intentExtra.value()));
                        } else if (field.getType() == Byte[].class) {
                            field.set(receiver, intent.getByteArrayExtra(intentExtra.value()));
                        } else if (field.getType() == Character[].class) {
                            field.set(receiver, intent.getCharArrayExtra(intentExtra.value()));
                        } else if (field.getType() == Double[].class) {
                            field.set(receiver, intent.getDoubleArrayExtra(intentExtra.value()));
                        } else if (field.getType() == Float[].class) {
                            field.set(receiver, intent.getFloatArrayExtra(intentExtra.value()));
                        } else if (field.getType() == Integer[].class) {
                            field.set(receiver, intent.getIntArrayExtra(intentExtra.value()));
                        } else if (field.getType() == Long[].class) {
                            field.set(receiver, intent.getLongArrayExtra(intentExtra.value()));
                        } else if (field.getType() == Short[].class) {
                            field.set(receiver, intent.getShortArrayExtra(intentExtra.value()));
                        } else if (field.getType() == String[].class) {
                            field.set(receiver, intent.getStringArrayExtra(intentExtra.value()));
                        } else if (field.getType() == Serializable.class) {
                            field.set(receiver, intent.getSerializableExtra(intentExtra.value()));
                        } else if (field.getType() == Bundle.class) {
                            field.set(receiver, intent.getBundleExtra(intentExtra.value()));
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void bindFragmentArgument(Fragment receiver) {
        Field[] fields = receiver.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            FragmentArgument fragmentArgument = field.getAnnotation(FragmentArgument.class);
            if (fragmentArgument != null) {
                try {
                    Bundle bundle = receiver.getArguments();
                    if (bundle != null) {
                        if (field.getType() == Boolean.class) {
                            field.set(receiver, bundle.getBoolean(fragmentArgument.value()));
                        } else if (field.getType() == Byte.class) {
                            field.set(receiver, bundle.getByte(fragmentArgument.value()));
                        } else if (field.getType() == Character.class) {
                            field.set(receiver, bundle.getChar(fragmentArgument.value()));
                        } else if (field.getType() == Double.class) {
                            field.set(receiver, bundle.getDouble(fragmentArgument.value()));
                        } else if (field.getType() == Float.class) {
                            field.set(receiver, bundle.getFloat(fragmentArgument.value()));
                        } else if (field.getType() == Integer.class) {
                            field.set(receiver, bundle.getInt(fragmentArgument.value()));
                        } else if (field.getType() == Long.class) {
                            field.set(receiver, bundle.getLong(fragmentArgument.value()));
                        } else if (field.getType() == Short.class) {
                            field.set(receiver, bundle.getShort(fragmentArgument.value()));
                        } else if (field.getType() == String.class) {
                            field.set(receiver, bundle.getString(fragmentArgument.value()));
                        } else if (field.getType() == Boolean[].class) {
                            field.set(receiver, bundle.getBooleanArray(fragmentArgument.value()));
                        } else if (field.getType() == Byte[].class) {
                            field.set(receiver, bundle.getByteArray(fragmentArgument.value()));
                        } else if (field.getType() == Character[].class) {
                            field.set(receiver, bundle.getCharArray(fragmentArgument.value()));
                        } else if (field.getType() == Double[].class) {
                            field.set(receiver, bundle.getDoubleArray(fragmentArgument.value()));
                        } else if (field.getType() == Float[].class) {
                            field.set(receiver, bundle.getFloatArray(fragmentArgument.value()));
                        } else if (field.getType() == Integer[].class) {
                            field.set(receiver, bundle.getIntArray(fragmentArgument.value()));
                        } else if (field.getType() == Long[].class) {
                            field.set(receiver, bundle.getLongArray(fragmentArgument.value()));
                        } else if (field.getType() == Short[].class) {
                            field.set(receiver, bundle.getShortArray(fragmentArgument.value()));
                        } else if (field.getType() == String[].class) {
                            field.set(receiver, bundle.getStringArray(fragmentArgument.value()));
                        } else if (field.getType() == Serializable.class) {
                            field.set(receiver, bundle.getSerializable(fragmentArgument.value()));
                        } else if (field.getType() == Bundle.class) {
                            field.set(receiver, bundle.getBundle(fragmentArgument.value()));
                        }
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
                } else if (receiver instanceof Dialog) {
                    view = ((Dialog) receiver).findViewById(onClick.value());
                } else if (parentView != null) {
                    view = parentView.findViewById(onClick.value());
                }
                if (view != null && receiver instanceof View.OnClickListener) {
                    view.setOnClickListener((View.OnClickListener) receiver);
                }
            }

            OnLongClick onLongClick = method.getAnnotation(OnLongClick.class);
            if (onLongClick != null) {
                View view = null;
                if (receiver instanceof Activity) {
                    view = ((Activity) receiver).findViewById(onLongClick.value());
                } else if (receiver instanceof Dialog) {
                    view = ((Dialog) receiver).findViewById(onLongClick.value());
                } else if (parentView != null) {
                    view = parentView.findViewById(onLongClick.value());
                }
                if (view != null && receiver instanceof View.OnLongClickListener) {
                    view.setOnLongClickListener((View.OnLongClickListener) receiver);
                }
            }

            OnFocusChange onFocusChange = method.getAnnotation(OnFocusChange.class);
            if (onFocusChange != null) {
                View view = null;
                if (receiver instanceof Activity) {
                    view = ((Activity) receiver).findViewById(onFocusChange.value());
                } else if (receiver instanceof Dialog) {
                    view = ((Dialog) receiver).findViewById(onFocusChange.value());
                } else if (parentView != null) {
                    view = parentView.findViewById(onFocusChange.value());
                }
                if (view != null && receiver instanceof View.OnFocusChangeListener) {
                    view.setOnFocusChangeListener((View.OnFocusChangeListener) receiver);
                }
            }

            OnTouch onTouch = method.getAnnotation(OnTouch.class);
            if (onTouch != null) {
                View view = null;
                if (receiver instanceof Activity) {
                    view = ((Activity) receiver).findViewById(onTouch.value());
                } else if (receiver instanceof Dialog) {
                    view = ((Dialog) receiver).findViewById(onTouch.value());
                } else if (parentView != null) {
                    view = parentView.findViewById(onTouch.value());
                }
                if (view != null && receiver instanceof View.OnTouchListener) {
                    view.setOnTouchListener((View.OnTouchListener) receiver);
                }
            }

            OnItemClick onItemClick = method.getAnnotation(OnItemClick.class);
            if (onItemClick != null) {
                View view = null;
                if (receiver instanceof Activity) {
                    view = ((Activity) receiver).findViewById(onItemClick.value());
                } else if (receiver instanceof Dialog) {
                    view = ((Dialog) receiver).findViewById(onItemClick.value());
                } else if (parentView != null) {
                    view = parentView.findViewById(onItemClick.value());
                }
                if (view instanceof AdapterView &&
                        receiver instanceof AdapterView.OnItemClickListener) {
                    ((AdapterView) view).setOnItemClickListener(
                            (AdapterView.OnItemClickListener) receiver);
                }
            }

            OnItemLongClick onItemLongClick = method.getAnnotation(OnItemLongClick.class);
            if (onItemLongClick != null) {
                View view = null;
                if (receiver instanceof Activity) {
                    view = ((Activity) receiver).findViewById(onItemLongClick.value());
                } else if (receiver instanceof Dialog) {
                    view = ((Dialog) receiver).findViewById(onItemLongClick.value());
                } else if (parentView != null) {
                    view = parentView.findViewById(onItemLongClick.value());
                }
                if (view instanceof AdapterView &&
                        receiver instanceof AdapterView.OnItemLongClickListener) {
                    ((AdapterView) view).setOnItemLongClickListener(
                            (AdapterView.OnItemLongClickListener) receiver);
                }
            }

            OnItemSelected onItemSelected = method.getAnnotation(OnItemSelected.class);
            if (onItemSelected != null) {
                View view = null;
                if (receiver instanceof Activity) {
                    view = ((Activity) receiver).findViewById(onItemSelected.value());
                } else if (receiver instanceof Dialog) {
                    view = ((Dialog) receiver).findViewById(onItemSelected.value());
                } else if (parentView != null) {
                    view = parentView.findViewById(onItemSelected.value());
                }
                if (view instanceof AdapterView &&
                        receiver instanceof AdapterView.OnItemSelectedListener) {
                    ((AdapterView) view).setOnItemSelectedListener(
                            (AdapterView.OnItemSelectedListener) receiver);
                }
            }

            OnCheckedChanged onCheckedChanged = method.getAnnotation(OnCheckedChanged.class);
            if (onCheckedChanged != null) {
                View view = null;
                if (receiver instanceof Activity) {
                    view = ((Activity) receiver).findViewById(onCheckedChanged.value());
                } else if (receiver instanceof Dialog) {
                    view = ((Dialog) receiver).findViewById(onCheckedChanged.value());
                } else if (parentView != null) {
                    view = parentView.findViewById(onCheckedChanged.value());
                }
                if (view instanceof CompoundButton &&
                        receiver instanceof CompoundButton.OnCheckedChangeListener) {
                    ((CompoundButton) view).setOnCheckedChangeListener(
                            (CompoundButton.OnCheckedChangeListener) receiver);
                }
            }
        }
    }

}
