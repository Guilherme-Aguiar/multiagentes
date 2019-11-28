package main;

import java.util.Scanner;

import agentes.Enemy;
import agentes.Fighter;
import agentes.Healer;
import agentes.Wizard;
import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main {
	
	private static AgentContainer containerController;
	private static AgentController agentController;

	public static void main(String[] args) {
				
	    Scanner myObj = new Scanner(System.in);  
	    System.out.println("Nome do seu herói");

	    String heroe = myObj.nextLine();  
	    System.out.println("Herói: " + heroe);  
	    
	    
	    System.out.println("Nome do seu Curador");
	    String healer = myObj.nextLine();  
	    System.out.println("Curador: " + healer); 
	    
	    System.out.println("Numero de inimigos");
	    int enemies = myObj.nextInt();  
	    System.out.println("Curador: " + enemies);
		
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.PLATFORM_ID, "172.18.0.1:1099/JADE");
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "true");

        containerController = runtime.createMainContainer(profile);
        
        try {
            agentController = containerController.createNewAgent(heroe, Fighter.class.getName(), null);
            agentController.start();
            agentController = containerController.createNewAgent(healer, Healer.class.getName(), null);
            agentController.start();
            
            for(int i = 0; i < enemies; i++) {
                agentController = containerController.createNewAgent("Monstro " + i, Enemy.class.getName(), null);
                agentController.start();
            }

        } catch (StaleProxyException s) {
            s.printStackTrace();
        }     

	}
}