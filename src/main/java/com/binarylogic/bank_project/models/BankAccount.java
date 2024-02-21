package com.binarylogic.bank_project.models;

import java.util.Date;

public class BankAccount {
	
	

	private int id;
	private float balance;
	private String status;
	private String type;
	private Date created_date;
	private String account_owner;
	public BankAccount(String account_owner, int id, float balance, String status, String type, Date created_date) {
		this.id = id;
		this.balance = balance;
		this.status = status;
		this.type = type;
		this.created_date = created_date;
		this.account_owner = account_owner;
	}
	
	@Override
	public String toString() {
		return "account number: " + this.id +" balance: " + this.balance + " created date: " + this.created_date.toString() + " status: " + this.status + " type: " + this.type + " account owner: " + this.account_owner;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
