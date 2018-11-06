package com.tkpphr.android.editor.demo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tkpphr.android.editor.demo.databinding.ActivityMainBinding;
import com.tkpphr.android.editor.util.EditorCache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FileListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        adapter=new FileListAdapter(this);
        setSupportActionBar(binding.toolbar);
        binding.buttonCreateNew.setOnClickListener(v->{
            startActivity(EditorActivity.createIntent(this));
        });
        binding.listViewFile.setAdapter(adapter);
        binding.listViewFile.setOnItemClickListener((parent, view, position, id) -> {
            startActivity(EditorActivity.createIntent(this,adapter.getItem(position)));
        });
        EditorCache editorCache=new EditorCache(this,getString(R.string.editor_cache_name));
        if(editorCache.isExists()){
            startActivity(EditorActivity.createIntent(this));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.refresh();
    }

    private static class FileListAdapter extends BaseAdapter{
        private Context context;
        private List<String> filePaths;

        public FileListAdapter(Context context) {
            this.context = context;
            this.filePaths =new ArrayList<>();
        }

        @Override
        public int getCount() {
            return filePaths.size();
        }

        @Override
        public String getItem(int position) {
            return filePaths.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView= LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,parent,false);
            }
            ((TextView)convertView.findViewById(android.R.id.text1)).setText(getItem(position));
            return convertView;
        }

        public void refresh(){
            filePaths = new ArrayList<>();
            for(File file : context.getFilesDir().listFiles()){
                filePaths.add(file.getAbsolutePath());
            }
            notifyDataSetChanged();
        }
    }
}