package pt.ulisboa.tecnico.learnjava.bank.exceptions;

public class PersonException extends Exception {
	
	private final String phoneNumber;
	
	public PersonException() {
		this(null);
	}
		
	public PersonException(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

}
