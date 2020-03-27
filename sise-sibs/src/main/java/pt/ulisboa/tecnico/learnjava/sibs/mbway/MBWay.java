package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.Random;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MBWayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWay {

	public static HashMap<String, MBWayAccount> mbwaydb = new HashMap<>();

	private String phoneNumber;
	private MBWayAccount account;
	private HashMap<String, MBWayAccount> friends = new HashMap<>();
	private int code;

	public MBWay(String phoneNumber, MBWayAccount account) {
		this.phoneNumber = phoneNumber;
		this.account = account;
		this.friends.put(phoneNumber, account);
		addMBWay();
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public MBWayAccount getAccount() {
		return this.account;
	}

	public HashMap<String, MBWayAccount> getFriends() {
		return this.friends;
	}

	public void setFriends(HashMap<String, MBWayAccount> friends) {
		this.friends = friends;
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean getAccountAssociationState(MBWayAccount account) {
		return account.isAssociated();
	}

	public void setAccountAssociationState(MBWayAccount account, boolean state) {
		account.setAssociated(state);
	}

	public int getAccountBalance(MBWayAccount account) throws MBWayException {
		return account.getAccount().getBalance();
	}

	public void addFriend(String number, MBWayAccount account) throws MBWayException {
		if (this.friends.containsKey(number)) {
			throw new MBWayException();
		}
		this.friends.put(number, account);
		mbwaydb.put(number, account);
	}

	public int code() {
		Random rnd = new Random();
		this.code = rnd.nextInt(999999);
		return this.code;
	}

	public void transfer(int amount, String targetNumber)
			throws MBWayException, SibsException, AccountException, OperationException {
				
		transferValidation(amount, targetNumber);
		
		Services service = new Services();
		Sibs sibs = new Sibs(100, service);
		sibs.transfer(this.account.getIban(), mbwaydb.get(targetNumber).getIban(), amount);
			
	}
	
	public void transferValidation(int amount, String targetNumber) throws MBWayException, SibsException, 
	AccountException, OperationException {
		
		if(!this.account.isAssociated()) {
			throw new MBWayException("Account not associated.");
		}
		
		if(!getFriends().containsKey(targetNumber)) {
			throw new MBWayException("Wrong phone number.");
		}
		
		if(amount > getAccountBalance(this.account)) {
			throw new MBWayException("Not enough money on the source account.");
		}
	}

	public boolean split(String sourceNumber, int amount) throws MBWayException {
		if (getFriends().containsKey(sourceNumber) || sourceNumber.contentEquals(this.phoneNumber)) {
			if (amount <= this.friends.get(sourceNumber).getAccount().getBalance()
					|| amount <= getAccountBalance(this.account)) {
				return true;
			} else {
				throw new MBWayException("Not enough money on the source account.");
			}
		} else {
			throw new MBWayException("Wrong phone number.");
		}
	}

	public void addMBWay() {
		mbwaydb.put(this.phoneNumber, this.account);
	}

}
