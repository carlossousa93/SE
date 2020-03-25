package pt.ulisboa.tecnico.learnjava.sibs.domain;

import State.Cancelled;
import State.Completed;
import State.Retry;
import State.Error;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Sibs {
	final Operation[] operations;
	Services services;

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.services = services;
	}

	public void transfer(String sourceIban, String targetIban, int amount)
			throws SibsException, AccountException, OperationException {

		validateAccounts(sourceIban, targetIban);

		TransferOperation operation = new TransferOperation(sourceIban, targetIban, amount);

		addOperation(operation);
	}

	public void validateAccounts(String sourceIban, String targetIban) throws SibsException {

		if (!this.services.accountExistance(sourceIban) || !this.services.accountExistance(targetIban)
				|| this.services.accountStatus(sourceIban) || this.services.accountStatus(targetIban)) {
			throw new SibsException();
		}
	}

	public int addOperation(Operation operation)

			throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {//
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		}

		if (position == -1) {
			throw new SibsException();
		}

		this.operations[position] = operation;
		return position;
	}

	public void processOperations() throws OperationException, AccountException {

		for (Operation operation : this.operations) {
			if (operation != null && operation.getType().equals(Operation.OPERATION_TRANSFER)) {
				TransferOperation transfer = (TransferOperation) operation;
				if (validateState(transfer)) {
					tryProcess(transfer);
				}
			}
		}
	}
	
	private void tryProcess(TransferOperation transfer) throws AccountException, OperationException {
		try {
            transfer.process(services);
        } catch (Exception e) {
            if (!(transfer.getState() instanceof Retry)) {
                (new Retry()).process(transfer, services);
            }
        }
	}
	
	private boolean validateState(TransferOperation transfer) {
		return !(transfer.getState() instanceof Completed) 
				&& !(transfer.getState() instanceof Cancelled) 
				&& !(transfer.getState() instanceof Error);
	}

	public void cancelOperations(int id) throws SibsException, OperationException, AccountException {

		if (getOperation(id) == null) {
			throw new SibsException("CancelOperations: The ID return null!");
		}

		if (getOperation(id).getType().equals(Operation.OPERATION_TRANSFER)) {
			TransferOperation transfer = (TransferOperation) getOperation(id);
			if (validateState(transfer)) {
				transfer.cancel(services);
			}
		}
	}

	public void removeOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException("ID out of bounds!");
		}
		this.operations[position] = null;
	}

	public Operation getOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException("ID out of bounds!");
		}
		return this.operations[position];
	}

	public int getNumberOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result++;
			}
		}
		return result;
	}

	public int getTotalValueOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}

	public int getTotalValueOfOperationsForType(String type) {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null && this.operations[i].getType().equals(type)) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}
}
