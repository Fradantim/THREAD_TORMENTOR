package com.ThreadTormentor.runner;

import java.util.Date;
import java.util.Map;

import com.ThreadTormentor.model.Sleeper;
import com.ThreadTormentor.model.Work;

public class Laburador implements Runnable{

	public static final int STATUS_CORRIENDO  = -1;
	public static final int STATUS_FINALIZADO_OK = 0;
	public static final int STATUS_FINALIZADO_ERROR = 1;
	Entregador entregador;
	
	private static int numerador;
	private int id;
	private Map<String,String> vars;
	
	private int status;
	
	public Laburador (Map<String,String> vars){
		numerador++;
		id = numerador;
		entregador = Entregador.getInstance();
		status=STATUS_CORRIENDO;
		this.vars= vars;
	}
	
	@Override
	public void run() {
		Work laburo= entregador.getLaburo();
		say("START THREAD");
		Date start;
		Date end;
		while (laburo!=null) {			
			int result = 1;
			start= new Date();
			try {
				if(!(laburo instanceof Sleeper)) {
					say("START "+laburo.toString());
				}
				result = laburo.execute(vars);
			}catch (Exception e ) {
				end= new Date();
				say("ERROR EXCEPTION! "+laburo.toString()+" "+getTimeDiff(start, end));
				e.printStackTrace();
				status=STATUS_FINALIZADO_ERROR;
				entregador.halt();
				return;
			}
			if(result != 0) {
				end= new Date();
				say("ERROR "+laburo.toString()+" "+getTimeDiff(start, end)+" err:"+result);
				status=STATUS_FINALIZADO_ERROR;
				entregador.halt();
				return;
			} 
			end= new Date();
			entregador.addTerminado(laburo.getId());
			if(laburo.getNext()!=null && laburo.getNext().size()>0) {
				entregador.recoverJobs(laburo.getNext());
			}
			if(!(laburo instanceof Sleeper)) {
				say("END "+laburo.toString()+" "+getTimeDiff(start, end));
			}
			

			laburo= entregador.getLaburo();
		}
		status=STATUS_FINALIZADO_OK;
	}
	
	private String getTimeDiff(Date start, Date end) {
		long segs = (end.getTime()-start.getTime())/1000;
		return String.format("(%03d:%02d:%02d)",segs/3600,segs%3600/60,segs%60);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void say(String in) {
		System.out.println("[T"+id+"]: "+in);
	}
}
