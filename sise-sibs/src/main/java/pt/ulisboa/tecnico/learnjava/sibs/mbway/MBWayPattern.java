package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.PersonException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MBWayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWayPattern {

	public static void main(String[] args) throws MBWayException, SibsException, AccountException, OperationException,
			BankException, PersonException, ClientException {

		MBWay model = retriveMBWayFromDatabase();
		MBWayView view = new MBWayView();
		MBWayController ctrl = new MBWayController(model, view);

		Scanner test = new Scanner(System.in);
		
		ctrl.mainAuxiliar(test);
		
		test.close();
	}
	
	private static MBWay retriveMBWayFromDatabase()
			throws MBWayException, BankException, PersonException, ClientException, AccountException {
		Bank bank = new Bank("cgd");
		String iban1 = bank.createAccount(AccountType.CHECKING, new Client(bank, new Person("Joao", "Reis", "Rua Nicolau Nasoni", "962770474"), "111111111", 24), 1000, 1000);
		String iban2 = bank.createAccount(AccountType.CHECKING, new Client(bank, new Person("Carlos", "Sousa", "Rua CR7", "912345678"), "222222222", 27), 1000, 1000);
		String iban3 = bank.createAccount(AccountType.CHECKING, new Client(bank, new Person("José", "Rodrigues", "Rua de Beja", "931234567"), "333333333", 25), 1000, 1000);
		MBWayAccount account1 = createMBWayAccount(iban1);
		MBWayAccount account2 = createMBWayAccount(iban2);
		MBWayAccount account3 = createMBWayAccount(iban3);
		MBWay mbway = new MBWay("962770474", account1);
		mbway.addFriend("912345678", account2);
		mbway.addFriend("931234567", account3);
		return mbway;
	}
	
	private static  MBWayAccount createMBWayAccount(String iban) {
		MBWayAccount account = new MBWayAccount();
		Account ckAccount = getAccountByIbanNumber(iban);
		account.setAccount(ckAccount);
		account.setIban(iban);
		return account;
	}
	
	public static Account getAccountByIbanNumber(String iban) {
		String code = iban.substring(0, 3);
		String accountId = iban.substring(3);

		Bank bank = Bank.getBankByCode(code);
		Account account = bank.getAccountByAccountId(accountId);
		return account;
	}
	
}
