package runner;

import java.util.List;

import model.Laburo;

public class Entregador {
	
	private static Entregador instancia;
	
	public static Entregador getInstance() {
		if (instancia == null) {
			instancia = new Entregador();
		}
		return instancia;
	}
	
	private List <Laburo> laburos;
	private int indice;
	
	private void setIndice(int indice) {
		this.indice = indice;
	}
	
	public void setLaburos(List <Laburo> laburos) {
		setIndice(0);
		this.laburos=laburos;
	}

	public Laburo getLaburo() {
		if (indice < laburos.size()) {
			indice++;
			return laburos.get(indice-1);
		}
		return null;
	}

	
}
