package pt.ulisboa.tecnico.learnjava.sibs.exceptions;

public class MBWayException extends Exception {
	private String type;

	public MBWayException(String type) {
		this.type = type;
	}

	public MBWayException() {

	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
