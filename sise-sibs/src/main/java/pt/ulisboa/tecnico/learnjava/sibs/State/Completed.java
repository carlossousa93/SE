package pt.ulisboa.tecnico.learnjava.sibs.State;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Completed implements State {
	@Override
	public void process(TransferOperation wrapper, Services services) throws OperationException {
		throw new OperationException("Completed");
    }

	@Override
	public void cancel(TransferOperation wrapper, Services services) throws OperationException {
		throw new OperationException("Completed: Can not change the actual state to Cancelled.");
	}
}
