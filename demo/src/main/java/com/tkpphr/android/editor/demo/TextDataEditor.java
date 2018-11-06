package com.tkpphr.android.editor.demo;

import android.content.Context;

import com.tkpphr.android.editor.util.Editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TextDataEditor extends Editor<TextData> {
    public TextDataEditor(Context context) {
        super(new TextData(), context.getFilesDir().getAbsolutePath(), ".txt",true);
    }

    public TextDataEditor(Context context,String filePath) throws IOException {
        super(filePath, context.getFilesDir().getAbsolutePath(), ".txt",true);
    }

    @Override
    protected TextData load(String filePath) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FileChannel fileChannel=null;
        try {
            fileChannel=new FileInputStream(filePath).getChannel();
            ByteBuffer buffer=ByteBuffer.allocate((int)fileChannel.size());
            fileChannel.read(buffer);
            return new TextData(filePath,new String(buffer.array()));
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }finally {
            if(fileChannel!=null){
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected boolean save(String savePath) {
        String tempPath=savePath+"-temp";
        if(saveAs(tempPath)){
            File srcFile=new File(tempPath);
            File dstFile=new File(savePath);
            if(srcFile.exists() && !srcFile.getAbsolutePath().equals(dstFile.getAbsolutePath())){
                if(dstFile.exists()){
                    return dstFile.delete() && srcFile.renameTo(dstFile);
                }else {
                    return srcFile.renameTo(dstFile);
                }
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    @Override
    protected boolean saveAs(String savePath) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FileOutputStream fileOutputStream=null;
        try {
            File file=new File(savePath);
            fileOutputStream=new FileOutputStream(file);
            fileOutputStream.write(getFileData().getText().getBytes());
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
