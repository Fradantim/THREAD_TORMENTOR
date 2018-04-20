package model;

import java.util.List;
import java.util.Map;

import runner.Entregador;

public class Finisher extends Job implements Work{
		
	public int execute(Map<String,String> vars) {
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
