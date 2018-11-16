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
import com.tkpphr.android.editor.util.SaveFileTaskLoader;

public class SaveFileProgressDialog extends AppCompatDialogFragment implements LoaderManager.LoaderCallbacks<Boolean>{
	private OnSaveFinishedListener onSaveFinishedListener;

	public static SaveFileProgressDialog newInstance(Editor<?> editor, String fileName){
		SaveFileProgressDialog instance=new SaveFileProgressDialog();
		Bundle arguments=new Bundle();
		arguments.putSerializable("editor",editor);
		arguments.putString("file_name",fileName);
		instance.setArguments(arguments);
		return instance;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnSaveFinishedListener) {
			onSaveFinishedListener = (OnSaveFinishedListener) context;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
			if (activity instanceof OnSaveFinishedListener) {
				onSaveFinishedListener = (OnSaveFinishedListener) activity;
			}
		}
	}


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLoaderManager().initLoader(getResources().getInteger(R.integer.edtr_save_progress_loader_id),null,this);
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if(getTargetFragment() instanceof OnSaveFinishedListener){
			onSaveFinishedListener=(OnSaveFinishedListener)getTargetFragment();
		}else if(getParentFragment() instanceof OnSaveFinishedListener){
			onSaveFinishedListener=(OnSaveFinishedListener)getParentFragment();
		}
		AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
		setCancelable(false);
		return dialog.setTitle(getArguments().getString("file_name"))
				.setView(View.inflate(getContext(),R.layout.edtr_save_progress,null))
				.create();
	}

	@Override
	public Loader<Boolean> onCreateLoader(int id, Bundle args) {
		Bundle arguments=getArguments();
		Editor<?> editor=(Editor<?>) arguments.getSerializable("editor");
		String fileName=arguments.getString("file_name");

		return new SaveFileTaskLoader(getContext(),editor,fileName);
	}

	@Override
	public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
		getLoaderManager().destroyLoader(loader.getId());
		onDismiss(getDialog());
		if(onSaveFinishedListener!=null){
			onSaveFinishedListener.onSaveFinished(data);
		}
	}

	@Override
	public void onLoaderReset(Loader<Boolean> loader) {

	}

	public interface OnSaveFinishedListener {
		void onSaveFinished(boolean isSucceed);
	}
}