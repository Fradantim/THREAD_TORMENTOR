package com.ThreadTormentor.runner;

import java.util.Map;

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
		while (laburo!=null) {			
			int result = 1;
			try {
				say("START "+laburo.toString());
				result = laburo.execute(vars);
			}catch (Exception e ) {
				say("ERROR EXCEPTION! "+laburo.toString());
				e.printStackTrace();
				status=STATUS_FINALIZADO_ERROR;
				entregador.halt();
				return;
			}
			if(result != 0) {
				say("ERROR "+laburo.toString()+" err:"+result);
				status=STATUS_FINALIZADO_ERROR;
				entregador.halt();
				return;
			} 
			entregador.addTerminado(laburo.getId());
			if(laburo.getNext()!=null && laburo.getNext().size()>0) {
				entregador.recoverJobs(laburo.getNext());
			}
			say("END "+laburo.toString());

			laburo= entregador.getLaburo();
		}
		status=STATUS_FINALIZADO_OK;
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
