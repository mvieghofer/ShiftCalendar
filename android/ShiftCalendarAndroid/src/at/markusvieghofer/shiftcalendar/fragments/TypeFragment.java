package at.markusvieghofer.shiftcalendar.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.activities.MainActivity;
import at.markusvieghofer.shiftcalendar.adapter.TypeAdapter;
import at.markusvieghofer.shiftcalendar.db.daos.TypeDAO;
import at.markusvieghofer.shiftcalendar.fragments.api.TypeListener;
import at.markusvieghofer.shiftcalendar.models.Type;

public class TypeFragment extends ListFragment implements TypeListener {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String TAG = TypeFragment.class.getSimpleName();
    protected static final String KEY = TypeFragment.class.getSimpleName();
    private TypeAdapter typeAdapter;

    @Override
    public void addType(Type type) {
        saveType(type);
        getTypeAdapter().add(type);
        setListAdapter(getTypeAdapter());
    }

    @Override
    public String getKey() {
        return KEY;
    }

    public TypeAdapter getTypeAdapter() {
        return typeAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_type, container,
                false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                setType(typeAdapter.getItem(position));
            }
        });

        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Type type = typeAdapter.getItem(position);
                openDeleteTypeDialog(type);
                return true;
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void removeType(Type type) {
        TypeDAO typeDAO = new TypeDAO(getActivity());
        typeDAO.delete(type);
        getTypeAdapter().remove(type);
    }

    @Override
    public void setType(Type type) {
        FragmentActivity activity = getActivity();
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.setType(type);
        }
    }

    public void setTypeAdapter(TypeAdapter typeAdapter) {
        this.typeAdapter = typeAdapter;
    }

    protected void openDeleteTypeDialog(Type type) {
        RemoveTypeFragment removeTypeFrag = new RemoveTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Type.KEY, type);
        bundle.putSerializable(getKey(), this);
        removeTypeFrag.setArguments(bundle);
        removeTypeFrag.show(getFragmentManager(), RemoveTypeFragment.TAG);
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
        setTypeAdapter(new TypeAdapter(getActivity(),
                android.R.layout.simple_list_item_single_choice, readAllTypes()));
        setListAdapter(getTypeAdapter());
    }

    @SuppressWarnings("unchecked")
    private List<Type> readAllTypes() {
        TypeDAO typeDAO = new TypeDAO(getActivity().getApplicationContext());
        return (List<Type>) typeDAO.readAll();
    }

    private void saveType(Type type) {
        TypeDAO typeDAO = new TypeDAO(getActivity().getApplicationContext());
        typeDAO.save(type);
    }
}
