package agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.introspection.ACLMessage;

public class Fighter extends Agent {
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage msg = new ACLMessage();
				if(msg != null) {
				}
				
			}
			
		});
	}

}
