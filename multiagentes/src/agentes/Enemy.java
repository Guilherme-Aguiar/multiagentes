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
		
		Random randomGenerator = new Random();
		int randomInt;
		                                             
		protected void setup() 
		{
			this.life = 1000; 
			
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
		            randomInt = randomGenerator.nextInt(200) + 1;
		            fightReply.setContent(""+randomInt);
		            
		            
		            takeDamage(Integer.parseInt(fightMsg.getContent()));
		            System.out.println("VILAO [" + myAgent.getLocalName() + "] - VIDA ATUAL:" +  getLife());
		            
		            addBehaviour( 
				              new DelayBehaviour( myAgent, 100)
				              {
				            	  static final long serialVersionUID = 1L;

								public void handleElapsedTimeout() { 
				                     send(fightReply); }
				              });
		               
		         }
		         
		         if(getLife() <= 0 ) {
					fightReply.setContent("Morri");
					send(fightReply);				
					doDelete();
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
		
		 protected void takeDown() {
			 try { DFService.deregister(this); 
			 } catch (Exception e) {}        
		}
		
		
		//--- generating distinct Random generator -------------------
		
		Random newRandom() 
		{	return  new Random( hashCode() + System.currentTimeMillis()); }

	}
