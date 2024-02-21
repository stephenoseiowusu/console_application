package com.binarylogic.bank_project.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.binarylogic.bank_project.models.BankAccount;
import com.binarylogic.bank_project.models.BankAccountApplication;
import com.binarylogic.bank_project.models.BankLoan;
import javax.xml.bind.DatatypeConverter;


public class BankCustomerDatabase {
	
  private Connection connection;
  
 
 
  public int addNewCustomer(BankAccountApplication customer, String password) throws NoSuchAlgorithmException {
	  int result = 0;
	  
	 try {
		  String hashed_password = this.hash_password(password);
		  Connection connection = new ConnectionFactory().setUp();
		  String query = "Select count(*) from bank_customer where username = ?";
		  PreparedStatement stmt = connection.prepareStatement(query);
		  stmt.setString(1, customer.getUsername());
		  ResultSet rst = stmt.executeQuery();
		  rst.next();
		  int count_of_username = rst.getInt(1);
		  stmt.close();
		  rst.close();
		
		  if(count_of_username == 0) {
			  String insert_query = "insert into bank_customer(first_name,last_name,username,password_,age,application_status,credit_score,gender,emailaddress, timestamp_added) values (?,?,?,?,?,?,?,?,?,current_timestamp)";
			  PreparedStatement stmt2 = connection.prepareStatement(insert_query);
			  stmt2.setString(1, customer.getFirst_name());
			  stmt2.setString(2, customer.getLast_name());
			  stmt2.setString(3, customer.getUsername());
			  stmt2.setString(4, hashed_password);
			  stmt2.setInt(5, customer.getAge());
			  stmt2.setInt(6, 0);
			  stmt2.setInt(7, customer.getCreditScore());
			  stmt2.setString(8, customer.getGender());
			  stmt2.setString(9, customer.getEmailAddress());
			  int result_of_insert = stmt2.executeUpdate();
			  if(result_of_insert >= 1) {
				  result = 1;
			  }
			  else {
				  result = -1;
			  }
			  stmt2.close();
		  }
		  connection.close();
		  
	 }catch(SQLException e) {
		 result = -1;
	 }
	 return result;
	 
	  
  }
  public int getIdOfBankAccountByIdentifier(String identifier) {
	  int result = 0;
	  try {
		  Connection connection = new ConnectionFactory().setUp();
		  String query = "Select account_id from bank_account where account_identifier = ?";
		  PreparedStatement stmt = connection.prepareStatement(query);
		  stmt.setString(1, identifier);
		  ResultSet rst = stmt.executeQuery();
		  rst.next();
		  result = rst.getInt(1);
		  rst.close();
		  stmt.close();
		  connection.close();
	  }catch(SQLException e) {
		  result = -1;
	  }
	  return result;
  }
  public int addApplicationForNewAccount(String username, String accountType, String account_name, String account_identifier) {
	  int result = 0;
	  try {
		  Connection connection = new ConnectionFactory().setUp();
		  String query = "insert into bank_account(account_balance, account_status, account_type,account_name, account_identifier,timestamp_added) values (?,?,?,?,?,?)";
		  PreparedStatement stmt = connection.prepareStatement(query);
		  stmt.setFloat(1, 0.0f);
		  stmt.setString(2, "pending");
		  stmt.setString(3, accountType);
		  stmt.setString(4, account_name);
		  stmt.setString(5, account_identifier);
		  stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
		  result = stmt.executeUpdate();
		  stmt.close();
		  int id_of_account = getIdOfBankAccountByIdentifier(account_identifier);
		  int id_of_bank_user  = getBankCustomerIdByEmailAddress(username);
		  if(result == 1) {
			  query = "insert into bank_account_joint(bank_account_id,customer_id) values (?,?)";
			  PreparedStatement stmt2 = connection.prepareStatement(query);
			  stmt2.setInt(1, id_of_account);
			  stmt2.setInt(2, id_of_bank_user);
			  result = stmt2.executeUpdate();
			  stmt2.close();
			  
		  }
		  connection.close();
	  }catch(SQLException e) {
		  result = -1;
	  }
	  return result;
  }
  
