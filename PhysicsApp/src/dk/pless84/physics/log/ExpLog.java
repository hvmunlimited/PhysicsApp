package dk.pless84.physics.log;

public class ExpLog {
	private long id;
	private long expId;
	private String time;
	private float xVal;
	private float yVal;
	private float zVal;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getExpId() {
		return expId;
	}
	public void setExpId(long expId) {
		this.expId = expId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public float getxVal() {
		return xVal;
	}
	public void setxVal(float xVal) {
		this.xVal = xVal;
	}
	public float getyVal() {
		return yVal;
	}
	public void setyVal(float yVal) {
		this.yVal = yVal;
	}
	public float getzVal() {
		return zVal;
	}
	public void setzVal(float zVal) {
		this.zVal = zVal;
	}
}