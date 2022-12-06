package Day4;

public enum Action {
	begins_shift("begins shift"), falls_asleep("falls asleep"), wakes_up("wakes up");
	
	private String str;
	
	Action (String str) {
		this.str = str;
	}
	
	public String getStr() {
		return str;
	}
	
	public String toString() {
		return str;
	}
}
