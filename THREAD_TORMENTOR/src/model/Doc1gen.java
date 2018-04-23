package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import exception.Doc1GenHimInexistenteException;

public class Doc1gen extends Job implements Work{

	private static final String KEY_OPS_TEMPLATE_FILE = "opsTemplateFile";
	private static final String KEY_OPS_FINAL_FILE = "IN_OPS_FILE";
	private static final String KEY_HIP_FILE = "HIP";
	private static final String KEY_TMP_WORKING_PATH = "tmpWorkingPath";
	private static final String KEY_HIM_PREFIX = "HIM_";
	private static final String KEY_HIM_CHOSEN_FILE = "IN_CHOSEN_HIM_FILE";
	private static final String KEY_EXECUTION_COMMAND = "IN_DOC1GEN_EXEC";
	private static final String KEY_LOG_PATH = "IN_LOG";
	
	private String hip;
	private String opsTemplateFile;
	private String tmpWorkingPath;
	
	private List<String> hims;	
	
	private String EXEC = "---";

	public Doc1gen() {	}
	
	public int execute(Map<String,String> inputVars) throws Exception {
		vars= new HashMap<String, String>(inputVars);
		vars.put(KEY_HIP_FILE, hip);
		vars.put(KEY_OPS_TEMPLATE_FILE, opsTemplateFile);
		vars.put(KEY_TMP_WORKING_PATH, tmpWorkingPath);
		
		int himIndex=0;
		for(String innerHim: hims) {
			vars.put(KEY_HIM_PREFIX+himIndex, innerHim);
			himIndex++;
		}
		int himSize=himIndex;
		
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

		vars.put(KEY_OPS_FINAL_FILE, vars.get(KEY_TMP_WORKING_PATH)+"/"+vars.get("MASK")+"_"+removeFilePathAndExtension(vars.get(KEY_OPS_TEMPLATE_FILE))+".ops");
		
		try {
			vars=reInterpolate(vars);
			
			if(hims!= null && hims.size()>0) {
				//se indicaron archivos him
				
				for(himIndex=0; himIndex<himSize; himIndex++) {
					String him= vars.get(KEY_HIM_PREFIX+himIndex);
					if(existFile(him)) {
						vars.put(KEY_HIM_CHOSEN_FILE, him);
						break;
					}
				}
				if(himIndex==himSize) {
					//no se encontro ninguno de los him
					String output ="Archivos him indicados:\r\n";
					for(himIndex=0; himIndex<himSize; himIndex++) {
						output=output+vars.get(KEY_HIM_PREFIX+himIndex)+"\r\n";
					}
					say(output);
					throw new Doc1GenHimInexistenteException("No se encontro en el filesystem ningun him para la ejecucion de doc1gen.");
				}
			}
			
			createConfigFile(vars.get(KEY_OPS_TEMPLATE_FILE),vars.get(KEY_OPS_FINAL_FILE) ,vars);
		} catch (IOException e1) {
			e1.printStackTrace();
			return STATUS_CONFIG_GEN_ERROR;			
		}

		EXEC = prop.getProperty(KEY_EXECUTION_COMMAND);
		EXEC = interpolate(EXEC, vars);
				
		
		int res=1;
		String logFile=vars.get(KEY_LOG_PATH)+"/"+vars.get("MASK")+"_"+getClass().getSimpleName()+"_"+removeFilePathAndExtension(vars.get(KEY_OPS_TEMPLATE_FILE))+".log";
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
		return getClass().getSimpleName()+" "+ getId() +" ("+opsTemplateFile+")";
	}
	
	public void setVars(Map<String,String> vars) {
		this.vars = vars;
	}

	public String getHip() {
		return hip;
	}

	public void setHip(String hip) {
		this.hip = hip;
	}

	public String getOpsTemplateFile() {
		return opsTemplateFile;
	}

	public void setOpsTemplateFile(String opsTemplateFile) {
		this.opsTemplateFile = opsTemplateFile;
	}

	public List<String> getHim() {
		return hims;
	}

	public void setHim(List<String> him) {
		this.hims = him;
	}

	public String getTmpWorkingPath() {
		return tmpWorkingPath;
	}

	public void setTmpWorkingPath(String tmpWorkingPath) {
		this.tmpWorkingPath = tmpWorkingPath;
	}

}

