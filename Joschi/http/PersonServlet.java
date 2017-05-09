package http;

import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import app.PersonRessource;
import model.Person;

@WebServlet("/PersonServlet")
public class PersonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static HashMap<String,String[]> fieldKeys =
			new HashMap<String, String[]>();
       
    public PersonServlet() {
        super();
        PersonRessource.init();
//        Person martin = new Person("Martin", "Weissensteiner", "Pfarrberg 2", "4407", "Dietach", "Austria", 
//				new Date(116, 04, 30), "M" ,  "Softwareentwickler bei der Firma CGM Clinical Oesterreich");
//		Person anna = new Person("Johann", "Stoebich",  "Höhenstrasse 7", "4120", "Neufelden", "Austria",
//				new Date(93, 00, 25), "M", "Student");
//		Person johann = new Person("Anna", "Mustermann",  "Ebelsberger Schlossberg 1", "4030", "Linz", "Austria",
//				new Date(110, 00, 01), "F", "Student; Verkäuferin");
//		PersonRessource.addPerson(martin);
//		PersonRessource.addPerson(anna);
//		PersonRessource.addPerson(johann);
		
		fieldKeys.put("Nachname", new String[] {"<http://www.w3.org/2001/vcard-rdf/3.0#Family>", "false"});		
		fieldKeys.put("Vorname", new String[] {"<http://www.w3.org/2001/vcard-rdf/3.0#Given>", "false"});
		fieldKeys.put("Gebdatum", new String[] {"<http://www.w3.org/2001/vcard-rdf/3.0#BDAY>", "false"});
		fieldKeys.put("Arbeitgeber", new String[] {"<http://www.example/x.rdf#Work>", "false"});
		fieldKeys.put("Geschlecht", new String[] {"<http://www.example/x.rdf#geschlecht>", "false"});
    }

    /**
     * Get items
     */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			//load persons
			List<Person> persons = null;
			String value = (String) request.getParameter("value");
			String id = (String) request.getParameter("id");
			String sortName = (String) request.getParameter("sortName");
			String showDeleted = (String) request.getParameter("showDeleted");
			
			boolean Fb_showDeleted = false;
			if(showDeleted != null)
			{
				Fb_showDeleted = Boolean.valueOf(showDeleted);
			}
			
			if( value != null)
			{
				persons = PersonRessource.filtern( value, PersonRessource.getPersonenModel());
			}
			else if(sortName != null && !sortName.isEmpty())
			{
				
				Boolean Fb_ascending = !Boolean.valueOf(fieldKeys.get(sortName)[1]);
				fieldKeys.put(sortName, new String[]{fieldKeys.get(sortName)[0], Fb_ascending.toString()});
				persons = PersonRessource.sort(fieldKeys.get(sortName)[0], Fb_ascending, Fb_showDeleted);
			}
			else if(id != null)
			{
				persons = new ArrayList<Person>();
				persons.add(PersonRessource.getPerson(Integer.valueOf(id),PersonRessource.getPersonenModel()));		
			}
			else
			{
				persons = PersonRessource.getAll(Fb_showDeleted);
			}
			
			ServletUtils.doResponse(persons, response);
		}
		catch(Exception exc){
			System.out.println(exc);
			response.setStatus(400);
		}
	}
	
	/**
	 * Add items
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try
		{		
			Person person = ServletUtils.requestToPerson(req);	
			PersonRessource.addPerson(person);
			res.setStatus(200);
		}
		catch(Exception exc){
			System.out.println(exc);
			res.setStatus(400);
		}
    }
	
	/**
	 * Edits items
	 */
	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try
		{
			Person person = ServletUtils.requestToPerson(req);	
			PersonRessource.editPerson(person);
			res.setStatus(200);
		}
		catch(Exception exc){
			System.out.println(exc);
			res.setStatus(400);
		}
	}
	
	/**
	 * Delets items
	 */
	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try
		{
			String request = ServletUtils.httpRequestToString(req);
			JSONObject jsonObject = new JSONObject(request);
			
			int id = jsonObject.getInt("id");
			boolean restore = jsonObject.getBoolean("restore");
			
			if(!restore)
			{
				PersonRessource.switchDateToModel(id, PersonRessource.getPersonenModel(),
						PersonRessource.getPersonenDeletedModel());	
			}
			else
			{
				PersonRessource.switchDateToModel(id, PersonRessource.getPersonenDeletedModel(),
						PersonRessource.getPersonenModel());	
			}
			// Die Personenliste neu auslesen, denn sonst kommt es zu einem
			// Fehler beim Aktualisieren nachdem Lï¿½schen.
			List<Person> persons = PersonRessource.getAll(restore);
			ServletUtils.doResponse(persons, res);
		}
		catch(Exception exc){
			System.out.println(exc);
			res.setStatus(400);
		}
    }
}
