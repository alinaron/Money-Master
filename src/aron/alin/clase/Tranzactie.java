package aron.alin.clase;

public class Tranzactie {
	private boolean tipTranzactie;
	private String descriere;
	private double suma;
	
	public Tranzactie() {
		super();
	}

	public Tranzactie(boolean tipTranzactie, String descriere, double suma) {
		super();
		this.tipTranzactie = tipTranzactie;
		this.descriere = descriere;
		this.suma = suma;
	}

	public boolean isTipTranzactie() {
		return tipTranzactie;
	}

	public void setTipTranzactie(boolean tipTranzactie) {
		this.tipTranzactie = tipTranzactie;
	}

	public String getDescriere() {
		return descriere;
	}

	public void setDescriere(String descriere) {
		this.descriere = descriere;
	}

	public double getSuma() {
		return suma;
	}

	public void setSuma(double suma) {
		this.suma = suma;
	}

	@Override
	public String toString() {
		StringBuilder mesaj = new StringBuilder();
		if(tipTranzactie) {
			mesaj.append("Debit: ");
			mesaj.append(this.suma).append(" lei").append("\n").append(this.descriere);
		}
		else {
			mesaj.append("Credit: ");
			mesaj.append(-this.suma).append(" lei").append("\n").append(this.descriere);
		}
		return mesaj.toString();
	}

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}
	
	
}
