package naho.app;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@SuppressWarnings("serial")
public class getwordServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String[] verb = {"hug", "kiss", "bake", "scatter", "kick", "fix"};
		String[] noun = {"circle", "beast", "crow", "crayon", "bomb", "brain"};
		String[] adjective = {"adorable", "gifted", "famous", "jealous", "mysterious", "lazy"};
		String[] animal = {"cat", "dog", "bird", "frog", "snake", "hedgehog", "bat"};
		String[] name = {"Albert Einstein", "Elvis", "Helen Keller", "Madonna", "Marilyn Monroe", "Dr. Seuss"};
		String[] adverb = {"abnormally", "carelessly", "especially", "calmly", "gently", "happily"};
		String[] exclaimation = {"eeek", "LOL", "grrrr", "yuck", "jeez louise", "yippie"};
		String[][] speechStrings = {verb, noun, adjective, animal, name, adverb, exclaimation};
		
		int randInt;
		String query = req.getQueryString();
		resp.setContentType("text/plain");
		if(query != null){
			String[] splitQuery = query.split("=");
			if(splitQuery.length == 2 && splitQuery[0].equals("pos")){
				String[] partOfSpeech;
				switch (splitQuery[1]){
					case "verb":
						partOfSpeech = verb;
						break;
					case "noun":
						partOfSpeech = noun;
						break;
					case "adjective":
						partOfSpeech = adjective;
						break;
					case "animal":
						partOfSpeech = animal;
						break;
					case "name":
						partOfSpeech = name;
						break;
					case "adverb":
						partOfSpeech = adverb;
						break;
					case "exclaimation":
						partOfSpeech = exclaimation;
						break;
					default:
						randInt = (int) (Math.random() * speechStrings.length);
						partOfSpeech = speechStrings[randInt];
						break;
				}
				randInt = (int) (Math.random() * partOfSpeech.length);
				resp.getWriter().println(partOfSpeech[randInt]);
			}
			else{
				resp.getWriter().println("The query was formatted incorrectly... :( \nRemember, pos=yourquery");
			}
		}
		else{
			resp.getWriter().println("Please insert query after getword.");	
		}
	}
}