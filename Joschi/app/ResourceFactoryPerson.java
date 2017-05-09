package app;

import model.Adresse;
import model.Person;

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


public class ResourceFactoryPerson
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
			p.setId(Id);
			Id = Id +1;
		}
		
		Resource F_person = m.createResource(Namespaces.nsPerson + p.getId());
		Property geschlecht = m.createProperty(Namespaces.nsGeschlecht);
		Property hasWork = m.createProperty(Namespaces.nsHasWork);
		Resource address = createAdressresource(p.getAdresse(), m);
		
		F_person.addProperty(VCARD.UID, m.createTypedLiteral(p.getId()))
				.addProperty(VCARD.ADR, address)
				.addProperty(VCARD.Given, m.createLiteral(p.getVorname()))
				.addProperty(VCARD.Family, m.createLiteral(p.getNachname()))
				.addProperty(VCARD.BDAY, m.createTypedLiteral(p.getGebDatum()))
				.addProperty(geschlecht, m.createLiteral(p.getGeschlecht()))
				.addProperty(hasWork, m.createLiteral(p.getArbeitgeber()));

		return F_person;
	}
	
	public static Resource getExternResource(String uri)
	{
//		RDFDataMgr.read(dbpediaCountry , "http://de.dbpedia.org/resource/" + Ps_stadt) ;
		Model externResourceModel = ModelFactory.createDefaultModel();
		externResourceModel.read(uri);
		Resource externResource =  externResourceModel.getResource(uri);
		
		//Überprüft ob die Resource auch korrekt ausgelesen wurde
//		dbpediaCountry.write(System.out);
//		System.out.println(dbpediaCountryResource.getURI());
//		StmtIterator stmtit = dbpediaCountryResource.listProperties();
//		while(stmtit.hasNext()){
//			Statement s = stmtit.next();
//			System.out.println(s.toString());
//		}
		return externResource;
	}

	/**
	 * Diese Methode konvertiert ein ResultSet in eine Personenliste
	 * Das erste Attribut muss ?person heißen.
	 * @param F_persons
	 * @return
	 */
	public static List<Person> getPersonFromResultSet(ResultSet allResultSet, Model m){
		
		List<Person> persons = new ArrayList<Person>();
		//ResultSetFormatter.out(System.out, F_persons);
		while(allResultSet.hasNext())
		{
			// alle Personendaten ausgeben, die den Filterkriterien entsprechen
	        QuerySolution qsPerson = allResultSet.nextSolution();
	        String uriString = qsPerson.get("person").toString();
	        
	        if(uriString.contains(Namespaces.nsPerson)){
		        try{
		        	Person person = loadPersonFromURI(uriString, m);
		        	if(!persons.contains(person)){
			        	persons.add(person);
		        	}

		        }
		        catch(Exception exc){
		        	System.out.print(exc);
		        	/**Es kann sein, dass andere resourcen sich im Ergebnis befinden als personen*/
		        	/**Dies wird hier gefangen*/
		        }
	        }	       
		}
		return persons;
	}
	
	public static Person loadPersonFromURI(String uriString, Model m){
		Person person = new Person(Integer.parseInt(uriString.replace(Namespaces.nsPerson, "")));
		Property geschlecht = m.createProperty(Namespaces.nsGeschlecht);
		Property hasWork = m.createProperty(Namespaces.nsHasWork);
		
    	Resource personResource = m.getResource(uriString);
        person.setNachname(personResource.getProperty(VCARD.Family).getString());
        person.setVorname(personResource.getProperty(VCARD.Given).getString());
        person.setArbeitgeber(personResource.getProperty(hasWork).getString());
        person.setGeschlecht(personResource.getProperty(geschlecht).getString());
        person.setGebDatum(  Date.valueOf(personResource.getProperty(VCARD.BDAY).getString()));
        
        String addressString = String.valueOf( personResource.getProperty(VCARD.ADR).getObject());
        Resource adressResource = m.getResource(addressString);
        Adresse adresse = new Adresse();
        adresse.setStrasse(adressResource.getProperty(VCARD.Street).getString());
        adresse.setStadt(adressResource.getProperty(VCARD.Locality).getString());
        adresse.setPlz(adressResource.getProperty(VCARD.Pcode).getString());
        adresse.setCountry(String.valueOf(adressResource.getProperty(VCARD.Country).getObject().toString()));
        person.setAdresse(adresse);
        
        return person;
	}
	
	public static void changeResource(Resource F_persons, Model m, Person person){
		StmtIterator stmts = m.listStatements(F_persons, null, (RDFNode)null);
		 
		System.out.println("----------------------------------------------------\n");
		System.out.println("editPerson: Model m: VORHER: \n");
		RDFDataMgr.write(System.out, m, Lang.TURTLE);
		Property geschlecht = m.createProperty(Namespaces.nsGeschlecht);
		Property hasWork = m.createProperty(Namespaces.nsHasWork);
			
		ArrayList<Statement> statementList=new ArrayList<Statement>();
		while (stmts.hasNext())
		{
			statementList.add(stmts.nextStatement());
		}
		  
		for (int i=0; i < statementList.size(); i++)
		{
			Statement F_stmt=statementList.get(i);

			if(F_stmt.getPredicate().toString().contains(VCARD.Given.toString()))
			{
				if(!F_stmt.getObject().equals(person.getVorname()))
				{
					F_stmt.changeObject(m.createLiteral(person.getVorname()));
				}
			}
			else if(F_stmt.getPredicate().toString().contains(VCARD.Family.toString()))
			{
				if(!F_stmt.getObject().equals(person.getNachname()))
				{
					F_stmt.changeObject(m.createTypedLiteral(person.getNachname()));
				}
			}
			else if(F_stmt.getPredicate().toString().contains(VCARD.BDAY.toString()))
			{
				if(!F_stmt.getObject().equals(person.getGebDatum().toString()))
				{
					F_stmt.changeObject(m.createLiteral(person.getGebDatum().toString()));
				}
			}
			else if(F_stmt.getPredicate().toString().contains(geschlecht.toString()))
			{
				if(!F_stmt.getObject().equals(person.getGeschlecht()))
				{
					F_stmt.changeObject(m.createLiteral(person.getGeschlecht()));
				}
			}
			else if(F_stmt.getPredicate().toString().contains(hasWork.toString()))
			{
				if(!F_stmt.getObject().equals(person.getArbeitgeber()))
				{
					F_stmt.changeObject(m.createLiteral(person.getArbeitgeber()));
				}
			}
			else if(F_stmt.getPredicate().toString().contains(VCARD.ADR.toString()))
			{
				try
				{
					Resource address = createAdressresource(person.getAdresse(),  m);
					if(!F_stmt.getObject().equals(address.getURI()))
					{
						F_stmt.changeObject(address);
					}
				}
				catch(Exception exc){
					
				}			
			}
		}
	}
	
	public static Resource createAdressresource(Adresse adress, Model m)
	{
		Resource dbpediaCountryResource =getExternResource(Namespaces.nsDbpedia  + adress.getCountry());
		
		Resource address = m.createResource(Namespaces.nsXAddress + adress.getPlz() /*+ "_" + pAdress.getCountry()*/)
				.addProperty(VCARD.Locality, adress.getStadt())
				.addProperty(VCARD.Street, adress.getStrasse())
				.addProperty(VCARD.Pcode, adress.getPlz())
//				.addProperty(VCARD.Country, adress.getCountry());
				.addProperty(VCARD.Country, dbpediaCountryResource);
		return address;
	}

}
