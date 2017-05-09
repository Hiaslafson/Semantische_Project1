package model;

import java.sql.Date;

public class Person {
	
	int id;
	String vorname;
	String nachname;
	Date gebDatum;
	String geschlecht;
	String arbeitgeber;
	Adresse adresse;
	
	public Person(int id){
		this.id = id;
	}

	public Person(String vorname, String nachname, String strasse, 
			String plz, String stadt, String country, Date gebDatum, String geschlecht, String arbeitgeber)
	{
		this.id = -1;
		this.vorname = vorname;
		this.nachname = nachname;
		this.gebDatum = gebDatum;
		this.geschlecht = geschlecht;
		this.arbeitgeber = arbeitgeber;
		this.adresse = new Adresse(strasse, plz, stadt, country);
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public Date getGebDatum() {
		return gebDatum;
	}

	public void setGebDatum(Date gebDatum) {
		this.gebDatum = gebDatum;
	}

	public String getGeschlecht() {
		return geschlecht;
	}

	public void setGeschlecht(String geschlecht) {
		this.geschlecht = geschlecht;
	}

	public String getArbeitgeber() {
		return arbeitgeber;
	}

	public void setArbeitgeber(String arbeitgeber) {
		this.arbeitgeber = arbeitgeber;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public String toJson(){
		String json = 	"{\"id\" : "+ this.id + ", " +
						"\"vorname\" : \""+ this.vorname + "\", " +
						"\"nachname\" : \""+ this.nachname + "\", " +
						"\"gebDatum\" : \""+ this.gebDatum + "\", " +
						"\"geschlecht\" : \""+ this.geschlecht + "\", " +
						"\"arbeitgeber\" : \""+ this.arbeitgeber + "\", " +
						"\"adresse\" : "+ this.adresse.toJson() + " }";		
		return json;
	}
}
