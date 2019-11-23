package aron.alin.clase;

import java.util.Vector;

public class Client {
	private String CNP;
	private String nume;
	private String functie;
	private Vector<ContBancar> listaConturi;
	public Client(String cNP, String nume, String functie) {
		super();
		CNP = cNP;
		this.nume = nume;
		this.functie = functie;
		this.listaConturi = new Vector<ContBancar>();
	}
	public Client(String cNP, String nume, String functie, Vector<ContBancar> listaConturi) {
		super();
		CNP = cNP;
		this.nume = nume;
		this.functie = functie;
		this.listaConturi = listaConturi;
	}
	public String getCNP() {
		return CNP;
	}
	public void setCNP(String cNP) {
		CNP = cNP;
	}
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	
	public String getFunctie() {
		return functie;
	}
	public void setFunctie(String functie) {
		this.functie = functie;
	}
	
	public void addCont(ContBancar cont) {
		this.listaConturi.add(cont);
	}
	
	public ContBancar getCont(int index) {
		return this.listaConturi.get(index);
	}
	
	public int getNumarConturi() {
		return this.listaConturi.size();
	}
	
	public void removeCont(ContBancar cont) {
		this.listaConturi.remove(cont);
	}
	@Override
	public String toString() {
		StringBuilder sb =  new StringBuilder("Clientul " + this.nume + " identificat prin CNP " + this.CNP);
		if(listaConturi.size()==0) {
			sb.append(" nu detine niciun cont...\n");
		}
		else {
			sb.append(" detine urmatoarele conturi:\n");
			for (ContBancar contBancar : listaConturi) {
				sb.append(contBancar.toString()).append("\n");
			}
		}
		return sb.toString();
	}
	
	
}
