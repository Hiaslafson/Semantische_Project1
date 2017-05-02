package Uebung01;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
public class Ex01 {
    public static void main(String[] args) {
        Model m = ModelFactory.createDefaultModel();
        m.setNsPrefix("rdfs","http://www.w3.org/2000/01/rdf-schema#");
        m.setNsPrefix("jku","http://www.jku.at/");
        Resource semtech = m.createResource("http://www.jku.at/semtech");
        Resource lva = m.createResource("http://www.jku.at/lva");
        semtech.addProperty(RDF.type, lva);
        semtech.addProperty(RDFS.label, "Semantische Technologien");
        m.write(System.out,"TURTLE");
    }
}