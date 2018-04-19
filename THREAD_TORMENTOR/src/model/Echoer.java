package model;

import java.util.List;

public class Echoer extends Job implements Work{

	private static int numerador;
	private String str;
	
	public Echoer (String str){
		numerador++;
		id=numerador;
		this.str=str;
	}
	
	public int execute() {
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
