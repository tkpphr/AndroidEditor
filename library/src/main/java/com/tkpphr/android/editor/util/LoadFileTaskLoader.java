package com.tkpphr.android.editor.util;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;

public class LoadFileTaskLoader extends AsyncTaskLoader<Boolean> {
	private Boolean result;
	private Editor<?> editor;
	private String filePath;
	private boolean isStarted;

	public LoadFileTaskLoader(Context context, Editor<?> editor, String filePath) {
		super(context);
		this.editor = editor;
		this.filePath = filePath;
		this.isStarted=false;
	}

	@Override
	public Boolean loadInBackground() {
		try {
			editor.loadFile(filePath);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		if(result!=null){
			deliverResult(result);
			return;
		}
		if(!isStarted || takeContentChanged()){
			forceLoad();
		}
	}

	@Override
	public void deliverResult(Boolean result) {
		this.result =result;
		super.deliverResult(result);
	}

	@Override
	public void forceLoad() {
		super.forceLoad();
		this.isStarted=true;
	}


}
