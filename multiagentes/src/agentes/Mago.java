package agentes;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Mago extends Agent{
	
	private int vida;
	private int poderHabilidade;
	private int poderAtaque;
	private int tempoRecarga;
	
	private static final long serialVersionUID = 1L;
	 
	protected void setup() {
		 this.vida = 200;
		 this.poderHabilidade = 55;
		 this.poderAtaque = 15; 
		 this.tempoRecarga = 10; 
		 
		 DFAgentDescription dfd = new DFAgentDescription();
		 ServiceDescription sd = new ServiceDescription();
		 
		 System.out.println("O " + getLocalName() + " entrou na Batalha");
		 System.out.println("Status:");
		 System.out.println("Vida: " + this.vida);
		 System.out.println("Poder de Ataque: " + this.poderAtaque);
		 System.out.println("Poder de Habilidade: " + this.poderHabilidade);
	 }

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public int getPoderHabilidade() {
		return poderHabilidade;
	}

	public void setPoderHabilidade(int poderHabilidade) {
		this.poderHabilidade = poderHabilidade;
	}

	public int getPoderAtaque() {
		return poderAtaque;
	}

	public void setPoderAtaque(int poderAtaque) {
		this.poderAtaque = poderAtaque;
	}

	public int getTempoRecarga() {
		return tempoRecarga;
	}

	public void setTempoRecarga(int tempoRecarga) {
		this.tempoRecarga = tempoRecarga;
	}
}
