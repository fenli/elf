package com.fenlisproject.elf.core.base;

public interface BaseTask {

    public void runDelayed(final String tag, long delayMilis, final Object... args);

    public void runOnUiThread(final String tag, final Object... args);

}
