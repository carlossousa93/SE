package pt.ulisboa.tecnico.learnjava.sibs.State;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Retry implements State{
	
	private int retries = 3;
    private State previewState = null;
   
    @Override
    public void process(TransferOperation wrapper, Services services) throws AccountException, OperationException{
       
        if (!(wrapper.getState() instanceof Retry)) {
            this.previewState = wrapper.getState();
            wrapper.setState(this);
        } else {
        	
            if (retries>0) {
            	retryAgain(wrapper, services);
            } else {
            	this.previewState.cancel(wrapper, services);
                wrapper.setState(new Error());
            }
        }
        this.retries -= 1;
    }

	@Override
	public void cancel(TransferOperation wrapper, Services services) throws AccountException, OperationException {
		this.previewState.cancel(wrapper, services);
	}
	
	public void retryAgain(TransferOperation wrapper, Services services) {
		try {
    		this.previewState.process(wrapper, services);
		} catch (Exception e) {
			System.out.println("It is not possibe to process the operation!");
		}
	}
	
}
