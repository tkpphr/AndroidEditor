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

import com.tkpphr.android.editor.R;
import com.tkpphr.android.editor.util.Editor;

public class ExitEditorDialog extends AppCompatDialogFragment{
    private OnClickListener onClickListener;

    public static ExitEditorDialog newInstance(Editor<?> editor){
        ExitEditorDialog instance=new ExitEditorDialog();
        Bundle args=new Bundle();
        args.putSerializable("editor",editor);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClickListener) {
            onClickListener = (OnClickListener) context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (activity instanceof OnClickListener) {
                onClickListener = (OnClickListener) activity;
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getTargetFragment() instanceof OnClickListener){
            onClickListener =(OnClickListener)getTargetFragment();
        }else if(getParentFragment() instanceof OnClickListener){
            onClickListener =(OnClickListener)getParentFragment();
        }

        final Editor<?> editor=(Editor<?>)getArguments().getSerializable("editor");
        AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
        if(editor.isDataChanged()){
            dialog.setMessage(R.string.edtr_confirm_save_and_exit)
                    .setPositiveButton(R.string.edtr_save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(onClickListener !=null){
                                onClickListener.onClickSave();
                            }
                        }
                    })
                    .setNeutralButton(R.string.edtr_cancel,null)
                    .setNegativeButton(R.string.edtr_dont_save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(onClickListener !=null){
                                onClickListener.onClickExit();
                            }
                        }
                    });
        }else {
            dialog.setMessage(R.string.edtr_confirm_exit)
                    .setPositiveButton(R.string.edtr_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(onClickListener !=null){
                                onClickListener.onClickExit();
                            }
                        }
                    })
                    .setNegativeButton(R.string.edtr_no,null);
        }
        return dialog.create();
    }

    public interface OnClickListener {
        void onClickSave();
        void onClickExit();
    }
}
