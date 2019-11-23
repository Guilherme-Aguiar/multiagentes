package agentes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import agentes.myReceiver;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;
import simple.Enemy;

public class Fighter extends Characters {

		private static final long serialVersionUID = 1L;
		
		AID [] enemies;

		ArrayList<Enemy> hostile = new ArrayList<Enemy>();
		
		Random rnd = new Random( hashCode());
		
		MessageTemplate template ;    
		                                             
		protected void setup() 
		
		{ 
			this.life = 1000;
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
		    
		    enemies =  searchDF("Enemy");

		    for (int i = 0; i < enemies.length; i++) {
		    	
//		    	msg.addReceiver(new AID("sagat" + i, AID.ISLOCALNAME));
		    	msg.addReceiver(enemies[i]);
		    	
		    	
		    	par.addSubBehaviour( new myReceiver(this, 1000, template, i )
			      {
					private static final long serialVersionUID = 1L;

					public void handle( ACLMessage msg ) 
			         {  
			            if (msg != null) {
							if(msg.getContent().split("-",2)[0] == "yes");
								System.out.println(msg.getContent().split("-",2)[1]);
		            			hostile.add(new Enemy(enemies[this.i],msg.getContent().split("-",2)[1]));
		            		
			            }   
			         }
			    });
		    	
			}
		    
		    seq.addSubBehaviour(new OneShotBehaviour() {

				private static final long serialVersionUID = 1L;

				public void action() {
					System.out.println("aq");
					System.out.println(hostile);
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
