package runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.Echoer;
import model.Finisher;
import model.Sleeper;
import model.Work;
import model.doc1gen;

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
		laburos= new ArrayList<>();
		allJobs= new ArrayList<>();
		terminados= new ArrayList<>();
	}
	
	private List <Work> laburos;
	private List <Integer> terminados;
	private List <Work> allJobs;
	private int returnStatus= STATUS_OK;
	private int sleeperTime;
	private Map<String,String> vars;
	
	private boolean noMoreWorks=false;
	
	public synchronized void addLaburos(List <Work> nuevosLaburos) {
		laburos.addAll(nuevosLaburos);
	}
	
	public synchronized void addLaburo(Work nuevosLaburo) {
		laburos.add(nuevosLaburo);
		
	}

	public void noMoreWorks() {
		noMoreWorks=true;
	}
	
	public synchronized Work getLaburo() {
		if(noMoreWorks)
			return null;
		if(laburos.size()>0) {
			return laburos.remove(0);
		} else {
			return new Sleeper(3);
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
		noMoreWorks();
	}
	
	public synchronized void recoverJobs(List<Integer> jobIds) {
		for(int jobId: jobIds) {
			recoverJob(jobId);
		}
	}
	
	public synchronized void recoverJob(int jobId) {
		Work newWork = null;
		for(Work work: allJobs) {
			if(work.getId()==jobId) {
				newWork=work;
			}
		}
		if(newWork==null) {
			return;
		}
		for(int id: newWork.getPrevious()) {
			if(!terminados.contains(id)) {
				return;
			}
		}
		addLaburo(newWork);
	}
	
	private void getJobsFromXml(String inputFile) throws ParserConfigurationException, SAXException, IOException {
		File fXmlFile = new File(inputFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("job");
				
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				
	            allJobs.add(getWorkByElement((Element) nNode));
				}
			}
	}

	private Work getWorkByElement(Element eElement) throws ParserConfigurationException {
		Work work;
		switch (eElement.getAttribute("type")) {
        case "Sleeper":
             work = new Sleeper(Integer.parseInt(eElement.getElementsByTagName("time").item(0).getTextContent()));
             break;
         case "Echoer":
             work = new Echoer(eElement.getElementsByTagName("text").item(0).getTextContent());
        	 break;
         case "Finisher":
             work = new Finisher();
        	 break;
         case "Doc1gen":
             work = new doc1gen(vars,eElement.getElementsByTagName("HIP").item(0).getTextContent(),eElement.getElementsByTagName("OPS_TEMPLATE_FILE").item(0).getTextContent());
        	 break;
         default:
             throw new ParserConfigurationException();
		}
		String next= null;
		work.setId(Integer.parseInt(eElement.getAttribute("id")));
		try {
			 next = eElement.getElementsByTagName("next").item(0).getTextContent();
		} catch(NullPointerException e) {
			
		}
        String previous = null;
        try {
        	previous = eElement.getElementsByTagName("previous").item(0).getTextContent();
        } catch(NullPointerException e) {
			
		}
        ArrayList<Integer> nextArr= new ArrayList<>();
        ArrayList<Integer> previousArr= new ArrayList<>();
        if(next != null && !next.equals("")) {
        	for(String str : next.split(";")) {
        		nextArr.add(new Integer(Integer.parseInt(str)));
        	}
        }
        if(previous != null && !previous.equals("")) {
        	for(String str : previous.split(";")) {
        		previousArr.add(new Integer(Integer.parseInt(str)));
        	}
        }
        work.setNext(nextArr);
        work.setPrevious(previousArr);
		return work;
	}
	
	public int getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(int returnStatus) {
		this.returnStatus = returnStatus;
	}
	
	

	public int execute(String inputFile, int canthilos, int sleeperTime, int tpoEsperaEntreChusmeos, Map<String,String> vars) throws ParserConfigurationException, SAXException, IOException {
		this.vars=vars;
		getJobsFromXml(inputFile);
		setSleeperTime(sleeperTime);
		addLaburo(allJobs.get(0));
				
		boolean hilosCorriendo= true;
		
		ArrayList<Laburador> laburadores = new ArrayList<>();
		
		for(int i=0; i< canthilos; i++) {
			laburadores.add(new Laburador());
		}
		
		for(int i=0; i< canthilos; i++) {
			(new Thread(laburadores.get(i))).start();
		}
		
		while(hilosCorriendo) {
			System.out.println("[D]: Subhilos corriendo, espero "+tpoEsperaEntreChusmeos+"s y chusmeo de nuevo.");
			try {
				TimeUnit.SECONDS.sleep(tpoEsperaEntreChusmeos);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				System.out.println("\"[D]: ERROR. Sleep "+tpoEsperaEntreChusmeos+ "s fallo:"+e.getMessage());
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
		return returnStatus;
	}
	
}
