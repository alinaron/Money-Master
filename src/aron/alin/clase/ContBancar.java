package aron.alin.clase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class ContBancar {
	private String IBAN;
	private Banca banca;
	private double sold;
	private Hashtable<Date, Tranzactie> listaTranzactii;
	
	public ContBancar(String iBAN, Banca banca, double sold) {
		super();
		IBAN = iBAN;
		this.banca = banca;
		this.sold = sold;
		this.listaTranzactii = new Hashtable<Date, Tranzactie>();
	}
	
	public ContBancar(String iBAN, Banca banca, double sold, Hashtable<Date, Tranzactie> listaTranzactii) {
		super();
		IBAN = iBAN;
		this.banca = banca;
		this.sold = sold;
		this.listaTranzactii = listaTranzactii;
	}


	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}

	public Banca getBanca() {
		return banca;
	}

	public void setBanca(Banca banca) {
		this.banca = banca;
	}

	public double getSold() {
		return sold;
	}

	public void setSold(double sold) {
		this.sold = sold;
	}
	
	public void addTranzactie(Tranzactie t) {
		this.listaTranzactii.put(new Date(), t);
	}
	
	public Hashtable<Date, Tranzactie> getListaTranzactii() {
		return this.listaTranzactii;
	}

	public int getNumarTranzactii() {
		return this.listaTranzactii.size();
	}
	
	@Override
	public String toString() {
		StringBuilder mesaj = new StringBuilder("IBAN: " + IBAN + " la banca " + banca + "\nValoare sold: " + String.format("%.2f",this.sold) + " lei\n");
		if(this.getNumarTranzactii()>0) {
			mesaj.append("Tranzactii:\n");
			Set<Date> date = listaTranzactii.keySet();
			List<Date> keys = new ArrayList<Date>();
			keys.addAll(date);
			Collection<Tranzactie> valori =  listaTranzactii.values();
			List<Tranzactie> values = new ArrayList<Tranzactie>();
			values.addAll(valori);
			for(int j=0;j<this.getNumarTranzactii();j++) {
				for (Date data : keys) {
					if(listaTranzactii.get(data).equals(values.get(j))) {
						mesaj.append(" - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(data)+"\n");
						mesaj.append(values.get(j).toString()+"\n");
					}
				}
			}
		}
		else {
			mesaj.append("Nicio tranzactie inregistrata!");
		}
		return mesaj.toString();
	}
	
}
