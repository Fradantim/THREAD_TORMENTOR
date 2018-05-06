package com.ThreadTormentor.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Doc1pce extends Job implements Work{

	private static final String KEY_INI_TEMPLATE_FILE = "iniTemplateFile";
	private static final String KEY_INI_FINAL_FILE = "IN_INI_FILE";
	public static final String KEY_TMP_INPUT_FILE_NO_EXT = "IN_TMP_INPUT_FILE_NO_EXT";
	private static final String KEY_INPUT_FILE = "IN_INI_INPUT_FILE";
	private static final String KEY_TMP_WORKING_PATH = "tmpWorkingPath";
	private static final String KEY_EXECUTION_COMMAND = "IN_DOC1PCE_EXEC";
	private static final String KEY_LOG_PATH = "IN_LOG";
	
	private String iniTemplateFile;
	private String inputFile;
	private String tmpWorkingPath;
	
	private String EXEC = "---";

	public Doc1pce() {	}
	
	public int execute(Map<String,String> inputVars) throws Exception {
		if(skippable) {
			return 0;
		}
		vars= new HashMap<String, String>(inputVars);
		vars.put(KEY_INI_TEMPLATE_FILE, iniTemplateFile);
		vars.put(KEY_TMP_WORKING_PATH, tmpWorkingPath);
		vars.put(KEY_INPUT_FILE, removeFilePathAndExtension(inputFile));
		
		vars.put(KEY_INI_FINAL_FILE, vars.get(KEY_TMP_WORKING_PATH)+"/"+vars.get("MASK")+"_"+removeFilePathAndExtension(vars.get(KEY_INPUT_FILE))+
				"_"+removeFilePathAndExtension(vars.get(KEY_INI_TEMPLATE_FILE))+".ini");
		
		try {
			vars=reInterpolate(vars);			
			createConfigFile(vars.get(KEY_INI_TEMPLATE_FILE),vars.get(KEY_INI_FINAL_FILE) ,vars);
		} catch (IOException e1) {
			e1.printStackTrace();
			return STATUS_CONFIG_ERROR;			
		}

		EXEC = vars.get(KEY_EXECUTION_COMMAND);
		EXEC = interpolate(EXEC, vars);
				
		
		int res=1;
		String logFile=vars.get(KEY_LOG_PATH)+"/"+vars.get("MASK")+"_"+getClass().getSimpleName()+"_"+removeFilePathAndExtension(iniTemplateFile)+"_"+removeFilePathAndExtension(inputFile)+".log";
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
		return getClass().getSimpleName()+" "+ getId() +" ("+iniTemplateFile+" "+removeFilePathAndExtension(inputFile)+")";
	}
	
	public void setVars(Map<String,String> vars) {
		this.vars = vars;
	}

	public String getIniTemplateFile() {
		return iniTemplateFile;
	}

	public void setIniTemplateFile(String iniTemplateFile) {
		this.iniTemplateFile = iniTemplateFile;
	}

	public String getTmpWorkingPath() {
		return tmpWorkingPath;
	}

	public void setTmpWorkingPath(String tmpWorkingPath) {
		this.tmpWorkingPath = tmpWorkingPath;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

}

