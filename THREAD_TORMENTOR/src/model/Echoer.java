package model;

import java.util.List;
import java.util.Map;

public class Echoer extends Job implements Work{

	private String str;
	
	public Echoer (String str){
		this.str=str;
	}
	
	public int execute(Map<String,String> vars) {
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

	public List<Work> getNext(){
		return next;
	}
	
	public void setNext(List<Work> next) {
		this.next=next;
	}

}
