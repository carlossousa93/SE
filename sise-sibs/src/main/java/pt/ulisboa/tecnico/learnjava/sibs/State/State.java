package pt.ulisboa.tecnico.learnjava.sibs.State;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public interface State {
	
	void process(TransferOperation wrapper, Services services) throws OperationException, AccountException;
	
	void cancel(TransferOperation wrapper, Services services) throws OperationException, AccountException;

}
