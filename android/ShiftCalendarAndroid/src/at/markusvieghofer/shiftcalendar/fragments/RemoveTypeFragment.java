package at.markusvieghofer.shiftcalendar.fragments;

import java.io.Serializable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.fragments.api.TypeListener;
import at.markusvieghofer.shiftcalendar.models.Type;

public class RemoveTypeFragment extends DialogFragment {
	public static final String TAG = RemoveTypeFragment.class.getName();
	private OnClickListener cancleClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			getDialog().cancel();
		}
	};
	private OnClickListener okClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			typeListener.removeType(type);
			getDialog().dismiss();
		}
	};

	private Type type;
	private TypeListener typeListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		Serializable typeSer = bundle.getSerializable(Type.KEY);
		Serializable typeListenerSer = bundle.getSerializable(TypeFragment.KEY);
		String warningStr = getString(R.string.remove_type);
		if (typeSer instanceof Type) {
			type = (Type) typeSer;
			warningStr = warningStr.replace("%1", type.getName());
		}

		if (typeListenerSer instanceof TypeListener) {
			typeListener = (TypeListener) typeListenerSer;
		}

		LayoutInflater inflator = LayoutInflater.from(getActivity());
		View view = inflator
				.inflate(R.layout.fragment_remove_type, null, false);
		TextView lblRemoveType = (TextView) view
				.findViewById(R.id.lblRemoveType);
		lblRemoveType.setText(warningStr);

		AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.setView(view).setPositiveButton(R.string.ok, null)
				.setNegativeButton(R.string.cancel, null).create();
		return dialog;
	}

	@Override
	public void onStart() {
		super.onStart();
		((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE)
				.setOnClickListener(cancleClickListener);
		((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE)
				.setOnClickListener(okClickListener);
	}
}
