package Uebung06;
import java.io.File;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class DemoMonolithic {

	OutputHandler out;
	
	public DemoMonolithic(OutputHandler out) {
		this.out = out;
	}

	
	public void execute()  throws OWLOntologyCreationException {
		out.output(2, "Start");
		  OWLOntologyManager m=OWLManager.createOWLOntologyManager();
		  //m.addIRIMapper(new AutoIRIMapper(new File("resources"), true));
		  m.addIRIMapper(new SimpleIRIMapper(IRI.create("http://project-best.eu/ontologies/location/"),IRI.create(new File(DemoMonolithic.class.getResource("location.owl").getPath()))));
		  m.addIRIMapper(new SimpleIRIMapper(IRI.create("http://project-best.eu/ontologies/time/"),IRI.create(new File(DemoMonolithic.class.getResource("time.owl").getPath()))));

		  OWLDataFactory df = OWLManager.getOWLDataFactory();
		  OWLOntologyLoaderConfiguration loaderConf = new OWLOntologyLoaderConfiguration();
		  

		  out.output(2, "Start Loading");

		  OWLOntology o=m.loadOntologyFromOntologyDocument(
				  new FileDocumentSource(new File(DemoMonolithic.class.getResource("multidimensional.owl").getPath())),
				  loaderConf);
		  
		  out.output(2, "Combined Reasoning");

		  OWLReasoner reasoner=new Reasoner.ReasonerFactory().createReasoner(o);
		  System.out.println("Ontology is consistent: " + reasoner.isConsistent());
		  Set<OWLClass> classes = reasoner.getSubClasses(df.getOWLClass(IRI.create("http://project-best.eu/ontologies/multidimensional/MD")), false).getFlattened();
		  for(OWLClass c : classes) {
			  System.out.println("Class: " + c.getIRI().getFragment());
			  System.out.print("  Subclasses: ");
			  for(Node<OWLClass> n : reasoner.getSubClasses(c, true)) 
				  for(OWLClass s : n.getEntities()) {
					  System.out.print(" " + s.getIRI().getFragment());
				  }
			  System.out.println();
			  
			  System.out.print("  Disjoint clases: ");
			  for(Node<OWLClass> n : reasoner.getDisjointClasses(c)) 
				  for(OWLClass s : n.getEntities()) {
					  System.out.print(" " + s.getIRI().getFragment());
				  }
			  System.out.println();			  
		  }
		  	  
		  out.output(2, "END");
		  
		
	} 
	
	public static void main(String[] args) throws OWLOntologyCreationException {
		DemoMonolithic demo = new DemoMonolithic(new OutputHandler(5));
		demo.execute();
		
	}

}
