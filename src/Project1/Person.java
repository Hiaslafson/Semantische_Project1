package Project1;

import java.sql.Date;

/**
 * Created by MatthiasW on 02.05.2017.
 */
public class Person {
    int id = -1;
    String name;
    String adress;
    String date;
    String gender;
    String employer;

    Person(){
    }
    Person(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }
     public String toString(){
         return "Name: " + getName() + "     Adresse: " + getAdress() + "   Geburtstag: " + getDate() + "    Gender: " + getGender() + "    Arbeitgeber: " + getEmployer() + "\n" ;
     }
}
