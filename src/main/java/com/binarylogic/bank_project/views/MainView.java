package com.binarylogic.bank_project.views;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.binarylogic.bank_project.models.BankAccount;
import com.binarylogic.bank_project.models.BankLoan;

public class MainView {
	Scanner scan = new Scanner(System.in);
	
	public static String [] intro_level_prompts = {
												   "Welcome to the Bank of America,Please choose which are you 1. A Customer 2.Employee 3. To quit",
	                                               "Choose an option \n1. View Account \n2. Open an account \n3. Go Back: "
	                                              };
	public static String [] bank_employee_or_administrator_prompts = {"Are you an administrator or a bank employee 1 for administrator, 2 for employee 3 to go back"};
	public static String [] administrator_prompts = {"1. View accounts 2. View loan applications 3. Go home "} ;
	public static String [] bank_employee_prompts = {"1. View loan applications 2. Go home"};
	public static String [] loan_options_prompts = {"1. Approve Loan. 2. Disapprove Loan 3. Go back"};
	public static String [] loan_choices = {"1. Approve Application","2. Deny Appplication"};
	public static String [] administrative_account_choices = {"1.View Loans 2.Approve Account  3.Close Account 4. Go back"};
	public static String [] secondary_level_prompts = { 
														"1.Apply for a loan \n2.Loan Payment \n3.Withdraw from an account \n4.Deposit into an account \n5.Go back",
												        "Select account to view more information: ",
												        "Choose Account to pay from: ",
												        "Choose Loan to pay: "
												         };
	
	public static String [] treciary_level_prompts = { 
													   "Enter loan amount requested: ",
			  									       "Enter amount you wish to pay: "
												 };

											   
	public static String [] fourth_level_prompts = {
												 "Enter your email address (-1 to escape regostration): ",
												 "Enter your gender (M,F) (-1 to escape regostration): ",
												 "Enter your first name (-1 to escape regostration): ",
												 "Enter your last name (-1 to escape regostration): ",
												 "Enter your age (-1 to escape regostration): ",
												 "Enter your credit score (-1 to escape regostration): ",
												 "Enter your password (-1 to escape regostration): ",
												 "Enter 1 for savings account or 2 for checkings account to create (-1 to escape regostration) :",
												 "Please enter an account name (-1 to escape regostration):"
											   };
	public static String [] account_login_information = {"Enter the email address on your account", "Enter the password on your account:"};
	public static String [] deposit_or_withdraw_prompt = {"Enter amount you wish to deposit: ", "Enter amount you wish to withdraw: "};
	
	public String getAccountInfo(int part) {
		System.out.println(fourth_level_prompts[part]);
		String result = scan.nextLine();
		return result;
	}
	
	public String getBankEmployeeChoice() {
		System.out.println(bank_employee_prompts[0]);
		String result = scan.nextLine();
		return result;
	}
	public String getAccountEmailAddress() {
		System.out.println(account_login_information[0]);
		String result = scan.nextLine();
		return result;
	}
	public String getAccountPassword() {
		System.out.println(account_login_information[1]);
		String result = scan.nextLine();
		return result;
	}
	public int getCustomerIntroChoice() {
		System.out.println(intro_level_prompts[1]);
		int choice = Integer.parseInt(scan.nextLine());
		return choice;
	}
	public int getAdministrativeActionOnAccount(BankAccount bankAccount) {
		System.out.println(bankAccount);
		System.out.println(administrative_account_choices[0]);
		int choice = Integer.parseInt(scan.nextLine());
		return choice;
	}
	public int getIntroLevelChoice() throws NumberFormatException {
		System.out.println(intro_level_prompts[0]);
		int choice;
		try {
		 choice = Integer.parseInt( scan.nextLine());
		}catch(NumberFormatException e) {
			throw new NumberFormatException();
		}
		return choice;
	}
	public int listLoansForChoice(ArrayList<BankLoan> loans) {
		int index = 1;
		System.out.println("Select loan by number on which loan to view -1 to return: ");
		for(BankLoan loan: loans) {
			System.out.println(index + ": " + loan);
			++index;
		}
		int choice = Integer.parseInt(scan.nextLine());
		return choice ;
		
	}
	public int getLoanToPayOn(ArrayList<BankLoan> loans) {
		int index = 1;
		System.out.println("Select loan by number on which loan to pay: ");
		for(BankLoan loan: loans) {
			System.out.println(index + ":" + loan);
			index++;
		}
		String integer = scan.nextLine();
		int choice = Integer.parseInt(integer);
		return (choice - 1);
		
	}
	public float getLoanRequestAmount() {
		System.out.println(treciary_level_prompts[0]);
		float amount = new Float(scan.nextLine());
		return amount;
	}
	
	public float getPaymentAmount() {
		System.out.println(treciary_level_prompts[1]);
		float amount = new Float(scan.nextLine());
		return amount;
	}
	
	public int getAccountChoice(ArrayList<BankAccount> bank_accounts, boolean isEmployedByBank) {
		System.out.println("Select a bank account to view -1 to go back: ");
		for(int i = 0; i < bank_accounts.size(); i++) {
			System.out.println( (i + 1) + ": " + bank_accounts.get(i).toString());
		}
		
		if(!isEmployedByBank) {
			System.out.println("open an account press " +  (bank_accounts.size() + 1));
		}
		int result = Integer.parseInt(scan.nextLine());
		return result;
		
	}
	public int getWhichEmployee() {
		System.out.println(bank_employee_or_administrator_prompts[0]);
		int choice = Integer.parseInt(scan.nextLine());
		return choice;
	}
	public float getDepositAmount() {
		System.out.println(deposit_or_withdraw_prompt[0]);
		float amount = Float.parseFloat(scan.nextLine());
		return amount;
	}
	public float getWithdrawalAmount() {
		System.out.println(deposit_or_withdraw_prompt[1]);
		float amount = Float.parseFloat(scan.nextLine());
		return amount;
	}
	public int getAccountActionChoices() {
		System.out.println(secondary_level_prompts[0]);
		int result = Integer.parseInt(scan.nextLine());
		return result;
	}
	
	public double getLoanAmount() {
		System.out.println(treciary_level_prompts[0]);
		double result = Double.parseDouble(scan.nextLine());
		return result;
	}
	
	public void printResponse(String response) {
		System.out.println(response); 
	}
	
	public int getNewAccountChoice() {
		System.out.println(fourth_level_prompts[7]);
		int choice = Integer.parseInt(scan.nextLine());
		return choice;
	}
	public int getLoanOptions() {
		System.out.println(loan_choices[0]);
		int choice = Integer.parseInt(scan.nextLine());
		return choice;
	}
	public String getAccountName() {
		System.out.println(fourth_level_prompts[8]);
		String account_name = scan.nextLine();
		return account_name;
	}
	
	public boolean isEmailAddress(String emailaddress) {
	    String regexPattern = "[A-Z0-9a-z]+@[A-Z0-9a-z]+.[A-Za-z]+";
	    Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
	    boolean result = pattern.matcher(emailaddress).matches();
	    return result;
	}
	
	public int getLoanApprovalOrDenial(BankLoan loan) {
		System.out.println(loan);
		System.out.println(loan_options_prompts[0]);
		int choice_ = Integer.parseInt(scan.nextLine());
		return choice_;
	}

}
