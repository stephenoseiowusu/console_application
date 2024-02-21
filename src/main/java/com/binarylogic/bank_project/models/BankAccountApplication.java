package com.binarylogic.bank_project.models;

public class BankAccountApplication {
	
	private String username;
	private String gender;
	private String first_name;
	private String last_name;
	private int age;
	private int credit_score;
	private String status;
	private String email_address;
	private int account_id;
	
	public BankAccountApplication( String gender, String first_name, String last_name,  String status, int age, int credit_score, String email_address) {
		this.username = email_address;
		this.gender = gender;
		this.first_name = first_name;
		this.last_name = last_name;
		this.status = status;
		this.age = age;
		this.credit_score = credit_score;
		this.email_address = email_address;
	}
	
	

	public String getUsername() {
		return username;
	}

    public void updateStatus(String status) {
    	this.status = status;
    }

	public String getGender() {
		return gender;
	}
  
	public int getAge() {
		return this.age;
	}


	public String getFirst_name() {
		return first_name;
	}


	public String getLast_name() {
		return last_name;
	}
	
	public String getEmailAddress() {
		return this.email_address;
	}
	
	public int getCreditScore() {
		return this.credit_score;
	}
	


	
	

}
