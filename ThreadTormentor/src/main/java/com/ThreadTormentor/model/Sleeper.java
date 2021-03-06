package com.ThreadTormentor.model;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Sleeper extends Job implements Work{
	private long time;
	
	public Sleeper (int maxSegs){
		super();
		time = maxSegs;
	}
	
	public Sleeper() {	}
	
	public int execute(Map<String,String> vars) {
		if(skippable) {
			return 0;
		}
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Laburo "+id+" excepcion "+ e.getMessage());
			return 1;
		}
		return 0;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time=time;
	}
	
	public String toString() {
		return getClass().getSimpleName()+" "+id +" ("+getTime()+"s)";
	}
}
