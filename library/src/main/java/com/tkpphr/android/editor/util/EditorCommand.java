package com.tkpphr.android.editor.util;

import java.io.Serializable;

public abstract class EditorCommand implements Serializable{
    public EditorCommand(){
    }

    public abstract void doCommand();
    public abstract void undo();
    public abstract void redo();

}
