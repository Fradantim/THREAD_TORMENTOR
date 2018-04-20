package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SagentETL extends Job implements Work{

	private String inputFileName;
	private Map<String,String> vars;
	
	public SagentETL (String inputFileName){
		this.inputFileName=inputFileName;
	}
	
	public int execute(Map<String,String> vars) {
		//TOTO TERMINAR!!!!!!!!
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream("master.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			return STATUS_PROP_ERROR;
		}
		
		for(Object obj: prop.keySet()) {
			String key = new String((String)obj);
			vars.put(key, prop.getProperty(key));
		}
		
		
		
		String SAGENT_EXEC = prop.getProperty("EXEC");
				
		Runtime rt = Runtime.getRuntime();
		Process pr;
		int res=1;
		try {
			pr = rt.exec("");
			res=pr.exitValue();
		} catch (IOException e) {
			e.printStackTrace();
			return STATUS_CMD_ERROR;
		}
		
		return res;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public String toString() {
		return getClass().getSimpleName()+" "+ getId() +" ("+inputFileName+")";
	}

	public List<Work> getNext(){
		return next;
	}
	
	public void setNext(List<Work> next) {
		this.next=next;
	}

	public Map<String,String> getVars() {
		return vars;
	}

	public void setVars(Map<String,String> vars) {
		this.vars = vars;
	}

}
