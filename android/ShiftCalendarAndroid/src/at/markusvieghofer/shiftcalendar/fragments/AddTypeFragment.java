package at.markusvieghofer.shiftcalendar.fragments;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.fragments.api.TimeChangeListener;
import at.markusvieghofer.shiftcalendar.fragments.api.TypeListener;
import at.markusvieghofer.shiftcalendar.models.Type;

public class AddTypeFragment extends DialogFragment implements
        TimeChangeListener, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1396680005873758806L;
    private EditText txtTypeName;
    private EditText txtFrom;
    private EditText txtTo;
    private Calendar to;
    private Calendar from;
    private boolean showTimePicker = false;

    private final OnClickListener okClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (from != null && to != null
                    & !txtTypeName.getText().toString().isEmpty()) {
                Type type = new Type();
                type.setFrom(from);
                type.setTo(to);
                type.setName(txtTypeName.getText().toString());
                parent.addType(type);
                getDialog().dismiss();
            } else {
                if (from == null) {
                    txtFrom.setError(getString(R.string.no_date_entered));
                }
                if (to == null) {
                    txtTo.setError(getString(R.string.no_date_entered));
                }
                if (txtTypeName.getText().toString().isEmpty()) {
                    txtTypeName.setError(getString(R.string.no_type_entered));
                }
            }
        }

    };

    private final OnClickListener cancleClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            getDialog().cancel();
        }
    };
    private TypeListener parent;

    public AddTypeFragment() {
    }

    @Override
    public void cancelTimeChange() {
        showTimePicker = false;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        Serializable parentSer = arguments.getSerializable(TypeFragment.KEY);
        if (parentSer instanceof TypeListener) {
            parent = (TypeListener) parentSer;
        }
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_add_type, null);
        initView(view);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view).setTitle(R.string.add_shift_type)
                .setPositiveButton(getString(R.string.ok), null)
                .setNegativeButton(getString(R.string.cancel), null).create();

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

    @Override
    public void timeChanged(String type, int hour, int minute) {
        showTimePicker = false;
        if (type.equals(FROM)) {
            createFromTime(hour, minute);
        } else if (type.equals(TO)) {
            createToTime(hour, minute);
        }
    }

    private void createFromTime(int hour, int minute) {
        from = Calendar.getInstance();
        from.set(2013, 11, 11, hour, minute);
        DateFormat timeFormat = android.text.format.DateFormat
                .getTimeFormat(getActivity().getApplicationContext());
        txtFrom.setText(timeFormat.format(from.getTime()));
    }

    private void createToTime(int hour, int minute) {
        to = Calendar.getInstance();
        to.set(2013, 11, 11, hour, minute);
        DateFormat timeFormat = android.text.format.DateFormat
                .getTimeFormat(getActivity().getApplicationContext());
        txtTo.setText(timeFormat.format(to.getTime()));
    }

    private void initView(View rootView) {
        txtTypeName = (EditText) rootView.findViewById(R.id.txtTypeName);
        txtFrom = (EditText) rootView.findViewById(R.id.txtTimeFrom);
        txtFrom.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (txtFrom.getText().toString().isEmpty()) {
                        from = Calendar.getInstance();
                    }
                    openTimePicker(from, txtFrom, FROM);
                }
            }
        });
        txtFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!showTimePicker) {
                    openTimePicker(from, txtFrom, FROM);
                }
            }
        });

        txtTo = (EditText) rootView.findViewById(R.id.txtTimeTo);
        txtTo.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (txtTo.getText().toString().isEmpty()) {
                        to = Calendar.getInstance();
                    }
                    openTimePicker(to, txtTo, TO);
                }
            }
        });
        txtTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!showTimePicker) {
                    openTimePicker(to, txtTo, TO);
                }
            }
        });
    }

    private void openTimePicker(Calendar cal, EditText editText, String type) {
        if (editText.getText().toString().isEmpty()) {
            cal = Calendar.getInstance();
        }
        DialogFragment timePicker = new TimeFragment();
        Bundle bundle = new Bundle();
        if (cal != null) {
            bundle.putSerializable(TimeFragment.CALENDAR, cal);
        }
        bundle.putSerializable(TimeFragment.TIME_CHANGE_LISTENER, this);
        showTimePicker = true;
        timePicker.setArguments(bundle);
        timePicker.show(getActivity().getSupportFragmentManager(), type);
    }
}
