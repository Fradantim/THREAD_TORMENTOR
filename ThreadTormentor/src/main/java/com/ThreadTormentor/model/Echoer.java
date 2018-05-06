package com.ThreadTormentor.model;

import java.util.Map;

public class Echoer extends Job implements Work{

	private String str;
	
	public Echoer (String str){
		this.str=str;
	}
	
	public int execute(Map<String,String> vars) {
		if(skippable) {
			return 0;
		}
		System.out.println("Echoer("+id+"): "+toString());
		return 0;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public String toString() {
		return getClass().getSimpleName()+" "+ getId() +" ("+str+")";
	}
}
