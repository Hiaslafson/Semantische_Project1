package model;

public class Adresse {
	String strasse;
	String plz;
	String stadt;
	String country;
	
	public Adresse(){
		
	}

	public Adresse(String strasse, String plz, String stadt, String country)
	{
		this.strasse = strasse;
		this.plz = plz;
		this.stadt = stadt;
		this.country = country;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getStadt() {
		return stadt;
	}

	public void setStadt(String stadt) {
		this.stadt = stadt;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String toJson(){
		String json = 	"[{\"strasse\" : \""+ this.strasse + "\", " +
				"\"plz\" : \""+ this.plz + "\", " +
				"\"country\" : \""+ this.country + "\", " +
				"\"stadt\" : \""+ this.stadt + "\" }]";		
		return json;
	}
}
