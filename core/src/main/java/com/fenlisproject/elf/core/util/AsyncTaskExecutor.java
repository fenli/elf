package com.fenlisproject.elf.core.util;

import android.os.AsyncTask;
import android.os.Build;

public class AsyncTaskExecutor {

    private AsyncTaskExecutor() {
    }

    public static <Params> void execute(AsyncTask task, Params... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    public static void execute(AsyncTask task) {
        execute(task, null);
    }
}