package com.ThreadTormentor.runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


import com.ThreadTormentor.model.Sleeper;
import com.ThreadTormentor.model.Work;


public class Entregador {
	
	public static final int STATUS_OK = 0;
	public static final int STATUS_WORK_ERROR = 1;
	public static final int STATUS_SLEEP_ERROR = 2;
	
	private static Entregador instancia;
	
	public static synchronized Entregador getInstance() {
		if (instancia == null) {
			instancia = new Entregador();
			instancia.ready();
		}
		return instancia;
	}

	private void ready() {
		laburos= new ArrayList<Work>();
		allJobs= new ArrayList<Work>();
		terminados= new ArrayList<Integer>();
	}
	
	private List <Work> laburos;
	private List <Integer> terminados;
	private List <Work> allJobs;
	private int returnStatus= STATUS_OK;
	private int sleeperTime;
	private int skippables=0;
	private int skipped=0;
	
	private boolean noMoreWorks=false;

	public synchronized void addJobs(List <Work> nuevosLaburos) {
		allJobs.addAll(nuevosLaburos);
	}
	
	private synchronized void addLaburos(List <Work> nuevosLaburos) {
		laburos.addAll(nuevosLaburos);
	}
	
	private synchronized void addLaburo(Work nuevosLaburo) {
		laburos.add(nuevosLaburo);
	}

	public void setNoMoreWorks() {
		noMoreWorks=true;
	}
	
	public synchronized Work getLaburo() {
		if(noMoreWorks)
			return null;
		if(laburos.size()>0) {
			Work laburoAEntragar= laburos.remove(0);
			if(skipped<skippables) {
				laburoAEntragar.setSkippable(true);
				skipped++;
			}
			return laburoAEntragar;
		} else {
			return new Sleeper(sleeperTime);
		}
		
	}
	
	public int getSleeperTime() {
		return sleeperTime;
	}

	public void setSleeperTime(int sleeperTime) {
		this.sleeperTime = sleeperTime;
	}
	
	public void addTerminado(int terminado) {
		terminados.add(terminado);
	}

	public void halt() {
		returnStatus=STATUS_WORK_ERROR;
		say("[D]: Recibido pedido de alto.");
		setNoMoreWorks();
	}
	
	public synchronized void recoverJobs(List<Work> works) {
		for(Work work: works) {
			recoverJob(work);
		}
	}
	
	public synchronized void recoverJob(Work work) {
		Work newWork = null;
		for(Work innerWork: allJobs) {
			if(innerWork.equals(work)) {
				newWork=work;
			}
		}
		if(newWork==null) {
			return;
		}
		for(Work previousWork: findPrevious(newWork)) {
			if(!terminados.contains(previousWork.getId())) {
				return;
			}
		}
		addLaburo(newWork);
	}
	
	public int getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(int returnStatus) {
		this.returnStatus = returnStatus;
	}
	
	

	public int execute(List<Work> works, int canthilos, int sleeperTime, int tpoEsperaEntreChusmeos, Map<String,String> vars) throws ParserConfigurationException, SAXException, IOException {
		allJobs=works;
		
		setSleeperTime(sleeperTime);
		skippables=Integer.parseInt(vars.get("IN_SKIP"));
		
		addLaburos(findStarters());
		
		boolean hilosCorriendo= true;
		
		ArrayList<Laburador> laburadores = new ArrayList<Laburador>();
		
		for(int i=0; i< canthilos; i++) {
			laburadores.add(new Laburador(vars));
		}
		
		for(int i=0; i< canthilos; i++) {
			(new Thread(laburadores.get(i))).start();
		}
		
		while(hilosCorriendo) {
			if(noMoreWorks) {
				say("[D]: Subhilos siquen corriendo, espero "+tpoEsperaEntreChusmeos+"s y chusmeo de nuevo.");
			}
			try {
				TimeUnit.SECONDS.sleep(tpoEsperaEntreChusmeos);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				say("[D]: ERROR. Sleep "+tpoEsperaEntreChusmeos+ "s fallo:"+e.getMessage());
				returnStatus=STATUS_SLEEP_ERROR;
				return returnStatus;
			}
			hilosCorriendo= false;
			for(int i=0; i< canthilos; i++) {
				if(laburadores.get(i).getStatus() == Laburador.STATUS_CORRIENDO && !hilosCorriendo) {
					hilosCorriendo=true;
				}
			}
		}
		System.out.println("[D]: Subhilos terminaron de correr.");
		return returnStatus;
	}
	
	private List<Work> findPrevious(Work work){
		ArrayList<Work> previousWorks = new ArrayList<Work>();
		for(Work innerWork : allJobs) {
			if (innerWork.getNext().contains(work)) {
				previousWorks.add(innerWork);
			}
		}
		return previousWorks;
	}
	
	private List<Work> findStarters(){
		ArrayList<Work> starterWorks = new ArrayList<Work>();
		for(Work innerWork : allJobs) {
			if (findPrevious(innerWork).size()==0) {
				starterWorks.add(innerWork);
			}
		}
		return starterWorks;
	}
	
	private void say(String str) {
		System.out.println(str);
	}
}
