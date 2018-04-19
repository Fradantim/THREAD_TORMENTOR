package model;

import java.io.File;
import java.util.List;
import java.util.Map;

public abstract class Job {
	
	protected static final int STATUS_PROP_ERROR=1;
	protected static final int STATUS_CMD_ERROR=2;
	protected static final int STATUS_CONFIG_GEN_ERROR=3;
	
	protected int id;
	protected String type;
	protected List<Integer> next;
	protected List<Integer> previous;
	
	public Job (){	}
	
	protected String interpolate(String str, Map<String,String> vars) {
		for(String var: vars.keySet()) {
			if(str.contains("$"+var+"$")) {
					str=str.replace("$"+var+"$", "%s");
					str= String.format(str, vars.get(var));
			}
		}
		return str;
	}
	
	protected String removeFileExtension(String inputFile) {
		return new File(inputFile).getName().replaceFirst("[.][^.]+$", "");
	}
}
