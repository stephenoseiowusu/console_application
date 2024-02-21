package com.binarylogic.bank_project.models;

import java.sql.Timestamp;

public class BankLoan {
	
	private String status;
	private float amount;
	private float pending_amount;
	private Timestamp applied_timestamp;
	private String associated_account_holder;
	private int id;
	private int related_account_id;
    private int applicant_credit_score;

	public BankLoan( int id,String status, float amount, float pending_amount, Timestamp application_applied_timestamp, String associated_account_holder, int assocoiated_account_id, int credit_score) {
		this.status = status;
		this.amount = amount;
		this.pending_amount = pending_amount;
		this.applied_timestamp = application_applied_timestamp;
		this.associated_account_holder = associated_account_holder;
		this.id = id;
		this.related_account_id = assocoiated_account_id;
		this.applicant_credit_score = credit_score;
		
	}
	
	public int getIdOfLoan() {
		return id;
	}
	
	public String toString() {
		return String.format("status: %s amount:%f  balance:%f loan request date: %s applicant credit score: %d", status, amount,pending_amount, applied_timestamp.toLocalDateTime().toString(), applicant_credit_score);
	}
	
	public String getStatus() {
		return status;
	}

	public double getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
		if(this.amount < 0) {
			this.amount = 0;
		}
	}
	public double getPending_amount() {
		return pending_amount;
	}

	public Timestamp getApplied_timestamp() {
		return applied_timestamp;
	}
	
	public void setCreditScore(int credit_score) {
		this.applicant_credit_score = credit_score;
	}

	public int getRelated_account_id() {
		return related_account_id;
	}
	

}
