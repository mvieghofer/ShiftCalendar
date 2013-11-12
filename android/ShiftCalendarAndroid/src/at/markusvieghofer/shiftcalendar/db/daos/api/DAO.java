package at.markusvieghofer.shiftcalendar.db.daos.api;

import java.util.List;

import at.markusvieghofer.shiftcalendar.models.api.Model;

public interface DAO {
	List<? extends Model> readAll();

	long save(Model model);
}
