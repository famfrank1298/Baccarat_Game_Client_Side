import java.io.Serializable;

public class Card implements Serializable {
	private String suite;
	private int value;
	private String type;
	
	Card(String theSuite, int theValue) {
		suite = theSuite;
		value = theValue;
	}
	
	void addType(String t) {
		type = t;
	}
	
	public int getValue() {
		return value;
	}
	
	public void test() {
		value = 2;
	}
	
	public String getSuite() {
		// TODO Auto-generated method stub
		return suite;
	}
}

