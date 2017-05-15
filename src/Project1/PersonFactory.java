package Project1;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.VCARD;


public class PersonFactory
{
	private static int Id = 0;
		
	public static Resource createResourcePerson(Person p, Model m)
	{
		if(p == null)
		{
			return null;
		}
		//generate ID
		if(p.getId() < 0)
		{
			Id = Id +1;
			p.setId(Id);
		}
		
		Resource r_person = m.createResource(Namespaces.nsPerson + p.getId());
		Property gender = m.createProperty(Namespaces.nsGender);
		Property employer = m.createProperty(Namespaces.nsXEmployer);


		r_person.addProperty(VCARD.UID, m.createTypedLiteral(p.getId()))
				.addProperty(VCARD.ADR, m.createLiteral(p.getAdress()))
				.addProperty(VCARD.NAME, m.createLiteral(p.getName()))
				.addProperty(VCARD.BDAY, m.createTypedLiteral(p.getDate()))
				.addProperty(gender, m.createLiteral(p.getGender()))
				.addProperty(employer, m.createLiteral(p.getEmployer()));


		return r_person;
	}
	
	public static Resource getExternResource(String uri)
	{
		Model externResourceModel = ModelFactory.createDefaultModel();
		externResourceModel.read(uri);
		Resource externResource =  externResourceModel.getResource(uri);
		

		return externResource;
	}


	public static List<Person> getPersonFromResultSet(ResultSet aResultSet, Model m){

		String temp ="";
		
		List<Person> persons = new ArrayList<Person>();
		while(aResultSet.hasNext())
		{


			QuerySolution qPerson = aResultSet.nextSolution();
	        String uriString = qPerson.get("person").toString();
	        if (!temp.equals(uriString)){
				if(uriString.contains(Namespaces.nsPerson)){
					try{
						Person person = loadPersonFromURI(uriString, m);
						if(!persons.contains(person)){
							persons.add(person);
						}

					}
					catch(Exception exc){
						System.out.print(exc);

					}
					temp = uriString;
	        }	       
		}}
		return persons;
	}
	public static Person loadPersonFromURI(String uriString, Model m){
		Person person = new Person(Integer.parseInt(uriString.replace(Namespaces.nsPerson, "")));
		Property gender = m.createProperty(Namespaces.nsGender);
		Property employer = m.createProperty(Namespaces.nsXEmployer);

		Resource personResource = m.getResource(uriString);
        person.setName(personResource.getProperty(VCARD.NAME).getString());
       	person.setEmployer(personResource.getProperty(employer).getString());
        person.setGender(personResource.getProperty(gender).getString());
        person.setDate(personResource.getProperty(VCARD.BDAY).getString());
		person.setAdress(personResource.getProperty(VCARD.ADR).getString());
        
        return person;
	}

	public static void changeResource(Resource r_persons, Model m, Person person){
		StmtIterator stmts = m.listStatements(r_persons, null, (RDFNode)null);
		 

		Property geschlecht = m.createProperty(Namespaces.nsGender);
		Property employer = m.createProperty(Namespaces.nsXEmployer);
			
		ArrayList<Statement> statementList=new ArrayList<Statement>();
		while (stmts.hasNext())
		{
			statementList.add(stmts.nextStatement());
		}
		  
		for (int i=0; i < statementList.size(); i++)
		{
			Statement F_stmt=statementList.get(i);

			if(F_stmt.getPredicate().toString().contains(VCARD.NAME.toString()))
			{
				if(!F_stmt.getObject().equals(person.getName()))
				{
					F_stmt.changeObject(m.createLiteral(person.getName()));
				}
			}
			else if(F_stmt.getPredicate().toString().contains(VCARD.BDAY.toString()))
			{
				if(!F_stmt.getObject().equals(person.getDate().toString()))
				{
					F_stmt.changeObject(m.createLiteral(person.getDate().toString()));
				}
			}
			else if(F_stmt.getPredicate().toString().contains(geschlecht.toString()))
			{
				if(!F_stmt.getObject().equals(person.getGender()))
				{
					F_stmt.changeObject(m.createLiteral(person.getGender()));
				}
			}
			else if(F_stmt.getPredicate().toString().contains(VCARD.ADR.toString()))
			{
				if(!F_stmt.getObject().equals(person.getAdress()))
				{
					F_stmt.changeObject(m.createLiteral(person.getAdress()));
				}			
			}
			else if(F_stmt.getPredicate().toString().contains(employer.toString()))
			{
				if(!F_stmt.getObject().equals(person.getEmployer()))
				{
					F_stmt.changeObject(m.createLiteral(person.getEmployer()));
				}
			}
		}
	}






}
