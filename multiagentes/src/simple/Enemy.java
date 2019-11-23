package simple;

import jade.core.AID;

public class Enemy {
	private int life;
	private AID aid;
	public Enemy(AID aid, String string) {
		this.life = Integer.parseInt(string);
		this.aid = aid;
	}
	public AID getAid() {
		return aid;
	}
	public void setAid(AID aid) {
		this.aid = aid;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
}
