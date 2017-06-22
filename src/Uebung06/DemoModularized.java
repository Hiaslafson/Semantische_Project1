package Uebung06;
import java.io.File;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class DemoModularized {

	OutputHandler out;
	
	public DemoModularized(OutputHandler out) {
		this.out = out;
	}

	public void importInferences(OWLOntology oCombined, OWLOntologyDocumentSource ontoSource) throws OWLOntologyCreationException {
		  OWLOntologyManager m=OWLManager.createOWLOntologyManager();
		  OWLDataFactory df = OWLManager.getOWLDataFactory();

		  OWLOntology o = m.loadOntologyFromOntologyDocument(ontoSource);
		  		  
		  OWLReasoner reasoner=new Reasoner.ReasonerFactory().createReasoner(o);
		  //System.out.println("Ontology " + o.getOntologyID() + " is consistent: " + reasoner.isConsistent());
		  Set<OWLClass> classes = o.getClassesInSignature();
		  for(OWLClass c : classes) {
			  //System.out.println("Class: " + c.getIRI().getFragment());
			  //System.out.print("  Subclasses: ");
			  for(Node<OWLClass> n : reasoner.getSubClasses(c, true)) 
				  for(OWLClass s : n.getEntities()) {
					  //System.out.print(" " + s.getIRI().getFragment());
					  OWLAxiom axiom = df.getOWLSubClassOfAxiom(s, c);
					  AddAxiom addAxiom = new AddAxiom(oCombined, axiom);
					  m.applyChange(addAxiom);
				  }
//			  System.out.println();
			  
//			  System.out.print("  Disjoint clases: ");
			  for(Node<OWLClass> n : reasoner.getDisjointClasses(c)) 
				  for(OWLClass s : n.getEntities()) {
//					  System.out.print(" " + s.getIRI().getFragment());
					  OWLAxiom axiom = df.getOWLDisjointClassesAxiom(s, c);
					  AddAxiom addAxiom = new AddAxiom(oCombined, axiom);
					  m.applyChange(addAxiom);					  
				  }
//			  System.out.println();			  
		  }		
	}
	
	public void execute()  throws OWLOntologyCreationException {
		out.output(2, "Start");
		  OWLOntologyManager m=OWLManager.createOWLOntologyManager();
		  OWLDataFactory df = OWLManager.getOWLDataFactory();
		  OWLOntologyLoaderConfiguration loaderConf = new OWLOntologyLoaderConfiguration();
		  
		  IRI locationIRI = IRI.create("http://project-best.eu/ontologies/location/");
		  loaderConf = loaderConf.addIgnoredImport(locationIRI);
		  loaderConf = loaderConf.addIgnoredImport(IRI.create("http://project-best.eu/ontologies/time/"));
		  
		  for(IRI ignored : loaderConf.getIgnoredImports()) {
			  System.out.println("IGNORE: " + ignored);
		  }

		  out.output(2, "Start Loading");

		  OWLOntology o=m.loadOntologyFromOntologyDocument(
				  new FileDocumentSource(new File(DemoModularized.class.getResource("multidimensional.owl").getPath())),
				  loaderConf);
		  
		  out.output(2, "Import Inferences");
		  
		  importInferences(o,new FileDocumentSource(new File(DemoModularized.class.getResource("time.owl").getPath())));
		  importInferences(o,new FileDocumentSource(new File(DemoModularized.class.getResource("location.owl").getPath())));
		  

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
		DemoModularized demo = new DemoModularized(new OutputHandler(5));
		demo.execute();
	}

}
