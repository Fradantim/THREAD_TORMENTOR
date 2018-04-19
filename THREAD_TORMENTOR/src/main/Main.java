package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import runner.Entregador;

public class Main {
	public static void main (String args[]) {
		
		int result=0;
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream("master.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
			return;
		}
		
		String MASK ="";//CICLO_ESQ_LOTE_TMSPTM...
		Map<String,String> vars = new HashMap<>();
		for(Object obj: prop.keySet()) {
			String key = new String((String)obj);
			vars.put(key, prop.getProperty(key));
		}
		vars.put("MASK", MASK);
		
		int canthilos = Integer.parseInt(prop.getProperty("canthilos"));
		int tpoEsperaEntreChusmeos= Integer.parseInt(prop.getProperty("tpoEsperaEntreChusmeos"));
		int sleeperTime = Integer.parseInt(prop.getProperty("sleeperTime"));
		
		
		//JARPATH new File(MyClass.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		String inputFile="input.xml";
		
		Entregador entregador = Entregador.getInstance();
		
		
		try {
			result=entregador.execute(inputFile, canthilos, sleeperTime, tpoEsperaEntreChusmeos);
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Subhilos terminaron de correr, sigo con otras operaciones.");
		System.out.println("FINMAIN");
		System.exit(result);
	}
}
