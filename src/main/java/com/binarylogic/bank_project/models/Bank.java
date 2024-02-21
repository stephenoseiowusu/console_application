package com.binarylogic.bank_project.models;

import java.util.ArrayList;

public class Bank {
	
	private String bank_name;
	private int bank_id;
	private ArrayList<BankEmployee> bankEmployees;
	
	public Bank(String bank_name, int bank_id, ArrayList<BankEmployee> bankEmployees) {
		this.bank_name = bank_name;
		this.bank_id = bank_id;
		this.bankEmployees = bankEmployees;
	}
	
	public ArrayList<BankEmployee> getBankEmployees(){
		return this.bankEmployees;
	}
	
	public int getBankId() {
		return this.bank_id;
	}
	public String getBankName() {
		return this.bank_name;
	}

}
