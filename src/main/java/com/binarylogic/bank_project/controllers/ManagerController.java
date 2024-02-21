package com.binarylogic.bank_project.controllers;

import java.util.ArrayList;
import java.util.InputMismatchException;

import com.binarylogic.bank_project.database.BankAdministratorDatabase;
import com.binarylogic.bank_project.models.BankAccount;
import com.binarylogic.bank_project.models.BankLoan;
import com.binarylogic.bank_project.views.MainView;

import Exceptions.InvalidInputException;

public class ManagerController {
	
	public static MainView mainView = new MainView();
    public static ArrayList<BankLoan> bankLoans = new ArrayList<BankLoan>();
    public static ArrayList<BankAccount> bankAccounts = new ArrayList<BankAccount>();
    public static ArrayList<BankAccount> bankAccountsWithLoans = new ArrayList<BankAccount>();
    private static BankAdministratorDatabase bankAdministratorDatabase = new BankAdministratorDatabase();
    private static int loan_choice = 0;
    private static int account_choice = 0;
    public static void administratorOrEmployee() {
		try {
		int choice_ = mainView.getWhichEmployee();
		switch(choice_) {
		  case 1:
			     bankAccounts =  bankAdministratorDatabase.getAllBankAccounts();
			     selectAccountView();
			  	 break;
		  case 2:
			  	 bankEmployeeOptionFlow();
			  	 break;
		  case 3:
			     return;
		  default:
			     mainView.printResponse("Invalid option");
			     throw new InvalidInputException();
			     
		}
		}catch(Exception e) {
			System.out.println("Error occurred, retrying");
			administratorOrEmployee();
		}
	}
    
    public static void bankEmployeeOptionFlow() {
    	try {
    		int choice_ = Integer.parseInt(mainView.getBankEmployeeChoice());
    		switch(choice_) {
	    		case 1:
	    			   listLoans();
	    			   break;
	    		case 2:
	    			   return;
	    		
	    			  
    		}
    		bankEmployeeOptionFlow();
    	}catch(Exception e) {
    		System.out.println("Invalid Input, try again");
    		bankEmployeeOptionFlow();
    	}
    }
    
    public static void listLoans() {
    	try {
    		bankLoans =  bankAdministratorDatabase.getAllLoansOnBankAccount(-1);
    		//bankAccountsWithLoans = bankAdministratorDatabase.getAllBankAccountWithLoans();
    		int choice_ = mainView.listLoansForChoice(bankLoans);
    		if(choice_ == -1) {
    			return;
    		}else if(choice_ > 0 && choice_ <= bankLoans.size()){
    			loan_choice = choice_ - 1;
    			approveOrDeny();
    			
    			
    		}else {
    			throw new Exception();
    		}
    		bankEmployeeOptionFlow();
    	}catch(Exception e) {
    		System.out.println("Invalid Input, try again");
    		bankEmployeeOptionFlow();
    	}
    }
    public static void selectAccountView() {
    	try {
    		account_choice = mainView.getAccountChoice(bankAccounts, true);
    		switch(account_choice) {
    		case -1:
    			    return;
    			    
    		}
    		if(account_choice < 1 || account_choice > bankAccounts.size()) {
    			throw new InvalidInputException();
    		}
    		account_choice -= 1;
    		getAdministrativeActionOnBankAccount();
    		
    	}catch(InvalidInputException e) {
    		System.out.println("InvalidAccountChoice, trying again.");
    		selectAccountView();
    	}
    	catch(Exception e) {
    		System.out.println("Something went wrong, trying again.");
    		selectAccountView();
    	}
    	selectAccountView();
    }
    
    public static void getAdministrativeActionOnBankAccount() {
    	try {
    		 int choice = mainView.getAdministrativeActionOnAccount(bankAccounts.get(account_choice));
    		 Boolean result_ = false;
    		 switch(choice) {
    		    case 1:
    		    	   chooseLoanOnBankAccount(); 
    		    	   break;
    		    case 2: 
    		    	   result_ = bankAdministratorDatabase.reopenOrApproveAccount(bankAccounts.get(account_choice).getId());
    		    	   if(result_ == true) {
    		    		   System.out.println("Account was approved");
    		    	   }else {
    		    		   throw new Exception();
    		    	   }
    		    	   break;
    		    case 3: 
    		    	   result_ = bankAdministratorDatabase.closeAccount(bankAccounts.get(account_choice).getId());
    		    	   if(result_ == true) {
    		    		   System.out.println("Account was closed");
    		    	   }else {
    		    		   throw new Exception();
    		    	   }
    		    case 4:
    		    	   return;  
    		 	default:
    		 		   throw new Exception();
    		 		   
    		 }
    	}catch(Exception e) {
    		System.out.println("Something went wrong, trying again.");
    		getAdministrativeActionOnBankAccount();
    	}
    	getAdministrativeActionOnBankAccount();
    }
    
    public static void chooseLoanOnBankAccount() {
    	try {
	    	bankLoans = bankAdministratorDatabase.getAllLoansOnBankAccount(bankAccounts.get(account_choice).getId());
	    	int choice_ = mainView.listLoansForChoice(bankLoans);
			if(choice_ == -1) {
				return;
			}else if(choice_ > 0 && choice_ <= bankLoans.size()){
				loan_choice = choice_ - 1;
				approveOrDeny();
			}
    	}
		catch(Exception e) {
    		System.out.println("Something went wrong, trying again.");
    		getAdministrativeActionOnBankAccount();
    	}
    }
    
    
    public static void approveOrDeny() {
    	try {
    		
    		int choice_ = mainView.getLoanApprovalOrDenial(bankLoans.get(loan_choice));
    		int loan_id;
    		int related_account_id;
    		float loan_amount;
    		Boolean result;
    		switch(choice_) {
    		case 1: 
    			    loan_id = bankLoans.get(loan_choice).getIdOfLoan();
    			    related_account_id = bankLoans.get(choice_ - 1).getRelated_account_id();
    			    loan_amount = Float.valueOf(bankLoans.get(choice_ - 1).getAmount() + "").floatValue();
    			    result  = bankAdministratorDatabase.approveOrDenyLoan(related_account_id, loan_id, loan_amount, true);
    			    bankLoans =  bankAdministratorDatabase.getAllLoansOnBankAccount(-1);
    				break;
    		case 2:
	    			loan_id = bankLoans.get(loan_choice).getIdOfLoan();
				    related_account_id = bankLoans.get(loan_choice).getRelated_account_id();
				    loan_amount = Float.valueOf(bankLoans.get(loan_choice).getAmount() +"").floatValue();
				    result = bankAdministratorDatabase.approveOrDenyLoan(related_account_id,loan_id,  loan_amount, false);
				    bankLoans =  bankAdministratorDatabase.getAllLoansOnBankAccount(-1);
				    break;
    		case 3:
    			    return;
    		default:
    			    throw new InputMismatchException();
    		}
    		if(result == null) {
    			System.out.println("Internal System error");
    		}
    		else if(result == true && choice_ == 1) {
    			System.out.println("Loan was successfully approved");
    		}else if(result == true && choice_ == 2) {
    			System.out.println("Loan was successfully denied");
    		}
    		
    		
    	}catch(InputMismatchException e) {
    		System.out.println("Invalid Input, try again");
    		bankEmployeeOptionFlow();
    	}
    	catch(Exception e) {
    		System.out.println("Something went wrong, please try again.");
    		bankEmployeeOptionFlow();
    	}
    }
}
