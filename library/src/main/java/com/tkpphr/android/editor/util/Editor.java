/*
   Copyright 2018 tkpphr

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.tkpphr.android.editor.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;


public abstract class Editor<T extends FileData> implements Serializable{
    private T fileData;
    private String saveDirectoryPath;
    private String fileExtension;
    private EditorCommandStack editorCommandStack;
    private boolean undoAfterSave;
    private int dataChangedPoint;

    public Editor(T fileData, String saveDirectoryPath, String fileExtension,boolean undoAfterSave) {
        this(saveDirectoryPath,fileExtension, undoAfterSave);
        this.fileData = fileData;
    }

    public Editor(String filePath, String saveDirectoryPath, String fileExtension,boolean undoAfterSave) throws IOException{
        this(saveDirectoryPath,fileExtension, undoAfterSave);
        this.fileData = load(filePath);
    }

    private Editor(@NonNull String saveDirectoryPath, @NonNull String fileExtension,boolean undoAfterSave) {
        if(!TextUtils.isEmpty(saveDirectoryPath) && saveDirectoryPath.charAt(saveDirectoryPath.length()-1)=='/') {
            this.saveDirectoryPath = saveDirectoryPath;
        }else {
            this.saveDirectoryPath = saveDirectoryPath +'/';
        }
        if(!TextUtils.isEmpty(saveDirectoryPath) && fileExtension.charAt(0)=='.'){
            this.fileExtension=fileExtension;
        }else {
            this.fileExtension='.'+fileExtension;
        }
        this.fileExtension=fileExtension;
        this.editorCommandStack=new EditorCommandStack();
        this.dataChangedPoint=0;
        this.undoAfterSave = undoAfterSave;
    }

    public T getFileData() {
        return fileData;
    }

    public boolean isExists(String fileName){
        if(TextUtils.isEmpty(fileName)){
            return false;
        }
        return new File(saveDirectoryPath+fileName+fileExtension).exists();
    }

    public String getTitle(){
        return (isDataChanged() ? "*" : "")+getFileData().getFileName();
    }

    public boolean canUndo(){
        return editorCommandStack.canUndo();
    }

    public boolean canRedo(){
        return editorCommandStack.canRedo();
    }

    public void doCommand(EditorCommand editorCommand) {
        editorCommandStack.doCommand(editorCommand);
    }

    public void undo(){
        editorCommandStack.undo();
    }

    public void redo(){
        editorCommandStack.redo();
    }

    public boolean isDataChanged(){
        return editorCommandStack.getUndoCount()!=dataChangedPoint;
    }

    public void setFileData(@NonNull T fileData){
        this.fileData = fileData;
        editorCommandStack.clear();
        dataChangedPoint = 0;
    }

    public void saveFile(String fileName) throws IOException{
        if(fileData==null) {
            throw new IOException("fileData is null.");
        }else {
            boolean isSucceed;
            String filePath= saveDirectoryPath +fileName+fileExtension;
            if(new File(filePath).exists()){
                isSucceed=save(filePath);
            }else {
                File directory=new File(saveDirectoryPath);
                if(!directory.exists()){
                    if(!directory.mkdirs()){
                        throw new IOException("Can't make save-directory.");
                    }
                }
                isSucceed=saveAs(filePath);
            }

            if(isSucceed){
                getFileData().setFilePath(filePath);
                if(!undoAfterSave) {
                    editorCommandStack.clear();
                }
                dataChangedPoint=editorCommandStack.getUndoCount();
            }
        }
    }

    public void loadFile(String filePath) throws IOException{
        T fileData=load(filePath);
        if(fileData==null){
            throw new IOException("fileData is null.");
        }else {
            setFileData(fileData);
        }
    }

    protected abstract T load(String filePath);
    protected abstract boolean save(String savePath);
    protected abstract boolean saveAs(String savePath);
}
