package aron.alin.program;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import aron.alin.clase.Banca;
import aron.alin.clase.Client;
import aron.alin.clase.ContBancar;
import aron.alin.clase.Tranzactie;
import aron.alin.exceptii.ExceptieSold;

public class Program {
	
	private static String[] readCredentials(String file) throws FileNotFoundException {
		String[] credentiale = new String[2];
		Scanner	scanner = new Scanner(new File(file));
			while(scanner.hasNext()) {
			credentiale[0] = scanner.nextLine();
			credentiale[1] = scanner.nextLine();
		}
		return credentiale;
	}
	
	private static List<Banca> readBanca(String file) throws FileNotFoundException {
		List<Banca> listaBanci = new ArrayList<Banca>();
		Scanner scanner = new Scanner(new File(file));
		while(scanner.hasNext()) {
			String[] banca = scanner.nextLine().split(",");
			Banca b = new Banca(Integer.parseInt(banca[0]), banca[1], banca[2]);
			listaBanci.add(b);
		}
		return listaBanci;
	}
	
	private static List<Client> readFile(String file) throws FileNotFoundException{
		List<Client> listaClienti = new ArrayList<Client>();
		Scanner scanner = new Scanner(new File(file));
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		while(scanner.hasNext()) {
			String CNP = scanner.nextLine();
			String nume = scanner.nextLine();
			String functie = scanner.nextLine();
			int numarConturi = Integer.parseInt(scanner.nextLine());
			Vector<ContBancar> listaConturi = new Vector<ContBancar>();
			for(int i=0;i<numarConturi;i++) {
				Hashtable<Date, Tranzactie> listaTranzactii = new Hashtable<Date, Tranzactie>();
				String IBAN = scanner.nextLine();
				String [] banci = (scanner.nextLine()).split(",");
				int cod = Integer.parseInt(banci[0]);
				String denumire = banci[1];
				String sucursala = banci[2];
				Banca banca = new Banca(cod,denumire,sucursala);
				double sold = Double.parseDouble(scanner.nextLine());
				int numarTranzactii = Integer.parseInt(scanner.nextLine());
				for(int j=0;j<numarTranzactii;j++) {
					Date dataTranzactie = null;
					try {
						dataTranzactie = (Date)formatter.parse(scanner.nextLine());
					} catch (ParseException e) {
						System.err.println("Eroare la conversia datei! Fisierul contine date eronate...");
					}
					boolean tipTranzactie = Boolean.parseBoolean(scanner.nextLine());
					String descriereTranzactie = scanner.nextLine();
					double sumaTranzactie = Double.parseDouble(scanner.nextLine());
					try {
					if(sumaTranzactie < 0)
						throw new ExceptieSold();
					}
					catch (ExceptieSold e) {
						sumaTranzactie = 0;
					}
					Tranzactie tranzactie = new Tranzactie(tipTranzactie, descriereTranzactie, sumaTranzactie);
					listaTranzactii.put(dataTranzactie, tranzactie);
				}
				ContBancar contBancar = new ContBancar(IBAN, banca, sold, listaTranzactii);
				listaConturi.add(contBancar);
			}
			Client client = new Client(CNP, nume, functie, listaConturi);
			listaClienti.add(client);
		}
		return listaClienti;
	}
	
