package Project1;

import com.sun.istack.internal.Nullable;
import org.apache.jena.query.*;
import org.apache.jena.tdb.TDB;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.TDBLoader;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDFS;

import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;



/**
 * Created by MatthiasW on 02.05.2017.
 */
public class RDFHelper {
    Dataset dataset;

        public RDFHelper(){
        //Initialize
        dataset = TDBFactory.assembleDataset(
                project1.class.getResource("tdb-assembler.ttl").getPath()) ;
    }
    public void Insert(Person p){

        //Model m = ModelFactory.createDefaultModel();
        dataset.begin(ReadWrite.WRITE);
        try {
            Model m = dataset.getDefaultModel();
         //   dataset.mo

            //Set Namespace Prefixes
            String nsX = "http://www.example/x.rdf#";
            String nsPersons = "http://www.example/persons/";
            String nsFoaf = FOAF.getURI();
            m.setNsPrefix("person", nsPersons);
            m.setNsPrefix("foaf", nsFoaf);
       //     m.setNsPrefix("rdfs", RDFS.getURI());

            //Create Resources
            Resource person = m.createResource(nsPersons + p.name);
         //   Property address = m.createProperty(nsFoaf + "address");
       //     Property employer = m.createProperty(nsFoaf + "employer");


            //Add Statements/Triples
         //   person.addProperty(FOAF.gender, m.createLiteral(p.gender));
           //  person.addProperty(address, m.createLiteral(p.adress));
            // person.addProperty(FOAF.birthday, m.createLiteral(p.date));
         //   person.addProperty(employer, m.createLiteral(p.employer));
            m.add(person, FOAF.name, p.name);


            //   TDBLoader.loadModel(m, );
            //Serialize in Turtle
        //   RDFDataMgr.write(System.out, m, Lang.TURTLE);

          //  TDB.sync(dataset);

            dataset.commit();
        }finally{
            dataset.end();
        }
    }



    public int Update(Person person, String name){
        return 1;

    }


    public int Delete(String name){

        return 1;
    }
    @Nullable
    public Person Get(String name){

        Query q = QueryFactory.create("SELECT ?s ?p ?o ?g WHERE {{?s ?p ?o} }");
        try (QueryExecution qEx = QueryExecutionFactory.create(q,dataset) ) {
            ResultSet res = qEx.execSelect();
            ResultSetFormatter.out(System.out, res, q);
        }




        return new Person();
    }
}
