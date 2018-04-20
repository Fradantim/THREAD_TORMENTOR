package model;

import java.util.List;
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
	
	public List<Work> getNext(){
		return next;
	}
	
	public void setNext(List<Work> next) {
		this.next=next;
	}

	public List<Work> getPrevious() {
		return previous;
	}

	public void setPrevious(List<Work> previous) {
		this.previous=previous;
		
	}


}
