package com.tkpphr.android.editor.demo;

import com.tkpphr.android.editor.util.EditorCommand;

import java.io.Serializable;

public class TextEditorCommand extends EditorCommand implements Serializable{
    private final TextData textData;
    private final String oldText;
    private final String newText;

    public TextEditorCommand(TextData textData, String newText) {
        this.textData = textData;
        this.oldText = textData.getText();
        this.newText = newText;
    }

    @Override
    public void doCommand() {
        textData.setText(newText);
    }

    @Override
    public void undo() {
        textData.setText(oldText);
    }

    @Override
    public void redo() {
        doCommand();
    }
}
