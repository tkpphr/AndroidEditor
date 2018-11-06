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
