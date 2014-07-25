
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

/*
 * Servlet that outputs a madlib sentence composed of a template sentence and words
 * from getword function on all servers that have implemented getword.
 * 
 * @author Naho Kitade
 */
@SuppressWarnings("serial")
public class madlibServlet extends HttpServlet {
	@SuppressWarnings("null")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String[] madlibSentence = {"A <animal> can't change his <noun>",
				"<noun> speak louder than <noun>",
				"<verb> a dead <animal>",
				"Don't <verb> your <animal> before they <verb>",
				"Get up on the <adjective> side of the <noun>",
				"Never <verb> the hand that <verb> you",
				"The <adjective> they are the <adjective> they <verb>"
		};

		int randInt;
		String currentSentence = "";

		String query = req.getQueryString();
		resp.setContentType("text/plain; charset=UTF-8");

		// if there is a query, that query becomes the madlib sentence.
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
		// if not just pick a already made sentence.
		else{
			randInt = (int) (Math.random() * madlibSentence.length);
			currentSentence = madlibSentence[randInt];
		}

		boolean startFirst = false;
		boolean wordRead = false;
		ArrayList<String> rawWordsNeeded = new ArrayList<String>();
		ArrayList<String> wordsNeeded = new ArrayList<String>();
		ArrayList<String> wordsGotten = new ArrayList<String>();

		// word gotten to replace in madlib sentence should start first
		if(currentSentence.charAt(0) == '<') startFirst = true;

		// make the non replacing word into an array
		String[] splitted = currentSentence.split("<[a-z]+>");

		ArrayList<String> splittedArray = new ArrayList<String>();

		// take out empty string from array
		for(String s : splitted) {
			if(s.length() > 0) {
				splittedArray.add(s);
			}
		}

		Pattern pattern = Pattern.compile("(<[a-z]+>)");
		Matcher matcher = pattern.matcher(currentSentence);

		// get all the part of speech of words that we need to replace
		while(matcher.find()){
			rawWordsNeeded.add(matcher.group(1));
		}

		// take of the first and last < > of the strings just gotten.
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

		boolean doesntExist = false;

		// for every server that has implemented getword, call the getword with the 
		// appropriate part of speech and store those in an array
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
					doesntExist = true;
					break;
				}
				BufferedReader peerReader = new BufferedReader(new InputStreamReader(peerUrl.openStream(), "UTF-8"));
				while ((line = peerReader.readLine()) != null) {
					if(!line.equals("")){
						wordsGotten.add(line);
						wordRead = true;
					}
				}
				// if nothing was returned, just call getword with the same part of speech on
				// my server.
				if(!wordRead){
					URL myUrl = new URL("http://1-dot-step-homework-kitade.appspot.com/getword?pos=" + word);
					BufferedReader myReader = new BufferedReader(new InputStreamReader(myUrl.openStream(), "UTF-8"));
					while ((line = myReader.readLine()) != null) {
						wordsGotten.add(line);
					}
				}
				peerReader.close();
				wordRead = false;
			}

			if(!doesntExist){
				// construct the final sentence appropriately making sure that the appropriate array
				// starts first.
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
			}
			wordsGotten.clear();
			
			doesntExist = false;

		}
	}
}


