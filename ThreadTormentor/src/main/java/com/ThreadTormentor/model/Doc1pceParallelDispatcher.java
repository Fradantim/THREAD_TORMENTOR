package com.ThreadTormentor.model;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import com.ThreadTormentor.runner.Entregador;

public class Doc1pceParallelDispatcher extends Job implements Work{

	private static final String KEY_INPUT_REGEX = "inputRegex";
	private static final String KEY_INPUT_PATH = "inputPath";
	
	private String inputRegex;
	private String inputPath;
	private String tmpWorkingPath;
	private String iniTemplateFile;
	
	public Doc1pceParallelDispatcher() {	}
	
	
	public int execute(Map<String, String> vars) throws Exception {
		ArrayList<Work> newWorks = new ArrayList<Work>();
		
		vars.put(KEY_INPUT_REGEX, inputRegex);
		vars.put(KEY_INPUT_PATH, inputPath);
		vars=reInterpolate(vars);
		if(! new File(vars.get(KEY_INPUT_PATH)).isDirectory()) {
	        throw new IllegalArgumentException(inputPath+" no puede ser abierto como un directorio.");
	    }
	    final Pattern p = Pattern.compile(vars.get(KEY_INPUT_REGEX)); // careful: could also throw an exception!
	    File[] matchedFiles = (new File(vars.get(KEY_INPUT_PATH))).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return p.matcher(file.getName()).matches();
			}
		});
	    
		for(File inputFile : matchedFiles) {
			//System.out.println(inputFile.toString());
			Doc1pce newWork = new Doc1pce();
			newWork.setInputFile(inputFile.toString());
			newWork.setIniTemplateFile(iniTemplateFile);
			newWork.setTmpWorkingPath(tmpWorkingPath);
			newWork.setNext(getNext());
			
			newWorks.add(newWork);
		}
		
		setNext(newWorks);
		Entregador.getInstance().addJobs(newWorks);
		return 0;
	}
	
	public String toString() {
		return getClass().getSimpleName()+" "+ getId() +" ("+inputPath+"/"+inputRegex+")";
	}
	
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}
	
	
	public void setId(int id) {
		this.id=id;		
	}

	public String getTmpWorkingPath() {
		return tmpWorkingPath;
	}


	public void setTmpWorkingPath(String tmpWorkingPath) {
		this.tmpWorkingPath = tmpWorkingPath;
	}


	public String getInputRegex() {
		return inputRegex;
	}


	public void setInputRegex(String inputRegex) {
		this.inputRegex = inputRegex;
	}


	public String getInputPath() {
		return inputPath;
	}


	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getIniTemplateFile() {
		return iniTemplateFile;
	}


	public void setIniTemplateFile(String iniTemplateFile) {
		this.iniTemplateFile = iniTemplateFile;
	}
}
