package at.markusvieghofer.shiftcalendar.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import at.markusvieghofer.shiftcalendar.models.Type;

public class TypeAdapter extends ArrayAdapter<Type> {

    public TypeAdapter(Context context, int resource, List<Type> types) {
        super(context, resource, types);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        return view;
    }

}
