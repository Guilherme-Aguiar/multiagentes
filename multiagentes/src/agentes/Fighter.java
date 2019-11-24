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

	//AID[] enemies;
	
	ArrayList<AID> enemies = new ArrayList<AID>();
	
	ArrayList<Enemy> hostile = new ArrayList<Enemy>();

	Random rnd = new Random(hashCode());

	MessageTemplate saluteTemplate;

	MessageTemplate fightTemplate;

	private AID Nemesis;
	private boolean isFighting = false;

	protected void setup()

	{
		this.life = 3000;
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Fighter");
		sd.setName(getLocalName());
		register(sd);

		ACLMessage salute = newMsg(ACLMessage.QUERY_REF);

		ACLMessage fight = newMsg(ACLMessage.INFORM);

		saluteTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchConversationId(salute.getConversationId()));

		fightTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchConversationId(fight.getConversationId()));

		SequentialBehaviour seq = new SequentialBehaviour();
		addBehaviour(seq);

		ParallelBehaviour par = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		seq.addSubBehaviour(par);

		enemies = searchDF("Enemy");
		for (int i = 0; i < this.enemies.size(); i++) {
//				    	salute.addReceiver(new AID("sagat" + i, AID.ISLOCALNAME));
			salute.addReceiver(enemies.get(i));

			par.addSubBehaviour(new myReceiver(this, 1000, saluteTemplate, i) {
				private static final long serialVersionUID = 1L;

				public void handle(ACLMessage salute) {
					if (salute != null) {
						if (salute.getContent().split("-", 2)[0] == "yes");
						System.out.println(salute.getContent().split("-", 2)[1]);
						hostile.add(new Enemy(enemies.get(0), salute.getContent().split("-", 2)[1]));
						enemies.remove(0);
					}
				}
			});

		}
		
		seq.addSubBehaviour(new CyclicBehaviour(this) {

			private static final long serialVersionUID = 1L;

			public void action() {
				if (!isFighting) {
					if (!hostile.isEmpty()) {
						fight.addReceiver(hostile.get(0).getAid());
						isFighting = true;
						fight.setContent("500");

					} else {
						enemies = searchDF("Enemy");
						System.out.println("t" + enemies.size());
						System.out.println(hostile.isEmpty());
						salute.addReceiver(enemies.get(0));
						send(salute);
						addBehaviour(new myReceiver(myAgent, 1000, saluteTemplate) {
							private static final long serialVersionUID = 1L;
							public void handle(ACLMessage salute) {
								System.out.println("EEEEEEEEEE" + salute.getContent());
								hostile.add(new Enemy(enemies.get(0), salute.getContent().split("-", 2)[1]));
							}
							
						});
						
					}
				} 
			}

		});

		addBehaviour(new TickerBehaviour(this, 3000) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {
				send(fight);
				addBehaviour(new myReceiver(myAgent, 1000, fightTemplate) {
					private static final long serialVersionUID = 1L;

					public void handle(ACLMessage fight) {
						if (fight != null) {
							if (fight.getContent().equals("Morri")) {
								System.out.println(hostile);
								hostile.remove(0);
								isFighting = false;
								System.out.println(hostile);
							} else {
								takeDamage(Integer.parseInt(fight.getContent()));
							}
							System.out.println("Fighter: " + getLife());
						}
						if (getLife() <= 0) {
							System.out.println("Morreu o fighter");
							doDelete();
						}
					}
				});
			}
		});
		
		send(salute);

	}

	// ========== Utility methods =========================

	// --- generating Conversation IDs -------------------

	protected static int cidCnt = 0;
	String cidBase;

	String genCID() {
		if (cidBase == null) {
			cidBase = getLocalName() + hashCode() + System.currentTimeMillis() % 10000 + "_";
		}
		return cidBase + (cidCnt++);
	}

	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (Exception e) {
		}
	}

	void register(ServiceDescription sd) {
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

	// --- Methods to initialize ACLMessages -------------------

	ACLMessage newMsg(int perf, String content, AID dest) {
		ACLMessage msg = newMsg(perf);
		if (dest != null)
			msg.addReceiver(dest);
		msg.setContent(content);
		return msg;
	}

	ACLMessage newMsg(int perf) {
		ACLMessage msg = new ACLMessage(perf);
		msg.setConversationId(genCID());
		return msg;
	}

}
