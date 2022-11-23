import java.io.Serializable;
import java.util.ArrayList;

public class BaccaratInfo implements Serializable {
	private int bid_amount;
	private String betOn;
	private Boolean natural;
	private String winner;
	
	public ArrayList<Card> players;
	public ArrayList<Card> bankers;
	private int playersValue;
	private int bankersValue;
	
	// placeholders 
	
	public boolean pExtraCard;
	public boolean bExtraCard;
	
	public double totalWon;
	
	public boolean playAgain;
	
	BaccaratInfo(int b, String who) {
		this.bid_amount = b;
		this.betOn = who;
		this.totalWon = 0.0;
		this.natural = false;
		this.winner = "";
		players = new ArrayList<Card>();
		bankers = new ArrayList<Card>();
		pExtraCard = false;
		pExtraCard = false;
		playAgain = false;
	}
	
	public void updatePlayer(ArrayList<Card> cards) {
		for(int i = 0; i < cards.size(); i++) {
			players.add(cards.get(i));
		}
	}
	
	public void updateBanker(ArrayList<Card> cards) {
		for(int i = 0; i < cards.size(); i++) {
			bankers.add(cards.get(i));
		}
	}
	
	public int playerSize() {
		return players.size();
	}
	
	public int bankerSize() {
		return bankers.size();
	}
	
	public void setWinning(double won) {
		this.totalWon = won;
	}
	
	public double getWinning() {
		return totalWon;
	}
	
	public void setNatural(Boolean w) {
		this.natural = w;
	}
	
	public Boolean getNatural() {
		return natural;
	}
	
	public int getBidAmount() {
		return this.bid_amount;
	}
	
	public String getBetOn() {
		return this.betOn;
	}
	
	public void setPValue(int v) {
		this.playersValue = v;
	}
	
	public void setBValue(int v) {
		this.bankersValue = v;
	}
	
	public int getPValue() {
		return this.playersValue;
	}
	
	public int getBValue() {
		return this.bankersValue;
	}
	
	public void setWinner(String s) {
		this.winner = s;
	}
	
	public String getWinner() {
		return this.winner;
	}
	
	public void again() {
		this.playAgain = true;
	}
	
	
	
	
	
}
