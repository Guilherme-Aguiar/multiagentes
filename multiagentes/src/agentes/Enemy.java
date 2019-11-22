package agentes;

import java.util.Random;

import agentes.DelayBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;


public class Enemy extends Characters {
	


		private static final long serialVersionUID = 1L;
		Random rnd = newRandom();
		MessageTemplate template = MessageTemplate.MatchPerformative( ACLMessage.QUERY_REF );    
		    
		ACLMessage reply;
		                                             
		protected void setup() 
		{
			
			ServiceDescription sd = new ServiceDescription();
			sd.setType("Enemy");
			sd.setName(getLocalName());
			register(sd);
			
		  addBehaviour(new CyclicBehaviour(this) 
		  {
			private static final long serialVersionUID = 1L;

			public void action() 
		     {
		        ACLMessage msg = receive( template );
		        if (msg!=null) {
		               
		        	// we create the reply 
		            reply = msg.createReply();
		            reply.setPerformative( ACLMessage.INFORM );
		            reply.setContent("" + rnd.nextInt(100));
		        
		            int delay = rnd.nextInt( 1000 );
		            System.out.println( " - " +
		               myAgent.getLocalName() + " <- QUERY from " +
		               msg.getSender().getLocalName() +
		               ".  Will answer in " + delay );
		               
		            // but only send it after a random delay
		    
		            addBehaviour( 
		              new DelayBehaviour( myAgent, delay)
		              {
		            	  static final long serialVersionUID = 1L;

						public void handleElapsedTimeout() { 
		                     send(reply); }
		              });
		         }
		         block();
		     }
		  });
		}
		
		//==========================================    
		//========== Utility methods ===============
		//==========================================    
		
		void register( ServiceDescription sd) {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			dfd.addServices(sd);
			try {
				DFService.register(this, dfd);
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//--- generating distinct Random generator -------------------
		
		Random newRandom() 
		{	return  new Random( hashCode() + System.currentTimeMillis()); }

	}
