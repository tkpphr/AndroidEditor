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
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


public class EditorCache {
    private Context context;
    private String cacheName;

    public EditorCache(Context context, String cacheName) {
        this.context = context;
        this.cacheName = cacheName;
    }

    public <T extends Editor<?>> void save(T editor){
        FileOutputStream fileOutputStream=null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream=context.openFileOutput(cacheName,Context.MODE_PRIVATE);
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(editor);
            objectOutputStream.flush();
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            closeOutputStream(fileOutputStream);
            closeOutputStream(byteArrayOutputStream);
            closeOutputStream(objectOutputStream);
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends Editor<?>> T load() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(cacheName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (T)objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeInputStream(fileInputStream);
        }
    }

    public boolean delete(){
        return context.deleteFile(cacheName);
    }

    public boolean isExists(){
        for (String file : context.fileList()){
            if(new File(file).getName().toLowerCase().equals(cacheName)){
                return true;
            }
        }
        return false;
    }

    private void closeInputStream(InputStream inputStream){
        if(inputStream!=null){
            try {
                inputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void closeOutputStream(OutputStream outputStream){
        if(outputStream!=null){
            try {
                outputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
