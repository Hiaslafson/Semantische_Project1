package Project1;
import org.apache.jena.query.Dataset;
import org.apache.jena.tdb.TDBFactory;

import java.util.List;
import java.util.Scanner;
/**
 * Created by MatthiasW on 02.05.2017.
 */
public class project1 {

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);

        PersonDataset rdf = new PersonDataset();

        while (true) {
            System.out.println("p: print all Person -  i: Insert Person  -   u: Update Person -  d: Delete Person   -  f: Filter Person   -  s:Search Person");
            String input = scanner.nextLine();
            if (input.equals("i")) {
                Person p = new Person();
                System.out.println("Insert Person");
                System.out.println("Name:");
                p.name = scanner.nextLine();
                System.out.println("Adress:");
                p.adress = scanner.nextLine();
                System.out.println("Birthday:");
                p.date = scanner.nextLine();
                System.out.println("Gender:");
                p.gender = scanner.nextLine();
                System.out.println("Employer:");
                p.employer = scanner.nextLine();
                rdf.addPerson(p);

            } else if (input.equals("u")) {
                System.out.println("Update Person with id:");
                //Find Person
                int id = Integer.parseInt(scanner.nextLine());
                Person p = rdf.getPerson(id);
                if (p != null) {
                    System.out.println("Update Person (mit - überspringen)");
                    System.out.println(p.name + "   neuer Name:");
                    String value = scanner.nextLine();
                    if (!value.equals("-")) {
                        p.name = value;
                    }
                    System.out.println(p.getAdress() + "  Adress:");
                    value = scanner.nextLine();
                    if (!value.equals("-")) {
                        p.adress = value;
                    }
                    System.out.println(p.getDate() + "   Birthday:");
                    value = scanner.nextLine();
                    if (!value.equals("-")) {
                        p.date = value;
                    }
                    System.out.println(p.getGender() + "   Gender:");
                    value = scanner.nextLine();
                    if (!value.equals("-")) {
                        p.gender = value;
                    }
                    System.out.println(p.getEmployer() + "   Employer:");
                    value = scanner.nextLine();
                    if (!value.equals("-")) {
                        p.employer = value;
                    }
                    rdf.updatePerson(p);
                    System.out.println("Person geändert");

                } else {
                    System.out.println("Keine Person gefunden");
                }


            } else if (input.equals("d")) {
                System.out.println("Delete Person with id:");
                int id = Integer.parseInt(scanner.nextLine());
                Person foundPerson = rdf.getPerson(id);
                if (foundPerson != null) {
                    rdf.deletePerson(id);
                    System.out.println("Person gelöscht");
                } else {
                    System.out.println("Keine Person gefunden");
                }
            } else if (input.equals("f")) {
                System.out.println("Filter Attribut:");
                String f = scanner.nextLine();
               // List<Person> foundPersons = rdf.filtern(f);
                rdf.filtern(f);
            /*    if (foundPersons != null) {
                    System.out.println("ID   |" + "  Name:   |"  +   "  Adresse:   |"  + "  Geburstag:   |"    +  "  Gender:   |"  + "  Arbeitgeber:   |" + "\n"+
                            "-----------------------------------------------------------------------------------------" );
                    System.out.println(foundPersons.toString());
                } else {
                    System.out.println("Keine Person gefunden");
                }*/
            } else if (input.equals("s")) {
                System.out.println("Search Person by id:");
                int id = Integer.parseInt(scanner.nextLine());
                Person foundPerson = rdf.getPerson(id);

                if (foundPerson != null) {
                    System.out.println("ID   |" + "  Name:   |"  +   "  Adresse:   |"  + "  Geburstag:   |"    +  "  Gender:   |"  + "  Arbeitgeber:   |" + "\n"+
                            "-----------------------------------------------------------------------------------------" );
                    System.out.println(foundPerson.toString());
                } else {
                    System.out.println("Keine Person gefunden");
                }
            } else if (input.equals("p")) {

                System.out.println("All Persons:");
                List<Person> foundPersonsAll = rdf.getAllPerson();

                if (foundPersonsAll != null) {
                    System.out.println("ID   |" + "  Name:   |"  +   "  Adresse:   |"  + "  Geburstag:   |"    +  "  Gender:   |"  + "  Arbeitgeber:   |" + "\n"+
                            "-----------------------------------------------------------------------------------------" );;
                    System.out.println(foundPersonsAll.toString());
                } else {
                    System.out.println("Keine Person gefunden");
                }

            }else if (input.equals("k")) {

             //   rdf.addPerson();

              rdf.printPerson();

            }
        }


    }}
