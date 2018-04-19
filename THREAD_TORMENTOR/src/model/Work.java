package model;

import java.util.List;

public interface Work {
	public int execute();
	
	public int getId();
	
	public List<Integer> getNext();
	public List<Integer> getPrevious();
	public void setNext(List<Integer> next);
	public void setPrevious(List<Integer> previous);
	
	public void setId(int id);
}
