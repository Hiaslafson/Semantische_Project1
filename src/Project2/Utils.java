package Project2;

/**
 * Created by MatthiasW on 20.06.2017.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


        import java.util.HashSet;
        import java.util.Iterator;
        import java.util.Set;
        import org.semanticweb.owlapi.model.OWLClass;
        import org.semanticweb.owlapi.model.OWLClassExpression;
        import org.semanticweb.owlapi.model.OWLDataProperty;
        import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
        import org.semanticweb.owlapi.model.OWLIndividual;
        import org.semanticweb.owlapi.model.OWLLiteral;
        import org.semanticweb.owlapi.model.OWLNamedIndividual;
        import org.semanticweb.owlapi.model.OWLObjectProperty;
        import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
        import org.semanticweb.owlapi.model.OWLOntology;
        import org.semanticweb.owlapi.reasoner.Node;
        import org.semanticweb.owlapi.reasoner.NodeSet;
        import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class Utils {
    public Utils() {
    }

    public static String classToJson(OWLClass owlclass, OWLOntology owl, OWLReasoner reasoner, boolean exportSubclasses, boolean exportPossibleProperties) {
        String json = "{";
        json = json + "\"classLongName\": \"" + owlclass.getIRI() + "\",";
        json = json + "\"classShortName\": \"" + owlclass.getIRI().toString().split("#")[1] + "\"";
        if(exportPossibleProperties) {
            json = json + ",\"possibleObjectProperties\": [";
            json = json + objectPropertiesToJson(owlclass, owl, reasoner);
            json = json + "],";
            json = json + "\"possibleDataProperties\": [";
            json = json + dataPropertiesPropertiesToJson(owlclass, owl, reasoner);
            json = json + "]";
        }

        if(exportSubclasses) {
            Set<OWLClassExpression> subClasses = owlclass.getSubClasses(owl);
            if(subClasses.size() > 0) {
                json = json + ",\"subclasses\": [";
                int n = 0;

                for(Iterator var9 = subClasses.iterator(); var9.hasNext(); ++n) {
                    OWLClassExpression subClass = (OWLClassExpression)var9.next();
                    if(n > 0) {
                        json = json + ",";
                    }

                    json = json + classToJson(subClass.asOWLClass(), owl, reasoner, exportSubclasses, exportPossibleProperties);
                }

                json = json + "]";
            }
        }

        json = json + "}";
        return json;
    }

    private static String objectPropertiesToJson(OWLClass owlClasses, OWLOntology owl, OWLReasoner reasoner) {
        if(owlClasses == null) {
            return "";
        } else {
            String json = "";
            Set<OWLClass> superClasses = getAllSuperClasses(owlClasses, owl);
            int n = 0;
            Iterator var7 = owl.getObjectPropertiesInSignature().iterator();

            while(true) {
                OWLObjectProperty prop;
                String domains;
                boolean addProperty;
                int i;
                Node classExp;
                Iterator var12;
                OWLClass entity;
                Iterator var14;
                do {
                    if(!var7.hasNext()) {
                        return json;
                    }

                    prop = (OWLObjectProperty)var7.next();
                    domains = "";
                    addProperty = false;
                    i = 0;
                    var12 = reasoner.getObjectPropertyDomains(prop.asOWLObjectProperty(), true).iterator();

                    while(var12.hasNext()) {
                        classExp = (Node)var12.next();
                        var14 = classExp.getEntities().iterator();

                        while(var14.hasNext()) {
                            entity = (OWLClass)var14.next();
                            if(i > 0) {
                                domains = domains + ",";
                            }

                            domains = domains + "\"" + entity.getIRI().toString().split("#")[1] + "\"";
                            ++i;
                            if(superClasses.contains(entity)) {
                                addProperty = true;
                            }
                        }
                    }
                } while(!addProperty);

                if(n > 0) {
                    json = json + ",";
                }

                json = json + "{";
                json = json + "\"propertyFullName\": \"" + prop.getIRI().toString() + "\",";
                json = json + "\"propertyShortName\": \"" + prop.getIRI().toString().split("#")[1] + "\",";
                json = json + "\"domains\": [ ";
                json = json + domains;
                json = json + "],";
                json = json + "\"ranges\": [";
                i = 0;
                var12 = reasoner.getObjectPropertyRanges(prop.asOWLObjectProperty(), true).iterator();

                while(var12.hasNext()) {
                    classExp = (Node)var12.next();

                    for(var14 = classExp.getEntities().iterator(); var14.hasNext(); ++i) {
                        entity = (OWLClass)var14.next();
                        if(i > 0) {
                            json = json + ",";
                        }

                        json = json + "\"" + entity.getIRI().toString().split("#")[1] + "\"";
                    }
                }

                json = json + "]";
                json = json + "}";
                ++n;
            }
        }
    }

    public static String dataPropertiesPropertiesToJson(OWLClass owlClasses, OWLOntology owl, OWLReasoner reasoner) {
        if(owlClasses == null) {
            return "";
        } else {
            String json = "";
            Set<OWLClass> superClasses = getAllSuperClasses(owlClasses, owl);
            int n = 0;
            Iterator var7 = owl.getDataPropertiesInSignature().iterator();

            while(var7.hasNext()) {
                OWLDataProperty prop = (OWLDataProperty)var7.next();
                String domains = "";
                boolean addProperty = false;
                int i = 0;
                Iterator var12 = reasoner.getDataPropertyDomains(prop.asOWLDataProperty(), true).iterator();

                while(var12.hasNext()) {
                    Node<OWLClass> classExp = (Node)var12.next();
                    Iterator var14 = classExp.getEntities().iterator();

                    while(var14.hasNext()) {
                        OWLClass entity = (OWLClass)var14.next();
                        if(i > 0) {
                            domains = domains + ",";
                        }

                        domains = domains + "\"" + entity.getIRI().toString().split("#")[1] + "\"";
                        ++i;
                        if(superClasses.contains(entity)) {
                            addProperty = true;
                        }
                    }
                }

                if(addProperty) {
                    if(n > 0) {
                        json = json + ",";
                    }

                    json = json + "{";
                    json = json + "\"propertyFullName\": \"" + prop.getIRI().toString() + "\",";
                    json = json + "\"propertyShortName\": \"" + prop.getIRI().toString().split("#")[1] + "\",";
                    json = json + "\"domains\": [ ";
                    json = json + domains;
                    json = json + "]";
                    json = json + "}";
                    ++n;
                }
            }

            return json;
        }
    }

    private static Set<OWLClass> getAllSuperClasses(OWLClass owlClass, OWLOntology owl) {
        Set<OWLClass> classes = new HashSet();
        classes.add(owlClass);
        Iterator var4 = owlClass.getSuperClasses(owl).iterator();

        while(var4.hasNext()) {
            OWLClassExpression classExpr = (OWLClassExpression)var4.next();
            Iterator var6 = classExpr.getClassesInSignature().iterator();

            while(var6.hasNext()) {
                OWLClass c = (OWLClass)var6.next();
                classes.addAll(getAllSuperClasses(c, owl));
            }
        }

        return classes;
    }

    public static String individualToJson(Set<OWLIndividual> owlIndividuals, OWLOntology owl, boolean exportProperties, OWLReasoner reasoner) {
        String json = "{";
        json = json + "\"individuals\": [";
        int n = 0;

        for(Iterator var7 = owlIndividuals.iterator(); var7.hasNext(); ++n) {
            OWLIndividual individual = (OWLIndividual)var7.next();
            if(n > 0) {
                json = json + ",";
            }

            json = json + individualToJson(individual, owl, exportProperties, reasoner);
        }

        json = json + "]";
        json = json + "}";
        return json;
    }

    public static String individualToJson(OWLIndividual owlIndividual, OWLOntology owl, boolean exportProperties, OWLReasoner reasoner) {
        String json = "{";
        json = json + "\"individualFullName\": \"" + owlIndividual.asOWLNamedIndividual().getIRI().toString() + "\",";
        json = json + "\"individualShortName\": \"" + owlIndividual.asOWLNamedIndividual().getIRI().toString().split("#")[1] + "\",";
        json = json + "\"individualClasses\": [ ";
        OWLNamedIndividual owlNamedIndividual = owlIndividual.asOWLNamedIndividual();
        if(owlNamedIndividual != null) {
            NodeSet<OWLClass> classes = reasoner.getTypes(owlNamedIndividual, true);
            int n = 0;
            Iterator var9 = classes.iterator();

            while(var9.hasNext()) {
                Node<OWLClass> owlClassNode = (Node)var9.next();

                for(Iterator var11 = owlClassNode.iterator(); var11.hasNext(); ++n) {
                    OWLClass owlClass = (OWLClass)var11.next();
                    Object[] result = writeSuperclass(owlClass, n, owl);
                    n += ((Integer)result[0]).intValue();
                    json = json + result[1];
                    if(n > 0) {
                        json = json + ",";
                    }

                    json = json + "{";
                    json = json + "\"classLongName\": \"" + owlClass.getIRI() + "\",";
                    json = json + "\"classShortName\": \"" + owlClass.getIRI().toString().split("#")[1] + "\"}";
                }
            }
        }

        json = json + "]";
        if(exportProperties) {
            json = json + ",";
            json = json + "\"objectProperties\": [";
            if(owlNamedIndividual != null) {
                json = json + objectPropertiesToJson(owlNamedIndividual, owl, reasoner);
            }

            json = json + "],";
            json = json + "\"dataProperties\": [";
            if(owlNamedIndividual != null) {
                json = json + dataPropertiesPropertiesToJson(owlNamedIndividual, owl, reasoner);
            }

            json = json + "]";
        }

        json = json + "}";
        return json;
    }

    public static Object[] writeSuperclass(OWLClass owlClass, int n, OWLOntology owl) {
        String json = "";
        Object[] object = new Object[2];
        Iterator var6 = owlClass.getSuperClasses(owl).iterator();

        while(var6.hasNext()) {
            OWLClassExpression owlSuperClasses = (OWLClassExpression)var6.next();

            Object[] result;
            for(Iterator var8 = owlSuperClasses.getClassesInSignature().iterator(); var8.hasNext(); json = json + result[1]) {
                OWLClass owlSuperClassNode = (OWLClass)var8.next();
                if(n > 0) {
                    json = json + ",";
                }

                json = json + "{";
                json = json + "\"classLongName\": \"" + owlSuperClassNode.getIRI() + "\",";
                json = json + "\"classShortName\": \"" + owlSuperClassNode.getIRI().toString().split("#")[1] + "\"}";
                ++n;
                result = writeSuperclass(owlSuperClassNode, n, owl);
                n += ((Integer)result[0]).intValue();
            }
        }

        object[0] = Integer.valueOf(n);
        object[1] = json;
        return object;
    }

    private static String objectPropertiesToJson(OWLNamedIndividual owlIndividual, OWLOntology owl, OWLReasoner reasoner) {
        String json = "";
        int n = 0;
        Iterator var6 = owl.getObjectPropertyAssertionAxioms(owlIndividual).iterator();

        while(var6.hasNext()) {
            OWLObjectPropertyAssertionAxiom propertyAxiom = (OWLObjectPropertyAssertionAxiom)var6.next();

            for(Iterator var8 = propertyAxiom.getObjectPropertiesInSignature().iterator(); var8.hasNext(); ++n) {
                OWLObjectProperty prop = (OWLObjectProperty)var8.next();
                if(n > 0) {
                    json = json + ",";
                }

                json = json + "{";
                json = json + "\"propertyFullName\": \"" + prop.getIRI().toString() + "\",";
                json = json + "\"propertyShortName\": \"" + prop.getIRI().toString().split("#")[1] + "\",";
                json = json + "\"ranges\": [";
                int i = 0;
                Iterator var11 = reasoner.getObjectPropertyRanges(prop.asOWLObjectProperty(), true).iterator();

                while(var11.hasNext()) {
                    Node<OWLClass> classExp = (Node)var11.next();

                    for(Iterator var13 = classExp.getEntities().iterator(); var13.hasNext(); ++i) {
                        OWLClass entity = (OWLClass)var13.next();
                        if(i > 0) {
                            json = json + ",";
                        }

                        json = json + "\"" + entity.getIRI().toString().split("#")[1] + "\"";
                    }
                }

                json = json + "]";
                NodeSet<OWLNamedIndividual> referencedIndividuals = reasoner.getObjectPropertyValues(owlIndividual, prop);
                json = json + ",\"objectProperties\": [";
                i = 0;

                for(Iterator var16 = referencedIndividuals.iterator(); var16.hasNext(); ++i) {
                    Node<OWLNamedIndividual> referencedIndividual = (Node)var16.next();
                    if(i > 0) {
                        json = json + ",";
                    }

                    json = json + individualToJson((OWLIndividual)referencedIndividual.getRepresentativeElement(), owl, false, reasoner);
                }

                json = json + "]";
                json = json + "}";
            }
        }

        return json;
    }

    public static String dataPropertiesPropertiesToJson(OWLNamedIndividual owlIndividual, OWLOntology owl, OWLReasoner reasoner) {
        String json = "";
        int n = 0;
        Iterator var6 = owl.getDataPropertyAssertionAxioms(owlIndividual).iterator();

        while(var6.hasNext()) {
            OWLDataPropertyAssertionAxiom propertyAxiom = (OWLDataPropertyAssertionAxiom)var6.next();

            for(Iterator var8 = propertyAxiom.getDataPropertiesInSignature().iterator(); var8.hasNext(); ++n) {
                OWLDataProperty prop = (OWLDataProperty)var8.next();
                if(n > 0) {
                    json = json + ",";
                }

                json = json + "{";
                json = json + "\"propertyFullName\": \"" + prop.getIRI().toString() + "\",";
                json = json + "\"propertyShortName\": \"" + prop.getIRI().toString().split("#")[1] + "\", ";
                json = json + "\"propertyValue\": [";
                Set<OWLLiteral> owlLiteral = reasoner.getDataPropertyValues(owlIndividual, prop);
                json = json + owlLiteralsPropertiesToJson(owlLiteral);
                json = json + "]";
                json = json + "}";
            }
        }

        return json;
    }

    public static String owlLiteralsPropertiesToJson(Set<OWLLiteral> owlLiteral) {
        String json = "";
        int i = 0;

        for(Iterator var4 = owlLiteral.iterator(); var4.hasNext(); ++i) {
            OWLLiteral literal = (OWLLiteral)var4.next();
            if(i > 0) {
                json = json + ",";
            }

            json = json + "{\"value\": " + literal.toString() + "}";
        }

        return json;
    }


}
