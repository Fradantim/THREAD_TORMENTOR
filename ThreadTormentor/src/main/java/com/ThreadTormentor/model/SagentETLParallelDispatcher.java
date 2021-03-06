package com.ThreadTormentor.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ThreadTormentor.runner.Entregador;

public class SagentETLParallelDispatcher extends Job implements Work{

	private List<String> plnConfigTemplateFiles;
	private String tmpWorkingPath;
	
	public SagentETLParallelDispatcher() {	}
	
	
	public int execute(Map<String, String> vars) throws Exception {
		ArrayList<Work> newWorks = new ArrayList<Work>();
		
		for(String plnConfigTemplateFile : plnConfigTemplateFiles) {
			SagentETL newWork = new SagentETL();
			newWork.setPlnConfigTemplateFile(plnConfigTemplateFile);
			newWork.setTmpWorkingPath(tmpWorkingPath);
			newWork.setNext(getNext());
			
			newWorks.add(newWork);
		}
		
		setNext(newWorks);
		Entregador.getInstance().addJobs(newWorks);
		//Entregador.getInstance().addLaburos(newWorkers);
		return 0;
	}
	
	public String toString() {
		return getClass().getSimpleName()+" "+ getId() +" (x"+plnConfigTemplateFiles.size()+")";
	}
	
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}
	
	
	public void setId(int id) {
		this.id=id;		
	}


	public List<String> getPlnConfigTemplateFiles() {
		return plnConfigTemplateFiles;
	}


	public void setPlnConfigTemplateFiles(List<String> plnConfigTemplateFiles) {
		this.plnConfigTemplateFiles = plnConfigTemplateFiles;
	}


	public String getTmpWorkingPath() {
		return tmpWorkingPath;
	}


	public void setTmpWorkingPath(String tmpWorkingPath) {
		this.tmpWorkingPath = tmpWorkingPath;
	}
}
