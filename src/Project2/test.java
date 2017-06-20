package Project2;


import Project1.PersonDataset;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;


import java.util.Scanner;

/**
 * Created by MatthiasW on 20.06.2017.
 */



public class test {

    public static void main(String[] args)
    {
        MotoGP m = new MotoGP();
        try {
            m.init();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Scanner scanner = new Scanner(System.in);

        PersonDataset rdf = new PersonDataset();

        while (true) {
            System.out.println("\n s:show All Classes -  c: Show one Class  -   i: show one Individual -  d: Delete Individula   -  a: add Individual ");
            String input = scanner.nextLine();
            if (input.equals("s")) {
                String test = m.getAllClasses();
                System.out.println(test);

            } else if (input.equals("c")) {
                System.out.println("Enter Class");
                String value = scanner.nextLine();
                System.out.println("Subclasses?   (y/n)");
                String b = scanner.nextLine();
                if(b.equals("y")) {
                    String test1 = m.getClasses(value, true);
                    System.out.println(test1);
                }else if(b.equals("n")){
                    String test1 = m.getClasses(value, false);
                    System.out.println(test1);
                }

            } else if (input.equals("i")) {
                System.out.println("Enter Individual");
                String value = scanner.nextLine();
                String test2 = m.getIndividual(value);
                System.out.println(test2);
            } else if (input.equals("d")) {
                System.out.println("Enter Individual to delete");
                String value = scanner.nextLine();

                try {
                    m.deleteIndividual(value);
                } catch (OWLOntologyStorageException e) {
                    e.printStackTrace();
                }

            } else if (input.equals("a")) {

                m.addIndividual("Michael_Lehner");



            }


        }









        //  String test3 =   m.getIndividuals("Driver");
          //  System.out.println(test3);




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