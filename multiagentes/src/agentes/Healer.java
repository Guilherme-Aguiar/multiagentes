package agentes;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Healer extends Characters {
	private static final long serialVersionUID = 1L;
	
	private int danoTotal;
	
	Random rnd = new Random( hashCode());
	
	MessageTemplate template ;    
	                                             
	protected void setup() 
	{ 
		this.life = 1000;
		this.danoTotal = 0;
		this.mana = 100;
		
		
	    ACLMessage msg = newMsg( ACLMessage.QUERY_REF); 
	    
	    template = MessageTemplate.and( 
	        MessageTemplate.MatchPerformative( ACLMessage.INFORM ),
	        MessageTemplate.MatchConversationId( msg.getConversationId() ));
	    
	    SequentialBehaviour seq = new SequentialBehaviour();
	    addBehaviour(seq);
	    
	    ParallelBehaviour par = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
	    seq.addSubBehaviour(par);
	    
	    for (int i = 1; i <= 3; i++) {
	    	
	    	msg.addReceiver( new AID( "sagat" + i,  AID.ISLOCALNAME ));
	    	
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
