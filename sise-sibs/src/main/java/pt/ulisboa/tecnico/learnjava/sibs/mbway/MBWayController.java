package pt.ulisboa.tecnico.learnjava.sibs.mbway;

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

}