	private static void writeFile(List<Client> lista, String file) throws IOException  {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file)));
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			for (Client client : lista) {
				bw.write(client.getCNP());
				bw.newLine();
				bw.write(client.getNume());
				bw.newLine();
				bw.write(client.getFunctie());
				bw.newLine();
				bw.write(String.valueOf(client.getNumarConturi()));
				bw.newLine();
				for(int i=0;i<client.getNumarConturi();i++) {
					bw.write(client.getCont(i).getIBAN());
					bw.newLine();
					String banca = client.getCont(i).getBanca().getCodBanca()+"," + 
								   client.getCont(i).getBanca().getDenumireBanca()+","+
								   client.getCont(i).getBanca().getSucursala();
					bw.write(banca);
					bw.newLine();
					bw.write(String.valueOf(client.getCont(i).getSold()));
					bw.newLine();
					bw.write(String.valueOf(client.getCont(i).getNumarTranzactii()));
					bw.newLine();
					Hashtable<Date, Tranzactie> listaTranzactii = client.getCont(i).getListaTranzactii();
					Set<Date> date = listaTranzactii.keySet();
					List<Date> keys = new ArrayList<Date>();
					keys.addAll(date);
					Collection<Tranzactie> valori =  listaTranzactii.values();
					List<Tranzactie> values = new ArrayList<Tranzactie>();
					values.addAll(valori);
					for(int j=0;j<client.getCont(i).getNumarTranzactii();j++) {
						for (Date data : keys) {
							if(listaTranzactii.get(data).equals(values.get(j))) {
								bw.write(formatter.format(data));
								bw.newLine();
								bw.write(String.valueOf(values.get(j).isTipTranzactie()));
								bw.newLine();
								bw.write(values.get(j).getDescriere());
								bw.newLine();
								bw.write(String.valueOf(values.get(j).getSuma()));
								bw.newLine();
							}
						}
					}
				}
			}
			bw.close();
		
	}
	
	private static void closeApp(Scanner scanner, List<Client> lista, String file) throws IOException {
		writeFile(lista, file);
		scanner.close();
		System.out.println("Aplicatia a fost inchisa!");
		System.exit(0);
	}
	
	public static void main(String[] args) {
		int menuItem;
		double menuSuma;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Bun venit la MoneyMaster, managerul tau financiar virtual!");
		String meniuAutentificare = "Selectati optiunea dorita:\n1. Autentificare ca administrator\n2. Autentificare client\n3. Inchidere aplicatie";
		String meniuAdministrator = "Selectati optiunea dorita:\n1. Vizualizare tranzactii\n2. Vizualizare rapoarte\n3. Inchidere aplicatie";
		String meniuAdministratorTranzactii = "Selectati optiunea dorita:\n1. Toti clientii\n2. Cautati client dupa CNP\n3. Inchidere aplicatie";
		String meniuCNP = "Introduceti CNP-ul:";
		String meniuCont = "Selectati contul dorit:";
		String meniuClient = "Selectati optiunea dorita:\n1. Vizualizare extras de cont\n2. Retragere numerar"+
								"\n3. Solicita imprumut\n4. Plata catre comercianti\n5. Depunere numerar"+
								"\n6. Interogare sold\n7. Inchidere aplicatie";
		String meniuClientSuma = "Introduceti suma dorita: ";
		String meniuOK = "1. Accept\n2. Anuleaza";
		String[] credentiale;
		try {
			credentiale = readCredentials("credentiale.txt");
		}
		catch (FileNotFoundException e){
			System.err.println("Eroare la citirea fisierului de credentiale! Nu puteti intra in contul de administrator...");
			credentiale = new String[]{null, null};
		}
		List<Client> listaClienti;
		try{
			listaClienti = readFile("clienti.txt");
		}
		catch (FileNotFoundException e) {
			System.err.println("Fisierul de clienti nu a fost incarcat...");
			listaClienti = new ArrayList<Client>();
		}
		List<Banca> listaBanci;
		try {
			listaBanci = readBanca("banci.txt");
		}
		catch (Exception e) {
			System.err.println("Eroare la incarcarea fisierului de banci...");
			listaBanci = new ArrayList<Banca>();
		}
		System.out.println(meniuAutentificare);		
		while(true) {
			menuItem = scanner.nextInt();
			switch(menuItem) {
			case 1:
				System.out.println("Username: ");
				String menuUsername = scanner.next();
				System.out.println("Parola: ");
				String menuPass = scanner.next();
				if(menuUsername.equals(credentiale[0]) && menuPass.equals(credentiale[1])) {
				while(true) {
					System.out.println(meniuAdministrator);
					menuItem = scanner.nextInt();
					switch (menuItem) {
					case 1:
						while(true) {
							System.out.println(meniuAdministratorTranzactii);
							menuItem = scanner.nextInt();
							switch (menuItem) {
							case 1:
								for (Client client : listaClienti) {
									System.out.println(client.toString());
								}
								break;
							case 2:
								System.out.println(meniuCNP);
								String menuCNP = scanner.next();
								int k = 0;
								for (Client client : listaClienti) {
									if(client.getCNP().equals(menuCNP)) {
										k = 1;
										int n = client.getNumarConturi();
										if(n==0) {
											System.out.println("Nu exista niciun cont pentru acest CNP.");
										}
										else {
												System.out.println(client.toString());
										}
									}
								}
								if(k==0) {
									System.out.println("Nu exista niciun client cu acest CNP!\n");
								}
								break;
							case 3:
								try{
									closeApp(scanner, listaClienti, "clienti.txt");
								}
								catch (IOException e) {
									System.err.println("Eroare la actualizarea fisierului de clienti...");
									scanner.close();
									System.exit(0);
								}
								break;
							default:
								System.out.println("Optiune indisponibila");
								break;
							}
						}
					case 2:
						int numarClienti = listaClienti.size();
						double [] soldTotal = new double[numarClienti];
						int [] numarConturi = new int[numarClienti];
						double [] ceaMaiMareTranzactie = new double[numarClienti];
						for(int i=0;i<numarClienti;i++) {
							soldTotal[i] = 0;
							numarConturi[i] = 0;
							ceaMaiMareTranzactie[i] = 0;
						}
						for(int i=0;i<numarClienti;i++) {
							numarConturi[i] = listaClienti.get(i).getNumarConturi();
							for(int j = 0;j < listaClienti.get(i).getNumarConturi();j++) {
								soldTotal[i] += listaClienti.get(i).getCont(j).getSold();
								if(listaClienti.get(i).getCont(j).getNumarTranzactii() > 0) {
								Set<Date> date = listaClienti.get(i).getCont(j).getListaTranzactii().keySet();
								List<Date> keys = new ArrayList<Date>();
								keys.addAll(date);
								for(int k = 0;k< listaClienti.get(i).getCont(j).getNumarTranzactii();k++) {
									for (Date data : keys) {
										if(listaClienti.get(i).getCont(j).getListaTranzactii().get(data).getSuma() > ceaMaiMareTranzactie[i])
											ceaMaiMareTranzactie[i] = listaClienti.get(i).getCont(j).getListaTranzactii().get(data).getSuma();
									}
								}
								}
							}
						}
						while(true) {
							System.out.println(meniuAdministratorTranzactii);
							menuItem = scanner.nextInt();
							switch (menuItem) {
							case 1:
								double soldTot = 0;
								int nrCont = 0;
								double ceaMaiMareTr;
								if(numarClienti == 0) {
									ceaMaiMareTr = 0;
								}
								ceaMaiMareTr = ceaMaiMareTranzactie[0];
								for (int p=0;p<numarClienti;p++) {
									soldTot += soldTotal[p];
									nrCont += numarConturi[p];
									if(ceaMaiMareTranzactie[p]>ceaMaiMareTr)
										ceaMaiMareTr = ceaMaiMareTranzactie[p];
								}
								System.out.println("Soldul total al clientilor este: " + String.format("%.2f", soldTot) + " lei");
								System.out.println("Numarul total de conturi din aplicatie este: " + nrCont );
								System.out.println("Cea mai mare tranzactie inregistrata in aplicatie este: " + String.format("%.2f", ceaMaiMareTr) + " lei");
								break;
							case 2:
								System.out.println(meniuCNP);
								String menuCNP = scanner.next();
								int k = 0;
								for (int i=0;i<listaClienti.size();i++) {
									if(listaClienti.get(i).getCNP().equals(menuCNP)) {
										k = 1;
										System.out.println("Soldul total al clientului "+listaClienti.get(i).getNume()+" este: " + String.format("%.2f", soldTotal[i]) + " lei");
										System.out.println("Numarul total de conturi al clientului "+listaClienti.get(i).getNume()+" este: " + numarConturi[i] );
										System.out.println("Cea mai mare tranzactie inregistrata de clientul "+listaClienti.get(i).getNume()+" este: " + String.format("%.2f", ceaMaiMareTranzactie[i]) + " lei");
									}
								}
								if(k==0) {
									System.out.println("Nu exista niciun client cu acest CNP!\n");
								}
								break;
							case 3:
								try{
									closeApp(scanner, listaClienti, "clienti.txt");
								}
								catch (IOException e) {
									System.err.println("Eroare la actualizarea fisierului de clienti...");
									scanner.close();
									System.exit(0);
								}
								break;
							default:
								System.out.println("Optiune indisponibila");
								break;
							}
						}
					case 3:
						try{
							closeApp(scanner, listaClienti, "clienti.txt");
						}
						catch (IOException e) {
							System.err.println("Eroare la actualizarea fisierului de clienti...");
							scanner.close();
							System.exit(0);
						}
					default:
						System.out.println("Optiune indisponibila");
						break;
					}
				}
				}
				else {
					System.out.println("Username sau parola gresite!\n");
					System.out.println(meniuAutentificare);
					break;
				}
			case 2:
				System.out.println(meniuCNP);
				String menuCNP = scanner.next();
				int k = 0;
				for (Client client : listaClienti) {
					if(client.getCNP().equals(menuCNP)) {
						ContBancar contSelectat = null;
						System.out.println(meniuCont);
						k = 1;
						int n = client.getNumarConturi();
						if(n==0) {
							System.out.println("Nu exista niciun cont pentru acest CNP. Adresati-va unei banci pentru crearea unui cont.");
						}
						else {
							for(int i = 0;i < n;i++) {
								System.out.println(i+1+". " + client.getCont(i).getIBAN() + " - " + client.getCont(i).getBanca().getDenumireBanca());
							}
							System.out.println(n+1+". Deschidere cont nou");
							menuItem = scanner.nextInt();
							while(menuItem > n+1 ) {
								System.out.println("Optiune eronata! Optiuni posibile:\n");
								for(int i = 0;i < n;i++) {
									System.out.println(i+1+". " + client.getCont(i).getIBAN() + " - " + client.getCont(i).getBanca().getDenumireBanca());
								}
								menuItem = scanner.nextInt();
							}
							if(menuItem == n+1) {
								System.out.println("IBAN: ");
								String menuIBAN = scanner.next();
								System.out.println("Banca: ");
								String menuBanca = scanner.next();
								System.out.println("Sucursala: ");
								String menuSucursala = scanner.next();
								ContBancar contNou = null;
								for (Banca banca : listaBanci) {
									if(banca.getDenumireBanca().equals(menuBanca) && banca.getSucursala().equals(menuSucursala)) {
										contNou = new ContBancar(menuIBAN, banca, 0);
									}
								}
								if(contNou == null) {
									System.out.println("Eroare la crearea noului cont! Banca eronata! Operatiune anulata...");
								}
								else {
									client.addCont(contNou);
									contSelectat = contNou;
								}
							}
							else {
							 contSelectat = client.getCont(menuItem-1);
							}
							if(contSelectat == null) {
								System.out.println("\n"+meniuAutentificare);
								continue;
							}
							else {
							while(true) {
								System.out.println(meniuClient);
								menuItem = scanner.nextInt();
								switch (menuItem) {
								case 1:
									System.out.println("\nVizualizare extras de cont");
									System.out.println(contSelectat);
									break;
								case 2:
									System.out.println(meniuClientSuma);
									menuSuma = scanner.nextDouble();
									try {
									if(menuSuma <= 0)
										throw new ExceptieSold();
									else {
										if(contSelectat.getSold() >= menuSuma) {
											listaClienti.remove(client);
											client.removeCont(contSelectat);
											contSelectat.setSold(contSelectat.getSold() - menuSuma);
											Tranzactie t = new Tranzactie(false, "Retragere numerar", menuSuma);
											contSelectat.addTranzactie(t);
											client.addCont(contSelectat);
											listaClienti.add(client);
											System.out.println("\nTranzactie acceptata!\n");
										}
										else {
											System.out.println("Fonuri insuficiente");
										}
									}
									}
									catch (ExceptieSold e) {
										System.out.println("Suma este eronata! Tranzactia a fost anulata...\n");
									}
									break;
								case 3:
									System.out.println(meniuClientSuma);
									menuSuma = scanner.nextDouble();
									try {
									if(menuSuma <= 0)
										throw new ExceptieSold();
									else {
										listaClienti.remove(client);
										client.removeCont(contSelectat);
										contSelectat.setSold(contSelectat.getSold() + menuSuma);
										Tranzactie t = new Tranzactie(true, "Imprumut", menuSuma);
										contSelectat.addTranzactie(t);
										client.addCont(contSelectat);
										listaClienti.add(client);
										System.out.println("\nTranzactie acceptata!\n");
									}
									}
									catch (ExceptieSold e) {
										System.out.println("Suma este eronata! Tranzactia a fost anulata...\n");
									}
									break;
								case 4:
									System.out.println("Introduceti comerciantul pentru care doriti executarea platii:");
									String menuComerciant = scanner.next();
									System.out.println(meniuClientSuma);
									menuSuma = scanner.nextDouble();
									try {
									if(menuSuma <= 0)
										throw new ExceptieSold();
									else {
										if(contSelectat.getSold() >= menuSuma) {
											listaClienti.remove(client);
											client.removeCont(contSelectat);
											contSelectat.setSold(contSelectat.getSold() - menuSuma);
											Tranzactie t = new Tranzactie(false, "Plata catre comercianti ("+menuComerciant+")", menuSuma);
											contSelectat.addTranzactie(t);
											client.addCont(contSelectat);
											listaClienti.add(client);
											System.out.println("\nTranzactie acceptata!\n");
										}
										else {
											System.out.println("Fonuri insuficiente");
										}
									}
									}
									catch(ExceptieSold e) {
										System.out.println("Suma este eronata! Tranzactia a fost anulata...\n");
									}
									break;
								case 5:
									System.out.println(meniuClientSuma);
									menuSuma = scanner.nextDouble();
									try {
									if(menuSuma <= 0)
										throw new ExceptieSold();
									else {
										listaClienti.remove(client);
										client.removeCont(contSelectat);
										contSelectat.setSold(contSelectat.getSold() + menuSuma);
										Tranzactie t = new Tranzactie(true, "Depunere numerar", menuSuma);
										contSelectat.addTranzactie(t);
										client.addCont(contSelectat);
										listaClienti.add(client);
										System.out.println("\nTranzactie acceptata!\n");
									}
									}
									catch (ExceptieSold e) {
										System.out.println("Suma este eronata! Tranzactia a fost anulata...");
									}
									break;
								case 6:
									System.out.println("\nSold disponibil: " + String.format("%.2f",contSelectat.getSold())+" lei");
									break;
								case 7:
									try{
										closeApp(scanner, listaClienti, "clienti.txt");
									}
									catch (IOException e) {
										System.err.println("Eroare la actualizarea fisierului de clienti...");
										scanner.close();
										System.exit(0);
									}
								default:
									System.out.println("Optiune indisponibila");
									System.out.println(meniuClient);
									break;
								}
							}
							}
						}
					}
				}
				if(k==0) {
					System.out.println("Nu exista niciun client cu acest CNP!");
					System.out.println("\n"+ meniuAutentificare);
				}
				break;
			case 3:
				try{
					closeApp(scanner, listaClienti, "clienti.txt");
				}
				catch (IOException e) {
					System.err.println("Eroare la actualizarea fisierului de clienti...");
					scanner.close();
					System.exit(0);
				}
				break;
			default:
				System.out.println("Optiune indisponibila");
				System.out.println(meniuAutentificare);
				break;
			}
		
	}
	
	}
}
