package State;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Cancelled implements State {
	@Override
	public void cancel(TransferOperation wrapper, Services services) throws OperationException {
		throw new OperationException("Cancelled.");
		//System.out.println("Cancelled");
	}

	@Override
	public void process(TransferOperation wrapper, Services services) throws OperationException {
		throw new OperationException("Cancelled: Can not change the actual state.");
		//System.out.println("Cancelled: Can not change the actual state.");
	}
}
