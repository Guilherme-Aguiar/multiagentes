package agentes;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;


public class Fighter extends Agent {
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		
		//Registering fighter in DF
		ServiceDescription sd  = new ServiceDescription();
	    sd.setType( "fighter" );
	    sd.setName( getLocalName() );
	    register( sd );
		
		
		addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage msg = receive();
				if(msg != null) {
					System.out.println(msg.getContent());
				}
				block();
			}
			
		});
		
	}
	
	void register( ServiceDescription sd) {
	    DFAgentDescription dfd = new DFAgentDescription();
	    dfd.setName(getAID());
	    dfd.addServices(sd);
	    try {  
	        DFService.register(this, dfd );  
	    }
	    catch (FIPAException fe) { fe.printStackTrace(); }
	}

}