  public Boolean addBalancetoBankAccount(String username, int account_id, float amount) {
	  Boolean result = false;
	  try {
		  Connection connection = new ConnectionFactory().setUp();
		  String query = "update bank_account set account_balance =  (select * from (select account_balance from bank_account where account_id = ?) as t_temp) + ? where account_id = ?";
		  PreparedStatement stmt = connection.prepareStatement(query);
		  stmt.setInt(1,account_id);
		  stmt.setFloat(2, amount);
		  stmt.setInt(3,account_id);
		  int result_of_update = stmt.executeUpdate();
		  if(result_of_update == 1 || result_of_update == 0) {
			  result = true;
		  }
		  stmt.close();
		  connection.close();
	  }catch(SQLException e) {
		  result = null;
	  }
	  return result;
  }
  
  public Boolean withdrawFromAccount(int account_id, float amount) {
	  
	  Boolean result = false;
	  try {
		  Connection connection = new ConnectionFactory().setUp();
		  String query = "select account_balance from bank_account where account_id = ?";
		  PreparedStatement stmt = connection.prepareStatement(query);
		  stmt.setInt(1, account_id);
		  ResultSet rst = stmt.executeQuery();
		  rst.next();
		  float account_amount = rst.getFloat(1);
		  stmt.close();
		  rst.close();
		  if(account_amount >= amount) {
			  String query2 = "update bank_account set account_balance = (select * from (select account_balance from bank_account where account_id = ?) as t_temp) - ? where account_id = ?";
			  PreparedStatement stmt2 = connection.prepareStatement(query2);
			  stmt2.setInt(1,account_id);
			  stmt2.setFloat(2, amount);
			  stmt2.setInt(3,account_id);
			  int result_of_update = stmt2.executeUpdate();
			  if(result_of_update == 1 || result_of_update == 0) {
				  result = true;
			  }
			  stmt2.close();
		  }else {
			  result = false;
		  }
		  connection.close();
	  }catch(SQLException e) {
		  result = null;
	  }
	  return result;
  }
  
  public int getBankCustomerIdByEmailAddress(String emailAddress) {
	  int id_ = 0;
	  try {
		  Connection connection = new ConnectionFactory().setUp();
		  String query = "Select customer_id from bank_customer where username = ?";
		  PreparedStatement stmt = connection.prepareStatement(query);
		  stmt.setString(1, emailAddress);
		  ResultSet rst = stmt.executeQuery();
		  if(rst.next()) {
			  id_ = rst.getInt(1);
		  }
		  stmt.close();
		  rst.close();
		  connection.close();
	  }catch(SQLException e) {
		  id_ = -1;
	  }
	  
	  return id_;
	  
  }
  
  public int addBankCustomerToBankAccount(String emailAddress, int account_id) {
	  int result = 0;
	  try {
		  Connection connection = new ConnectionFactory().setUp();
		  String query = "Insert into bank_account_joint(bank_account_id,customer_id,timestamp_added) values (?,?,?)";
		  PreparedStatement stmt = connection.prepareStatement(query);
		  int bank_customer_id = getBankCustomerIdByEmailAddress(emailAddress);
		  stmt.setInt(1, account_id);
		  stmt.setInt(2,bank_customer_id);
		  stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
		  result = stmt.executeUpdate();
		  stmt.close();
		  connection.close();
	  }catch(SQLException e) {
		  result = -1;
	  }
	  return result;
  }
  
  public Boolean doesUsernameExistInDatabase(String username) {
	  Boolean result = false;
	  try {
		  Connection connection = new ConnectionFactory().setUp();
		  String query = "Select count(*) from bank_customer where username = ?";
		  PreparedStatement stmt= connection.prepareStatement(query);
		  stmt.setString(1, username);
		  ResultSet rst = stmt.executeQuery();
		  rst.next();
		  result = rst.getInt(1) >= 1;
		  rst.close();
		  stmt.close();
		  connection.close();
	  }catch(SQLException e ) {
		  result = null;
	  }
	  return result;
  }
  
