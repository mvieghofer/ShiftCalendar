package mvieghofer.github.com.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ShiftCalendarServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ShiftCalendarServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String param1 = null;
		String message = "";
		resp.setContentType("text/plain");
		try {
			param1 = req.getParameter("taskParam1");
		} catch (Exception e) {
		}
		logger.info("got param1 : " + param1);
		try {
			//
			// PUT YOUR TASK CODE HERE
			//
			
		}
		catch (Exception ex) {
			message = "FAIL: ShiftCalendar failed : " + ex.getMessage();
			logger.info(message);
			resp.getWriter().println(message);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
