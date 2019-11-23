package agentes;

import jade.core.Agent;

public abstract class Characters extends Agent {
	protected int life = 0;
	protected int stamina = 0;
	protected int mana = 0;
	
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public int getStamina() {
		return stamina;
	}
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
}
