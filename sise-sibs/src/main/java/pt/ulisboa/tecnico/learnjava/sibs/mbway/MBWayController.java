package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MBWayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWayController {

	private MBWay model;
	private MBWayView view;

	public MBWayController(MBWay model, MBWayView view) {
		this.model = model;
		this.view = view;
	}

	public MBWay getModel() {
		return this.model;
	}

	public void setModel(MBWay model) {
		this.model = model;
	}

	public int associateMbway() {
		int code = this.model.code();
		return code;
	}

	public void associationMessage(int code) {
		this.view.printCode(code);
	}

	public boolean confirmMbway(int code) {
		if (this.model.getCode() == code) {
			this.model.setAccountAssociationState(this.model.getAccount(), true);
		}
		return this.model.getCode() == code;
	}

	public void confirmationMessage(Boolean value) {
		this.view.printConfirmation(value);
	}

	public void mbwayTransfer(String targetNumber, int amount)
			throws MBWayException, SibsException, AccountException, OperationException {
		this.model.transfer(amount, targetNumber);
	}

	public void transferMessage(String output) {
		this.view.printTransferOutput(output);
	}

	public boolean friend(String sourceNumber, int amount) throws MBWayException {
		return this.model.split(sourceNumber, amount);
	}

	public void splitBillMessage(String output) {
		this.view.printSplitBillOutput(output);
	}
	
	public void mainAuxiliar(Scanner test) throws SibsException, AccountException, OperationException, MBWayException {
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
				commandA();
				break;
			case "confirm-mbway":
				commandB(test);
				break;
			case "mbway-transfer":
				commandC(command);
				break;
			case "mbway-split-bill":
				commandD(command, test);
				break;
			case "exit":
				System.out.println("Thanks for using MBWay!");
				state = false;
				break;
			default:
				System.out.println("Invalid operation. Try again!");
			}
		}
	}
	
	public void commandA() {
		int code = associateMbway();
		associationMessage(code);
	}

	public void commandB(Scanner test) {
		System.out.println("Insert the code:");
		int codeInserted = Integer.parseInt(test.nextLine());
		Boolean value = confirmMbway(codeInserted);
		confirmationMessage(value);
	}

	public void commandC(String[] command) // (Controller, String targetNumber, String Amount)
			throws SibsException, AccountException, OperationException {
		String targetNumber = command[1];
		int amount = Integer.parseInt(command[2]);
		try {
			mbwayTransfer(targetNumber, amount);
			transferMessage("Transfer performed successfully!");
		} catch (MBWayException e) {
			transferMessage(e.getType());
		}
	}

	public void commandD(String[] command, Scanner test)
			throws MBWayException, SibsException, AccountException, OperationException {
		int numberFriends = Integer.parseInt(command[1]);
		int totalAmount = Integer.parseInt(command[2]);
		Map<String, Integer> splitbill = new HashMap<String, Integer>();
		boolean active = true;
		//Scanner split = new Scanner(System.in);
		while (active) {
			System.out.println("Which operation to you want to perform? friend <PHONE_NUMBER> <AMOUNT>; submit");
			String operation = test.nextLine();
			String[] command2 = operation.split(" ");
			switch (command2[0]) {
			case "friend":
				active = commandF(command2, splitbill);
				break;
			case "submit":
				active = commandG(numberFriends, totalAmount, splitbill);
				break;
			default:
				System.out.println("Invalid operation. Try again!");
			}
		}
		//split.close();
	}

	public boolean commandF(String[] command, Map<String, Integer> splitbill) {
		String sourceNumber = command[1];
		int amountPerPerson = Integer.parseInt(command[2]);
		boolean active = true;
		try {
			if (friend(sourceNumber, amountPerPerson)) {
				splitbill.put(sourceNumber, amountPerPerson);
			}
		} catch (MBWayException e) {
			splitBillMessage(e.getType());
			active = false;
		}
		return active;
	}

	public boolean commandG(int numberFriends, int totalAmount,
			Map<String, Integer> splitbill) throws MBWayException, SibsException, AccountException, OperationException {
		boolean active = true;
		if (splitbill.size() < numberFriends) {
			splitBillMessage("Oh no! One or more friends are missing.");
			active = false;
			return active;
		} else if (splitbill.size() > numberFriends) {
			splitBillMessage("Oh no! Too many friends.");
			active = false;
			return active;
		}
		int sumAmounts = 0;
		for (int amount : splitbill.values()) {
			sumAmounts += amount;
		}
		if (sumAmounts == totalAmount) {
			for (String number : splitbill.keySet()) {
				if (!number.equals(getModel().getPhoneNumber())) {
					try {
						mbwayTransfer(number, splitbill.get(number));
					} catch (MBWayException e) {
						System.out.println(e.getType());
						active=false;
						return active;
					}
				}
			}
			splitBillMessage("Bill payed successfully!");
		} else {
			splitBillMessage("Something is wrong. Did you set the bill amount right?");
		}
		active = false;
		return active;
	}
}
