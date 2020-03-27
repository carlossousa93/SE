package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.Map;
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
		System.out.println("Welcome to MbWay!");

		boolean state = true;

		while (state) {
			System.out.println(
					"Which operation to you want to perform? associate-mbway; confirm-mbway; mbway-transfer <TARGET_PHONE_NUMBER> <AMOUNT>; "
							+ "mbway-split-bill <NUMBER_OF_FRIENDS> <AMOUNT>; exit");
			String operation = test.nextLine();
			String[] command = operation.split(" ");
			switch (command[0]) {
			case "associate-mbway":
				commandA(ctrl);
				break;
			case "confirm-mbway":
				commandB(ctrl, test);
				break;
			case "mbway-transfer":
				commandC(ctrl, command);
				break;
			case "mbway-split-bill":
				commandD(ctrl, command);
				break;

			case "exit":
				System.out.println("Thanks for using MBWay!");
				state = false;
				break;
			default:
				System.out.println("Invalid operation. Try again!");
			}
		}
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

	private static MBWayAccount createMBWayAccount(String iban) {
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

	public static void commandA(MBWayController ctrl) {
		int code = ctrl.associateMbway();
		ctrl.associationMessage(code);
	}

	public static void commandB(MBWayController ctrl, Scanner test) {
		System.out.println("Insert the code:");
		int codeInserted = Integer.parseInt(test.nextLine());
		Boolean value = ctrl.confirmMbway(codeInserted);
		ctrl.confirmationMessage(value);
	}

	public static void commandC(MBWayController ctrl, String[] command) // (Controller, String targetNumber, String Amount)
			throws SibsException, AccountException, OperationException {
		String targetNumber = command[1];
		int amount = Integer.parseInt(command[2]);
		try {
			ctrl.mbwayTransfer(targetNumber, amount);
			ctrl.transferMessage("Transfer performed successfully!");
		} catch (MBWayException e) {
			ctrl.transferMessage(e.getType());
		}
	}

	public static void commandD(MBWayController ctrl, String[] command)
			throws MBWayException, SibsException, AccountException, OperationException {
		int numberFriends = Integer.parseInt(command[1]);
		int totalAmount = Integer.parseInt(command[2]);
		Map<String, Integer> splitbill = new HashMap<String, Integer>();
		boolean active = true;
		Scanner split = new Scanner(System.in);
		while (active) {
			System.out.println("Which operation to you want to perform? friend <PHONE_NUMBER> <AMOUNT>; submit");
			String operation = split.nextLine();
			String[] command2 = operation.split(" ");
			switch (command[0]) {
			case "friend":
				active = commandF(ctrl, command2, splitbill);
				break;
			case "G":
				active = commandG(ctrl, numberFriends, totalAmount, splitbill);
				break;
			default:
				System.out.println("Invalid operation. Try again!");
			}
		}
	}

	public static boolean commandF(MBWayController ctrl, String[] command, Map<String, Integer> splitbill) {
		String sourceNumber = command[1];
		int amountPerPerson = Integer.parseInt(command[2]);
		boolean active = true;
		try {
			if (ctrl.friend(sourceNumber, amountPerPerson)) {
				splitbill.put(sourceNumber, amountPerPerson);
				System.out.println(active);
			}
		} catch (MBWayException e) {
			ctrl.splitBillMessage(e.getType());
			active = false;
		}
		return active;
	}

	public static boolean commandG(MBWayController ctrl, int numberFriends, int totalAmount,
			Map<String, Integer> splitbill) throws MBWayException, SibsException, AccountException, OperationException {
		boolean active = true;
		if (splitbill.size() < numberFriends) {
			ctrl.splitBillMessage("Oh no! One or more friends are missing.");
			active = false;
			return active;
		} else if (splitbill.size() > numberFriends) {
			ctrl.splitBillMessage("Oh no! Too many friends.");
			active = false;
			return active;
		}
		int sumAmounts = 0;
		for (int amount : splitbill.values()) {
			sumAmounts += amount;
		}
		if (sumAmounts == totalAmount) {
			for (String number : splitbill.keySet()) {
				if (number != ctrl.getModel().getPhoneNumber()) {
					ctrl.mbwayTransfer(number, splitbill.get(number));
				}
			}
			ctrl.splitBillMessage("Bill payed successfully!");
		} else {
			ctrl.splitBillMessage("Something is wrong. Did you set the bill amount right?");
		}
		active = false;
		return active;
	}

}
