package at.markusvieghofer.shiftcalendar.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.adapter.api.TypeEditListener;
import at.markusvieghofer.shiftcalendar.fragments.api.TypeListener;
import at.markusvieghofer.shiftcalendar.models.Type;
import at.markusvieghofer.shiftcalendar.views.TypeEntryView;

public class TypeAdapter extends ArrayAdapter<Type> implements TypeListener,
        TypeEditListener {

    private TypeListener typeListener;

    private Map<Long, TypeEntryView> views = new HashMap<Long, TypeEntryView>();

    public TypeAdapter(Context context, List<Type> types,
            TypeListener typeListener) {
        super(context, R.layout.view_type_entry, types);
        this.typeListener = typeListener;
    }

    @Override
    public void addType(Type type) {
        typeListener.addType(type);
    }

    @Override
    public String getKey() {
        return typeListener.getKey();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TypeEntryView view = null;
        Type type = getItem(position);
        if (convertView != null && convertView instanceof TypeEntryView) {
            view = (TypeEntryView) convertView;
        } else if (views.get(type.getId()) != null) {
            view = views.get(type.getId());
        } else {
            view = new TypeEntryView(getContext(), type);
        }
        view.setTypeListener(this);
        view.setTypeEditListener(this);
        views.put(type.getId(), view);
        return view;
    }

    @Override
    public void hideEditTypeOptions(UUID callerID) {
        for (Long key : views.keySet()) {
            TypeEntryView view = views.get(key);
            if (view.ID != callerID) {
                view.hideEditTypeOptions();
            }
        }
    }

    @Override
    public void removeType(Type type) {
        typeListener.removeType(type);
    }

    @Override
    public void setType(Type type) {
        typeListener.setType(type);
        toggleTypeSelection(type);
    }

    private void toggleTypeSelection(Type type) {
        for (Long key : views.keySet()) {
            TypeEntryView typeEntryView = views.get(key);
            if (!typeEntryView.getType().equals(type)) {
                typeEntryView.setChecked(false);
            }
        }
    }
}
