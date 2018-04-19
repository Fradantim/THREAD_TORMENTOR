package runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class Entregador {
	
	private static Entregador instancia;
	
	public static Entregador getInstance(){ 
		try {
			return getInstance("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return null;
	}
	
	public static Entregador getInstance(String inputFile) throws ParserConfigurationException, SAXException, IOException {
		if (instancia == null) {
			instancia = new Entregador();
			instancia.ready(inputFile);
		}
		return instancia;
	}

	public void ready(String inputFile) throws ParserConfigurationException, SAXException, IOException {
		laburos= new ArrayList<>();
		allJobs= new ArrayList<>();
		terminados= new ArrayList<>();
		getJobsFromXml(inputFile);
		addLaburo(allJobs.get(0));
	}
	
	private List <Work> laburos;
	private List <Integer> terminados;
	private List <Work> allJobs;
	private int sleeperTime;
	
	private boolean noMoreWorks=false;
	
	public void addLaburos(List <Work> nuevosLaburos) {
		laburos.addAll(nuevosLaburos);
	}
	
	public void addLaburo(Work nuevosLaburo) {
		laburos.add(nuevosLaburo);
		
	}

	public void noMoreWorks() {
		noMoreWorks=true;
	}
	
	public Work getLaburo() {
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
		noMoreWorks();
	}
	
	public void recoverJobs(List<Integer> jobIds) {
		for(int jobId: jobIds) {
			recoverJob(jobId);
		}
	}
	
	public void recoverJob(int jobId) {
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
				Element eElement = (Element) nNode;
				Work work;
				switch (eElement.getAttribute("type")) {
		        case "Sleeper":
		        	System.out.println(Integer.parseInt(eElement.getElementsByTagName("time").item(0).getTextContent()));
		             work = new Sleeper(Integer.parseInt(eElement.getElementsByTagName("time").item(0).getTextContent()));
		             break;
		         case "Echoer":
		             work = new Echoer(eElement.getElementsByTagName("text").item(0).getTextContent());
		        	 break;
		         case "Finisher":
		             work = new Finisher();
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
	            allJobs.add(work);
				}
			}
	}
	
}
