package naho.app;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/* 
 * Servlet to convert given query into binary numbers.
 * 
 * @author Naho Kitade
 */
@SuppressWarnings("serial")
public class convertServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String query = req.getQueryString();
		resp.setContentType("text/plain");
		if(query != null){
			String[] splitQuery = query.split("=");
			
			// make sure format is correct, and convert the message into binary.
			if(splitQuery.length == 2 && splitQuery[0].equals("message")){
				String binary = new BigInteger(splitQuery[1].getBytes()).toString(2);
				resp.getWriter().println("Computer talk: " + binary);
			}
			else{
				resp.getWriter().println("The query was formatted incorrectly... :( \nRemember, message=yourquery");
			}
		}
		else{
			resp.getWriter().println("Please insert query after convert.");
		}
	}
}