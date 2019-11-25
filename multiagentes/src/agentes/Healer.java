package agentes;

import java.util.ArrayList;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Healer extends Characters {
	private static final long serialVersionUID = 1L;
	
	
	Random rnd = new Random( hashCode());
	
	MessageTemplate offerTemplate ;  
	MessageTemplate sendHelpTemplate ; 
	
	ArrayList<AID> heroes = new ArrayList<AID>();
	
	int mana = 1000;
	
	
	                                             
	protected void setup() 
	{ 
		ACLMessage offerHelp = newMsg(ACLMessage.QUERY_REF);
		
		ACLMessage sendHelp = newMsg(ACLMessage.INFORM);
		
		offerTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchConversationId(offerHelp.getConversationId()));
		
		sendHelpTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchConversationId(sendHelp.getConversationId()));
		
		heroes = searchDF("Fighter");
		
		addBehaviour(new TickerBehaviour(this,5000) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {
				
				//System.out.println(myAgent.getLocalName() + " mana = " +mana);
				if(!heroes.isEmpty()) {
						offerHelp.addReceiver(heroes.get(0));
						offerHelp.setContent("400");
						send(offerHelp);
						
				}
				else {
					System.out.println(myAgent.getLocalName() + " NAO ACHOU O FIGHTER, TENTANDO NOVAMENTE");
					heroes = searchDF("Fighter");
				}
				
			}
			
		});
		
		
	    
	}
	
	//========== Utility methods =========================
	ArrayList<AID> searchDF(String service)
	// ---------------------------------
	{
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(service);
		dfd.addServices(sd);

		SearchConstraints ALL = new SearchConstraints();
		ALL.setMaxResults(new Long(-1));

		try {
			DFAgentDescription[] result = DFService.search(this, dfd, ALL);
			ArrayList<AID> agents = new ArrayList<AID>();
			for (int i = 0; i < result.length; i++)
				agents.add(result[i].getName());
			return agents;

		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		return null;
	}
	
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
