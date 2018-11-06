package com.tkpphr.android.editor.demo;

import com.tkpphr.android.editor.util.FileData;

public class TextData extends FileData {
    private String text;

    public TextData() {
        super("Untitled");
        text="";
    }

    public TextData(String filePath,String text) {
        super(filePath);
        this.text=text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
