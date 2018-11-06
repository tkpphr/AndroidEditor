package com.tkpphr.android.editor.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;

import com.tkpphr.android.editor.R;
import com.tkpphr.android.editor.util.Editor;
import com.tkpphr.android.editor.util.LoadFileTaskLoader;

import java.io.File;

public class LoadFileProgressDialog extends AppCompatDialogFragment implements LoaderManager.LoaderCallbacks<Boolean> {
	private OnLoadFinishedListener onLoadFinishedListener;

	public static LoadFileProgressDialog newInstance(Editor<?> editor, String filePath){
		LoadFileProgressDialog instance=new LoadFileProgressDialog();
		Bundle arguments=new Bundle();
		arguments.putSerializable("editor",editor);
		arguments.putString("file_path",filePath);
		instance.setArguments(arguments);
		return instance;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnLoadFinishedListener) {
			onLoadFinishedListener = (OnLoadFinishedListener) context;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
			if (activity instanceof OnLoadFinishedListener) {
				onLoadFinishedListener = (OnLoadFinishedListener) activity;
			}
		}
	}


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLoaderManager().initLoader(getResources().getInteger(R.integer.edtr_load_progress_loader_id),null,this);
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if(getTargetFragment() instanceof OnLoadFinishedListener){
			onLoadFinishedListener =(OnLoadFinishedListener)getTargetFragment();
		}else if(getParentFragment() instanceof OnLoadFinishedListener){
			onLoadFinishedListener =(OnLoadFinishedListener)getParentFragment();
		}
		AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
		setCancelable(false);
		File file=new File(getArguments().getString("file_path"));
		String fileName=file.getName();
		int index=file.getName().lastIndexOf(".");
		if(index!=-1){
			fileName=fileName.substring(0,index);
		}
		return dialog.setTitle(fileName)
				.setView(View.inflate(getContext(),R.layout.edtr_load_progress,null))
				.create();
	}

	@Override
	public Loader<Boolean> onCreateLoader(int id, Bundle args) {
		Bundle arguments=getArguments();
		Editor<?> editor=(Editor<?>) arguments.getSerializable("editor");
		String filePath=arguments.getString("file_path");
		return new LoadFileTaskLoader(getContext(),editor,filePath);
	}

	@Override
	public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
		getLoaderManager().destroyLoader(loader.getId());
		onDismiss(getDialog());
		if(onLoadFinishedListener !=null){
			onLoadFinishedListener.onLoadFinished(data);
		}
	}

	@Override
	public void onLoaderReset(Loader<Boolean> loader) {

	}

	public interface OnLoadFinishedListener {
		void onLoadFinished(boolean isSucceed);
	}
}
