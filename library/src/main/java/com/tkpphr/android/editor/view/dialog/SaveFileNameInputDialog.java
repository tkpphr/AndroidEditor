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
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;

import com.tkpphr.android.editor.R;
import com.tkpphr.android.editor.util.Editor;
import com.tkpphr.android.editor.view.customview.SaveFileNameInputView;

public class SaveFileNameInputDialog extends AppCompatDialogFragment{
    private SaveFileNameInputView saveFileInputView;
    private OnNameInputFinishedListener onNameInputFinishedListener;

    public static SaveFileNameInputDialog newInstance(Editor<?> fileDataEditor){
        SaveFileNameInputDialog instance=new SaveFileNameInputDialog();
        Bundle args=new Bundle();
        args.putSerializable("editor",fileDataEditor);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNameInputFinishedListener) {
            onNameInputFinishedListener = (OnNameInputFinishedListener) context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (activity instanceof OnNameInputFinishedListener) {
                onNameInputFinishedListener = (OnNameInputFinishedListener) activity;
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getTargetFragment() instanceof OnNameInputFinishedListener){
            onNameInputFinishedListener =(OnNameInputFinishedListener)getTargetFragment();
        }else if(getParentFragment() instanceof OnNameInputFinishedListener){
            onNameInputFinishedListener =(OnNameInputFinishedListener)getParentFragment();
        }
        final Editor<?> editor=(Editor<?>) getArguments().getSerializable("editor");
        if(editor==null){
            setShowsDialog(false);
            dismiss();
            return new AlertDialog.Builder(getContext()).create();
        }
        View view=View.inflate(getContext(),R.layout.edtr_dialog_save_file_name_input,null);
        saveFileInputView=(SaveFileNameInputView)view;
        if(savedInstanceState==null) {
            saveFileInputView.setEditor(editor);
        }
        saveFileInputView.setOnInputErrorChangedListener(new SaveFileNameInputView.OnInputErrorChangedListener() {
            @Override
            public void onInputErrorChanged(boolean isInputErrorEnabled) {
                ((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(!isInputErrorEnabled);
            }
        });
        return new AlertDialog.Builder(getContext())
                    .setTitle(R.string.edtr_save_to)
                    .setMessage(R.string.edtr_hint_enter_save_name)
                    .setView(view)
                    .setPositiveButton(R.string.edtr_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(onNameInputFinishedListener !=null){
                                onNameInputFinishedListener.onNameInputFinished(saveFileInputView.getText());
                            }
                        }
                    })
                    .setNegativeButton(R.string.edtr_cancel,null)
                    .create();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(saveFileInputView!=null && getDialog()!=null){
            ((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(!saveFileInputView.isInputErrorEnabled());
        }
    }

    public interface OnNameInputFinishedListener {
        void onNameInputFinished(String name);
    }
}
