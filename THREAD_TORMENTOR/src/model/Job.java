package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import runner.SyncPipe;

public abstract class Job {
	
	protected static final int STATUS_PROP_ERROR=1;
	protected static final int STATUS_CMD_ERROR=2;
	protected static final int STATUS_CONFIG_GEN_ERROR=3;
	protected Map<String,String> vars;
	
	protected int id;
	protected String type;
	protected List<Integer> next;
	protected List<Integer> previous;
	
	public Job () {	}
	
	protected String interpolate(String str, Map<String,String> vars) {
		for(String var: vars.keySet()) {
			while(str.contains("$"+var+"$")) {
					str=str.replaceFirst(Pattern.quote("$"+var+"$"), "%s");
					str= String.format(str, vars.get(var));
			}
		}
		return str;
	}
	
	
	protected String removeFilePathAndExtension(String inputFile) {
		return new File(inputFile).getName().replaceFirst("[.][^.]+$", "");
	}
	
	protected void createConfigFile(String inputTemplateFile, String outputFile, Map<String,String> vars) throws IOException {
        BufferedReader inputReader = new BufferedReader(new FileReader(new File(inputTemplateFile)));
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile));
        
        String readLine = "";
        while ((readLine = inputReader.readLine()) != null) {
        	outputWriter.write(interpolate(readLine+"\r\n", vars));
        }
        
        inputReader.close();
        outputWriter.close();
	}
	
	protected Map<String,String> reInterpolate(Map<String,String> inputMap){
		Map<String,String> outputMap = new HashMap<>();
		for(String key: inputMap.keySet()) {
			outputMap.put(key, interpolate(inputMap.get(key),inputMap));
		}
		return outputMap;
	}
	
	protected int execCMD(String commandToExecute, String consoleOutput) throws IOException, InterruptedException {
		BufferedWriter outputWriter = null;
		if(consoleOutput!=null && !"".equals(consoleOutput)) {
			outputWriter = new BufferedWriter(new FileWriter(consoleOutput));
		}
		
		ProcessBuilder builder = new ProcessBuilder(("cmd.exe /c "+commandToExecute).split(" "));
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line= r.readLine())!=null) {
			outputWriter.write(line+"\r\n");
		}
		p.destroy();
		return p.exitValue(); 
	}
}
