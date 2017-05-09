package test;
import java.sql.Date;
import java.util.List;

import app.PersonRessource;
import model.Person;

public class Test {

	public static void main(String[] args) {

		Person martin = new Person("Martin", "Weissensteiner", "Hauptstrasse 10", "4040", "Steyr", "Austria", 
				new Date(System.currentTimeMillis()), "M" ,  "CGM Clinical Oesterreich");
		Person max = new Person("Max", "Mustermann",  "Altenbergerstrasse 69", "4000", "Linz", "Austria",
				new Date(System.currentTimeMillis()), "F", "Student");
		
		PersonRessource.init();
		PersonRessource.addPerson(martin);
		PersonRessource.addPerson(max);
		Person person = PersonRessource.getPerson(0, PersonRessource.getPersonenModel());
		List<Person> persons = PersonRessource.getAll(false);
		persons = PersonRessource.filtern("F", PersonRessource.getPersonenModel());
		persons = PersonRessource.filtern( "CGM Clinical Oesterreich", PersonRessource.getPersonenModel());
		persons = PersonRessource.filtern("", PersonRessource.getPersonenModel());
		persons = PersonRessource.filtern("asdfsadfasdfadsf", PersonRessource.getPersonenModel());
		
		PersonRessource.sort("<http://www.example/x.rdf#geschlecht>", true);
		Person newMartin = new Person("Martin2", "Weissensteiner2", "Hauptstrasse 11", "4041", "Steyr", "Austria", 
				new Date(System.currentTimeMillis()), "M" ,  "CGM Clinical Oesterreich2");
		newMartin.setId(0);
		PersonRessource.editPerson(newMartin);
		
		PersonRessource.setHasFriend(1,0);
		PersonRessource.setHasEmployee(0, 1);
		
		persons = PersonRessource.getFriends(0, PersonRessource.getPersonenModel());
		persons = PersonRessource.getFriends(1, PersonRessource.getPersonenModel());
		persons = PersonRessource.getEmployees(0, PersonRessource.getPersonenModel());
		persons = PersonRessource.getEmployees(1, PersonRessource.getPersonenModel());
				
		PersonRessource.switchDateToModel(2, PersonRessource.getPersonenModel(), PersonRessource.getPersonenDeletedModel());
	}
}