  public Boolean hasCorrectCredentials(String username, String password) throws NoSuchAlgorithmException {
	  Boolean result = false;
	  password =  hash_password(password);
	  try {
		  Connection connection = new ConnectionFactory().setUp();
		  String query = "Select count(*) from bank_customer where username = ? and password_ = ?";
		  PreparedStatement stmt  = connection.prepareStatement(query);
		  stmt.setString(1, username);
		  stmt.setString(2, password);
		  ResultSet rst = stmt.executeQuery();
		  rst.next();
		  result = rst.getInt(1) == 1 ? true:false;
		  rst.close();
		  stmt.close();
		  connection.close();
		  
	  }catch(SQLException e) {
		  result = null;
	  }
	  return result;
	  
  }
  
  
  public int applyForALoan(Float loanAmount, String emailAddress, int account_id, String identifier ) {
	  int result = 0;
	  try {
		  Connection connection = new ConnectionFactory().setUp();
		  String query = "Insert into bank_loan_application(application_status, application_amount,pending_amount,loan_application_applied_date, identifier) values (?,?,?,?,?)";
		  PreparedStatement stmt = connection.prepareStatement(query);
		  stmt.setString(1, "pending");
		  stmt.setFloat(2,  loanAmount);
		  stmt.setFloat(3, 0.0f);
		  stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
		  stmt.setString(5, identifier);
		  result = stmt.executeUpdate();
		  stmt.close();
		  if(result == 1) {
			  query = "select application_loan_id from bank_loan_application where identifier = ?";
			  stmt = connection.prepareStatement(query);
			  stmt.setString(1, identifier);
			  ResultSet rst = stmt.executeQuery();
			 
			  if(rst.next()) {
				  int application_loan_id = rst.getInt(1);
				  query = "Insert into bank_loan_application_joint values (?,?)";
				  stmt = connection.prepareStatement(query);
				  stmt.setInt(1, account_id);
				  stmt.setInt(2, application_loan_id);
				  result = stmt.executeUpdate();
				  stmt.close();
				  result = 1;
				  
			  }
			  stmt.close();
			  
			  rst.close();
			  
		  }else { 
			  result = -1;
		  }
		  connection.close();
	  }catch(SQLException e) {
		  result = -1;
	  }
	  return result;
  }
  
  public Boolean payOffALoan(int loanid, float amount, int account_id) {
	  Boolean result = false;
	  try{
		  Connection connection = new ConnectionFactory().setUp();
		  String query = "Select pending_amount from bank_loan_application where application_loan_id = ?";
		  PreparedStatement stmt= connection.prepareStatement(query);
		  stmt.setInt(1, loanid);
		  ResultSet rst = stmt.executeQuery();
		  if(rst.next()) {
			  float pending_amount = rst.getFloat(1);
			  stmt.close();
			  rst.close();
			  float final_amount = amount > pending_amount ? amount- pending_amount : amount;
			  query = "Select account_balance from bank_account where account_id = ?";
			  stmt= connection.prepareStatement(query);
			  stmt.setInt(1, account_id);
			  rst = stmt.executeQuery();
			  rst.next();
			  float balance_ = rst.getFloat(1);
			  stmt.close();
			  rst.close();
			  if(balance_ >= final_amount) {
				  float residue = pending_amount  - amount;
				  query = "update bank_loan_application set pending_amount = ? where application_loan_id = ?";
				  stmt= connection.prepareStatement(query);
				  stmt.setFloat(1, residue);
				  stmt.setInt(2, loanid);
				  result = stmt.executeUpdate() > 0;
				  stmt.close();
				  float account_balance = balance_ - final_amount;
				  query = "update bank_account set account_balance = ? where account_id = ?";
				  stmt= connection.prepareStatement(query);
				  stmt.setFloat(1, account_balance);
				  stmt.setInt(2, account_id);
				  result = stmt.executeUpdate() >= 1;
				  stmt.close();
				  result = true;
				  
				  
			  }else {
				  result = false;
			  }
			  
		  }
		  
		  rst.close();
          connection.close();
		  
	  }catch(SQLException e) {
		  result = null;
	  }
	  return result;
  }
  
