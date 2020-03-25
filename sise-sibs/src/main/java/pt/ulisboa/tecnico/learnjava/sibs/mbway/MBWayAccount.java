package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;

public class MBWayAccount {

	private Account account;

	private String iban;

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	private boolean associated = false;

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public boolean isAssociated() {
		return this.associated;
	}

	public void setAssociated(boolean associated) {
		this.associated = associated;
	}

}
