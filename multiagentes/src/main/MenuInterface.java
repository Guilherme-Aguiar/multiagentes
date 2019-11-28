package main;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MenuInterface {
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
		
		Icon imgMago = new ImageIcon(this.getClass().getResource("veigar.jpeg"));
		JButton mago = new JButton(imgMago);
		mago.setBounds(400, 120, 300, 300);
		
		panel.add(title);
		panel.add(labelHero);
		panel.add(labelWizard);
		panel.add(hero);
		panel.add(mago);
	}
	
}
