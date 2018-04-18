package runner;

import model.Laburo;

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
		Laburo laburo= entregador.getLaburo();
		while (laburo!=null) {
			laburo.execute();
			System.out.println("Thread:"+id+"	Item:"+laburo.getId() +"	("+laburo.getTime()+"s)");
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

	
}
