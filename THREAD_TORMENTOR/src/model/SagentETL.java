package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SagentETL extends Job implements Work{

	private static final String KEY_PLN_CONF_TEMPLATE_FILE = "plnConfTemplateFile";
	private static final String KEY_PLN_CONF_FINAL_FILE = "IN_CONF_FILE";
	private static final String KEY_TMP_WORKING_PATH = "tmpWorkingPath";
	private static final String KEY_EXECUTION_COMMAND = "IN_EXEC";
	
	private String plnConfigTemplateFile;
	private String tmpWorkingPath;
	
	public SagentETL (){	}
	
	public int execute(Map<String,String> vars) {
		vars.put(KEY_PLN_CONF_TEMPLATE_FILE, plnConfigTemplateFile);
		vars.put(KEY_TMP_WORKING_PATH, tmpWorkingPath);
		
		
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream("sagentETL.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			return STATUS_PROP_ERROR;
		}
		
		for(Object obj: prop.keySet()) {
			String key = new String((String)obj);
			vars.put(key, prop.getProperty(key));
		}
		
		
		
		vars.put(KEY_PLN_CONF_FINAL_FILE, vars.get(KEY_TMP_WORKING_PATH)+"/"+vars.get("MASK")+"_"+removeFilePathAndExtension(vars.get(KEY_PLN_CONF_TEMPLATE_FILE))+".conf");
		vars=reInterpolate(vars);
		String EXEC = prop.getProperty(KEY_EXECUTION_COMMAND);
		EXEC = interpolate(EXEC, vars);
		int res=1;
		try {
			String logFile=vars.get("DOC1_SRC_LOG")+"/"+vars.get("MASK")+"_"+getClass().getSimpleName()+"_"+removeFilePathAndExtension(vars.get(KEY_PLN_CONF_TEMPLATE_FILE))+".log";
			createConfigFile(vars.get(KEY_PLN_CONF_TEMPLATE_FILE),vars.get(KEY_PLN_CONF_FINAL_FILE) ,vars);
			res=execCMD(EXEC, logFile);
		} catch (Exception e) {
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
