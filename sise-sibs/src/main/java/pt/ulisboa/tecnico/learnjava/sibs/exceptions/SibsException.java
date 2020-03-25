package pt.ulisboa.tecnico.learnjava.sibs.exceptions;

public class SibsException extends Exception {
	private final String type;
	
	public SibsException() {
		this(null);
	}
	
	public SibsException(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
}
