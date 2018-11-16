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
