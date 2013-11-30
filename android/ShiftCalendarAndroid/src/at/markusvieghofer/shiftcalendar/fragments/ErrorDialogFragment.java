package at.markusvieghofer.shiftcalendar.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import at.markusvieghofer.shiftcalendar.R;

public class ErrorDialogFragment extends DialogFragment {
	public static final String KEY = ErrorDialogFragment.class.getName()
			+ "MESSAGE";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		String message = arguments.getString(KEY);

		AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.setMessage(message).setTitle(R.string.error)
				.setPositiveButton(getString(R.string.ok), null)
				.setNegativeButton(getString(R.string.cancel), null).create();

		return dialog;
	}
}
