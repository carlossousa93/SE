package pt.ulisboa.tecnico.learnjava.bank.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.PersonException;

public class Person {
	
	private final String firstName;
	private final String lastName;
	private final String address;
	private final String phoneNumber;
	
	public Person(String firstName, String lastName, String address,String phoneNumber) throws PersonException {
		
		checkParameters(phoneNumber);
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
		
	}
	
	public void checkParameters(String phoneNumber) throws PersonException {
		if (phoneNumber.length() != 9 || !phoneNumber.matches("[0-9]+")) {
			throw new PersonException();
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getAddress() {
		return address;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
}
