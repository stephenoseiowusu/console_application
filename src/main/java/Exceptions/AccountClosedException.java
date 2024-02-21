package Exceptions;

public class AccountClosedException extends Exception{
	
	public AccountClosedException() {
		super("Bank Account was closed");
	}

}
