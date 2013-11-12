package at.markusvieghofer.shiftcalendar.fragments;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.db.daos.TypeDAO;
import at.markusvieghofer.shiftcalendar.fragments.api.TimeChangeListener;
import at.markusvieghofer.shiftcalendar.models.Type;

public class TypeFragment extends ListFragment implements TimeChangeListener,
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ARG_SECTION_NUMBER = "section_number";
	private EditText txtTypeName;
	private EditText txtFrom;
	private EditText txtTo;
	private Calendar from;
	private Calendar to;
	private List<Type> types = new ArrayList<Type>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_type, container,
				false);
		readAllTypes();
		initView(rootView);
		return rootView;
	}

	@Override
	public void timeChanged(String type, int hour, int minute) {
		if (type.equals(FROM)) {
			createFromTime(hour, minute);
		} else if (type.equals(TO)) {
			createToTime(hour, minute);
		}
	}

	protected Type createTypeModel() {
		Type type = new Type();
		type.setName(txtTypeName.getText().toString());
		type.setFrom(from);
		type.setTo(to);
		return type;
	}

	private void addType(final View contNewType, final Button btnAddShiftType) {
		Type type = createTypeModel();
		TypeDAO typeDAO = new TypeDAO(getActivity().getApplicationContext());
		type.setId(typeDAO.save(type));
		types.add(type);
		contNewType.setVisibility(View.GONE);
		btnAddShiftType.setText(getString(R.string.new_shift_type));
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
		final View contNewType = rootView.findViewById(R.id.contNewType);

		txtTypeName = (EditText) rootView.findViewById(R.id.txtTypeName);
		txtFrom = (EditText) rootView.findViewById(R.id.txtTimeFrom);
		txtFrom.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (txtFrom.getText().toString().isEmpty()) {
						from = Calendar.getInstance();
					}
					openTimePicker(from, FROM);
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
					openTimePicker(to, TO);
				}
			}
		});

		final Button btnAddShiftType = (Button) rootView
				.findViewById(R.id.btnAddShiftType);
		btnAddShiftType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (contNewType.getVisibility() == View.GONE) {
					contNewType.setVisibility(View.VISIBLE);
					btnAddShiftType.setText(getString(R.string.add_shift_type));
				} else {
					addType(contNewType, btnAddShiftType);
				}
			}
		});

		setListAdapter(new ArrayAdapter<Type>(getActivity(),
				android.R.layout.simple_list_item_single_choice, types));
	}

	private void openTimePicker(Calendar cal, String type) {
		DialogFragment timePicker = new TimeFragment();
		Bundle bundle = new Bundle();
		if (cal != null) {
			bundle.putSerializable(TimeFragment.CALENDAR, cal);
		}
		bundle.putSerializable(TimeFragment.TIME_CHANGE_LISTENER, this);
		timePicker.setArguments(bundle);
		timePicker.show(getActivity().getSupportFragmentManager(), type);
	}

	@SuppressWarnings("unchecked")
	private void readAllTypes() {
		TypeDAO typeDAO = new TypeDAO(getActivity().getApplicationContext());
		types = (List<Type>) typeDAO.readAll();
	}
}
