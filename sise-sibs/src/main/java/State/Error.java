package State;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Error implements State {
	
	@Override
	public void process(TransferOperation wrapper, Services services) throws AccountException, OperationException {
		throw new OperationException("Error: Can't process an error state!");
    }

	@Override
	public void cancel(TransferOperation wrapper, Services services) throws AccountException, OperationException {
		throw new OperationException("Error: Can't cancel an error state!!");
	}

}
