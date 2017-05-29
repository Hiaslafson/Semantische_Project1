package Tutorial;

import org.apache.jena.query.ResultSetFormatter;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.io.File;
import java.io.InputStream;

/**
 * Created by MatthiasW on 26.05.2017.
 */
public class Sesame {

    RepositoryConnection repoConn;
    Repository repo;
    String baseURI = "http://example.org/";
    String fullURI = "http://example.org/poi/";



    public void init(){
        try {
            File dataDir = new File(System.getProperty("user.dir") + "/DataRdf/rdf");
            repo = createRepository(dataDir);
            //add data
            ;
            //addResource(System.getProperty("user.dir") + "/DataRdf/category.ttl", RDFFormat.TURTLE);
          addResource(System.getProperty("user.dir") + "/DataRdf/poi.ttl",RDFFormat.TURTLE);
        }catch(Exception ex) {
            System.out.println(ex.getLocalizedMessage() + "\n" + ex.getStackTrace().toString());
        }
    }

    private Repository createRepository(File dataDir) throws RepositoryException {
        Repository repos = new SailRepository(new MemoryStore(dataDir));
        repos.initialize();
        return repos;
    }

  //  String filePath, RDFFormat format
    public boolean addResource(String filePath, RDFFormat format) {
        boolean success = false;
        try {
            repoConn = repo.getConnection();

            String filename = "poi.ttl";
            File poiFile = new File(filePath);

            repoConn.add(poiFile, baseURI,format);
            success = true;
        }catch(Exception ex) {
            System.out.println(ex.getLocalizedMessage() + "\n" + ex.getStackTrace().toString());
            success = false;
        }
        finally {
            try {
                repoConn.close();
            }catch(Exception ex1){
                System.out.println("Connection could not be closed:\n" + ex1.getLocalizedMessage());
            }
        }
        return false;
    }




    public void getPOIS() {
        String poiQueryString = "Select ?id ?name ?lat ?long ?count ?postcode ?category Where { ?poi rdf:id ?id. ?poi rdf:name ?name.  ?poi rdf:lat ?lat. ?poi rdf:long ?long. ?poi rdf:count ?count. ?poi rdf:postcode ?postcode. ?poi rdf:category ?category. }";
        TupleQueryResult result = null;
        try{
            repoConn = repo.getConnection();
            TupleQuery tupleQuery = repoConn.prepareTupleQuery(QueryLanguage.SPARQL, poiQueryString);
            result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Value id = bindingSet.getValue("id");
                Value name = bindingSet.getValue("name");
                Value lat = bindingSet.getValue("lat");
                Value lon = bindingSet.getValue("long");
                Value count = bindingSet.getValue("count");
                Value pc = bindingSet.getValue("postcode");
                Value cat = bindingSet.getValue("category");

                // PointofInterest poi = new PointofInterest(new Double(id.stringValue()).intValue(),name.stringValue(),new Double(cat.stringValue()).intValue(),Double.valueOf(lat.stringValue()),Double.valueOf(lon.stringValue()),new Double(count.stringValue()).intValue(),0);
               // pois.add(poi);


                System.out.println("" + id + name + lat + lon + count + pc + cat +"\n");
            }
            result.close();
        } catch(Exception ex) {
            System.out.println(ex.getLocalizedMessage() + "\n" + ex.getStackTrace().toString());
        }
        finally {
            try {
                if(result != null) {
                    result.close();
                }
                repoConn.close();
            }catch(Exception ex1){
                System.out.println("Connection could not be closed:\n" + ex1.getLocalizedMessage());
            }
        }
    }


}
