package mvieghofer.github.com.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvieghofer.github.com.utils.SingletonCache;
import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;

import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String CLIENT_ID = "20882899860.apps.googleusercontent.com";
	private static final String CALLBACK_URL = "https://myshiftcal.appspot.com/oauth2callback";
	private static final String SCOPE = "https://www.googleapis.com/auth/calendar";
	private static Logger logger = Logger.getLogger(LoginServlet.class
			.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Object token = null;
		try {
			Cache cache = SingletonCache.getInstance().getCache();
			token = cache.get("ACCESS_TOKEN");
		} catch (CacheException ex) {
			logger.log(Level.ALL, ex.getMessage());
		}
		if (token == null) {
			String url = new GoogleBrowserClientRequestUrl(CLIENT_ID,
					CALLBACK_URL, Arrays.asList(
							"https://www.googleapis.com/auth/userinfo.email",
							"https://www.googleapis.com/auth/userinfo.profile"))
					.build();
			resp.sendRedirect(url);
		} else {
			resp.getWriter().write(token.toString());
		}
	}
}
