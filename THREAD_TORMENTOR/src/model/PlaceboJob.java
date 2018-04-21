package model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlaceboJob extends Job implements Work{

	public PlaceboJob() {	}
	
	public int execute(Map<String,String> vars) {
		return 0;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public String toString() {
		return getClass().getSimpleName()+" "+id;
	}
	
	public List<Work> getNext(){
		return next;
	}
	
	public void setNext(List<Work> next) {
		this.next=next;
	}
}
