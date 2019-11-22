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


public class Enemy extends Agent {

		private static final long serialVersionUID = 1L;
		Random rnd = newRandom();
		MessageTemplate template = MessageTemplate.MatchPerformative( ACLMessage.QUERY_REF );    
		    
		ACLMessage reply;
		                                             
		protected void setup() 
		{
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
		            reply.setContent("Mermão eu sou mal!" + rnd.nextInt(100));
		        
		            int delay = rnd.nextInt( 2000 );
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
		
		
		//--- generating distinct Random generator -------------------
		
		Random newRandom() 
		{	return  new Random( hashCode() + System.currentTimeMillis()); }

	}
