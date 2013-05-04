package mvieghofer.github.com.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvieghofer.github.com.utils.SingletonCache;
import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;

public class OAuthServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(OAuthServlet.class
			.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String token = (String) req.getParameter("access_token");
		try {
			Cache cache = SingletonCache.getInstance().getCache();
			cache.put("ACCESS_TOKEN", token);
		} catch (CacheException ex) {
			logger.log(Level.ALL, ex.getMessage());
		}

		resp.getWriter().write(token);

	}
}
