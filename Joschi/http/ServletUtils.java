package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import model.Person;

public class ServletUtils {
	public static void doResponse(List<Person> persons , HttpServletResponse response) throws IOException{
		//create response
		response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");
	    
		PrintWriter out = response.getWriter();
			
		String json = personToJSONString(persons);
		
		System.out.println(json);
		out.write(json);
		
		out.close();
		response.setStatus(200);
	}
	
	private static String personToJSONString(List<Person> persons){
		String json = "{\"persons\" : [";
		int i = 0;
		for(Person p : persons){
			if(i>0){
				json += ",";
			}
			json += p.toJson();
			i++;
		}
		
		json += "]}";
		return json;
	}
	
	public static Person requestToPerson(HttpServletRequest req){
		
		String request = httpRequestToString(req);
		JSONObject jsonObject = new JSONObject(request);
		String id = jsonObject.getString("id");
		String vorname = jsonObject.getString("vorname");
		String nachname = jsonObject.getString("nachname");
		String strasse = jsonObject.getString("strasse");
		String plz = jsonObject.getString("plz");
		String stadt = jsonObject.getString("stadt");
		String country = jsonObject.getString("country");
		Date gebDatum = Date.valueOf(jsonObject.getString("gebdatum")); //yyyy-mm-dd
		String geschlecht = jsonObject.getString("geschlecht");
		String arbeitgeber = jsonObject.getString("arbeitgeber");;
		Person person = new Person(vorname, nachname, strasse, plz, stadt, country, gebDatum, geschlecht, arbeitgeber);
		
		if(!"".equals(id)){
			person.setId(Integer.valueOf(id));
		}
		
		return person;
	}
	

	
	public static String httpRequestToString(HttpServletRequest req){
		String jb = "";
		String line = null;
		try {			
		    BufferedReader reader = req.getReader();
		    while ((line = reader.readLine()) != null){
		    	jb = jb + URLDecoder.decode(line);
		    }
		} catch (Exception e) {
			System.out.println(e);
			}
		return jb;
	}
}
