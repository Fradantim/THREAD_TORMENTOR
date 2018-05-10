package com.ThreadTormentor.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.xml.sax.SAXException;

import com.ThreadTormentor.model.Work;
import com.ThreadTormentor.runner.Entregador;

public class Main {
	public static void main (String args[]) {
		Date start= new Date();
		System.out.println("INICIO: "+start);
		if(args.length<1) {
			System.out.println("Indicar archivo de parametros como primer parametro.\n");
			System.exit(1);
			return;
		}
		
		String inputParams=args[0];
		
		int result=0;
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream("master.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
		
		
		Map<String,String> vars = new HashMap<String,String>();
		for(Object obj: prop.keySet()) {
			String key = new String((String)obj);
			vars.put(key, prop.getProperty(key));
		}
		
		prop = new Properties();
		try {
			prop.load(new FileInputStream(inputParams));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}

		for(Object obj: prop.keySet()) {
			String key = new String((String)obj);
			vars.put(key, prop.getProperty(key));
		}

		int canthilos = Integer.parseInt(vars.get("IN_CANTIDAD_HILOS"));
		int tpoEsperaEntreChusmeos= Integer.parseInt(vars.get("IN_TPO_ESPERA_ENTRE_CHUSMEOS"));
		int sleeperTime = Integer.parseInt(vars.get("IN_SLEEPER_TIME"));
		
		
		//JARPATH new File(MyClass.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		String inputLoteContextFile=vars.get("IN_APPLICATION_CONTEXT_PATH")+"/"+"context"+vars.get("LOTE_NRO")+".xml";
		//String inputLoteContextFile="context"+vars.get("LOTE_NRO")+".xml";
		ApplicationContext inputLoteContext = new FileSystemXmlApplicationContext(inputLoteContextFile);
		
		
		Map<String, String> stringMap = inputLoteContext.getBeansOfType(String.class);
		for(String key : stringMap.keySet()) {
			vars.put(key, stringMap.get(key));
		}
		
		Map<String, Work> worksMap = inputLoteContext.getBeansOfType(Work.class);
		ArrayList<Work> works = new ArrayList<Work>();
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
		
		
		((ConfigurableApplicationContext)inputLoteContext).close();
		Date end = new Date();
		long segs = (end.getTime()-start.getTime())/1000;
		System.out.print("FINMAIN "+ end + " ");
		System.out.println(String.format("(%02d:%02d:%02d)",segs/3600,segs%3600/60,segs%60));
		System.exit(result);
	}
}
