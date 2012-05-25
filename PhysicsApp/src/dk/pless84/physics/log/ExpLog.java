package dk.pless84.physics.log;

public class ExpLog {
	private long id;
	private long expId;
	private String time;
	private long xVal;
	private long yVal;
	private long zVal;
	
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
	public long getxVal() {
		return xVal;
	}
	public void setxVal(long xVal) {
		this.xVal = xVal;
	}
	public long getyVal() {
		return yVal;
	}
	public void setyVal(long yVal) {
		this.yVal = yVal;
	}
	public long getzVal() {
		return zVal;
	}
	public void setzVal(long zVal) {
		this.zVal = zVal;
	}
}