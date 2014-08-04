package com.mHealthDroid.mhealthapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;

public class ProgressDialogFragment extends DialogFragment {
	 
	public static ProgressDialogFragment newInstance() {
		ProgressDialogFragment frag = new ProgressDialogFragment ();
		return frag;
	}
 
@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	 
		final ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setMessage("probando esta mierda");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
	 
		// Disable the back button
		DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
	
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if( keyCode == KeyEvent.KEYCODE_BACK){	
					return true;
				}
				return false;
			}
		 
		};
		dialog.setOnKeyListener(keyListener);
		return dialog;
	}
 
}