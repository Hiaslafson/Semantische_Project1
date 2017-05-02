package Uebung04;

import java.io.File;

import org.apache.jena.query.Dataset;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;


public class C3_UpdateDataset {
 
   public static void executeUpdate(Dataset ds, File queryFile) {
	  UpdateRequest request = UpdateFactory.read(queryFile.getPath());
	  System.out.println(request);
	  UpdateAction.execute(request, ds) ;
	  RDFDataMgr.write(System.out, ds, Lang.TRIG);
   }
 
   
   
	public static void main(String[] args) {
		
	      String datasetUri = C2_QueryDataset.class.getResource("social.trig").getPath();

		  Dataset ds = RDFDataMgr.loadDataset(datasetUri);
	            
	      File queryDir = new File(C1_UpdateModel.class.getResource("updates2").getPath());     
		
		File[]   files = queryDir.listFiles();
		
      for (int i = 0; i < files.length; i++) {
         File file = files[i];
	      System.out.println("\n\n------------------------------");
         System.out.println(" " + file.getName() + ":");
	      System.out.println("------------------------------");
	      executeUpdate(ds,file);
	   }
	}
}
