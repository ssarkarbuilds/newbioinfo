
public class jobPOJO {

	int jid;
	String DATE;
	public int getJid() {
		return jid;
	}
	public void setJid(int jid) {
		this.jid = jid;
	}
	public String getDATE() {
		return DATE;
	}
	public void setDATE(String dATE) {
		DATE = dATE;
	}
	public jobPOJO(int jid, String dATE) {
		super();
		this.jid = jid;
		DATE = dATE;
	}
	
	
}
