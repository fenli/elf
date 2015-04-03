package com.fenlisproject.elf.core.widget;

public interface FontFace {

    String FORMAT_TTF = "ttf";
    String FORMAT_OTF = "otf";

    void setFontFace(String fontName);

    void setFontFace(String fontName, String fontFormat);

}
