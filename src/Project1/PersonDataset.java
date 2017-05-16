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

	private static int Id = 0;


	public PersonDataset() {


		dataset = TDBFactory.assembleDataset(
				PersonDataset.class.getResource("tdb-assembler.ttl").getPath());

		List<Person> persons = getPersons();
		for(Person p : persons){
			Id = p.getId();
		}



	}


	public static void addPerson(Person p) {
		Id = Id + 1;
		dataset.begin(ReadWrite.WRITE); // START TRANSACTION
		try {
			UpdateRequest request = UpdateFactory.create(
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
					"PREFIX person: <http://www.example/person/>" +
					"PREFIX st:     <http://www.example/st.rdf#>" +
					"PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>" +
					"INSERT DATA { " +
					"GRAPH st:actualGraph {" +
					"person:p" + Id + " a st:Person;" +
					"    vcard:UID \"" + Id + "\";" +
					"    vcard:Name \"" + p.getName() + "\";" +
					"    st:gender \"" + p.getGender() + "\";" +
					"    vcard:ADR \"" + p.getAdress() + "\";" +
					"    st:employer \"" + "<http://www.example/person/p" + p.getEmployer() + ">\";" +
					"    st:birthday \"" + p.getDate() + "\";" +
					".}}"
			);
			UpdateAction.execute(request, dataset);
			dataset.commit();
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			dataset.abort(); //
		} finally {
			dataset.end(); // END TRANSACTION (ABORT IF NO COMMIT)
		}


	}


	public static void deletePerson() {
		//Person by ID
		//get The Person
		//Insert in deltetGraph
		//delete Person
		List<String> x = new ArrayList<>();
		dataset.begin(ReadWrite.READ);
		try {
			Query q = QueryFactory.create(  "DELETE {?v :1 }\n" +
					"INSERT {?p :age ?age_new}\n" +
					"WHERE " +
					"  { ?p a :Person. " +
					"    ?p :age ?age_old." +
					"  }");
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

	public static void updatePerson(Person p) {

		dataset.begin(ReadWrite.WRITE); // START TRANSACTION
		try {
			UpdateRequest request = UpdateFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
					"PREFIX person: <http://www.example/person/>\n" +
					"PREFIX st:     <http://www.example/st.rdf#>\n" +
					"PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
					"\n" +
					"INSERT DATA { \n" +
					"\tGRAPH st:defaultGraph {\n" +
					"\tperson:p" + p.getId() + " a st:Person;\n" +
					"\t    vcard:UID \"" + p.getId() + "\";\n" +
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


	public static List<Person> getPersons() {
		List<Person> persons = new ArrayList<>();
		dataset.begin(ReadWrite.READ);
		try {
			Query q = QueryFactory.create("SELECT * WHERE {{?person ?o ?v} UNION {GRAPH ?g {?person ?o ?v}}}");
			try (QueryExecution qEx = QueryExecutionFactory.create(q, dataset)) {
				ResultSet res = qEx.execSelect();
				if(res.hasNext()) {
					//ResultSetFormatter.out(System.out, res, q);
					persons = extractPersonList(res);

					// ResultSetFormatter.out(System.out, res, q);

					//System.out.println(f.getResource("UID"));
				}else{
					Person p = new Person();
					p.setId(0);
					persons.add(p);
					return persons;
				}

			}
		} finally {
			dataset.end();
		}
		return persons;
	}

	public static Person getPersonByID(int id) {
		Person p = new Person();
		List<Person> persons = new ArrayList<>();
		dataset.begin(ReadWrite.READ);
		try {
			Query q = QueryFactory.create("SELECT * WHERE {{?person ?o ?v} UNION {GRAPH ?g {?person ?o ?v}}}");
			try (QueryExecution qEx = QueryExecutionFactory.create(q, dataset)) {
				ResultSet res = qEx.execSelect();

				persons = extractPersonList(res);

			}
		} finally {
			dataset.end();
		}
		p = persons.get(id - 1);
		return p;
	}

/*	public static void filtern(String filter) {

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
	} */

	public static List<Person> getPersonByGender(String gender) {
		List<Person> persGender = new ArrayList<>();
		List<Person> persons = new ArrayList<>();
		dataset.begin(ReadWrite.READ);
		try {
			Query q = QueryFactory.create("SELECT * WHERE {{?person ?o ?v} UNION {GRAPH ?g {?person ?o ?v}}}");
			try (QueryExecution qEx = QueryExecutionFactory.create(q, dataset)) {
				ResultSet res = qEx.execSelect();

				persons = extractPersonList(res);

			}
		} finally {
			dataset.end();
		}
		for (Person p : persons) {
			if (p.getGender().equals(gender)) {
				persGender.add(p);
			}

		}
		return persGender;
	}


	public static List<Person> extractPersonList(ResultSet r) {
		List<Person> persons = new ArrayList<>();
		Person p = new Person();
		QuerySolution qs = r.nextSolution();
		String s = qs.get("person").toString();
		while (r.hasNext()) {
			qs = r.nextSolution();

			if (s != qs.get("person").toString()) {
				persons.add(p);
				p = new Person();
			}

			s = qs.get("person").toString();

//Nach Langer suche nur so einen Lösungsvorschlag gefunden
			switch (qs.get("o").toString()) {
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


}