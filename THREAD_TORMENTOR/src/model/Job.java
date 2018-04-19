package model;

import java.util.List;

public abstract class Job {
	
	protected int id;
	protected String type;
	protected List<Integer> next;
	protected List<Integer> previous;
	
	public Job (){	}
}
