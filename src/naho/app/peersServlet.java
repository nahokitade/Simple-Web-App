
package naho.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class peersServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String query = req.getQueryString();
		resp.setContentType("text/plain; charset=UTF-8");
		if(query == null){
			URL url = new URL("http://step-test-krispop.appspot.com/peers");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				resp.getWriter().println(line);
			}
			reader.close();		
		}
		else{
			String[] splitQuery = query.split("=");
			if(splitQuery.length == 2 && splitQuery[0].equals("endpoint")){
				URL url = new URL("http://step-test-krispop.appspot.com/peers?" + query);
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					resp.getWriter().println(line);
				}
				reader.close();
			}
			else{
				resp.getWriter().println("The query was formatted incorrectly... :( \nRemember, message=yourquery");
			}
		}


	}

}


