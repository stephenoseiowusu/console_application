package com.binarylogic.bank_project.database;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.binarylogic.bank_project.models.*;
public class BankAdministratorDatabase {

	public ArrayList<BankAccount> getAllBankAccounts(){
		ArrayList<BankAccount> result = new ArrayList<BankAccount>();
		try {
			Connection connection = new ConnectionFactory().setUp();
			String query = "Select * from bank_account ba inner join bank_account_joint baj on ba.account_id = baj.bank_account_id inner join bank_customer bc on bc.customer_id = baj.customer_id = bc.customer_id";
			PreparedStatement stmt = connection.prepareStatement(query);
			ResultSet rst = stmt.executeQuery();
			while(rst.next()) {
				int id_ = rst.getInt("account_id");
				float account_balance = rst.getFloat("account_balance");
				String account_type = rst.getString("account_type");
				String account_status = rst.getString("account_status");
				String account_name = rst.getString("account_name");
				Date createdDate = new Date(rst.getTimestamp("timestamp_added").getTime());
				String emailaddress = rst.getString("emailaddress");
			  	result.add(new BankAccount(emailaddress,id_,account_balance,account_status, account_type, createdDate));
			}
			stmt.close();
			rst.close();
			connection.close();
		}catch(SQLException e) {
			result = null;
		}
		return result;
	}
	
	public Boolean closeAccount(int account_id) {
		Boolean result;
		try {
			Connection connection = new ConnectionFactory().setUp();
			String query = "update  bank_account set account_status = 'closed' where account_id = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, account_id);
			int result_ =  stmt.executeUpdate();
			result = result_ >= 0;
			stmt.close();
			connection.close();
		}catch(SQLException e) {
			result = null;
		}
		return result;
	}
	
	public Boolean reopenOrApproveAccount(int account_id) {
		Boolean result;
		try {
			Connection connection = new ConnectionFactory().setUp();
			String query = "update  bank_account set account_status = 'functional' where account_id = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, account_id);
			int result_ =  stmt.executeUpdate();
			result = result_ >= 0;
			stmt.close();
			connection.close();
		}catch(SQLException e) {
			result = null;
		}
		return result;
	} 
	
