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

@WebServlet("/PersonEmployeeServlet")
public class PersonEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Get items
     */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try
		{
			String personId = (String) req.getParameter("employeesOfPerson");
			
			if( personId != null)
			{
				List<Person> persons = PersonRessource.getEmployees(Integer.valueOf(personId), PersonRessource.getPersonenModel());
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
			
			int employer = jsonObject.getInt("employer");
			int employee = jsonObject.getInt("employee");
			
			PersonRessource.setHasEmployee(employer, employee);
			res.setStatus(200);
		}
		catch(Exception exc){
			System.out.println(exc);
		}
    }
	
	/**
	 * Edits items
	 */
	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
	}
	
	/**
	 * Delets items
	 */
	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
    }	
}
