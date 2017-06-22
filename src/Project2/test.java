package Project2;



import org.semanticweb.owlapi.model.*;


import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by MatthiasW on 20.06.2017.
 */



public class test {

    public static void main(String[] args) {
        MotoGP m = new MotoGP();
        try {
            m.init();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Scanner scanner = new Scanner(System.in);



        while (true) {
            System.out.println("\n s:show All Classes -  c: Show one Class  -   i: show one Individual -  d: Delete Individula   -  a: add Individual  -  y:Get Individuals (Class)");
            String input = scanner.nextLine();
            if (input.equals("s")) {
                m.getAllClasses();


            } else if (input.equals("c")) {
                System.out.println("Enter Class");
                String value = scanner.nextLine();
                System.out.println("Subclasses?   (y/n)");
                String b = scanner.nextLine();
                if (b.equals("y")) {
                     m.getClasses(value, true);

                } else if (b.equals("n")) {
                    m.getClasses(value, false);

                }

            } else if (input.equals("i")) {
                System.out.println("Enter Individual");
                String value = scanner.nextLine();
                m.getIndividual(value);

            } else if (input.equals("d")) {
                System.out.println("Enter Individual to delete");
                String value = scanner.nextLine();

                try {
                    m.deleteIndividual(value);
                } catch (OWLOntologyStorageException e) {
                    e.printStackTrace();
                }

            } else if (input.equals("a")) {

                System.out.println("Enter Class");
                String value = scanner.nextLine();
                System.out.println("Enter Individual");
                String b = scanner.nextLine();
                m.addIndividual(b, value);

            } else if (input.equals("y")) {
                System.out.println("Enter Class");
                String value = scanner.nextLine();

                m.getIndividuals(value);


            }


        }
    }

        }

