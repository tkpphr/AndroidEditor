package com.tkpphr.android.editor.util;

import android.text.TextUtils;

import java.io.File;
import java.io.Serializable;


public abstract class FileData implements Serializable{
    private String filePath;

    public FileData() {
        filePath="";
    }

    public FileData(String filePath){
        this.filePath =filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFullFileName(){
        if(TextUtils.isEmpty(filePath)) {
            return "";
        }
        return new File(filePath).getName();
    }

    public String getFileName(){
        String fullFileName=getFullFileName();
        int index=fullFileName.lastIndexOf(".");
        if(index!=-1){
            return fullFileName.substring(0,index);
        }
        return fullFileName;
    }

    public String getFileExtension(){
        String fullFileName=getFullFileName();
        int index=fullFileName.lastIndexOf(".");
        if(index!=-1){
            return fullFileName.substring(index,fullFileName.length());
        }
        return fullFileName;
    }
}
