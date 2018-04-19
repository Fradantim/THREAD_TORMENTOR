package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import runner.Entregador;
import runner.Laburador;

public class Main {
	public static void main (String args[]) {
		int canthilos = 5;
		int tpoEsperaEntreChusmeos= 5;
		
		String inputFile="input.xml";
		
		Entregador entregador;
		try {
			entregador = Entregador.getInstance(inputFile);
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			return;
		} catch (SAXException e1) {
			e1.printStackTrace();
			return;
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		ArrayList<Laburador> laburadores = new ArrayList<>();
		
		for(int i=0; i< canthilos; i++) {
			laburadores.add(new Laburador());
		}
		
		for(int i=0; i< canthilos; i++) {
			System.out.println("Hilo "+ i);
			(new Thread(laburadores.get(i))).start();
		}
		
		boolean hilosCorriendo= true;
		while(hilosCorriendo) {
			System.out.println("Subhilos siguen corriendo, espero y chusmeo de nuevo.");
			try {
				TimeUnit.SECONDS.sleep(tpoEsperaEntreChusmeos);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				System.out.println("Oooops!");
			}
			hilosCorriendo= false;
			for(int i=0; i< canthilos; i++) {
				if(laburadores.get(i).getStatus() == Laburador.STATUS_CORRIENDO && !hilosCorriendo) {
					hilosCorriendo=true;
				}
			}
		}
		System.out.println("Subhilos terminaron de correr, sigo con otras operaciones.");
		System.out.println("FINMAIN");
	}
}
