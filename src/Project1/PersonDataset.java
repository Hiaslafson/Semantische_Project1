package Project1;
import Uebung05.C5_2_UpdateTDB;
import org.apache.jena.assembler.Mode;
import org.apache.jena.query.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDB;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
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

public class PersonDataset {
	public static Dataset dataset;
	private static Model m, m_deleted;
	private static int Id = 0;

//	public static String source = "C:\\Users\\MatthiasW\\Documents\\Semantische_Project1\\src\\Project1\\tdb\\person.rdf";


//Testdaten


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

		//addPerson(p1);
		//addPerson(p2);
	}

	public PersonDataset() {

		dataset = TDBFactory.assembleDataset(
				PersonDataset.class.getResource("tdb-assembler.ttl").getPath());

		//m = dataset.getDefaultModel();
		//m = dataset.getNamedModel("DatasetTDB");
		m = ModelFactory.createDefaultModel();
		setPersonModel(m);
		m_deleted = ModelFactory.createDefaultModel();
		getPersonModel().setNsPrefix("x", Namespaces.nsX);
		getPersonModel().setNsPrefix("person", Namespaces.nsPerson);
		getPersonModel().setNsPrefix("vcard", VCARD.getURI());
		//RDFDataMgr.write(System.out, getPersonModel(), Lang.TURTLE);
		addModels();

	}
	
	/* public static void addPerson(Person person)
	{

		PersonFactory.createResourcePerson(person, getPersonModel());
	} */

	public static void addPerson(Person p) {
		Id = Id +1;
		dataset.begin(ReadWrite.WRITE); // START TRANSACTION
		try {
			UpdateRequest request = UpdateFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
					"PREFIX person: <http://www.example/person/>\n" +
					"PREFIX st:     <http://www.example/st.rdf#>\n" +
					"PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
					"\n" +
					"INSERT DATA { \n" +
					"\tGRAPH st:defaultGraph {\n" +
					"\tperson:p" + Id + " a st:Person;\n" +
					"\t    vcard:UID \"" + Id + "\";\n" +
					"\t    vcard:Name \"" + p.getName() + "\";\n" +
					"\t    st:gender \"" + p.getGender() + "\";\n" +
					"\t    vcard:ADR \"" + p.getAdress() + "\";\n" +
					"\t    st:employer \"" + p.getEmployer() + "\";\n" +
					"\t    st:birthday \"" + p.getDate() + "\";\n" +
					"\t    .\n" +
					"\t}\n" +
					"}");
			UpdateAction.execute(request, dataset);
			dataset.commit();
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			dataset.abort(); //
		} finally {
			dataset.end(); // END TRANSACTION (ABORT IF NO COMMIT)
		}
	}

	/* public static void deletePerson(int id) {
		Model persons = getPersonModel();
		Model deletedpersons = getPersonDeletedModel();

		Resource F_personToDelete = persons.getResource(Namespaces.nsPerson + id);

		StmtIterator stmts = persons.listStatements(F_personToDelete, null, (RDFNode) null);


		deletedpersons.add(persons.listStatements(F_personToDelete, null, (RDFNode) null));

		Resource r = persons.getResource(Namespaces.nsPerson + id);
		persons.removeAll(r, null, (RDFNode) null);
		persons.removeAll(null, null, r);


		System.out.println("Model m:\n");
		RDFDataMgr.write(System.out, persons, Lang.TURTLE);

		System.out.println("Model m_deleted:\n");
		RDFDataMgr.write(System.out, deletedpersons, Lang.TURTLE);
	}*/
	public static void deletePerson(int id) {
		//Person by ID
		//get The Person
		//Insert in deltetGraph
		//delete Person
		List<String> x = new ArrayList<>();
 		dataset.begin(ReadWrite.READ);
		try {
			Query q = QueryFactory.create("SELECT ?person ?o ?v ?g WHERE {{?person ?o ?v} UNION {GRAPH ?g {?person ?o ?v}}}");
			try (QueryExecution qEx = QueryExecutionFactory.create(q, dataset)) {
				ResultSet res = qEx.execSelect();
				Model m = res.getResourceModel();

				ResultSetFormatter.out(System.out, res, q);
				ResultSetFormatter.toList(res);

			}
		} finally {
			dataset.end();
		}

	}

	public static void updatePerson(Person person) {
		Resource F_personToEdit = getPersonModel().getResource(Namespaces.nsPerson + person.getId());
		if (person.getId() != -1) {
			PersonFactory.changeResource(F_personToEdit, getPersonModel(), person);
		}

	}


	public static Person getPerson(Integer id) {
		return PersonFactory.loadPersonFromURI(Namespaces.nsPerson + id, getPersonModel());
	}

	public static List<Person> getAllPerson() {
		Model personsModel = getPersonModel();
		//String filter = "";
		List<Person> persons = new ArrayList<Person>();


		ResultSet r_personsWithValue = executeSelectFilter("SELECT  ?person ?v ?o  WHERE {?person ?v ?o. }", personsModel);


		persons = PersonFactory.getPersonFromResultSet(r_personsWithValue, getPersonModel());

		//persons = PersonFactory.getPersonFromResultSet(F_personsWithValue, getPersonModel());

		return persons;


	}





	/* public static void printPerson(){
		RDFDataMgr.write(System.out, getPersonModel(), Lang.TURTLE);

	} */

	public static List<Person> getPerson() {
		List<Person> persons = new ArrayList<>();
		dataset.begin(ReadWrite.READ);
		try {
			Query q = QueryFactory.create("SELECT ?person ?o ?v ?g WHERE {{?person ?o ?v} UNION {GRAPH ?g {?person ?o ?v}}}");
			try (QueryExecution qEx = QueryExecutionFactory.create(q, dataset)) {
				ResultSet res = qEx.execSelect();

				persons = extractPersonList(res);

				//ResultSetFormatter.out(System.out, res, q);

				//System.out.println(f.getResource("UID"));


			}
		} finally {
			dataset.end();
		}
		return persons;
	}

	public static void filtern(String filter) {

		dataset.begin(ReadWrite.READ);
		try {
			Query q = QueryFactory.create("SELECT ?person ?o ?v ?g WHERE {{?person ?o ?v. FILTER regex(?v,\"" + filter  + "\", \"i\")} UNION {GRAPH ?g {?person ?o ?v" +
					"}}}");
			try (QueryExecution qEx = QueryExecutionFactory.create(q, dataset)) {
				ResultSet res = qEx.execSelect();


				ResultSetFormatter.out(System.out, res, q);
			}
		} finally {
			dataset.end();
		}
	}