  public Boolean customerHasValidCredentials(String username, String password) throws NoSuchAlgorithmException {
	  Boolean result_ = false;
	  password = hash_password(password);
	  String query = "select count(*) from bank_customer where username = ? and password_ = ?";
	  try {
		 Connection connection = new ConnectionFactory().setUp();
		 PreparedStatement stmt = connection.prepareStatement(query);
		 stmt.setString(1, username);
		 stmt.setString(2, password);
		 ResultSet rst = stmt.executeQuery();
		 rst.next();
		 result_ = rst.getInt(1) == 1;
		 if(rst != null) {
			 rst.close();
		 }
		 if(stmt != null) {
			 stmt.close();
		 }
		 if(connection != null) {
			 
			connection.close();
		 }
	}catch(SQLException e) {
		result_ = null;
	}
	return result_;
	  
  }
  
  public int getApplicantCreditScore(int account_id_) {
		int credit_score = 0;
		try {
			
			Connection connection = new ConnectionFactory().setUp();
			String query = "Select credit_score from bank_customer bc inner join bank_account_joint baj on bc.customer_id = baj.customer_id where baj.bank_account_id = ? ";
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
			if(stmt != null) {
				stmt.close();
				stmt = null;
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
			String query = "Select * from bank_loan_application bla inner join bank_loan_application_joint blaj on bla.application_loan_id = blaj.bank_loan_application_id inner join bank_account ba on ba.account_id = blaj.bank_account_id inner join bank_account_joint baj on baj.bank_account_id = ba.account_id inner join bank_customer bc on bc.customer_id = baj.customer_id where blaj.bank_account_id = ? ";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, account_id_);
			ResultSet rst = stmt.executeQuery();
			while(rst.next()) {
				int application_loan_id = rst.getInt("application_loan_id");
				String application_status = rst.getString("application_status");
				float application_amount = rst.getFloat("application_amount");
				float pending_amount = rst.getFloat("pending_amount");
				Timestamp loan_application_date = rst.getTimestamp("loan_application_applied_date");
				String account_holder = rst.getString("first_name") + " " + rst.getString("last_name");
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
  
  public ArrayList<BankAccount> getAllBankAccountsOnUserAccount(String username){
	  ArrayList<BankAccount> usersBankAccounts = new ArrayList<BankAccount>();
	  try {
			Connection connection = new ConnectionFactory().setUp();
			String query = "select * from bank_account ba inner join bank_account_joint baj on ba.account_id = baj.bank_account_id inner join bank_customer bc on bc.customer_id = baj.customer_id  where baj.customer_id = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, getBankCustomerIdByEmailAddress(username));
			ResultSet rst = stmt.executeQuery();
			while(rst.next()) {
				int account_id = rst.getInt("account_id");
				String account_status = rst.getString("account_status");
				float account_balance = rst.getFloat("account_balance");
				String account_type = rst.getString("account_type");
				Timestamp account_opening_date = rst.getTimestamp("timestamp_added");
				String account_holder = rst.getString("first_name") + " " + rst.getString("last_name");
				BankAccount bankAccount = new BankAccount(account_holder,account_id,account_balance,account_status,account_type, new Date(account_opening_date.getTime()));
				usersBankAccounts.add(bankAccount);
			}
			stmt.close();
			rst.close();
			connection.close();
		}catch(SQLException e) {
			return null;
		}
	  return usersBankAccounts;
  }
  
public String hash_password(String myPassword) throws NoSuchAlgorithmException {
	String hash = "35454B055CC325EA1AF2126E27707052";
    String password = "ILoveJava";
        
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(password.getBytes());
    byte[] digest = md.digest();
    String myHash = DatatypeConverter.
      printHexBinary(digest).toUpperCase();
    return myHash;
}

}
