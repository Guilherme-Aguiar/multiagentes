package userInterface;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import agentes.Enemy;
import agentes.Fighter;
import agentes.Healer;
import agentes.Wizard;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MenuInterface {
	
	private static AgentContainer containerController;
	private static AgentController agentController;

	
	private JFrame window;
	private JPanel panel;
	private JLabel title; 
	
	public void MenuInterface() {
	}
	
	public void createWindow() {
		window = new JFrame("RPG Multiagentes");
		window.setSize(800, 600);
		window.setLocationRelativeTo(null);
		window.setLayout(null);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel(null);
		panel.setBounds(5 ,5, 790, 590);
		window.add(panel);
		
		this.createPanel();
;		
		window.setVisible(true);
	}
	
	private void createPanel() {
		title = new JLabel("Chose your hero:");
		title.setBounds(300, 50, 300, 30);
		Font f = new Font("SansSerif", Font.BOLD, 14);
		title.setFont(f);
		
		JLabel labelHero = new JLabel("Hero");
		labelHero.setBounds(200, 50, 100, 100);
		labelHero.setFont(f);
		
		JLabel labelWizard = new JLabel("Wizard");
		labelWizard.setBounds(520, 50, 100, 100);
		labelWizard.setFont(f);
		
		Icon imgIcon = new ImageIcon(this.getClass().getResource("geralt.jpg"));
		JButton hero = new JButton(imgIcon);
		hero.setBounds(70, 120, 300, 300);
		hero.setActionCommand("playHero");
		hero.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.dispose();
				callAgent("Hero");
			}
		} );

		Icon imgWizard = new ImageIcon(this.getClass().getResource("veigar.jpeg"));
		JButton wizard = new JButton(imgWizard);
		wizard.setBounds(400, 120, 300, 300);
		wizard.setActionCommand("platWizard");
		wizard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.dispose();
				callAgent("Wizard");
			}
		} );
		
		panel.add(title);
		panel.add(labelHero);
		panel.add(labelWizard);
		panel.add(hero);
		panel.add(wizard);
	}
	
	public void callAgent(String character) {
		
		jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.PLATFORM_ID, "172.18.0.1:1099/JADE");
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "true");

        containerController = runtime.createMainContainer(profile);
        
        try {
        	if(character.equals("Hero")) {
    
        		agentController = containerController.createNewAgent("Hero", Fighter.class.getName(), null);
        		
        	}else {
        		
        		agentController = containerController.createNewAgent("Wizard", Wizard.class.getName(), null);
        		
        	}
            agentController.start();
            agentController = containerController.createNewAgent("Healer", Healer.class.getName(), null);
            agentController.start();
            
            for(int i = 0; i < 20; i++) {
                agentController = containerController.createNewAgent("Monster " + i, Enemy.class.getName(), null);
                agentController.start();
            }

        } catch (StaleProxyException s) {
            s.printStackTrace();
        }    
	}
	
}
