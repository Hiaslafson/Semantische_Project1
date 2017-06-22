package Uebung06;
import java.io.File;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class Performance_AIRM {
	
	OutputHandler out;
	OWLOntologyManager m;
	OWLDataFactory df;
	final IRI AIRM_PREFIX = IRI.create("http://www.best-project.org/owl/AIRM#");
	
	
	public Performance_AIRM(OutputHandler out) {
		this.out = out;
		m=OWLManager.createOWLOntologyManager();
		df = OWLManager.getOWLDataFactory();

	}
	
	OWLClass airmClass(String name) {
		return df.getOWLClass(IRI.create(AIRM_PREFIX + name));
	}
	
	OWLDataProperty airmDataProperty(String name) {
		return df.getOWLDataProperty(IRI.create(AIRM_PREFIX + name));		
	}
	
	OWLObjectProperty airmObjectProperty(String name) {
		return df.getOWLObjectProperty(IRI.create(AIRM_PREFIX + name));		
	}

	OWLIndividual airmIndividual(String name) {
		return df.getOWLNamedIndividual(IRI.create(AIRM_PREFIX + name));		
	}

	OWLLiteral owlLiteral(String name) {
		return df.getOWLLiteral(name);		
	}
	
	public void execute()  throws OWLOntologyCreationException {
	
		out.output(2, "Start");

		  out.output(2, "Load AIRM ontoloy");
		  
		  OWLOntology o=m.loadOntologyFromOntologyDocument(new FileDocumentSource(new File(Performance_AIRM.class.getResource("AIRM_OWL_v06.owl").getPath())));
		  
		  out.output(2, "Consistency Check");

		  OWLReasoner reasoner=new Reasoner.ReasonerFactory().createReasoner(o);
		  System.out.println("Ontology is consistent: " + reasoner.isConsistent());

		  out.output(2, "Subsumption (Superclasses of Runway) ");

		  Set<OWLClass> classes = reasoner.getSuperClasses(airmClass("Runway"), true).getFlattened();
		  for(OWLClass c : classes) {
			  System.out.print(" " + c.getIRI().getFragment());
		  }
		  System.out.println();
		  
		  /* ADD SOME SIMPLE DEFINITIONS */
		  out.output(2, "Add simple class definitions");
		  
		  m.addAxiom(o, df.getOWLEquivalentClassesAxiom(
				  airmClass("Runway_or_Taxiway"), 
				  df.getOWLObjectUnionOf(airmClass("Runway"),airmClass("Taxiway"))));

		  m.addAxiom(o, df.getOWLEquivalentClassesAxiom(
				  airmClass("Runway_and_Taxiway"), 
				  df.getOWLObjectIntersectionOf(airmClass("Runway"),airmClass("Taxiway"))));
		  
		  m.addAxiom(o, df.getOWLEquivalentClassesAxiom(
				  airmClass("Runway_and_Taxiway"), 
				  df.getOWLObjectIntersectionOf(airmClass("Runway"),airmClass("Taxiway"))));


		  out.output(2, "Check Consistency");
		  reasoner=new Reasoner.ReasonerFactory().createReasoner(o);
		  System.out.println("Ontology is consistent: " + reasoner.isConsistent());
		  
		  out.output(2, "Subsumption (Subclasses of Runway_or_Taxiway) ");
		  for(OWLClass c : reasoner.getSubClasses(airmClass("Runway_or_Taxiway"), false).getFlattened()) {
			  System.out.print(" " + c.getIRI().getFragment());
		  }
		  System.out.println();


		  /* ADD SOME COMPLEX DEFINITIONS */
		  out.output(2, "Add complex class definitions");
		  
		  m.addAxiom(o, df.getOWLEquivalentClassesAxiom(
				  airmClass("Balloon"), 
				  df.getOWLObjectHasValue(airmObjectProperty("Aircraft-icaoAircraftCategory"),airmIndividual("BALLOON"))));

		  m.addAxiom(o, df.getOWLEquivalentClassesAxiom(
				  airmClass("RedBalloon"), 
				  df.getOWLObjectIntersectionOf(
						  df.getOWLObjectHasValue(airmObjectProperty("Aircraft-icaoAircraftCategory"),airmIndividual("BALLOON")),
						  df.getOWLDataHasValue(airmDataProperty("AircraftColourAndMarking-aircraftColour"), owlLiteral("red")))));

		  m.addAxiom(o, df.getOWLEquivalentClassesAxiom(
				  airmClass("BlueBalloon"), 
				  df.getOWLObjectIntersectionOf(
						  df.getOWLObjectHasValue(airmObjectProperty("Aircraft-icaoAircraftCategory"),airmIndividual("BALLOON")),
						  df.getOWLDataHasValue(airmDataProperty("AircraftColourAndMarking-aircraftColour"), owlLiteral("blue")))));

		  
		  out.output(2, "Check Consistency");
		  reasoner=new Reasoner.ReasonerFactory().createReasoner(o);
		  System.out.println("Ontology is consistent: " + reasoner.isConsistent());
 
		  out.output(2, "Subsumption (Subclasses of Balloon) ");
		  //classes = reasoner.getSubClasses(getClass("Balloon"), false).getFlattened();
		  for(OWLClass c : reasoner.getSubClasses(airmClass("Balloon"), false).getFlattened()) {
			  System.out.print(" " + c.getIRI().getFragment());
		  }
		  System.out.println();

		  
		  out.output(2, "END");
		  
		
	} 
	
	public static void main(String[] args) throws OWLOntologyCreationException {
		Performance_AIRM demo = new Performance_AIRM(new OutputHandler(5));
		demo.execute();
		
	}

}
