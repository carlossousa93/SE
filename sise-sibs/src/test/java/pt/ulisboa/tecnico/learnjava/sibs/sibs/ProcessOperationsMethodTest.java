package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.PersonException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.State.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.State.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.State.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.State.Error;
import pt.ulisboa.tecnico.learnjava.sibs.State.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.State.Retry;
import pt.ulisboa.tecnico.learnjava.sibs.State.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class ProcessOperationsMethodTest {
	
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";

	private Sibs sibs;
	private Bank sourceBank;
	private Bank targetBank;
	private Person person;
	private Client sourceClient;
	private Client targetClient;
	private Client targetClientSameBank;
	private String sourceIban;
	private String targetIban;
	private String targetIbanSameBank;
	private Services services;
	
	@Before
	public void setUp() throws BankException, AccountException, ClientException, PersonException, SibsException, OperationException {
		this.services = new Services();
		this.sibs = new Sibs(100, services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.person = new Person( FIRST_NAME, LAST_NAME, ADDRESS, PHONE_NUMBER);
		this.sourceClient = new Client(this.sourceBank, this.person, NIF, 33);
		this.targetClient = new Client(this.targetBank, this.person, "222222222", 22);
		this.targetClientSameBank = new Client(this.sourceBank, this.person,"222222222", 22);
		this.sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0); 
		this.targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);
		this.targetIbanSameBank = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.targetClientSameBank, 1000, 0);
	}
	
	@Test
	public void success() throws BankException, AccountException, SibsException, OperationException, ClientException {
		
		this.sibs.transfer(this.sourceIban, this.targetIban, 100);
		this.sibs.transfer(this.sourceIban, this.targetIban, 100);
		
		this.sibs.processOperations();
		
		this.sibs.transfer(this.sourceIban, this.targetIban, 100);
		
		TransferOperation operation1 = (TransferOperation) this.sibs.getOperation(0);
		TransferOperation operation2 = (TransferOperation) this.sibs.getOperation(1);
		TransferOperation operation3 = (TransferOperation) this.sibs.getOperation(2);
		
		assertTrue(operation1.getState() instanceof Withdrawn);
		assertTrue(operation2.getState() instanceof Withdrawn);
		assertTrue(operation3.getState() instanceof Registered);
		assertEquals(3, this.sibs.getNumberOfOperations());
		assertEquals(800, this.services.getAccountByIban(this.sourceIban).getBalance());
		assertEquals(300, this.sibs.getTotalValueOfOperations());
		assertEquals(300, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));	
	}
	
	@Test
	public void sucessDiffStatesDiffBank() throws SibsException, AccountException, OperationException {
		this.sibs.transfer(this.sourceIban, this.targetIban, 100);
		this.sibs.processOperations();
		this.sibs.transfer(this.sourceIban, this.targetIban, 100);
		this.sibs.processOperations();
		this.sibs.transfer(this.sourceIban, this.targetIban, 100);
		this.sibs.processOperations();
		
		TransferOperation operation1 = (TransferOperation) this.sibs.getOperation(0);
		TransferOperation operation2 = (TransferOperation) this.sibs.getOperation(1);
		TransferOperation operation3 = (TransferOperation) this.sibs.getOperation(2);
		
		assertTrue(operation1.getState() instanceof Completed);
		assertTrue(operation2.getState() instanceof Deposited);
		assertTrue(operation3.getState() instanceof Withdrawn);
		
		int comission = operation1.commission();
		assertEquals(3, this.sibs.getNumberOfOperations());
		assertEquals(700-comission, this.services.getAccountByIban(this.sourceIban).getBalance());
		assertEquals(1200, this.services.getAccountByIban(this.targetIban).getBalance());
		assertEquals(300, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
	}
	
	@Test
    public void successDiffStatesSameBank() throws OperationException, SibsException, AccountException, BankException, ClientException {
        this.sibs.transfer(this.sourceIban, this.targetIbanSameBank, 100);
        this.sibs.processOperations();
       
        this.sibs.transfer(this.sourceIban, this.targetIbanSameBank, 100);
        this.sibs.processOperations();
       
        this.sibs.transfer(this.sourceIban, this.targetIbanSameBank, 100);
        this.sibs.processOperations();
        
		TransferOperation operation1 = (TransferOperation) this.sibs.getOperation(0);
		TransferOperation operation2 = (TransferOperation) this.sibs.getOperation(1);
		TransferOperation operation3 = (TransferOperation) this.sibs.getOperation(2);
       
        assertTrue(operation1.getState() instanceof Completed);
        assertTrue(operation2.getState() instanceof Completed);
        assertTrue(operation3.getState() instanceof Withdrawn);
        assertEquals(700, this.services.getAccountByIban(this.sourceIban).getBalance());
        assertEquals(1200, this.services.getAccountByIban(this.targetIbanSameBank).getBalance());
        assertEquals(3, this.sibs.getNumberOfOperations());
        assertEquals(300, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
    }
	
	@Test
    public void successDiffStatesSameBankWCancel() throws OperationException, SibsException, AccountException, BankException, ClientException {
        this.sibs.transfer(this.sourceIban, this.targetIbanSameBank, 100);
        this.sibs.processOperations();
       
        this.sibs.transfer(this.sourceIban, this.targetIbanSameBank, 100);
        this.sibs.processOperations();
        this.sibs.cancelOperations(1);
       
        this.sibs.transfer(this.sourceIban, this.targetIbanSameBank, 100);
        this.sibs.processOperations();
        
		TransferOperation operation1 = (TransferOperation) this.sibs.getOperation(0);
		TransferOperation operation2 = (TransferOperation) this.sibs.getOperation(1);
		TransferOperation operation3 = (TransferOperation) this.sibs.getOperation(2);
		
        assertTrue(operation1.getState() instanceof Completed);
        assertTrue(operation2.getState() instanceof Cancelled);
        assertTrue(operation3.getState() instanceof Withdrawn);
        assertEquals(800, this.services.getAccountByIban(this.sourceIban).getBalance());
        assertEquals(1100, this.services.getAccountByIban(this.targetIbanSameBank).getBalance());
        assertEquals(3, this.sibs.getNumberOfOperations());
        assertEquals(300, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
    }
	
	@Test
	public void processRetry() throws BankException, AccountException, ClientException, OperationException, SibsException {
		
		this.sibs.transfer(this.sourceIban, this.targetIbanSameBank, 1001);
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Registered);
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
	}
	
	@Test
	public void processRetry3TimesToErrorState() throws BankException, AccountException, ClientException, OperationException, SibsException {
		
		this.sibs.transfer(this.sourceIban, this.targetIbanSameBank, 1001);
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Registered);
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
        this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
	}
	
	@Test
	public void withdrawComissionRetry3TimesToErrorState() throws BankException, AccountException, ClientException, OperationException, SibsException {
		
		this.sibs.transfer(this.sourceIban, this.targetIban, 1000);
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Registered);
        this.sibs.processOperations();
        assertEquals(0, this.services.getAccountByIban(this.sourceIban).getBalance());
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Withdrawn);
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Deposited);
        assertEquals(2000, this.services.getAccountByIban(this.targetIban).getBalance());
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
        assertEquals(1000, this.services.getAccountByIban(this.sourceIban).getBalance());
        assertEquals(1000, this.services.getAccountByIban(this.targetIban).getBalance());
	}
	
	@Test
	public void processTwoErrorState() throws BankException, AccountException, ClientException, OperationException, SibsException {
		
		this.sibs.transfer(this.sourceIban, this.targetIban, 1001);
		this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
        this.sibs.transfer(this.sourceIban, this.targetIban, 1001);
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
        assertTrue(((TransferOperation) this.sibs.getOperation(1)).getState() instanceof Retry);
        this.sibs.processOperations();
        assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
        assertTrue(((TransferOperation) this.sibs.getOperation(1)).getState() instanceof Retry);
	}
	
	@Test
	public void cancelStateRetry() throws SibsException, AccountException, OperationException {
		this.sibs.transfer(this.sourceIban, this.targetIban, 1001);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
		this.sibs.cancelOperations(0);
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Cancelled);
	}
	
	@Test
	public void tryToCancelAnErrorState() throws SibsException, AccountException, OperationException {
		this.sibs.transfer(this.sourceIban, this.targetIbanSameBank, 1001);
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
		this.sibs.cancelOperations(0);
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
	}
	
	@Test
	public void tryToProcessAnErrorState() throws SibsException, AccountException, OperationException {
		this.sibs.transfer(this.sourceIban, this.targetIbanSameBank, 1001);
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
	}
			
	@After
	public void tearDown() {
		Bank.clearBanks();
		this.sibs = null;
	}

}
