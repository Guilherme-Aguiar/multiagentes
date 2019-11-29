package userInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import agentes.Characters;
import agentes.DelayBehaviour;
import agentes.Fighter;
import agentes.Wizard;
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

public class WizardGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JFrame window;
	private JPanel panel;
	private JLabel title;
	private Wizard myAgent;
	private JProgressBar bar;
	private JProgressBar barEnemy;
	private JLabel phase;
	private JLabel vs;
	private JLabel hero;
	private JLabel enemy;
	private int phaseNum;
	private int life;
	private int lifeEnemy;

public WizardGUI(Wizard a) {
		super(a.getLocalName());
		myAgent = a;

		window = new JFrame("RPG Multiagentes");
		window.setSize(800, 600);
		window.setLocationRelativeTo(null);
		window.setLayout(null);

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setBounds(5, 5, 790, 590);
		panel.setLayout(null);

		phaseNum = 1;
		
		title = new JLabel("RPG Multiagentes");
		title.setBounds(310,10, 200, 100);

		phase = new JLabel("Fase " + phaseNum);
		phase.setBounds(350,60, 100, 100);
		
		vs = new JLabel("VS");
		vs.setBounds(360, 200, 100, 100);

		bar = new JProgressBar(0, a.getLife());
		bar.setBounds(80, 100, 200, 20);
		bar.setForeground(Color.blue);
		
		barEnemy = new JProgressBar(0, 1000);
		barEnemy.setBounds(480, 100, 200, 20);
		barEnemy.setForeground(Color.RED);
		
		Icon imgIcon = new ImageIcon(this.getClass().getResource("wizard.gif"));
		hero = new JLabel(imgIcon);
		hero.setBounds(30, 160, 300, 300);
		
		Icon imgEnemy = new ImageIcon(this.getClass().getResource("enemy1.gif"));
		enemy = new JLabel(imgEnemy);
		enemy.setBounds(430, 130, 300, 300);

		life = a.getLife();
		int aux = a.getLife();
		bar.setValue(life);

		lifeEnemy = 1000;
		barEnemy.setValue(lifeEnemy);
		// bar.setStringPainted(true);

		panel.add(title);
		panel.add(phase);
		panel.add(vs);
		panel.add(bar);
		panel.add(barEnemy);
		panel.add(enemy);
		panel.add(hero);

		window.add(panel);
	}

	public void showGui() {
		window.setVisible(true);
	}

	public void updateBar(int life) {
		if (life > this.life) {
			bar.setForeground(Color.GREEN);
		} else {
			bar.setForeground(Color.blue);
		}
		this.life = life;

		if (this.life <= 0) {
			title.setText("GAME OVER!");
			title.setBounds(330, 10, 200, 100);
			panel.remove(hero);
		}
		bar.setValue(this.life);
		window.repaint();
	}

	public void updateEnemyBar(int damage) {
		this.lifeEnemy = this.lifeEnemy - damage;

		if (this.lifeEnemy <= 0) {
			phaseNum++;
			phase.setText("Fase " + phaseNum);
			this.lifeEnemy = 1000;
			barEnemy.setValue(this.lifeEnemy);
		} else {
			barEnemy.setValue(this.lifeEnemy);
		}
		window.repaint();
	}

}