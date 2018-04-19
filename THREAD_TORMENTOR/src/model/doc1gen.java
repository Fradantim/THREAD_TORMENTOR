package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class doc1gen extends Job implements Work{

	private String opsTemplateFile;
	private String HIP;
	private Map<String,String> vars;
	private String EXEC = "---";
	
	public doc1gen (String HIP, String OpsFile){
		this.opsTemplateFile=OpsFile;
		this.HIP=HIP;
	}
	
	public int execute() {
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

		String finalOpsFile = removeFileExtension(opsTemplateFile)+vars.get("MASK")+".ops";
		vars.put("HIP",HIP);
		vars.put("OPSFILE", finalOpsFile);

		try {
			createConfigFile(vars.get("DOC1_GEN_SRC")+opsTemplateFile,finalOpsFile );
		} catch (IOException e1) {
			e1.printStackTrace();
			return STATUS_CONFIG_GEN_ERROR;			
		}

		
		//vars.put("OPSFILE",opsTemplateFile);
		
		EXEC = prop.getProperty("EXEC");
		EXEC=interpolate(EXEC, vars);
				
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
		return getClass().getSimpleName()+" "+ getId() +" ("+HIP+" "+opsTemplateFile+")";
	}

	public List<Integer> getNext(){
		return next;
	}
	
	public void setNext(List<Integer> next) {
		this.next=next;
	}
	
	public List<Integer> getPrevious() {
		return previous;
	}

	public void setPrevious(List<Integer> previous) {
		this.previous=previous;
		
	}

	public void setVars(Map<String,String> vars) {
		this.vars = vars;
	}

	private void createConfigFile(String inputTemplateFile, String outputFile) throws IOException {
        BufferedReader inputReader = new BufferedReader(new FileReader(new File(inputTemplateFile)));
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile));
        
        String readLine = "";
        while ((readLine = inputReader.readLine()) != null) {
        	outputWriter.write(interpolate(readLine, vars));
        }
        
        inputReader.close();
        outputWriter.close();
	}
	
}

