package com.ThreadTormentor.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ThreadTormentor.exception.CorruptConfigurationFileException;

public class SagentETL extends Job implements Work{

	private static final String KEY_PLN_CONF_TEMPLATE_FILE = "plnConfTemplateFile";
	private static final String KEY_PLN_CONF_FINAL_FILE = "IN_CONF_FILE";
	private static final String KEY_TMP_WORKING_PATH = "tmpWorkingPath";
	private static final String KEY_EXECUTION_COMMAND = "IN_SAGENT_EXEC";
	private static final String KEY_LOG_PATH = "IN_LOG";
	
	private String plnConfigTemplateFile;
	private String tmpWorkingPath;
	
	public SagentETL (){	}
	
	public int execute(Map<String,String> vars) {
		if(skippable) {
			return 0;
		}
		vars.put(KEY_PLN_CONF_TEMPLATE_FILE, plnConfigTemplateFile);
		vars.put(KEY_TMP_WORKING_PATH, tmpWorkingPath);
				
		vars.put(KEY_PLN_CONF_FINAL_FILE, vars.get(KEY_TMP_WORKING_PATH)+"/"+vars.get("MASK")+"_"+removeFilePathAndExtension(vars.get(KEY_PLN_CONF_TEMPLATE_FILE))+".conf");
		vars=reInterpolate(vars);
		String EXEC = vars.get(KEY_EXECUTION_COMMAND);
		EXEC = interpolate(EXEC, vars);
		int res=1;
		try {
			String logFile=vars.get(KEY_LOG_PATH)+"/"+vars.get("MASK")+"_"+getClass().getSimpleName()+"_"+removeFilePathAndExtension(vars.get(KEY_PLN_CONF_TEMPLATE_FILE))+".log";
			createConfigFile(vars.get(KEY_PLN_CONF_TEMPLATE_FILE),vars.get(KEY_PLN_CONF_FINAL_FILE) ,vars);
			checkInputFiles(vars.get(KEY_PLN_CONF_FINAL_FILE));
			res=execCMD(EXEC, logFile);
		} catch (Exception e) {
			e.printStackTrace();
			return STATUS_CMD_ERROR;
		} 
	
		return res;
	}
	
	public void checkInputFiles(String file) throws IOException, FileNotFoundException, CorruptConfigurationFileException{
		BufferedReader inputReader = new BufferedReader(new FileReader(new File(file)));
        
		String errores="";
		try {
			String readLine = "";
	        while ((readLine = inputReader.readLine()) != null) {
	        	String[] arr = readLine.split("=");
	        	if(arr[0].startsWith("ARCH-IN")) {
	        		File inputFile = new File(arr[1]);
	        		if(!inputFile.exists() && !inputFile.toString().substring(inputFile.toString().lastIndexOf("/") +1).equals("NUL")) { 
	        		    errores=errores+ "No existe "+inputFile+"\n";
	        		}
	        	}
	        }
		} catch(Exception e) {
			throw new CorruptConfigurationFileException("El archivo generado de configuracion "+file+" se encuentra corrupto, o no posee valores para alguna clave.");
		} finally {
			inputReader.close();
		}
		if(!errores.equals("")) {
			throw new FileNotFoundException("\n"+errores);
		}
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public String toString() {
		return getClass().getSimpleName()+" "+ getId() +" ("+plnConfigTemplateFile+")";
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

	public String getPlnConfigTemplateFile() {
		return plnConfigTemplateFile;
	}

	public void setPlnConfigTemplateFile(String plnConfigTemplateFile) {
		this.plnConfigTemplateFile = plnConfigTemplateFile;
	}

	public String getTmpWorkingPath() {
		return tmpWorkingPath;
	}

	public void setTmpWorkingPath(String tmpWorkingPath) {
		this.tmpWorkingPath = tmpWorkingPath;
	}

}
