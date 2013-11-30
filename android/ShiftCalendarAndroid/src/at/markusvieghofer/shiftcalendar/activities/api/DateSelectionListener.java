package at.markusvieghofer.shiftcalendar.activities.api;

import java.util.Date;

public interface DateSelectionListener {
    void addDate(Date date);

    void removeDate(Date date);
}
