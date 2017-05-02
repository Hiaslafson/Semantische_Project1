package Uebung05;

import java.io.File;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;


/*
 * Load data into default graph of a TDB and load named graphs
 * Set NamespacePrefixMapping
 */
public class C5_2_UpdateTDB {

	public static void executeUpdate(Dataset ds, File queryFile) {
		System.out.println("\n\n------------------------------");
		System.out.println(" " + queryFile.getName() + ":");
		System.out.println("------------------------------");
		UpdateRequest request = UpdateFactory.read(queryFile.getPath());
		System.out.println(request);
		UpdateAction.execute(request, ds) ;
		System.out.println("------------------------------");
		System.out.println(" Dataset after execution :");
		System.out.println("------------------------------");

		RDFDataMgr.write(System.out, ds, Lang.TRIG);
	}

	public static void main(String[] args) {      
        /* open (and create if not exists) a TDB database */
		//String directory = "c:/dev/semtech/ue05/tdb/dataset1"; //CHANGE TO A DIRECTORY ON YOUR FILE-SYSTEM
		//Dataset dataset = TDBFactory.createDataset(directory);

		// Alternative: Use assembler file - see https://jena.apache.org/documentation/tdb/assembler.html for more information
		Dataset dataset = TDBFactory.assembleDataset(
				C5_2_UpdateTDB.class.getResource("tdb-assembler.ttl").getPath()) ;
		
		File updatesDir = new File(C5_2_UpdateTDB.class.getResource("updates").getPath());     
		File[] updates = updatesDir.listFiles();

		dataset.begin(ReadWrite.WRITE); // START TRANSACTION
		try {
			dataset.getDefaultModel().removeAll(); // delete everything from default model
			dataset.commit();
		} finally { dataset.end(); } // END TRANSACTION (ABORT IF NO COMMIT)

		/* execute every update request in its own transaction */
		for (int i = 0; i < updates.length; i++) {
			dataset.begin(ReadWrite.WRITE); // START TRANSACTION
			try {
				executeUpdate(dataset,updates[i]);
				dataset.commit();
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
				dataset.abort(); //
			} finally { 
				dataset.end(); // END TRANSACTION (ABORT IF NO COMMIT)
			} 
		}

		dataset.begin(ReadWrite.READ);  
		try {
			Query q = QueryFactory.create("SELECT ?s ?p ?o ?g WHERE {{?s ?p ?o} UNION {GRAPH ?g {?s ?p ?o}}}");
			try (QueryExecution qEx = QueryExecutionFactory.create(q,dataset) ) {
				ResultSet res = qEx.execSelect();
				ResultSetFormatter.out(System.out, res, q);
			}
		} finally { dataset.end(); }

		dataset.close(); //CLOSE DB-CONNECTION 

	}

}
