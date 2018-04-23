package com.ThreadTormentor.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class Job {
	
	protected static final int STATUS_PROP_ERROR=1;
	protected static final int STATUS_CMD_ERROR=2;
	protected static final int STATUS_CONFIG_GEN_ERROR=3;
	protected Map<String,String> vars;
	
	protected List<Work> next;
	
	private static int numerador;
	protected int id;
	protected String workingPath;
	
	public Job () {
		id=++numerador;
	}
	
	protected String interpolate(String str, Map<String,String> inputVars) {
		for(String var: inputVars.keySet()) {
			while(str.contains("$"+var+"$")) {
					str=str.replaceFirst(Pattern.quote("$"+var+"$"), "%s");
					str= String.format(str, inputVars.get(var));
					str= interpolate(str,inputVars);
			}
		}
		return str;
	}
	
	
	protected String removeFilePathAndExtension(String inputFile) {
		return new File(inputFile).getName().replaceFirst("[.][^.]+$", "");
	}
	
	protected void createConfigFile(String inputTemplateFile, String outputFile, Map<String,String> inputVars) throws IOException {
        BufferedReader inputReader = new BufferedReader(new FileReader(new File(inputTemplateFile)));
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile));
        
        String readLine = "";
        while ((readLine = inputReader.readLine()) != null) {
        	outputWriter.write(interpolate(readLine+"\r\n", inputVars));
        }
        
        inputReader.close();
        outputWriter.close();
	}
	
	protected Map<String,String> reInterpolate(Map<String,String> inputMap){
		Map<String,String> outputMap = new HashMap<String,String>(inputMap);
		for(String key: outputMap.keySet()) {
			outputMap.replace(key, interpolate(outputMap.get(key),outputMap));
		}
		return outputMap;
	}
	
	protected int execCMD(String inputCommand, String consoleOutput) throws IOException, InterruptedException {
		BufferedWriter outputWriter = null;
		if(consoleOutput!=null && !"".equals(consoleOutput)) {
			outputWriter = new BufferedWriter(new FileWriter(consoleOutput));
		}
		
		String commandToExecute = "cmd.exe /c "+inputCommand;
		ProcessBuilder builder = new ProcessBuilder((commandToExecute).split(" "));
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		outputWriter.write(commandToExecute+"\r\n");
		while ((line= buffReader.readLine())!=null) {
			outputWriter.write(line+"\r\n");
		}
		p.destroy();
		buffReader.close();
		outputWriter.close();
		return p.exitValue(); 
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Job))return false;
	    Job otherMyClass = (Job)other;
	    if(this.id == otherMyClass.id) {
	    	return true;
	    }
	    return false;
	}
	
	public String getWorkingPath() {
		return workingPath;
	}

	public void setWorkingPath(String workingPath) {
		this.workingPath = workingPath;
	}

	protected boolean existFile(String file) {
		File f = new File(file);
		if(f.exists() && !f.isDirectory()) { 
		    return true;
		}
		return false;
	}
	
	protected void say(String str) {
		System.out.println(str);
	}
	
	public List<Work> getNext(){
		return next;
	}
	
	public void setNext(List<Work> next) {
		this.next=next;
	}
}
