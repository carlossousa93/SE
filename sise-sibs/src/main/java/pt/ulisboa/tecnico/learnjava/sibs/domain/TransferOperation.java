package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.State.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.State.State;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperation extends Operation {
	private final String sourceIban;
	private final String targetIban;

	private State currentState;
	Services services;

	public TransferOperation(String sourceIban, String targetIban, int value) throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}
		this.currentState = new Registered();
		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	@Override
	public int commission() {
		return (int) Math.round(super.commission() + getValue() * 0.05);
	}

	public String getSourceIban() {
		return this.sourceIban;
	}

	public String getTargetIban() {
		return this.targetIban;
	}

	public State getState() {
		return this.currentState;
	}

	public void setState(State s) {
		this.currentState = s;
	}

	public void process(Services services) throws OperationException, AccountException {
		this.currentState.process(this, services);
	}

	public void cancel(Services services) throws OperationException, AccountException {
		this.currentState.cancel(this, services);
	}
}