	public Boolean approveOrDenyLoan(int account_id,int loan_id, float loan_amount, boolean approved) {
		Boolean result = null;
		try {
			Connection connection = new ConnectionFactory().setUp();
			connection.setAutoCommit(true);
			String query = approved ? "update  bank_loan_application set application_status = ?, pending_amount = ? where application_loan_id = ?":"update  bank_loan_application set application_status = ? where application_loan_id = ?" ;
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, (approved == true ? "approved":"disapproved"));
			if(approved) {
				 stmt.setFloat(2, loan_amount);
				 stmt.setInt(3, loan_id);
			}else {
			 stmt.setInt(2, loan_id);
			} 
			int result_ =  stmt.executeUpdate();
			result = result_ > 0;
			if(result == true && approved == true) {
				disperseLoanAmount(account_id,loan_amount);
			}
			stmt.close();
			connection.close();
		}catch(SQLException e) {
			result = null;
		}
		return result;
	}
	public ArrayList<BankAccount> getAllBankAccountWithLoans(){
		ArrayList<BankAccount> result = null;
		try {
			Connection connection = new ConnectionFactory().setUp();
			String query = "Select * from bank_account ba inner join bank_account_joint baj on ba.account_id = baj.bank_account_id inner join bank_loan_application_joint blaj on baj.bank_account_id = blaj.bank_account_id inner join bank_customer bc on bc.customer_id = baj.customer_id = bc.customer_id";
			PreparedStatement stmt = connection.prepareStatement(query);
			ResultSet rst = stmt.executeQuery();
			while(rst.next()) {
				int id_ = rst.getInt("account_id");
				float account_balance = rst.getFloat("account_balance");
				String account_type = rst.getString("account_type");
				String account_status = rst.getString("account_status");
				String account_name = rst.getString("account_name");
				Date createdDate = new Date(rst.getTimestamp("timestamp_added").getTime());
				String emailaddress = rst.getString("emailaddress");
			  	result.add(new BankAccount(emailaddress,id_,account_balance,account_status, account_type, createdDate));
			}
			stmt.close();
			rst.close();
			connection.close();
		}catch(SQLException e) {
			result = null;
		}
		return result;
	}
	public Boolean disperseLoanAmount(int account_id, float amount) {
		Boolean result = null;
		try {
			Connection connection = new ConnectionFactory().setUp();
			String query = "Update bank_account set account_balance = account_balance + ? where account_id = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setFloat(1, amount);
			stmt.setInt(2, account_id);
			result = stmt.executeUpdate() == 1;
			stmt.close();
			connection.close();
		}catch(SQLException e) {
			return null;
		}
		return result;
	}
	public int getBankAccountIdAssociatedWithLoan(int loan_id) {
		int account_id = -1;
		try {
			Connection connection = new ConnectionFactory().setUp();
			String query = "Select bank_account_id from bank_loan_application_joint where bank_loan_application_id = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, loan_id);
			ResultSet rst  = stmt.executeQuery();
			if(rst.next()) {
			  account_id = rst.getInt(1);
			}
			if(rst != null) {
				rst.close();
				rst = null;
			}
			
			if(connection != null) {
				connection.close();
			}
		}
		catch(SQLException e) {
				return -1;
		}
		return account_id;
	}
	public int getApplicantCreditScore(int account_id_) {
		int credit_score = 0;
		try {
			
			Connection connection = new ConnectionFactory().setUp();
			String query = "Select credit_score from bank_customer bc inner join bank_account_joint baj on bc.customer_id = baj.customer_id what baj.account_id = ?; ";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, account_id_);
			ResultSet rst  = stmt.executeQuery();
			if(rst.next()) {
			  credit_score = rst.getInt(1);
			}
			if(rst != null) {
				rst.close();
				rst = null;
			}
			
			if(connection != null) {
				connection.close();
			}
		}catch(SQLException e) {
			credit_score = -1;
		}
		return credit_score;
	}
	public ArrayList<BankLoan> getAllLoansOnBankAccount(int account_id_){
		ArrayList<BankLoan> bankLoans = new ArrayList<BankLoan>();
		try {
			Connection connection = new ConnectionFactory().setUp();																																																																																														    
			String query = (account_id_ == -1) ? "Select * from bank_loan_application bla inner join bank_loan_application_joint blaj on bla.application_loan_id = blaj.bank_loan_application_id inner join bank_account ba on ba.account_id = blaj.bank_account_id inner join bank_account_joint baj on blaj.bank_account_id = baj.bank_account_id inner join bank_customer bc on bc.customer_id = baj.customer_id where bla.application_status = 'pending'" :"Select * from bank_loan_application bla inner join bank_loan_application_joint blaj on bla.application_loan_id = blaj.bank_loan_application_id inner join bank_account ba on ba.account_id = blaj.bank_account_id inner join bank_account_joint baj on blaj.bank_account_id = baj.bank_account_id inner join bank_customer bc on bc.customer_id = baj.customer_id where blaj.bank_account_id = ? and bla.application_status = 'pending'";
			PreparedStatement stmt = connection.prepareStatement(query);
			if(account_id_ != -1) {
				stmt.setInt(1, account_id_);
			}
			ResultSet rst = stmt.executeQuery();
			while(rst.next()) {
				int application_loan_id = rst.getInt("application_loan_id");
				String application_status = rst.getString("application_status");
				float application_amount = rst.getFloat("application_amount");
				float pending_amount = rst.getFloat("pending_amount");
				Timestamp loan_application_date = rst.getTimestamp("loan_application_applied_date");
				String account_holder = rst.getString("first_name") + " " + rst.getString("last_name");
				account_id_ = getBankAccountIdAssociatedWithLoan(application_loan_id);
				int credit_score = getApplicantCreditScore(account_id_);
				bankLoans.add(new BankLoan(application_loan_id,application_status,application_amount,pending_amount,loan_application_date,account_holder,account_id_,credit_score));
			}
			stmt.close();
			rst.close();
			connection.close();
		}catch(SQLException e) {
			return null;
		}
		return bankLoans;
		
	}
}
