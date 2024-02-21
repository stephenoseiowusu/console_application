package com.binarylogic.bank_project.models;

import java.util.ArrayList;

public class Bank_Customer {
	private String first_name;
	private String username;
	private String last_name;
	private int credit_score;
	private String gender;
	private String emailAddress;
	private ArrayList<BankAccount> accounts;
	private int age;
	public Bank_Customer(String first_name, String last_name, int age, int credit_score, String gender, String emailAddress) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.age  = age;
		this.credit_score = credit_score;
		this.gender = gender;
		this.emailAddress = emailAddress;

	}
    public String getEmailAddress() {
    	return this.emailAddress;
    }
	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public int getAge() {
		return age;
	}

	public int getCredit_score() {
		return credit_score;
	}
   
	public String getGender() {
		return gender;
	}
	
	
	
	                                                                                                  
	
}
