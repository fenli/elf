package com.fenlisproject.elf.core.handler;

import com.fenlisproject.elf.core.annotation.OnClick;
import com.fenlisproject.elf.core.annotation.OnItemClick;
import com.fenlisproject.elf.core.annotation.Tag;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseTaskExecutor {

    public static void executeOnClickListener(Object receiver, int id) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                try {
                    if (onClick.value() == id) {
                        method.invoke(receiver);
                        return;
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void executeOnItemClickListener(Object receiver, int id, int position) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            OnItemClick onItemClick = method.getAnnotation(OnItemClick.class);
            if (onItemClick != null) {
                try {
                    if (onItemClick.value() == id) {
                        method.invoke(receiver, position);
                        return;
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object executeMethodByTag(final Object receiver, String tag, Object... args) {
        Method[] methods = receiver.getClass().getDeclaredMethods();
        for (final Method method : methods) {
            method.setAccessible(true);
            Tag methodAnnotation = method.getAnnotation(Tag.class);
            if (methodAnnotation != null) {
                if (methodAnnotation.value().equalsIgnoreCase(tag)) {
                    try {
                        return method.invoke(receiver, args);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
