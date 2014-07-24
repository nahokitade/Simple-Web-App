
package naho.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class madlibServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String[] madlibSentence = {"hello <noun> "};

		int randInt;
		String currentSentence = "";

		String query = req.getQueryString();
		resp.setContentType("text/plain; charset=UTF-8");
		if(query != null){
			String[] splitQuery = query.split("=");
			if(splitQuery.length == 2 && splitQuery[0].equals("sentence")){

				currentSentence = java.net.URLDecoder.decode(splitQuery[1], "UTF-8");
				resp.getWriter().println("Current sentence: " + currentSentence);

			}
			else{
				resp.getWriter().println("The query was formatted incorrectly... :( \nRemember, sentence=yourquery");
			}
		}
		else{
			randInt = (int) (Math.random() * madlibSentence.length);
			currentSentence = madlibSentence[randInt];

		}

		boolean startFirst = false;
		ArrayList<String> rawWordsNeeded = new ArrayList<String>();
		ArrayList<String> wordsNeeded = new ArrayList<String>();
		ArrayList<String> wordsGotten = new ArrayList<String>();

		if(currentSentence.charAt(0) == '<') startFirst = true;

		String[] splitted = currentSentence.split("<[a-z]+>");
		
		ArrayList<String> splittedArray = new ArrayList<String>();

	    for(String s : splitted) {
	       if(s.length() > 0) {
	          splittedArray.add(s);
	       }
	    }


		Pattern pattern = Pattern.compile("(<[a-z]+>)");
		Matcher matcher = pattern.matcher(currentSentence);
		while(matcher.find()){
			rawWordsNeeded.add(matcher.group(1));
		}
		for(String wordProcessing : rawWordsNeeded){
			wordsNeeded.add(wordProcessing.substring(1, wordProcessing.length() - 1));
		}


		URL url = new URL("http://step-test-krispop.appspot.com/peers?endpoint=getword");
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		String line;
		ArrayList<String> peers = new ArrayList<String>();
		while ((line = reader.readLine()) != null) {
			peers.add(line);
		}
		reader.close();
		for(String peer : peers){
			if(peer.equals("")) continue;
			resp.getWriter().println(peer);
			for(String word : wordsNeeded){
				URL peerUrl = new URL(peer + "/getword?pos=" + word);

				HttpURLConnection urlCheck = (HttpURLConnection) peerUrl.openConnection();
				int responseCode = urlCheck.getResponseCode();
				if(responseCode == 404){
					resp.getWriter().println("Sorry, this page does not exist :("); 
					resp.getWriter().println();
					continue;
				}
				BufferedReader peerReader = new BufferedReader(new InputStreamReader(peerUrl.openStream(), "UTF-8"));
				while ((line = peerReader.readLine()) != null) {
					wordsGotten.add(line);
				}
				peerReader.close();
			}

			for(int i = 0; i < wordsGotten.size() && i < splittedArray.size(); i++){
				if(startFirst){
					resp.getWriter().print(wordsGotten.get(i));
					resp.getWriter().print(splittedArray.get(i));
				}
				else{
					resp.getWriter().print(splittedArray.get(i));							
					resp.getWriter().print(wordsGotten.get(i));
				}
			}
			if(wordsGotten.size() < splittedArray.size()) resp.getWriter().print(splittedArray.get(splittedArray.size() - 1));
			if(wordsGotten.size() > splittedArray.size()) resp.getWriter().print(wordsGotten.get(wordsGotten.size() - 1));
			resp.getWriter().println();
			resp.getWriter().println();

			wordsGotten.clear();
			
		}
	}
}


