package app;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class Utils {
	
	
	public static Model executeConstruct(String sql, Model m){
		Query query = QueryFactory.create(sql);
		QueryExecution qexec = QueryExecutionFactory.create(query, m) ;
		try {
			if (query.isConstructType()) { 
	            Model results = qexec.execConstruct() ;
	            return results;
	         }
			else{
				throw new RuntimeException("Wrong query type.");
			}
		 } finally { qexec.close() ; }   
	}
		
	public static ResultSet executeSelect(String sql, Model m){
		Query query = QueryFactory.create(sql);
		QueryExecution qexec = QueryExecutionFactory.create(query, m) ;
		try {
			if (query.isSelectType()) { 
	            ResultSet results = qexec.execSelect() ;
	            return results;
	         }
	         else
	         {
				throw new RuntimeException("Wrong query type.");
	         }
	      } 
		catch(Exception exc){
			System.out.println(exc.toString());
		}
		return null;
	}

	public static boolean executeAsk(String sql, Model m){
		Query query = QueryFactory.create(sql);
		QueryExecution qexec = QueryExecutionFactory.create(query, m) ;
		try {
			if (query.isAskType()) {
		           return qexec.execAsk();
			} 
			else
	        {
				throw new RuntimeException("Wrong query type.");
	        }
		}finally { qexec.close() ; }   
	}
	
	public static Model executeDescribe(String sql, Model m){
		Query query = QueryFactory.create(sql);
		QueryExecution qexec = QueryExecutionFactory.create(query, m) ;
		try {
			if (query.isDescribeType()) {   
				return qexec.execDescribe() ; 
			}
			else
	        {
				throw new RuntimeException("Wrong query type.");
	        }
		}finally { qexec.close() ; } 	
	}
}
