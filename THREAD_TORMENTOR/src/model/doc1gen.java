package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class doc1gen extends Job implements Work{

	private static final String KEY_OPS_TEMPLATE_FILE = "opsTemplateFile";
	private static final String KEY_OPS_FINAL_FILE = "OPS_FILE";
	private static final String KEY_HIP_FILE = "HIP";
	
	private String hip;
	private String OpsFile;
	
	private String EXEC = "---";

	public doc1gen() {
		
	}
	
	public int execute(Map<String,String> vars) {
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream("doc1gen.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			return STATUS_PROP_ERROR;
		}
		
		for(Object obj: prop.keySet()) {
			String key = new String((String)obj);
			vars.put(key, prop.getProperty(key));
		}

		vars.put(KEY_OPS_FINAL_FILE, vars.get("DOC1_GEN_TMP")+"/"+vars.get("MASK")+"_"+removeFilePathAndExtension(vars.get(KEY_OPS_TEMPLATE_FILE))+".ops");
		
		try {
			vars=reInterpolate(vars);
			createConfigFile(vars.get(KEY_OPS_TEMPLATE_FILE),vars.get(KEY_OPS_FINAL_FILE) ,vars);
		} catch (IOException e1) {
			e1.printStackTrace();
			return STATUS_CONFIG_GEN_ERROR;			
		}

		EXEC = prop.getProperty("EXEC");
		EXEC = interpolate(EXEC, vars);
				
		
		int res=1;
		String logFile=vars.get("DOC1_SRC_LOG")+"/"+vars.get("MASK")+"_"+getClass().getSimpleName()+"_"+getId()+"_"+removeFilePathAndExtension(vars.get(KEY_OPS_TEMPLATE_FILE))+".log";
		try {
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
		return getClass().getSimpleName()+" "+ getId() +" ("+vars.get(KEY_OPS_TEMPLATE_FILE)+")";
	}

	public List<Work> getNext(){
		return next;
	}
	
	public void setNext(List<Work> next) {
		this.next=next;
	}
	
	public List<Work> getPrevious() {
		return previous;
	}

	public void setPrevious(List<Work> previous) {
		this.previous=previous;
		
	}

	public void setVars(Map<String,String> vars) {
		this.vars = vars;
	}

	public String getHip() {
		return hip;
	}

	public void setHip(String hip) {
		this.vars.put(KEY_HIP_FILE, hip);
		this.hip = hip;
	}

	public String getOpsFile() {
		return OpsFile;
	}

	public void setOpsFile(String opsFile) {
		this.vars.put(KEY_OPS_TEMPLATE_FILE, OpsFile);
		OpsFile = opsFile;
	}

}

