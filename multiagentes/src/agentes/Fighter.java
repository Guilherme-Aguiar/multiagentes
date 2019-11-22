package agentes;

import java.util.Random;

import agentes.myReceiver;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;


public class Fighter extends Characters {

		private static final long serialVersionUID = 1L;
		
		private int danoTotal;
		
		Random rnd = new Random( hashCode());
		
		MessageTemplate template ;    
		                                             
		protected void setup() 
		{ 
			this.life = 1000;
			this.danoTotal = 0;
			AID [] inimigos =  searchDF("Enemy");
			
			System.out.println(inimigos);
			
			ServiceDescription sd = new ServiceDescription();
			sd.setType("Fighter");
			sd.setName(getLocalName());
			register(sd);
			
		    ACLMessage msg = newMsg( ACLMessage.QUERY_REF); 
		    
		    template = MessageTemplate.and( 
		        MessageTemplate.MatchPerformative( ACLMessage.INFORM ),
		        MessageTemplate.MatchConversationId( msg.getConversationId() ));
		    
		    SequentialBehaviour seq = new SequentialBehaviour();
		    addBehaviour(seq);
		    
		    ParallelBehaviour par = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		    seq.addSubBehaviour(par);
		    
		    for (int i = 0; i < 3; i++) {
		    	
		    	msg.addReceiver(inimigos[i]);
		    	
		    	par.addSubBehaviour( new myReceiver(this, 1000, template )
			      {
					private static final long serialVersionUID = 1L;
			
					public void handle( ACLMessage msg ) 
			         {  
			            if (msg != null) {
			            	int damage = Integer.parseInt(msg.getContent());
			            	System.out.println("Recebeu dano de " + damage);
			            	life -= damage;
			            	danoTotal += damage;
			            	System.out.println("Vida atual é " + life);
			            }   
			         }
			    });
			}
		    
		    seq.addSubBehaviour(new OneShotBehaviour() {

				private static final long serialVersionUID = 1L;

				public void action() {
					if( life != 1000)
						System.out.println("dano total levado: " + danoTotal);
					else
						System.out.println("no damage");
				}
		    	
		    });
		         
		    send ( msg );
		}
		
		//========== Utility methods =========================
		
		
		//--- generating Conversation IDs -------------------
		
		protected static int cidCnt = 0;
		String cidBase ;
		
		String genCID() 
		{ 
		  if (cidBase==null) {
		     cidBase = getLocalName() + hashCode() +
		                  System.currentTimeMillis()%10000 + "_";
		  }
		  return  cidBase + (cidCnt++); 
		}
		
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
		
		
		AID [] searchDF( String service )
		//---------------------------------
		{
		    DFAgentDescription dfd = new DFAgentDescription();
		        ServiceDescription sd = new ServiceDescription();
		        sd.setType( service );
		    dfd.addServices(sd);
		    
		    SearchConstraints ALL = new SearchConstraints();
		    ALL.setMaxResults(new Long(-1));

		    try
		    {
		        DFAgentDescription[] result = DFService.search(this, dfd, ALL);
		        AID[] agents = new AID[result.length];
		        for (int i=0; i<result.length; i++) 
		            agents[i] = result[i].getName() ;
		        return agents;

		    }
		    catch (FIPAException fe) { fe.printStackTrace(); }
		    
		      return null;
		}
		
		//--- Methods to initialize ACLMessages -------------------
		
		ACLMessage newMsg( int perf, String content, AID dest)
		{
		  ACLMessage msg = newMsg(perf);
		  if (dest != null) msg.addReceiver( dest );
		  msg.setContent( content );
		  return msg;
		}
		
		ACLMessage newMsg( int perf)
		{
		  ACLMessage msg = new ACLMessage(perf);
		  msg.setConversationId( genCID() );
		  return msg;
		}


	}
