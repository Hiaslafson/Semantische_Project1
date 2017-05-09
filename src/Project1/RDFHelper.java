package Project1;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

/**
 * Created by MatthiasW on 09.05.2017.
 */
public  class rdfHelper {

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

    }
