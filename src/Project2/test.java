package Project2;

/**
 * Created by MatthiasW on 20.06.2017.
 */



public class test {

    public static void main(String[] args)
    {

        try {
            MotoGP m = new MotoGP();
            m.init();
            String test = m.getAllClasses();
         //   System.out.println(test);

            String test1 = m.getClasses("Person"  ,true);
            //System.out.println(test1);

            String test2 = m.getIndividual("Richtsfeld_Werner");
            System.out.println(test2);

            //<http://www.semanticweb.org/fussball-ontology/EM#Country>
            //test flat structure
           /*  String test = MotoGP.getClasses("Country", true);
            System.out.println(test);
            //test deep structure
            test = MotoGP.getClasses("Person", true);
            System.out.println(test);

            //Delete Inidivual
            System.out.println("Before: " +  MotoGP.getIndividual("David_Alaba"));
            MotoGP.deleteIndividual("David_Alaba");
            System.out.println("After: " +  MotoGP.getIndividual("David_Alaba"));

            //Getindividual and subproperties
            //test flat structure
            String individualJson = MotoGP.getIndividuals("Country");
            System.out.println(individualJson);
            //test deep structure1
            individualJson = MotoGP.getIndividuals("Person");
            System.out.println(individualJson);
            //test deep structure2
            individualJson = MotoGP.getIndividualsForClassAndSubClass("Person");
            System.out.println(individualJson); */


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//		Person martin = new Person("Martin", "Weissensteiner", "Hauptstrasse 10", "4040", "Steyr", "Austria",
//				new Date(System.currentTimeMillis()), "M" ,  "CGM Clinical Oesterreich");
//		Person max = new Person("Max", "Mustermann",  "Altenbergerstrasse 69", "4000", "Linz", "Austria",
//				new Date(System.currentTimeMillis()), "F", "Student");
//
//		SoccerOntologyRessource.init();
//		SoccerOntologyRessource.addPerson(martin);
//		SoccerOntologyRessource.addPerson(max);
//		Person person = SoccerOntologyRessource.getPerson(0, SoccerOntologyRessource.getPersonenModel());
//		List<Person> persons = SoccerOntologyRessource.getAll(false);
//		persons = SoccerOntologyRessource.filtern("F", SoccerOntologyRessource.getPersonenModel());
//		persons = SoccerOntologyRessource.filtern( "CGM Clinical Oesterreich", SoccerOntologyRessource.getPersonenModel());
//		persons = SoccerOntologyRessource.filtern("", SoccerOntologyRessource.getPersonenModel());
//		persons = SoccerOntologyRessource.filtern("asdfsadfasdfadsf", SoccerOntologyRessource.getPersonenModel());
//
//		SoccerOntologyRessource.sort("<http://www.example/x.rdf#geschlecht>", true);
//		Person newMartin = new Person("Martin2", "Weissensteiner2", "Hauptstrasse 11", "4041", "Steyr", "Austria",
//				new Date(System.currentTimeMillis()), "M" ,  "CGM Clinical Oesterreich2");
//		newMartin.setId(0);
//		SoccerOntologyRessource.editPerson(newMartin);
//
//		SoccerOntologyRessource.setHasFriend(1,0);
//		SoccerOntologyRessource.setHasEmployee(0, 1);
//
//		persons = SoccerOntologyRessource.getFriends(0, SoccerOntologyRessource.getPersonenModel());
//		persons = SoccerOntologyRessource.getFriends(1, SoccerOntologyRessource.getPersonenModel());
//		persons = SoccerOntologyRessource.getEmployees(0, SoccerOntologyRessource.getPersonenModel());
//		persons = SoccerOntologyRessource.getEmployees(1, SoccerOntologyRessource.getPersonenModel());
//
//		SoccerOntologyRessource.switchDateToModel(2, SoccerOntologyRessource.getPersonenModel(), SoccerOntologyRessource.getPersonenDeletedModel());
    }
}