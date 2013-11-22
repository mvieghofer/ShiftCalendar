package at.markusvieghofer.shiftcalendar.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import at.markusvieghofer.shiftcalendar.fragments.RemoveTypeFragment;
import at.markusvieghofer.shiftcalendar.fragments.api.TypeListener;
import at.markusvieghofer.shiftcalendar.models.Type;

public class TypeAdapter extends ArrayAdapter<Type> {

	private FragmentManager fragmentManager;
	private TypeListener typeListener;

	public TypeAdapter(Context context, int resource, List<Type> types,
			FragmentManager fragmentManager, TypeListener typeListener) {
		super(context, resource, types);
		this.fragmentManager = fragmentManager;
		this.typeListener = typeListener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		view.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Type type = getItem(position);
				openDeleteTypeDialog(type);
				return true;
			}
		});
		return view;
	}

	protected void openDeleteTypeDialog(Type type) {
		RemoveTypeFragment removeTypeFrag = new RemoveTypeFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(Type.KEY, type);
		bundle.putSerializable(typeListener.getKey(), typeListener);
		removeTypeFrag.setArguments(bundle);
		removeTypeFrag.show(fragmentManager, RemoveTypeFragment.TAG);
	}
}
