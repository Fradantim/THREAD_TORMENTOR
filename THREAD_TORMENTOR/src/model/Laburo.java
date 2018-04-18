package model;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Laburo {
	private static int numerador;
	private int id;
	private long time;
	
	public Laburo (int maxSegs){
		numerador++;
		id=numerador;
		time = new Random().nextInt(maxSegs - 1) + 1;
	}
	
	public void execute() {
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Laburo "+id+" excepcion "+ e.getMessage());
		}
	}
	
	public int getId() {
		return id;
	}
	
	public long getTime() {
		return time;
	}
}
