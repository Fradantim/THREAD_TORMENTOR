package runner;

import model.Work;

public class Laburador implements Runnable{

	public static final int STATUS_CORRIENDO  = 0;
	public static final int STATUS_FINALIZADO = 1;
	Entregador entregador;
	
	private static int numerador;
	private int id;
	
	private int status;
	
	public Laburador (){
		numerador++;
		id = numerador;
		entregador = Entregador.getInstance();
		status=STATUS_CORRIENDO;
	}
	
	@Override
	public void run() {
		Work laburo= entregador.getLaburo();
		while (laburo!=null) {
			int result = laburo.execute();
			if(result != 0) {
				entregador.halt();
				return;
			}
			if(laburo.getId()!=0) {
				entregador.addTerminado(laburo.getId());
				if(laburo.getNext()!=null && laburo.getNext().size()>0) {
					entregador.recoverJobs(laburo.getNext());
				}
				say(laburo.toString());
			} else {
				say("Sleep inducido "+ laburo.toString());
			}
			laburo= entregador.getLaburo();
		}
		status=STATUS_FINALIZADO;
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
