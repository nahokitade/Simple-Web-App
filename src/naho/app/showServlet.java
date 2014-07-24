
package naho.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.*;

/*
 * Servlet that will show everyone's server's convert function on a queried word.
 * 
 * @author Naho Kitade
 */
@SuppressWarnings("serial")
public class showServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String query = req.getQueryString();
		resp.setContentType("text/plain; charset=UTF-8");
		if(query != null){
			String[] splitQuery = query.split("=");
			if(splitQuery.length == 2 && splitQuery[0].equals("message")){
				URL url = new URL("http://step-test-krispop.appspot.com/peers");
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				String line;
				ArrayList<String> peers = new ArrayList<String>();
				while ((line = reader.readLine()) != null) {
					peers.add(line);
				}
				reader.close();
				
				// call convert on all servers who have implemented that feature and print each
				// page's result.
				for(String peer : peers){
					if(peer.equals("")) continue;
					URL peerUrl = new URL(peer + "/convert?" + query);
					resp.getWriter().println(peerUrl);
					HttpURLConnection urlCheck = (HttpURLConnection) peerUrl.openConnection();
					int responseCode = urlCheck.getResponseCode();
					
					// carry on to another url if the current url gives error
					if(responseCode == 404){
						resp.getWriter().println("Sorry, this page does not exist :("); 
						resp.getWriter().println();
						continue; 
					}
					
					BufferedReader peerReader = new BufferedReader(new InputStreamReader(peerUrl.openStream(), "UTF-8"));
					while ((line = peerReader.readLine()) != null) {
						resp.getWriter().println(line);
					}
					resp.getWriter().println();
					peerReader.close();
				}			
			}
			else{
				resp.getWriter().println("The query was formatted incorrectly... :( \nRemember, message=yourquery");
			}
		}
		else{
			resp.getWriter().println("Please insert query after show.");
		}


	}

}


