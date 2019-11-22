package agentes;

import java.util.Random;

import agentes.myReceiver;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;


public class Fighter extends Agent {

		private static final long serialVersionUID = 1L;
		
		Random rnd = new Random( hashCode());
		
		MessageTemplate template ;    
		                                             
		protected void setup() 
		{ 
		    ACLMessage msg = newMsg( ACLMessage.QUERY_REF, "Você é hostil?",
		                         new AID( "s1", AID.ISLOCALNAME) ); 
		    
		    template = MessageTemplate.and( 
		        MessageTemplate.MatchPerformative( ACLMessage.INFORM ),
		        MessageTemplate.MatchConversationId( msg.getConversationId() ));
		         
		   addBehaviour( new myReceiver(this, 1000, template )
		      {
				private static final long serialVersionUID = 1L;
		
				public void handle( ACLMessage msg ) 
		         {  
		            if (msg == null) 
		               System.out.println("Fighter: Timeout");
		            else 
		               System.out.println("Fighter received answer: "+ msg);
		
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
