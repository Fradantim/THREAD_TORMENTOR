package model;

import java.util.List;

import runner.Entregador;

public class Finisher extends Job implements Work{
		
	public int execute() {
		Entregador.getInstance().noMoreWorks();
		return 0;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public String toString() {
		return getClass().getSimpleName()+" "+getId();
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
