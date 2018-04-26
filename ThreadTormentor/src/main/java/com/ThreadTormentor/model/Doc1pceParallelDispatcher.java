package com.ThreadTormentor.model;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import com.ThreadTormentor.runner.Entregador;

public class Doc1pceParallelDispatcher extends Job implements Work{

	//private List<String> plnConfigTemplateFiles;
	private String hip;
	private String inputRegex;
	private String inputPath;
	private String tmpWorkingPath;
	private String iniFile;
	
	public Doc1pceParallelDispatcher() {	}
	
	
	public int execute(Map<String, String> vars) throws Exception {
		ArrayList<Work> newWorkers = new ArrayList<Work>();
		
		vars=reInterpolate(vars);
		if(! new File(inputPath).isDirectory()) {
	        throw new IllegalArgumentException(inputPath+" no puede ser abierto como un directorio.");
	    }
	    final Pattern p = Pattern.compile(inputRegex); // careful: could also throw an exception!
	    File[] matchedFiles = (new File(inputPath)).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return p.matcher(file.getName()).matches();
			}
		});
	    
		for(File inputFile : matchedFiles) {
			Doc1pce worker = new Doc1pce();
			worker.setHip(hip);
			worker.setTmpWorkingPath(tmpWorkingPath);
			//worker.setNext(getNext());
			
			//newWorkers.add(worker);
		}
		
		setNext(newWorkers);
		Entregador.getInstance().addJobs(newWorkers);
		return 0;
	}
	
	public String toString() {
		return getClass().getSimpleName()+" "+ getId() +" (x"+inputRegex+")";
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


	public String getHip() {
		return hip;
	}


	public void setHip(String hip) {
		this.hip = hip;
	}


	public String getIniFile() {
		return iniFile;
	}


	public void setIniFile(String iniFile) {
		this.iniFile = iniFile;
	}
}
