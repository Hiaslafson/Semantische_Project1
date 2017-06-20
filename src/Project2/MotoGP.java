package Project2;

/**
 * Created by MatthiasW on 20.06.2017.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import rationals.converters.ToString;

public class MotoGP {

    private static OWLOntologyManager manager;
    private static OWLOntology ontology;
    private static OWLReasoner reasoner;
    private static OWLDataFactory dataFactory;
    private static final String URI = "http://www.semanticweb.org/motorrad/rennen#";
    private static final String OWLFILEABSOLUTINPUT = MotoGP.class.getResource("motorrad_man_neu.owl").getPath();
    private static final String OWLFILEABSOLUTOUTPUT = MotoGP.class.getResource("motorrad_xml_new.owl").getPath();
    private static boolean isInit = false;

    public MotoGP() {
    }

    public static synchronized void init() throws Exception {
        if(!isInit) {
            manager = OWLManager.createOWLOntologyManager();
            File inputOntologyFile = new File(OWLFILEABSOLUTINPUT);
            ontology = manager.loadOntologyFromOntologyDocument(inputOntologyFile);
            dataFactory = OWLManager.getOWLDataFactory();
            ReasonerFactory factory = new ReasonerFactory();
            reasoner = factory.createNonBufferingReasoner(ontology);
            System.out.println(ontology.toString());
            isInit = true;
        }

    }

   /* public static String getIndividualStruct(String structName) {
      String x;
        OWLClass owlClass = dataFactory.getOWLClass(IRI.create("http://www.semanticweb.org/motorrad#" +structName));
        x = ToString (Utils.writeSuperclass(owlClass, 1, ontology)[1]);
        json = json + "],";
        json = json + "\"objectProperties\":[";
        JSONArray jsonObjectPropeties = jsonClass.getJSONArray("possibleObjectProperties");

        for(int i = 0; i < jsonObjectPropeties.length(); ++i) {
            if(i > 0) {
                json = json + ",";
            }

            json = json + "{\"propertyFullName\":\"" + jsonObjectPropeties.getJSONObject(i).getString("propertyFullName") + "\"," + "\"propertyShortName\":\"" + jsonObjectPropeties.getJSONObject(i).getString("propertyShortName") + "\"," + "\"ranges\":" + jsonObjectPropeties.getJSONObject(i).getJSONArray("ranges").toString() + "," + "\"objectProperties\":[" + "{ \"individualFullName\":\"\"," + " \"individualShortName\":\"\" }]}";
        }

        json = json + "],";
        json = json + "\"dataProperties\":[";
        JSONArray jsonDataProperties = jsonClass.getJSONArray("possibleDataProperties");

        for(int i = 0; i < jsonDataProperties.length(); ++i) {
            if(i > 0) {
                json = json + ",";
            }

            json = json + "{";
            json = json + "\"propertyFullName\":\"" + jsonDataProperties.getJSONObject(i).getString("propertyFullName") + "\"," + "\"propertyShortName\":\"" + jsonDataProperties.getJSONObject(i).getString("propertyShortName") + "\"," + "\"propertyValue\":[{" + "\"value\":\"\"" + "}]" + "}";
        }

        json = json + "]";
        json = json + "}";
        return json;
    } */

    public static String getAllClasses() {
        Set<OWLClass> owlClasses = ontology.getClassesInSignature();
        String strClasses = "[";
        int i = 0;

        for(Iterator var4 = owlClasses.iterator(); var4.hasNext(); ++i) {
            OWLClass owlClass = (OWLClass)var4.next();
            if(i > 0) {
                strClasses = strClasses + "," +"\n";
            }

            strClasses = strClasses + Utils.classToJson(owlClass, ontology, reasoner, false, true );
        }

        strClasses = strClasses + "]" ;
        return strClasses;
    }

    public static String getClasses(String headClassName, boolean exportSubclasses) {
        OWLClass owlClass = dataFactory.getOWLClass(IRI.create(URI + headClassName));
        return '[' + Utils.classToJson(owlClass, ontology, reasoner, exportSubclasses, true) + ']';
    }

    public static String getIndividual(String individualName) {
        OWLNamedIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(URI + individualName));
        return Utils.individualToJson(owlIndividual, ontology, true, reasoner);
    }

    public static String queryIndividuals(String varQuery) {
        varQuery = varQuery.toLowerCase();
        String[] varQueryArray = varQuery.split("\\s+");
        OWLClass owlClass = dataFactory.getOWLClass(IRI.create(URI));
        Set<OWLIndividual> allIndividuals = owlClass.getIndividuals(ontology);
        addIndividualsToSet(allIndividuals, owlClass);
        Set<OWLIndividual> owlIndiviuals = new HashSet();
        Iterator var6 = allIndividuals.iterator();

        while(true) {
            while(var6.hasNext()) {
                OWLIndividual ind = (OWLIndividual)var6.next();
                String Fs_jsonStringInd = Utils.individualToJson(ind, ontology, true, reasoner).toLowerCase();
                String[] var11 = varQueryArray;
                int var10 = varQueryArray.length;

                for(int var9 = 0; var9 < var10; ++var9) {
                    String splitquery = var11[var9];
                    if(Fs_jsonStringInd.contains(splitquery)) {
                        owlIndiviuals.add(ind);
                        break;
                    }
                }
            }

            return Utils.individualToJson(owlIndiviuals, ontology, true, reasoner);
        }
    }

    public static String getIndividuals(String className) {
        OWLClass owlClass = dataFactory.getOWLClass(IRI.create(URI + className));
        Set<OWLIndividual> individuals = owlClass.getIndividuals(ontology);
        return Utils.individualToJson(individuals, ontology, true, reasoner);
    }

    public static String getIndividualsForClassAndSubClass(String className) {
        OWLClass owlClass = dataFactory.getOWLClass(IRI.create(URI + className));
        Set<OWLIndividual> individuals = owlClass.getIndividuals(ontology);
        addIndividualsToSet(individuals, owlClass);
        return Utils.individualToJson(individuals, ontology, true, reasoner);
    }

    private static void addIndividualsToSet(Set<OWLIndividual> individuals, OWLClass owlClass) {
        Iterator var3 = owlClass.getSubClasses(ontology).iterator();

        while(var3.hasNext()) {
            OWLClassExpression subClassExp = (OWLClassExpression)var3.next();
            OWLClass subClass = subClassExp.asOWLClass();
            individuals.addAll(subClass.getIndividuals(ontology));
            addIndividualsToSet(individuals, subClass);
        }

    }

    /*  public static void changeOrAddIndivdual(String json) throws Exception {
        System.out.println(json);
        JSONObject jsonIndividual = new JSONObject(json);
        String shortName = jsonIndividual.getString("individualShortName");
        if(shortName != null && !"".equals(shortName)) {
            try {
                deleteIndividual(shortName);
                OWLNamedIndividual individual = addIndividual(shortName);
                JSONArray classes = jsonIndividual.getJSONArray("individualClasses");

                for(int i = 0; i < classes.length(); ++i) {
                    JSONObject singeClass = classes.getJSONObject(i);
                    if(singeClass.has("classShortName") && !singeClass.isNull("classShortName") && !"".equals(singeClass.getString("classShortName"))) {
                        System.out.println("Class: " + individual + ", " + singeClass.getString("classShortName"));
                        addClassPropertyToIndividual(individual, singeClass.getString("classShortName"));
                    }
                }

                JSONArray objectProperties = jsonIndividual.getJSONArray("objectProperties");

                for(int i = 0; i < objectProperties.length(); ++i) {
                    JSONObject jsonObjectProperty = objectProperties.getJSONObject(i);
                    if(!jsonObjectProperty.has("propertyShortName") || jsonObjectProperty.isNull("propertyShortName") || "".equals(jsonObjectProperty.getString("propertyShortName"))) {
                        throw new Exception("ObjectProeprty could not be added du to an invalid owl URI identifyer.");
                    }

                    JSONArray refrenecedIndividuals = jsonObjectProperty.getJSONArray("objectProperties");

                    for(int k = 0; k < refrenecedIndividuals.length(); ++k) {
                        JSONObject refrenecedIndividual = refrenecedIndividuals.getJSONObject(k);
                        if(refrenecedIndividual.has("individualShortName") && !refrenecedIndividual.isNull("individualShortName") && !"".equals(refrenecedIndividual.getString("individualShortName"))) {
                            System.out.println("Property: " + individual + ", " + jsonObjectProperty.getString("propertyShortName") + ", " + refrenecedIndividual.getString("individualShortName"));
                            addObjectPropertyToIndividual(individual, jsonObjectProperty.getString("propertyShortName"), refrenecedIndividual.getString("individualShortName"));
                        }
                    }
                }

                JSONArray dataProperties = jsonIndividual.getJSONArray("dataProperties");

                for(int i = 0; i < dataProperties.length(); ++i) {
                    JSONObject jsonDataProperty = dataProperties.getJSONObject(i);
                    if(!jsonDataProperty.has("propertyShortName") || jsonDataProperty.isNull("propertyShortName") || "".equals(jsonDataProperty.getString("propertyShortName"))) {
                        throw new Exception("Dataproperty could not be added du to an invalid owl URI identifyer");
                    }

                    JSONArray dataPropertyValues = jsonDataProperty.getJSONArray("propertyValue");

                    for(int k = 0; k < dataPropertyValues.length(); ++k) {
                        JSONObject dataPropertyValue = dataPropertyValues.getJSONObject(k);
                        if(dataPropertyValue.has("value") && !dataPropertyValue.isNull("value")) {
                            System.out.println("Property: " + individual + ", " + jsonDataProperty.getString("propertyShortName") + ", " + dataPropertyValue.getString("value"));
                            addDataPropertyToIndividual(individual, jsonDataProperty.getString("propertyShortName"), dataPropertyValue.getString("value"));
                        }
                    }
                }

            } catch (Exception var12) {
                throw var12;
            }
        }
    } */

    public static OWLNamedIndividual addIndividual(String newIndividualName) {
        OWLNamedIndividual individual = dataFactory.getOWLNamedIndividual(IRI.create(URI + newIndividualName));
        saveOntology();
        return individual;
    }

    public static void addClassPropertyToIndividual(OWLIndividual owlIndividual, String className) {
        OWLClass owlClass = dataFactory.getOWLClass(IRI.create(URI + className));
        OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(owlClass, owlIndividual);
        manager.addAxiom(ontology, classAssertion);
        reasoner.flush();
        saveOntology();
    }

    public static void addDataPropertyToIndividual(OWLIndividual owlIndividual, String property, String value) {
        OWLDataProperty owlProperty = dataFactory.getOWLDataProperty(IRI.create(URI + property));
        OWLLiteral literal = dataFactory.getOWLLiteral(value, "");
        OWLDataPropertyAssertionAxiom axiom1 = dataFactory.getOWLDataPropertyAssertionAxiom(owlProperty, owlIndividual, literal);
        manager.addAxiom(ontology, axiom1);
        reasoner.flush();
        saveOntology();
    }

    public static void addObjectPropertyToIndividual(OWLIndividual owlIndividual, String property, String value) {
        OWLIndividual owlValueIndivudal = dataFactory.getOWLNamedIndividual(IRI.create(URI + value));
        OWLObjectProperty owlProperty = dataFactory.getOWLObjectProperty(IRI.create(URI + property));
        OWLObjectPropertyAssertionAxiom axiom1 = dataFactory.getOWLObjectPropertyAssertionAxiom(owlProperty, owlIndividual, owlValueIndivudal);
        AddAxiom addAxiom1 = new AddAxiom(ontology, axiom1);
        manager.applyChange(addAxiom1);
        reasoner.flush();
        saveOntology();
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
            File outputOntologyFile = new File(OWLFILEABSOLUTOUTPUT);
            FileOutputStream outputStream = new FileOutputStream(outputOntologyFile);
            manager.saveOntology(ontology, outputStream);
        } catch (OWLOntologyStorageException var2) {
            var2.printStackTrace();
        } catch (FileNotFoundException var3) {
            var3.printStackTrace();
        }

    }
}
