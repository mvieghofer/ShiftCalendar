package at.markusvieghofer.shiftcalendar.fragments.api;

import java.io.Serializable;

import at.markusvieghofer.shiftcalendar.models.Type;

public interface TypeListener extends Serializable {
    void addType(Type type);

    String getKey();

    void removeType(Type type);

    void setType(Type type);
}
