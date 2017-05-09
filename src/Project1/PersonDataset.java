package Project1;
import org.apache.jena.query.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.VCARD;



import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class PersonDataset
{
	public static Dataset dataset;
	private static Model m, m_deleted;


	private static void addModels() {
		Person p1 = new Person(-1);
		p1.setDate("1.1.2017");
		p1.setName("hans");
		p1.setAdress("Linz 1");
		p1.setEmployer("Voest");
		p1.setGender("männlich");

		Person p2 = new Person(-1);
		p2.setDate("1.1.2015");
		p2.setName("sara");
		p2.setAdress("Linz 13");
		p2.setEmployer("Asfinag");
		p2.setGender("weiblich");

		addPerson(p1);
		addPerson(p2);
	}

	public PersonDataset()
	{
		setPersonModel(ModelFactory.createDefaultModel());
		m_deleted = ModelFactory.createDefaultModel();
		// Namespace definitions in RDF model
		getPersonModel().setNsPrefix("x", Namespaces.nsX);
		getPersonModel().setNsPrefix("person", Namespaces.nsPerson);
		getPersonModel().setNsPrefix("vcard", VCARD.getURI());

		addModels();
	}
	
	public static void addPerson(Person person)
	{

		PersonFactory.createResourcePerson(person, getPersonModel());
	}

	public static void deletePerson(int id)
	{
			Model persons = getPersonModel();
			Model deletedpersons = getPersonDeletedModel();

		Resource F_personToDelete = persons.getResource(Namespaces.nsPerson + id);
			
			// Alle Daten der zu loeschenden Person in das Archiv kopieren
			StmtIterator stmts = persons.listStatements(F_personToDelete, null, (RDFNode)null);
			
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

			Resource F_addressToDelete = persons.getResource(Fs_addressID);
			StmtIterator stmtsAdr = persons.listStatements(F_addressToDelete, null, (RDFNode)null);

			deletedpersons.add(persons.listStatements(F_personToDelete, null, (RDFNode)null));
			deletedpersons.add(stmtsAdr);

			// Alle Daten der zu loeschenden Person aus dem Model entfernen
			Resource r = persons.getResource(Namespaces.nsPerson + id);
			// remove statements where resource is subject
			persons.removeAll(r, null, (RDFNode) null);
			// remove statements where resource is object
			persons.removeAll(null, null, r);
			
			System.out.println("----------------------------------------------------\n");
			System.out.println("Model m:\n");
			RDFDataMgr.write(System.out, persons, Lang.TURTLE);
			System.out.println("----------------------------------------------------\n");
			System.out.println("Model m_deleted:\n");
			RDFDataMgr.write(System.out, deletedpersons, Lang.TURTLE);
	}
	
	public static void updatePerson(Person person)
	{
		Resource F_personToEdit = getPersonModel().getResource(Namespaces.nsPerson + person.getId());
		if(person.getId() != -1)
		{
			PersonFactory.changeResource(F_personToEdit, getPersonModel(), person);
		}

		System.out.println("----------------------------------------------------\n");
		System.out.println("editPerson: Model m: NACHER: \n");
		RDFDataMgr.write(System.out, getPersonModel(), Lang.TURTLE);
	}



	
	public static Person getPerson(Integer id)
	{
		return PersonFactory.loadPersonFromURI( Namespaces.nsPerson + id ,  getPersonModel());
	}


	/*public static void getAllPersons(){
		Query query = QueryFactory.create("SELECT  ?person ?v ?o WHERE {?person ?v ?o }");
		QueryExecution qexec = QueryExecutionFactory.create(query, getPersonModel()) ;

		ResultSet F_allData  = qexec.execSelect();


	}*/

	public static void printPerson(){
		System.out.println("No Persons found");
		RDFDataMgr.write(System.out, getPersonModel(), Lang.TURTLE);
	}


	public static List<Person> filtern(String Ps_feldWert)
	{
		Model personsModel = getPersonModel();
		List<Person> persons = new ArrayList<Person>();

		try
		{

			ResultSet F_personsWithValue  = rdfHelper.executeSelect("SELECT  ?person ?v ?o  WHERE {?person ?v ?o. "+
					"FILTER regex(?o, \"" +Ps_feldWert + "\", \"i\")}", personsModel); //i means insensitive


			List<Person> newPersons =  PersonFactory.getPersonFromResultSet(F_personsWithValue, getPersonModel());

			//persons = PersonFactory.getPersonFromResultSet(F_personsWithValue, getPersonModel());

		return newPersons;


		/*	for(Person newPerson : newPersons){
				if(newPerson.getGender().contains(Ps_feldWert) || newPerson.getAdress().contains(Ps_feldWert)){
					persons.add(newPerson);
				}*/

			//loadPersons by addresss
		/*	while(F_personsWithValue.hasNext())
			{
				QuerySolution qsPerson = F_personsWithValue.nextSolution();
				String uriString = qsPerson.get("person").toString();
				if(uriString.contains(Namespaces.nsGender)){ //address is nsX
					ResultSet personResultSet  = rdfHelper.executeSelect("SELECT  ?person ?v ?o WHERE {?person ?v <" + uriString + "> }", m);
					// alle Personendaten ausgeben, die den Filterkriterien entsprechen
					List<Person> newPersons = PersonFactory.getPersonFromResultSet(personResultSet, getPersonModel());
					for(Person newPerson : newPersons){
						if(newPerson.getGender().contains(Ps_feldWert) || newPerson.getAdress().contains(Ps_feldWert)){
							persons.add(newPerson);
						}
					}

				}*/

		}



		catch(Exception exc){
			System.out.println(exc);
		}
		return persons;
	}






	
	public static Model getPersonModel() {
		return m;
	}

	public static void setPersonModel(Model P_model) {
		PersonDataset.m = P_model;
	}
	
	public static Model getPersonDeletedModel() {
		return m_deleted;
	}

	public static void setPersonDeletedModel(Model P_model) {
		PersonDataset.m_deleted = P_model;
	}
}
