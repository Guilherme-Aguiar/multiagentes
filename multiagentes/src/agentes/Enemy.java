package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;


public class Enemy extends Agent {
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		AID agent = getService("fighter");
		System.out.println("\nFighter: "+(agent==null ? "not Found" : agent.getName()));
		
		
		addBehaviour( new SimpleBehaviour(this ) {
			private static final long serialVersionUID = 1L;
			private boolean result = false;

			@Override
			public void action() {
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setContent( "Teje atacado!" );
			    msg.addReceiver(agent);
				send(msg);
				result = true;
			}

			@Override
			public boolean done() {
				return result;
			}
		});
		
	}
	
	AID getService( String service ) {
		DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType( service );
		dfd.addServices(sd);
		try
		{
			DFAgentDescription[] result = DFService.search(this, dfd);
			if (result.length>0)
				return result[0].getName() ;
		}
	    catch (FIPAException fe) { fe.printStackTrace(); }
	  	return null;
	}
	
	

}
