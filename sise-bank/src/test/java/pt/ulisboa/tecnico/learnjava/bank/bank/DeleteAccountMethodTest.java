package pt.ulisboa.tecnico.learnjava.bank.bank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.PersonException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class DeleteAccountMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";

	private Bank bank;
	private Person person;
	private Client client;
	private Account account;
	private Services services;

	@Before
	public void setUp() throws BankException, ClientException, AccountException, PersonException {
		this.services = new Services();
		this.bank = new Bank("CGD");
		
		this.person = new Person(FIRST_NAME, LAST_NAME, ADDRESS, PHONE_NUMBER);
		this.client = new Client(this.bank, this.person, NIF, 33);
		String iban = this.bank.createAccount(Bank.AccountType.CHECKING, this.client, 100, 0);
		this.account = this.services.getAccountByIban(iban);
	}

	@Test
	public void success() throws BankException, AccountException {
		this.bank.deleteAccount(this.account);

		assertEquals(0, this.bank.getTotalNumberOfAccounts());
		assertFalse(this.client.hasAccount(this.account));
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
