package com.ThreadTormentor.model;

import java.util.HashMap;
import java.util.Map;


public class SimpleCmdLineJob extends Job implements Work{
	private static final String KEY_LOG_PATH = "IN_LOG";
	private static final String KEY_BIN_WITH_PATH = "IN_BIN_WITH_PATH";
	private static final String KEY_PARAMS = "IN_PARAMS";
	
	private String binWithPath;
	private String params;

	public SimpleCmdLineJob() {	}
	
	public int execute(Map<String,String> inputVars) throws Exception {
		if(skippable) {
			return 0;
		}
		vars= new HashMap<String, String>(inputVars);
		vars.put(KEY_BIN_WITH_PATH, binWithPath);
		vars.put(KEY_PARAMS, params);
		
		vars=reInterpolate(vars);

		int res=1;
		String logFile=vars.get(KEY_LOG_PATH)+"/"+vars.get("MASK")+"_"+getClass().getSimpleName()+"_"+removeFilePathAndExtension(vars.get(KEY_BIN_WITH_PATH)+" "+vars.get(KEY_PARAMS))+".log";
		try {
			res=execCMD(vars.get(KEY_BIN_WITH_PATH)+" "+vars.get(KEY_PARAMS), logFile);
		} catch (Exception e) {
			e.printStackTrace();
			return STATUS_CMD_ERROR;
		} 
		return res;
	}
	
	public String toString() {
		return getClass().getSimpleName()+" "+ getId() +" ("+binWithPath.replace("/", "_")+"_"+params.replaceAll("/", "_")+")";
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public String getBinWithPath() {
		return binWithPath;
	}

	public void setBinWithPath(String binWithPath) {
		this.binWithPath = binWithPath;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}

