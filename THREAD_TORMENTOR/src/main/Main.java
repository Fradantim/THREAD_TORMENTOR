package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.xml.sax.SAXException;

import model.Work;
import runner.Entregador;

public class Main {
	public static void main (String args[]) {
		int result=0;
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream("master.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
		
		
		Map<String,String> vars = new HashMap<>();
		for(Object obj: prop.keySet()) {
			String key = new String((String)obj);
			vars.put(key, prop.getProperty(key));
		}
		
		prop = new Properties();
		try {
			prop.load(new FileInputStream("inputParams.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}

		for(Object obj: prop.keySet()) {
			String key = new String((String)obj);
			vars.put(key, prop.getProperty(key));
		}

		/* UBICAME BIEN*/
		vars.put("NEGOCIO", "movil");
		vars.put("LOTE_ID", "ML");
		
		int canthilos = Integer.parseInt(vars.get("canthilos"));
		int tpoEsperaEntreChusmeos= Integer.parseInt(vars.get("tpoEsperaEntreChusmeos"));
		int sleeperTime = Integer.parseInt(vars.get("sleeperTime"));
		
		
		//JARPATH new File(MyClass.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		String inputLoteContextFile="context15.xml";
		
		ApplicationContext inputLoteContext = new FileSystemXmlApplicationContext(inputLoteContextFile);
		Map<String, Work> worksMap = inputLoteContext.getBeansOfType(Work.class);
		ArrayList<Work> works = new ArrayList<>();
		
		for(String key : worksMap.keySet()) {
			works.add(worksMap.get(key));
		}
		
		Entregador entregador = Entregador.getInstance();
		
		
		try {
			result=entregador.execute(works, canthilos, sleeperTime, tpoEsperaEntreChusmeos, vars);
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Subhilos terminaron de correr, sigo con otras operaciones.");
		System.out.println("FINMAIN");
		((ConfigurableApplicationContext)inputLoteContext).close();
		System.exit(result);
	}
}
