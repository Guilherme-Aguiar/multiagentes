package userInterface;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import agentes.Characters;
import agentes.DelayBehaviour;
import agentes.Fighter;
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

public class FighterGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JFrame window;
	private JPanel panel;
	private JLabel title;
	private Fighter myAgent;
	private static JProgressBar bar;
	private static JProgressBar barEnemy;
	private static JLabel phase;
	private int phaseNum;
	private int life;
	private int lifeEnemy;


	public FighterGUI(Fighter a) {
		super(a.getLocalName());
		myAgent = a;

		window = new JFrame("RPG Multiagentes");
		window.setSize(800, 600);
		window.setLocationRelativeTo(null);
		window.setLayout(null);

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setBounds(5, 5, 790, 590);
		
		phaseNum = 1;
		
		phase = new JLabel("Fase " + phaseNum);
		
		bar = new JProgressBar(0,a.getLife());
		barEnemy = new JProgressBar(0,1000);
		
		life = a.getLife();
		bar.setValue(life);
		
		lifeEnemy = 1000;
		barEnemy.setValue(lifeEnemy);
		//bar.setStringPainted(true);
		
		panel.add(phase);
		panel.add(bar);
		panel.add(barEnemy);
		
		window.add(panel);
	}

	public void showGui() {
		window.setVisible(true);
	}
	
	public void updateBar(int life) {
		this.life = life;
		if(this.life <= 0) {
			JLabel gameOver = new JLabel("GAME OVER");
			panel.removeAll();
			panel.add(gameOver);
		}
		bar.setValue(this.life);
		window.repaint();
	}
	
	public void updateEnemyBar(int damage) {
		this.lifeEnemy = this.lifeEnemy - damage;
		
		if(this.lifeEnemy <= 0) {
			phaseNum++;
			phase.setText("Fase " + phaseNum);
			this.lifeEnemy = 1000;
			barEnemy.setValue(this.lifeEnemy);
		}else {
			barEnemy.setValue(this.lifeEnemy);			
		}
		window.repaint();
	}
	

}