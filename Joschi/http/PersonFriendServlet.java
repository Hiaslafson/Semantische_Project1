package http;

import java.io.*;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import app.PersonRessource;
import model.Person;

@WebServlet("/PersonFriendServlet")
public class PersonFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Get items
     */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try
		{
			String personId = (String) req.getParameter("friendsOfPerson");
			
			if( personId != null)
			{
				List<Person> persons = PersonRessource.getFriends(Integer.valueOf(personId), PersonRessource.getPersonenModel());
				ServletUtils.doResponse(persons, res);
			}
			else{
				res.setStatus(400);
			}
		}
		catch(Exception exc){
			System.out.println(exc);
		}
	}
	
	/**
	 * Add items
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try
		{
			String request = ServletUtils.httpRequestToString(req);
			JSONObject jsonObject = new JSONObject(request);
			
			int f1 = jsonObject.getInt("friend1");
			int f2 = jsonObject.getInt("friend2");
			
			PersonRessource.setHasFriend(f1, f2);
			res.setStatus(200);
		}
		catch(Exception exc){
			System.out.println(exc);
		}
    }
}
