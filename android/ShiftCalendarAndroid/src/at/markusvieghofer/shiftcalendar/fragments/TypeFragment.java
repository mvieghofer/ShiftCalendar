package at.markusvieghofer.shiftcalendar.fragments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.db.daos.TypeDAO;
import at.markusvieghofer.shiftcalendar.models.Type;

public class TypeFragment extends ListFragment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String TAG = TypeFragment.class.getSimpleName();
	protected static final String KEY = TypeFragment.class.getSimpleName();
	private EditText txtTypeName;
	private EditText txtFrom;
	private EditText txtTo;
	private Calendar from;
	private Calendar to;
	private List<Type> types = new ArrayList<Type>();

	public void addType(Type type) {
		saveType(type);
		types.add(type);
		setListAdapter(new ArrayAdapter<Type>(getActivity(),
				android.R.layout.simple_list_item_single_choice, types));
	}

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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		initView(view);
		super.onViewCreated(view, savedInstanceState);
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

	private void initView(View rootView) {

		final Button btnAddShiftType = (Button) rootView
				.findViewById(R.id.btnAddShiftType);
		btnAddShiftType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AddTypeFragment frag = new AddTypeFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(KEY, TypeFragment.this);
				frag.setArguments(bundle);
				frag.setCancelable(true);
				frag.show(getActivity().getSupportFragmentManager(), TAG);
			}
		});

		setListAdapter(new ArrayAdapter<Type>(getActivity(),
				android.R.layout.simple_list_item_single_choice, types));
	}

	@SuppressWarnings("unchecked")
	private void readAllTypes() {
		TypeDAO typeDAO = new TypeDAO(getActivity().getApplicationContext());
		types = (List<Type>) typeDAO.readAll();
	}

	private void saveType(Type type) {
		TypeDAO typeDAO = new TypeDAO(getActivity().getApplicationContext());
		typeDAO.save(type);
	}
}
