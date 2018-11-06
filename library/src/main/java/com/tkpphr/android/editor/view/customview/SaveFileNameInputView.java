package com.tkpphr.android.editor.view.customview;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.tkpphr.android.editor.R;
import com.tkpphr.android.editor.util.Editor;

public class SaveFileNameInputView extends TextInputLayout{
	private Editor<?> editor;
	private TextView hint;
	private TextInputLayout textInputLayout;
	private TextInputEditText textInputEditText;
	private boolean isInputErrorEnabled;
	private OnNameChangedListener onNameChangedListener;
	private OnInputErrorChangedListener onInputErrorChangedListener;

	public SaveFileNameInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.edtr_save_file_input,this,true);
		if(isInEditMode()){
			return;
		}
		hint=findViewById(R.id.edtr_file_name_hint);
		textInputLayout=findViewById(R.id.edtr_text_input_layout);
		textInputEditText=findViewById(R.id.edtr_text_input_edit_text);
		textInputEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if(onNameChangedListener!=null){
					onNameChangedListener.onNameChanged(charSequence.toString());
				}
				checkFileName();
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Bundle outState=new Bundle();
		outState.putParcelable("super_state",super.onSaveInstanceState());
		outState.putSerializable("editor",editor);
		outState.putString("text",textInputEditText.getText().toString());
		return outState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if(state instanceof Bundle){
			Bundle savedState=(Bundle)state;
			state=savedState.getParcelable("super_state");
			editor=(Editor<?>)savedState.getSerializable("editor");
			textInputEditText.setText(savedState.getString("text"));
			this.isInputErrorEnabled=checkInputError();
			textInputLayout.setErrorEnabled(this.isInputErrorEnabled);
		}
		super.onRestoreInstanceState(state);
	}

	public void setEditor(Editor<?> editor) {
		this.editor = editor;
		textInputEditText.setText(editor.getFileData().getFileName());
		isInputErrorEnabled=checkInputError();
		checkFileName();
	}

	public String getText(){
		return textInputEditText.getText().toString();
	}

	public void setOnNameChangedListener(OnNameChangedListener onNameChangedListener) {
		this.onNameChangedListener = onNameChangedListener;
	}

	public void setOnInputErrorChangedListener(OnInputErrorChangedListener onInputErrorChangedListener) {
		this.onInputErrorChangedListener = onInputErrorChangedListener;
	}

	public boolean isInputErrorEnabled(){
		return this.isInputErrorEnabled;
	}

	private void checkFileName(){
		if(editor==null){
			return;
		}
		boolean isInputErrorEnabled;
		String fileName=textInputEditText.getText().toString();
		if(checkInputError()){
			isInputErrorEnabled=true;
			//textInputLayout.setHint("");
			hint.setVisibility(GONE);
			textInputLayout.setError(getContext().getString(R.string.edtr_error_empty_save_name));
		}else {
			isInputErrorEnabled=false;
			if(editor.isExists(fileName)){
				//textInputLayout.setHint(getContext().getString(R.string.edtr_hint_save));
				hint.setText(getContext().getString(R.string.edtr_hint_save));
			}else {
				//textInputLayout.setHint(getContext().getString(R.string.edtr_hint_save_as));
				hint.setText(getContext().getString(R.string.edtr_hint_save_as));
			}
			hint.setVisibility(VISIBLE);
			textInputLayout.setError("");
		}
		if(this.isInputErrorEnabled!=isInputErrorEnabled){
			if(onInputErrorChangedListener!=null){
				onInputErrorChangedListener.onInputErrorChanged(isInputErrorEnabled);
			}
			this.isInputErrorEnabled=isInputErrorEnabled;
		}
		textInputLayout.setErrorEnabled(isInputErrorEnabled);
	}

	private boolean checkInputError(){
		return TextUtils.isEmpty(textInputEditText.getText());
	}

	public interface OnNameChangedListener{
		void onNameChanged(String name);
	}

	public interface OnInputErrorChangedListener{
		void onInputErrorChanged(boolean isInputErrorEnabled);
	}

}
