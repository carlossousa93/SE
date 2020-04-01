package pt.ulisboa.tecnico.learnjava.bank.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.PersonException;

public class personConstructorMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";
	

	@Test
	public void success() throws ClientException, PersonException {
		Person person = new Person(FIRST_NAME, LAST_NAME, ADDRESS, PHONE_NUMBER);

		assertTrue(person instanceof Person);
		assertEquals(FIRST_NAME, person.getFirstName());
		assertEquals(LAST_NAME, person.getLastName());
		assertEquals(PHONE_NUMBER, person.getPhoneNumber());
		assertEquals(ADDRESS, person.getAddress());
		
	}

	@Test(expected = PersonException.class)
	public void no9DigitsPhoneNumber() throws ClientException, PersonException {
		new Person(FIRST_NAME, LAST_NAME, ADDRESS, "A12345678");
	}

}
