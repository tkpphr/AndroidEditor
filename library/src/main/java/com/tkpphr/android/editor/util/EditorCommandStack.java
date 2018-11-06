package com.tkpphr.android.editor.util;

import android.util.Log;

import java.io.Serializable;
import java.util.LinkedList;


public class EditorCommandStack implements Serializable{
    private LinkedList<EditorCommand> undoStack;
    private LinkedList<EditorCommand> redoStack;

    public EditorCommandStack(){
        undoStack=new LinkedList<>();
        redoStack=new LinkedList<>();
    }

    public int getUndoCount() {
        return undoStack.size();
    }

    public int getRedoCount() {
        return redoStack.size();
    }

    public void clear(){
        undoStack.clear();
        redoStack.clear();
    }

    public void doCommand(EditorCommand command) {
        command.doCommand();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo(){
        if(undoStack.size()<1) {
            Log.d("Undo",String.valueOf(undoStack.size())+","+String.valueOf(redoStack.size()));
            return;
        }
        EditorCommand command=undoStack.pop();
        command.undo();
        redoStack.push(command);
    }

    public void redo(){
        if(redoStack.size()<1){
            Log.d("Redo",String.valueOf(undoStack.size())+","+String.valueOf(redoStack.size()));
            return;
        }
        EditorCommand command=redoStack.pop();
        command.redo();
        undoStack.push(command);
    }

    public boolean canUndo(){
        return getUndoCount()>0;
    }

    public boolean canRedo(){
        return getRedoCount()>0;
    }


}
