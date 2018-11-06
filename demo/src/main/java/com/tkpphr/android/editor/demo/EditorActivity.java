package com.tkpphr.android.editor.demo;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tkpphr.android.editor.demo.databinding.ActivityEditorBinding;
import com.tkpphr.android.editor.util.EditorCache;
import com.tkpphr.android.editor.view.dialog.ExitEditorDialog;
import com.tkpphr.android.editor.view.dialog.LoadFileProgressDialog;
import com.tkpphr.android.editor.view.dialog.SaveFileNameInputDialog;
import com.tkpphr.android.editor.view.dialog.SaveFileProgressDialog;

import java.lang.reflect.Method;

public class EditorActivity extends AppCompatActivity implements ExitEditorDialog.OnClickListener,
                                                                    LoadFileProgressDialog.OnLoadFinishedListener,
                                                                    SaveFileProgressDialog.OnSaveFinishedListener,
                                                                    SaveFileNameInputDialog.OnNameInputFinishedListener {
    private ActivityEditorBinding binding;
    private TextDataEditor editor;
    private EditorCache editorCache;

    public static Intent createIntent(Context context){
        Intent intent=new Intent(context,EditorActivity.class);
        return intent;
    }

    public static Intent createIntent(Context context, String filePath){
        Intent intent=new Intent(context,EditorActivity.class);
        intent.putExtra("file_path",filePath);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editorCache=new EditorCache(this,getString(R.string.editor_cache_name));
        if(savedInstanceState==null) {
            String filePath=getIntent().getStringExtra("file_path");
            if(TextUtils.isEmpty(filePath)) {
                if(editorCache.isExists()){
                    editor=editorCache.load();
                }else {
                    editor=new TextDataEditor(this);
                }
            }else {
                editor=new TextDataEditor(this);
                LoadFileProgressDialog.newInstance(editor,filePath).show(getSupportFragmentManager(),null);
            }
        }else {
           editor=((TextDataEditor)savedInstanceState.getSerializable("editor"));
        }
        binding= DataBindingUtil.setContentView(this,R.layout.activity_editor);
        setSupportActionBar(binding.toolbar);
        binding.editText.setText(editor.getFileData().getText());
        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!TextUtils.equals(s.toString(),editor.getFileData().getText())) {
                    editor.doCommand(new TextEditorCommand(editor.getFileData(),s.toString()));
                    invalidateOptionsMenu();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onPause() {
        if(!isFinishing()) {
            editorCache.save(editor);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("editor", editor);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        setIconVisibility(menu,true);
        setMenuItemEnabled(menu.findItem(R.id.menu_undo), editor.canUndo());
        setMenuItemEnabled(menu.findItem(R.id.menu_redo), editor.canRedo());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuUndo=menu.findItem(R.id.menu_undo);
        if(menuUndo.isEnabled() && !editor.canUndo()){
            setMenuItemEnabled(menuUndo,false);
        }else if(!menuUndo.isEnabled() && editor.canUndo()){
            setMenuItemEnabled(menuUndo,true);
        }
        MenuItem menuRedo=menu.findItem(R.id.menu_redo);
        if(menuRedo.isEnabled() && !editor.canRedo()){
            setMenuItemEnabled(menuRedo,false);
        }else if(!menuRedo.isEnabled() && editor.canRedo()){
            setMenuItemEnabled(menuRedo,true);
        }
        setTitle(editor.getTitle());
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.menu_undo){
            editor.undo();
            binding.editText.setText(editor.getFileData().getText());
            invalidateOptionsMenu();
        }else if(item.getItemId()== R.id.menu_redo){
            editor.redo();
            binding.editText.setText(editor.getFileData().getText());
            invalidateOptionsMenu();
        }else if(item.getItemId()== R.id.menu_save){
            SaveFileNameInputDialog.newInstance(editor).show(getSupportFragmentManager(),null);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        ExitEditorDialog.newInstance(editor).show(getSupportFragmentManager(),null);
    }

    @Override
    public void onClickSave() {
        SaveFileNameInputDialog.newInstance(editor).show(getSupportFragmentManager(),null);
    }

    @Override
    public void onClickExit() {
        if(editorCache.isExists()){
            editorCache.delete();
        }
        finish();
    }

    @Override
    public void onSaveFinished(boolean isSucceed) {
        Toast.makeText(getApplicationContext(), isSucceed ? R.string.message_succeed_saving_file : R.string.message_failed_saving_file, Toast.LENGTH_SHORT).show();
        if(isSucceed){
            if(editorCache.isExists()){
                editorCache.delete();
            }
            setTitle(editor.getTitle());
            invalidateOptionsMenu();
        }

    }

    @Override
    public void onNameInputFinished(String name) {
        SaveFileProgressDialog.newInstance(editor,name).show(getSupportFragmentManager(),null);
    }

    @Override
    public void onLoadFinished(boolean isSucceed) {
        if(isSucceed) {
            binding.editText.setText(editor.getFileData().getText());
            setTitle(editor.getTitle());
            invalidateOptionsMenu();
        }else {
            Toast.makeText(this,"Error loading file",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setIconVisibility(Menu menu,boolean visible){
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, visible);
                } catch (Exception e) {
                    Log.e("Error", "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
    }

    private void setMenuItemEnabled(MenuItem menuItem, boolean enabled){
        menuItem.setEnabled(enabled);
        Drawable menuIcon=menuItem.getIcon();
        if(menuIcon!=null){
            menuIcon.mutate().setColorFilter(enabled ? Color.WHITE:Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }
    }



}

