package Uebung04;

import java.io.File;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;


public class C2_QueryDataset {
 
   public static void executeQuery(Dataset ds, File queryFile) {
      Query query = QueryFactory.read(queryFile.getPath());
      System.out.println(query);
      System.out.println("------------------------------");
      
      Op op = Algebra.compile(query);
      System.out.println(op.toString(query.getPrefixMapping()));
      System.out.println("------------------------------");
      
      QueryExecution qexec = QueryExecutionFactory.create(query, ds) ;
      try {
         if (query.isConstructType()) { 
            Model results = qexec.execConstruct() ;
            RDFDataMgr.write(System.out, results, Lang.TURTLE);
         } else if (query.isSelectType()) { 
            ResultSet results = qexec.execSelect() ;
            ResultSetFormatter.out(System.out, results, query) ;
         } else if (query.isAskType()) {
            System.out.println( (qexec.execAsk())? "-------\n| YES |\n-------" : "------\n| NO |\n------");
         } else if (query.isDescribeType()) {   
            Model results = qexec.execDescribe() ; 
            RDFDataMgr.write(System.out, results, Lang.TURTLE);
         }
      } finally { qexec.close() ; }         
   }
 
   
   
	public static void main(String[] args) {
		
		String datasetUri = C2_QueryDataset.class.getResource("social.trig").getPath();
      File queryDir = new File(C2_QueryDataset.class.getResource("query").getPath());     
		
		Dataset ds = RDFDataMgr.loadDataset(datasetUri);
		File[]   files = queryDir.listFiles();

		
      for (int i = 0; i < files.length; i++) {
         File file = files[i];
	      System.out.println("\n\n------------------------------");
         System.out.println(" " + file.getName() + ":");
	      System.out.println("------------------------------");
	      executeQuery(ds,file);
	   }
	}
}
