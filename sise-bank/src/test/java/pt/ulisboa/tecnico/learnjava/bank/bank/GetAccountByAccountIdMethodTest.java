package pt.ulisboa.tecnico.learnjava.bank.bank;

import static org.junit.Assert.assertEquals;

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

public class GetAccountByAccountIdMethodTest {
	private Bank bank;
	private Person person;
	private Client client;

	@Before
	public void setUp() throws BankException, AccountException, ClientException, PersonException {
		this.bank = new Bank("CGD");
		this.person = new Person("Jo�o", "Manuel", "Street", "987654321");
		this.client = new Client(this.bank, this.person, "123456789", 33);
	}

	@Test
	public void success() throws BankException, AccountException, ClientException {
		this.bank.createAccount(Bank.AccountType.CHECKING, this.client, 100, 0);
		Account account = this.bank.getAccounts().findFirst().get();

		assertEquals(account, this.bank.getAccountByAccountId(account.getAccountId()));
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
