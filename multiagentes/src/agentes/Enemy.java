package agentes;

import java.io.IOException;
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
		MessageTemplate saluteTemplate = MessageTemplate.MatchPerformative( ACLMessage.QUERY_REF );    
		MessageTemplate fightTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		    
		ACLMessage saluteReply;
		ACLMessage fightReply;
		
		int life = 1000;
		                                             
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
		        ACLMessage saluteMsg = receive( saluteTemplate );
		        ACLMessage fightMsg = receive(fightTemplate);
		        
		        
		        if (saluteMsg!=null) {
		               
		        	
		        	// we create the reply 
		            saluteReply = saluteMsg.createReply();
		            saluteReply.setPerformative( ACLMessage.INFORM );
					saluteReply.setContent("yes-" + life);
		        
		            System.out.println( " - " +
		               myAgent.getLocalName() + " <- Recebeu QUERY do " +
		               saluteMsg.getSender().getLocalName());
		               
		            // but only send it after a random delay
		    
		            addBehaviour( 
		              new DelayBehaviour( myAgent, 100)
		              {
		            	  static final long serialVersionUID = 1L;

						public void handleElapsedTimeout() { 
		                     send(saluteReply); }
		              });
		         }
		        
		         if (fightMsg!=null) {
		           
		        	// we create the reply 
		            fightReply = fightMsg.createReply();
		            fightReply.setPerformative( ACLMessage.INFORM );
		            fightReply.setContent("devolvi" + 500);
		        
		            System.out.println(fightMsg.getContent());
		               
		            send(fightReply); 
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
