package dk.pless84.physics.log;

public class Experiment {
	public static final int TYPE_ACC = 1;
	public static final int TYPE_MAGNET = 2;
	private long id;
	private long type;
	private String date;
	private long rate;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getRate() {
		return rate;
	}

	public void setRate(long rate) {
		this.rate = rate;
	}
}