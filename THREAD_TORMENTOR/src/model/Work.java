package model;

import java.util.List;
import java.util.Map;

public interface Work {
	
	public int execute(Map<String,String> vars);
	
	public int getId();
	
	public List<Work> getNext();
	public void setNext(List<Work> next);
	
	public void setId(int id);
}
