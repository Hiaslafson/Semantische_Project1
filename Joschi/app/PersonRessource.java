package app;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.VCARD;

import model.Person;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class PersonRessource
{
	public static Dataset dataset;
	private static Model m, m_deleted;

	
	public static void init()
	{
		setPersonenModel(ModelFactory.createDefaultModel());
		m_deleted = ModelFactory.createDefaultModel();
		// Namespace definitions in RDF model
		getPersonenModel().setNsPrefix("x", Namespaces.nsX);
		getPersonenModel().setNsPrefix("person", Namespaces.nsPerson);
		getPersonenModel().setNsPrefix("vcard", VCARD.getURI());
		
	}
	
	public static void addPerson(Person person)
	{
		ResourceFactoryPerson.createResourcePerson(person, getPersonenModel());

		// Serialize in Turtle 
		System.out.println("----------------------------------------------------\n");
		System.out.println("addPerson - Model m: \n");
		RDFDataMgr.write(System.out, getPersonenModel(), Lang.TURTLE) ;	
	}

	public static void switchDateToModel(int id, Model deleteFromModel, Model addToModel)
	{
			Resource F_personToDelete = deleteFromModel.getResource(Namespaces.nsPerson + id);
			
			// Alle Daten der zu loeschenden Person in das Archiv kopieren
			StmtIterator stmts = deleteFromModel.listStatements(F_personToDelete, null, (RDFNode)null);
			
			String Fs_addressID = null;
			while(stmts.hasNext())
			{
				Statement F_stmt = stmts.next();
				if(F_stmt.getPredicate().toString().contains("http://www.w3.org/2001/vcard-rdf/3.0#ADR"))
				{
					Fs_addressID = F_stmt.getObject().toString();
					break;
				}
			}
			
			Resource F_addressToDelete = deleteFromModel.getResource(Fs_addressID);
			StmtIterator stmtsAdr = deleteFromModel.listStatements(F_addressToDelete, null, (RDFNode)null);
			
			addToModel.add(deleteFromModel.listStatements(F_personToDelete, null, (RDFNode)null));
			addToModel.add(stmtsAdr);
			
			// Alle Daten der zu loeschenden Person aus dem Model entfernen
			deleteResource(deleteFromModel, F_personToDelete);
			
			System.out.println("----------------------------------------------------\n");
			System.out.println("Model m:\n");
			RDFDataMgr.write(System.out, deleteFromModel, Lang.TURTLE);
			System.out.println("----------------------------------------------------\n");
			System.out.println("Model m_deleted:\n");
			RDFDataMgr.write(System.out, addToModel, Lang.TURTLE);
	}

	public static void deleteResource(Model model, Resource resource)
	{
	    // remove statements where resource is subject
	    model.removeAll(resource, null, (RDFNode) null);
	    // remove statements where resource is object
	    model.removeAll(null, null, resource);	    
	 }
	
	public static void editPerson(Person person)
	{
		Resource F_personToEdit = getPersonenModel().getResource(Namespaces.nsPerson + person.getId());
		if(person.getId() != -1)
		{
			ResourceFactoryPerson.changeResource(F_personToEdit, getPersonenModel(), person);
		}
				 
		System.out.println("----------------------------------------------------\n");
		System.out.println("editPerson: Model m: NACHER: \n");
		RDFDataMgr.write(System.out, getPersonenModel(), Lang.TURTLE);
	}
	
	public static void setHasFriend(int Ps_personID, int Ps_persionID)
	{
		Property hasFriend = getPersonenModel().createProperty(Namespaces.nsXHasFriend);
		Resource F_person1 = getPersonenModel().getResource(Namespaces.nsPerson + Ps_personID);
		Resource F_person2 = getPersonenModel().getResource(Namespaces.nsPerson + Ps_persionID);
		
		F_person1.addProperty(hasFriend, F_person2);
		
		System.out.println("----------------------------------------------------\n");
		System.out.println("hasFriend: Model m: NACHER: \n");
		RDFDataMgr.write(System.out, getPersonenModel(), Lang.TURTLE);
	}
	
	public static List<Person> getFriends(int personID, Model model)
	{
		String sql = "SELECT ?k ?p ?person WHERE {<" + Namespaces.nsPerson + personID + "> <" + Namespaces.nsXHasFriend + "> ?person}";
		ResultSet res = Utils.executeSelect(sql, model);
				
		return ResourceFactoryPerson.getPersonFromResultSet(res, model);
	}
	
	public static void setHasEmployee(int Ps_employerID, int Ps_employeeID)
	{
		Property hasEmployee = getPersonenModel().createProperty(Namespaces.nsXHasEmployee);
		Resource F_employer = getPersonenModel().getResource(Namespaces.nsPerson + Ps_employerID);
		Resource F_employee = getPersonenModel().getResource(Namespaces.nsPerson + Ps_employeeID);
		
		F_employer.addProperty(hasEmployee, F_employee);
		
		System.out.println("----------------------------------------------------\n");
		System.out.println("hasEmployee: Model m: NACHER: \n");
		RDFDataMgr.write(System.out, getPersonenModel(), Lang.TURTLE);
	}
	
	public static List<Person> getEmployees(int personID, Model model)
	{
		String sql = "SELECT ?k ?p ?person WHERE {<" + Namespaces.nsPerson + personID + ">  <" + Namespaces.nsXHasEmployee + "> ?person}";
		ResultSet res = Utils.executeSelect(sql, model);
			
		return ResourceFactoryPerson.getPersonFromResultSet(res, model);
	}
	
	public static Person getPerson(Integer id, Model P_model)
	{
		return ResourceFactoryPerson.loadPersonFromURI( Namespaces.nsPerson + id ,  getPersonenModel());
	}
	
	public static List<Person> filtern(String Ps_feldWert, Model P_model)
	{	
		List<Person> persons = new ArrayList<Person>();
		try
		{
			//load Perons
			ResultSet F_personsWithValue  = Utils.executeSelect("SELECT  ?person ?v ?o  WHERE {?person ?v ?o. "+
																				"FILTER regex(?o, \"" +Ps_feldWert + "\", \"i\")}", P_model); //i means insensitive
			
	   		System.out.println("----------------------------------------------------\n");
	   		System.out.println("filtern: " + Ps_feldWert + ": " + Ps_feldWert);
	   		
	   		 persons = ResourceFactoryPerson.getPersonFromResultSet(F_personsWithValue, getPersonenModel());
	   		
	   		//loadPersons by addresss
	  		 F_personsWithValue  = Utils.executeSelect("SELECT  ?person ?v ?o  WHERE {?person ?v ?o. "+
					"FILTER regex(?o, \"" +Ps_feldWert + "\", \"i\")}", P_model); //i means insensitive
	  		while(F_personsWithValue.hasNext())
			{
	  			QuerySolution qsPerson = F_personsWithValue.nextSolution();
		        String uriString = qsPerson.get("person").toString();
	  			if(uriString.contains(Namespaces.nsXAddress)){ //address is nsX
		  			ResultSet personResultSet  = Utils.executeSelect("SELECT  ?person ?v ?o WHERE {?person ?v <" + uriString + "> }", m);
					// alle Personendaten ausgeben, die den Filterkriterien entsprechen
		  			List<Person> newPersons = ResourceFactoryPerson.getPersonFromResultSet(personResultSet, getPersonenModel());
		  			for(Person newPerson : newPersons){
		  				if(!persons.contains(newPerson)){
		  					persons.add(newPerson);
		  				}
		  			}
		        }
			}
		}
		catch(Exception exc){
			System.out.println(exc);
		}
   		return persons;   		
}
	
	/**
	 * This method sorts a given model by a certain label
	 * and a certain direction.
	 * @param label
	 * @param ascending
	 */
	public static List<Person> sort(String Ps_feldBezeichnung, boolean ascending)
	{
		return sort(Ps_feldBezeichnung, ascending, false);
	}
	
	public static List<Person> sort(String Ps_feldBezeichnung, boolean ascending, boolean Fb_showDeleted)
	{
		String sql = "SELECT ?person ?v ?sortattribute " +
				" WHERE { " +
				//" 	?x ?predicate ?y. " +
				" 	?person "+ Ps_feldBezeichnung  +  " ?sortattribute." + 
				" }";
			
		if(ascending)
		{
			sql += " ORDER BY ?sortattribute  ";
		}
		else
		{
			sql += " ORDER BY DESC(?sortattribute) ";
		}
		
		System.out.println("----------------------------------------------------\n");
   		System.out.println("sort by: " + Ps_feldBezeichnung );
   		
   		Model model;
   		if(Fb_showDeleted)
   		{
   			model = getPersonenDeletedModel();
   		}
   		else
   		{
   			model = getPersonenModel();
   		}
   		
		ResultSet result = Utils.executeSelect(sql, model);
		return ResourceFactoryPerson.getPersonFromResultSet(result, model);
	}
	
	public static List<Person> getAll()
	{
		return getAll(false);
	}
	
	public static List<Person> getAll(boolean Pb_showDeleted)
	{
		Model model;
		if(Pb_showDeleted)
		{
			model = getPersonenDeletedModel();
		}
		else
		{
			model = getPersonenModel();
		}
        ResultSet F_allData  = Utils.executeSelect("SELECT  ?person ?v ?o WHERE {?person ?v ?o }", model);
   		
//   			AKTUELLE DATENSTRUKTUR: 
//   			------------------------------------------------------------------------------------------------------------------------------------------
//   			| person                                 | v                                               | o                                           |
//   			==========================================================================================================================================
//   			| <http://www.example/person/2>          | <http://www.example/x.rdf#hasFriend>            | <http://www.example/person/1>               |
//   			| <http://www.example/person/2>          | <http://www.example/x.rdf#Work>                 | "Student"                                   |
//   			| <http://www.example/person/2>          | <http://www.example/x.rdf#geschlecht>           | "F"                                         |
//   			| <http://www.example/person/2>          | <http://www.w3.org/2001/vcard-rdf/3.0#BDAY>     | "2016-05-20"^^<java:java.sql.Date>          |
//   			| <http://www.example/person/2>          | <http://www.w3.org/2001/vcard-rdf/3.0#Family>   | "Mustermann"                                |
//   			| <http://www.example/person/2>          | <http://www.w3.org/2001/vcard-rdf/3.0#Given>    | "Max"                                       |
//   			| <http://www.example/person/2>          | <http://www.w3.org/2001/vcard-rdf/3.0#ADR>      | <http://www.example/x.rdf#address4000>      |
//   			| <http://www.example/person/2>          | <http://www.w3.org/2001/vcard-rdf/3.0#UID>      | "2"^^<http://www.w3.org/2001/XMLSchema#int> |
//   			| <http://www.example/x.rdf#address4040> | <http://www.w3.org/2001/vcard-rdf/3.0#Country>  | <http://de.dbpedia.org/resource/Austria>    |
//   			| <http://www.example/x.rdf#address4040> | <http://www.w3.org/2001/vcard-rdf/3.0#Pcode>    | "4040"                                      |
//   			| <http://www.example/x.rdf#address4040> | <http://www.w3.org/2001/vcard-rdf/3.0#Street>   | "Hauptstrasse 10"                           |
//   			| <http://www.example/x.rdf#address4040> | <http://www.w3.org/2001/vcard-rdf/3.0#Locality> | "Steyr"                                     |
//   			| <http://www.example/x.rdf#address4000> | <http://www.w3.org/2001/vcard-rdf/3.0#Country>  | <http://de.dbpedia.org/resource/Austria>    |
//   			| <http://www.example/x.rdf#address4000> | <http://www.w3.org/2001/vcard-rdf/3.0#Pcode>    | "4000"                                      |
//   			| <http://www.example/x.rdf#address4000> | <http://www.w3.org/2001/vcard-rdf/3.0#Street>   | "Altenbergerstrasse 69"                     |
//   			| <http://www.example/x.rdf#address4000> | <http://www.w3.org/2001/vcard-rdf/3.0#Locality> | "Linz"                                      |
//   			| <http://www.example/person/1>          | <http://www.example/x.rdf#hasEmployee>          | <http://www.example/person/2>               |
//   			| <http://www.example/person/1>          | <http://www.example/x.rdf#Work>                 | "CGM Clinical Oesterreich"                  |
//   			| <http://www.example/person/1>          | <http://www.example/x.rdf#geschlecht>           | "M"                                         |
//   			| <http://www.example/person/1>          | <http://www.w3.org/2001/vcard-rdf/3.0#BDAY>     | "2016-05-20"^^<java:java.sql.Date>          |
//   			| <http://www.example/person/1>          | <http://www.w3.org/2001/vcard-rdf/3.0#Family>   | "Weissensteiner"                            |
//   			| <http://www.example/person/1>          | <http://www.w3.org/2001/vcard-rdf/3.0#Given>    | "Martin"                                    |
//   			| <http://www.example/person/1>          | <http://www.w3.org/2001/vcard-rdf/3.0#ADR>      | <http://www.example/x.rdf#address4040>      |
//   			| <http://www.example/person/1>          | <http://www.w3.org/2001/vcard-rdf/3.0#UID>      | "1"^^<http://www.w3.org/2001/XMLSchema#int> |
//   			------------------------------------------------------------------------------------------------------------------------------------------
	
		return ResourceFactoryPerson.getPersonFromResultSet(F_allData, model);
	}
	
	public static Model getPersonenModel() {
		return m;
	}

	public static void setPersonenModel(Model P_model) {
		PersonRessource.m = P_model;
	}
	
	public static Model getPersonenDeletedModel() {
		return m_deleted;
	}

	public static void setPersonenDeletedModel(Model P_model) {
		PersonRessource.m_deleted = P_model;
	}
}
