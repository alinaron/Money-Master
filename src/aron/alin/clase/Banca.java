package aron.alin.clase;

public class Banca {
	private int codBanca;
	private String denumireBanca;
	private String sucursala;
	
	public Banca() {
		super();
	}
	
	public Banca(int codBanca, String denumireBanca, String sucursala) {
		super();
		this.codBanca = codBanca;
		this.denumireBanca = denumireBanca;
		this.sucursala = sucursala;
	}

	public int getCodBanca() {
		return codBanca;
	}

	public void setCodBanca(int codBanca) {
		this.codBanca = codBanca;
	}

	public String getDenumireBanca() {
		return denumireBanca;
	}

	public void setDenumireBanca(String denumireBanca) {
		this.denumireBanca = denumireBanca;
	}

	public String getSucursala() {
		return sucursala;
	}

	public void setSucursala(String sucursala) {
		this.sucursala = sucursala;
	}

	@Override
	public String toString() {
		return this.denumireBanca + ", sucursala " + this.sucursala + " (" + this.codBanca + ")";
	}
	
	
}
