package Project2;

/**
 * Created by MatthiasW on 20.06.2017.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;;
import java.util.Iterator;
import java.util.Set;


import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLEntityRemover;

public class MotoGP {

    private static OWLOntologyManager manager;
    private static OWLOntology ontology;
    private static OWLReasoner reasoner;
    private static OWLDataFactory dataFactory;
    private static final String URI = "http://www.semanticweb.org/motorrad/rennen#";
    private static final String input = MotoGP.class.getResource("motorrad_man_neu.owl").getPath();
    private static final String output = MotoGP.class.getResource("motorrad_xml_new.owl").getPath();
    private static boolean isInit = false;

    public MotoGP() {
    }

    public static synchronized void init() throws Exception {
        if(!isInit) {
            manager = OWLManager.createOWLOntologyManager();
            File inputOntologyFile = new File(input);
            ontology = manager.loadOntologyFromOntologyDocument(inputOntologyFile);
            dataFactory = OWLManager.getOWLDataFactory();
            ReasonerFactory factory = new ReasonerFactory();
            reasoner = factory.createNonBufferingReasoner(ontology);
            System.out.println(ontology.toString());
            isInit = true;
        }

    }


    public static void getAllClasses() {
        Set<OWLClass> owlClasses = ontology.getClassesInSignature();

        for(OWLClass c : owlClasses) {
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



    }

   public static void getClasses(String headClassName, boolean exportSubclasses) {
        OWLClass owlClass = dataFactory.getOWLClass(IRI.create(URI + headClassName));

            System.out.println("Class: " + owlClass.getIRI().getFragment());
            if(exportSubclasses == true){
            System.out.print("  Subclasses: ");
            for(Node<OWLClass> n : reasoner.getSubClasses(owlClass, true))
                for(OWLClass s : n.getEntities()) {
                    System.out.print(" " + s.getIRI().getFragment());
                }
            System.out.println(); }

            System.out.print("  Disjoint clases: ");
            for(Node<OWLClass> n : reasoner.getDisjointClasses(owlClass))
                for(OWLClass s : n.getEntities()) {
                    System.out.print(" " + s.getIRI().getFragment());
                }
            System.out.println();

    }

    public static void getIndividual(String individualName) {
        OWLNamedIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(URI + individualName));

        OWLNamedIndividual owlNamedIndividual = owlIndividual.asOWLNamedIndividual();
        if(owlNamedIndividual != null) {
            System.out.println("Individual: " + owlIndividual.getIRI().getFragment());

            NodeSet<OWLClass> classes = reasoner.getTypes(owlNamedIndividual, true);
            Iterator var = classes.iterator();
            System.out.println("Individual is from Class:");
            while(var.hasNext()) {
                Node<OWLClass> owlClassNode = (Node)var.next();

                for(Iterator var1 = owlClassNode.iterator(); var1.hasNext(); ) {
                    OWLClass owlClass = (OWLClass)var1.next();
                    System.out.println(owlClass.getIRI().getFragment());
                }}

                System.out.println();
                 System.out.println("Individual has ObjectProperties:");


                Iterator var2 = ontology.getObjectPropertyAssertionAxioms(owlIndividual).iterator();

                    if(var2.hasNext()){
                    OWLObjectPropertyAssertionAxiom propertyAxiom = (OWLObjectPropertyAssertionAxiom)var2.next();

                     Iterator var3 = propertyAxiom.getObjectPropertiesInSignature().iterator();
                    while(var3.hasNext()) {
                        OWLObjectProperty prop = (OWLObjectProperty)var3.next();
                        System.out.print("ObjectProperty:");
                        System.out.println("    " +prop.getIRI().getFragment());

                        Iterator var4 = reasoner.getObjectPropertyRanges(prop.asOWLObjectProperty(), true).iterator();

                        while(var4.hasNext()) {
                            Node<OWLClass> classExp = (Node)var4.next();

                            for(Iterator var5 = classExp.getEntities().iterator(); var5.hasNext();) {
                                OWLClass entity = (OWLClass)var5.next();
                                System.out.print("Class:");
                                System.out.println("    " +entity.getIRI().getFragment());


                            }
                        }

                        NodeSet<OWLNamedIndividual> referencedIndividuals = reasoner.getObjectPropertyValues(owlIndividual, prop);
                        for(Iterator var6 = referencedIndividuals.iterator(); var6.hasNext();) {
                            Node<OWLNamedIndividual> referencedIndividual = (Node)var6.next();
                            System.out.print("Type:");

                           System.out.println("     "+referencedIndividual.getRepresentativeElement().getIRI().getFragment());
                        }


                    }}






    }

        System.out.println("-------------------------------------------------------------------------");
    }










    public static void getIndividuals(String className) {
        OWLClass owlClass = dataFactory.getOWLClass(IRI.create(URI + className));
        Set<OWLIndividual> individuals = owlClass.getIndividuals(ontology);
        for(Iterator var = individuals.iterator(); var.hasNext();) {
            OWLIndividual individual = (OWLIndividual)var.next();
            OWLNamedIndividual owlNamedIndividual = individual.asOWLNamedIndividual();
            getIndividual(owlNamedIndividual.getIRI().getFragment());
        }

    }






    public static void addIndividual(String individualName, String className) {
        OWLClass owlClass = dataFactory.getOWLClass(IRI.create(URI + className));
        OWLNamedIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(URI + individualName));
        OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(owlClass, owlIndividual);
        manager.addAxiom(ontology, classAssertion);
        reasoner.flush();
        saveOntology();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteIndividual(String name) throws OWLOntologyStorageException {
        OWLNamedIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(URI + name));
        OWLEntityRemover remover = new OWLEntityRemover(manager, Collections.singleton(ontology));
        owlIndividual.accept(remover);
        manager.applyChanges(remover.getChanges());
        reasoner.flush();
        saveOntology();
    }

    public static void saveOntology() {
        try {
            File outputOntologyFile = new File(output);
            FileOutputStream outputStream = new FileOutputStream(outputOntologyFile);
            manager.saveOntology(ontology, outputStream);
        } catch (OWLOntologyStorageException var1) {
            var1.printStackTrace();
        } catch (FileNotFoundException var2) {
            var2.printStackTrace();
        }

    }
}
