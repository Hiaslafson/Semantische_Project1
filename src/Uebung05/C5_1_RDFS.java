package Uebung05;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;


public class C5_1_RDFS {


   public static void main(String[] args) {
      String rdfsFile = C5_1_RDFS.class.getResource("family_.ttl").getPath();

      System.out.println("--- MODEL ---");
      Model model = RDFDataMgr.loadModel(rdfsFile);
      RDFDataMgr.write(System.out, model, Lang.TURTLE);

      System.out.println("--- INF-MODEL ---");
      InfModel inf = ModelFactory.createRDFSModel(model);
      System.out.println(inf.getReasoner());
      RDFDataMgr.write(System.out, inf, Lang.TURTLE);

      
      String whereClause =
    		  "?a rdf:type :Woman; :marriedTo ?b."    		  
//    		  "?s :spouse ?o."
//    		  "?p a ?Person."
    		  ;
      
      String queryStr =             
    		  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
              "PREFIX : <http://example.org/>\n"+
              "SELECT * "
              + "WHERE { "+whereClause+" }";
    		  
      
      Query query = QueryFactory.create(queryStr);
 
      
      try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
         ResultSet results = qexec.execSelect() ;
         ResultSetFormatter.out(System.out, results, query) ;
      }       
     
      try (QueryExecution qexec = QueryExecutionFactory.create(query, inf)) {
         ResultSet results = qexec.execSelect() ;
         ResultSetFormatter.out(System.out, results, query) ;
      }          
  
       
   }

}
