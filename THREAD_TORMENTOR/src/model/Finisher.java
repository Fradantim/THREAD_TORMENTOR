package model;

import java.util.Map;

import runner.Entregador;

public class Finisher extends Job implements Work{
		
	public int execute(Map<String,String> vars) {
		Entregador.getInstance().setNoMoreWorks();
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
}
