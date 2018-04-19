package model;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Sleeper extends Job implements Work{
	private long time;
	
	public Sleeper (int maxSegs){
		super();
		time = maxSegs;
	}
	
	public int execute() {
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
	
	public String toString() {
		return getClass().getSimpleName()+" "+id +" ("+getTime()+"s)";
	}
	
	public List<Integer> getNext(){
		return next;
	}
	
	public void setNext(List<Integer> next) {
		this.next=next;
	}

	public List<Integer> getPrevious() {
		return previous;
	}

	public void setPrevious(List<Integer> previous) {
		this.previous=previous;
		
	}


}