/*	public static List<Person> filtern(String filter)
	{
		Model personsModel = getPersonModel();
		List<Person> persons = new ArrayList<Person>();

		try
		{

			ResultSet r_personsWithValue  = executeSelectFilter("SELECT  ?person ?v ?o  WHERE {?person ?v ?o. "+
					"FILTER regex(?o, \"" +filter + "\", \"i\")}", personsModel);


			List<Person> newPersons =  PersonFactory.getPersonFromResultSet(r_personsWithValue, getPersonModel());

			//persons = PersonFactory.getPersonFromResultSet(F_personsWithValue, getPersonModel());

		return newPersons;


		}



		catch(Exception exc){
			System.out.println(exc);
		}
		return persons;
	} */

	public static ResultSet executeSelectFilter(String sql, Model m){
		Query query = QueryFactory.create(sql);
		QueryExecution qexec = QueryExecutionFactory.create(query, m) ;
		try {
			if (query.isSelectType()) {
				ResultSet results = qexec.execSelect() ;
				return results;
			}
			else
			{
				throw new RuntimeException("Wrong query");
			}
		}
		catch(Exception exc){
			System.out.println(exc.toString());
		}
		return null;
	}


	public static List<Person> extractPersonList(ResultSet r){
		List<Person> persons =  new ArrayList<>();
		Person p = new Person();
		QuerySolution qs = r.nextSolution();
		String s = qs.get("person").toString();
		while(r.hasNext()){
		 qs = r.nextSolution();

			if(s != qs.get("person").toString()){
				persons.add(p);
				p = new Person();
			}

			s = qs.get("person").toString();
			System.out.println(s);
			System.out.println(qs.get("v").toString());

			switch (qs.get("o").toString()){
				case "http://www.w3.org/2001/vcard-rdf/3.0#UID":
					p.setId(Integer.parseInt(qs.get("v").toString()));
				case "http://www.w3.org/2001/vcard-rdf/3.0#Name":
					p.setName(qs.get("v").toString());
				case "http://www.example/st.rdf#gender":
					p.setGender(qs.get("v").toString());
				case "http://www.w3.org/2001/vcard-rdf/3.0#ADR":
					p.setAdress(qs.get("v").toString());
				case "http://www.example/st.rdf#employer":
					p.setEmployer(qs.get("v").toString());
				case "http://www.example/st.rdf#birthday":
					p.setDate(qs.get("v").toString());
			}

		}


		persons.add(p);
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
