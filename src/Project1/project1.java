package Project1;
import org.apache.jena.query.Dataset;
import org.apache.jena.tdb.TDBFactory;

import java.util.Scanner;
/**
 * Created by MatthiasW on 02.05.2017.
 */
public class project1 {

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        RDFHelper rdf = new RDFHelper();
       while (true) {
           System.out.println("i: Insert Person  -   u: Update Person -  d: Delete Person");
           String input = scanner.nextLine();
            if(input.equals("i")){
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
                rdf.Insert(p);
            } else if(input.equals("u")){
                System.out.println("Update Person with name:");
                //Find Person
                String name = scanner.nextLine();
                Person foundPerson = rdf.Get(name);
                if(foundPerson != null) {
                    Person p = new Person();
                    System.out.println("Update Person (mit - überspringen)");
                    System.out.println(foundPerson.name + " neuer Name:");
                    String value = scanner.nextLine();
                    if(!value.equals("-")) {
                        p.name = scanner.nextLine();
                    }
                   System.out.println("Adress:");
                    value = scanner.nextLine();
                    if(!value.equals("-")) {
                        p.adress = scanner.nextLine();
                    }
                    System.out.println("Birthday:");
                    value = scanner.nextLine();
                    if(!value.equals("-")) {
                        p.date = scanner.nextLine();
                    }System.out.println("Gender:");
                    value = scanner.nextLine();
                    if(!value.equals("-")) {
                        p.gender = scanner.nextLine();
                    }System.out.println("Employer:");
                    value = scanner.nextLine();
                    if(!value.equals("-")) {
                        p.employer = scanner.nextLine();
                    }
                    int returnValue = rdf.Update(p,name);
                    if(returnValue == 0) {
                        System.out.println("Person geändert");
                    } else {
                        System.out.println("Fehler");
                    }
                } else {
                    System.out.println("Keine Person gefunden");
                }

            } else  if(input.equals("d")){
                System.out.println("Delete Person with name:");
                String name = scanner.nextLine();
                Person foundPerson = rdf.Get(name);
                if(foundPerson != null) {
                   int returnValue = rdf.Delete(name);
                    if(returnValue == 0) {
                        System.out.println("Person gelöscht");
                    } else {
                        System.out.println("Fehler");
                    }
                } else {
                    System.out.println("Keine Person gefunden");
                }
            }
        }
    }





}
