package com.binarylogic.bank_project.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

import com.binarylogic.bank_project.models.BankAccount;
import com.binarylogic.bank_project.models.BankAccountApplication;
import com.binarylogic.bank_project.models.BankLoan;
import com.binarylogic.bank_project.models.Bank_Customer;
import com.binarylogic.bank_project.database.BankAdministratorDatabase;
import com.binarylogic.bank_project.database.BankCustomerDatabase;
import com.binarylogic.bank_project.views.MainView;

import Exceptions.AccountClosedException;
import Exceptions.AccountPendingException;
import Exceptions.EscapeException;

public class MainController {
	
	private static BankAdministratorDatabase bankAdministratorDatabase = new  BankAdministratorDatabase();
	private static BankCustomerDatabase bankCustomerDatabase = new BankCustomerDatabase();
	//private static Scanner scan = new Scanner(System.in);
	private static MainView mainView = new MainView();
	private static ArrayList<BankAccount> bankAccounts = new ArrayList<BankAccount>();
	private static int current_account_index = 0;
	private static boolean running = true;
	private static String session_username = "";
	public static void main(String[] args) {
		
		while(running) {
			entryLevel();
		}
		mainView.printResponse("Exiting System");
		System.exit(0);
	}
	
	public static void entryLevel() {
		try {
			int choice = mainView.getIntroLevelChoice();
			int choice2;
			switch(choice) {
				case 1: 
					    flowCustomerChoice();
						break;
				case 2: 
					    ManagerController.administratorOrEmployee();
					    break;
				case 3:
					    running = false;
						return;
						

			}
			
		}catch(NumberFormatException e) {
			mainView.printResponse("Something went wrong, no worries try again.");
			entryLevel();
		}
		entryLevel();
	}
	
	public static void flowCustomerChoice() {
		try {
			int choice = mainView.getCustomerIntroChoice();
			switch(choice) {
				case 1:
						String username = mainView.getAccountEmailAddress();
						session_username = username;
						String password = mainView.getAccountPassword();
						Boolean result = bankCustomerDatabase.customerHasValidCredentials(username, password);
						if(result) {
							bankAccounts = bankCustomerDatabase.getAllBankAccountsOnUserAccount(username);
							flowCustomerAccountOptions();
						}else {
							mainView.printResponse("Invalid login credentials");
							return;
							
						}
						break;
				case 2: 
						String [] inputValues = new String[8];
						for(int i = 0; i < 8; i++) {
							inputValues[i] = mainView.getAccountInfo(i);
							if(inputValues[i].equalsIgnoreCase("-1")) {
								throw new EscapeException();
							}
							switch(i) {
							 case 0:
								   if(!mainView.isEmailAddress(inputValues[0])){
									   mainView.printResponse("Please enter a valid email address!");
									   i -= 1;
								   }
								   else if(mainView.isEmailAddress(inputValues[0])) {
									   if(bankCustomerDatabase.doesUsernameExistInDatabase(inputValues[0])) {
										mainView.printResponse("Email is already registered, returning to previous menu");   
										return;
										
									   }
									   
								   }
								   break;
							 case 1:
								   inputValues[1] = inputValues[1].toUpperCase();
								   if(!inputValues[1].equals("M") && !inputValues[1].equals("F")) {
									   mainView.printResponse("Please enter M or F for gender.");
									   i -= 1;
								   }
								   break;
							 case 2:
								  
								   if(inputValues[2] == null || inputValues[2].equalsIgnoreCase("")) {
									   mainView.printResponse("Please a value for your first name");
									   i -= 1;
								   }
								   break;
							 case 3:
								  
								   if(inputValues[3] == null || inputValues[3].equals("")) {
									   mainView.printResponse("Please enter a value for your last name value");
									   i -= 1;
								   }
								   break;
							 case 4:
								 try {
									 Integer.parseInt(inputValues[4]);
								 }catch(Exception e) {
									 mainView.printResponse("Please enter a number for your age");
									 i -= 1;
								 }
								 break;
							 case 5:
								   try {
									   Integer.parseInt(inputValues[5]);
								   }catch(Exception e) {
									   mainView.printResponse("Please enter a number for your credit score");
										i -= 1;
								   }   
								   break;
							 case 7:
								   try {
									   Integer.parseInt(inputValues[7]);
									   if( Integer.parseInt(inputValues[7]) != 1 && Integer.parseInt(inputValues[7]) != 2 ) {
										   i -= 1;
										   mainView.printResponse("Please enter 1 for savings or 2 for checkings");
									   }
								   }catch(Exception e) {
									   mainView.printResponse("Please enter a number to choose the account type");
										i -= 1;
								   }   
								   break;
								 
								 
								 
						     
							}
						}
						BankAccountApplication bankAccountApplication =  new BankAccountApplication(inputValues[1], inputValues[2],inputValues[3],"pending",Integer.parseInt(inputValues[4]), Integer.parseInt(inputValues[5]),inputValues[0]);
						int didAddtoDatabase = bankCustomerDatabase.addNewCustomer(bankAccountApplication, inputValues[6]);
						if(didAddtoDatabase == 1) {
							mainView.printResponse("successfully added customer");
						}else {
							mainView.printResponse("database error");
							return;
						}
						
						
						break;
				case 3:
					return;
			}
			
		}catch(EscapeException e) {
			return;
		}
		catch(Exception e) {
			mainView.printResponse("Something went wrong, no worries try again.");
			flowCustomerChoice();
		}
	}
	
	public static void flowCustomerAccountOptions(){
		try {
			int account_choice = mainView.getAccountChoice(bankAccounts,false);
			if(account_choice > bankAccounts.size() + 1 || account_choice <= 0) {
				if(account_choice == -1) {
					return;
				}
				else {
					System.out.println("selection is out of index");
					flowCustomerAccountOptions();
				}
			}
			else if (account_choice ==  bankAccounts.size() + 1){
				String account_name;
				int result;
				int choice = mainView.getNewAccountChoice();
				String account_type = choice == 1 ? "savings" : (choice == 2) ? "checkings":null;
				if(account_type == null) {
					System.err.print("\nInvalid Input");
					throw new Exception();
				}
				switch(choice) {
				case 1: 
				case 2: 
						account_name = mainView.getAccountName();
					    result = bankCustomerDatabase.addApplicationForNewAccount(session_username, account_type , account_name, UUID.randomUUID().toString());
					    if(result == 1 ) {
					    	mainView.printResponse("Sucessfully opened the account. Approval pending");
					    }else {
					    	mainView.printResponse("something went wrong");
					    	return;
					    }
				        break;
				default:
					mainView.printResponse("Invalid choice");
					    flowCustomerAccountOptions();
					    break;
				}
			}
			else {
			    current_account_index = account_choice - 1;
			    if(bankAccounts.get(current_account_index).getStatus().equalsIgnoreCase("closed")) {
			    	throw new AccountClosedException();
			    	
			    }else if (bankAccounts.get(current_account_index).getStatus().equalsIgnoreCase("pending")) {
			    	throw new AccountPendingException();
			    }else {
			    	flowAccountChoice();
			    }
			    
			}
			
		}catch(AccountClosedException e) {
			mainView.printResponse("Can't choose that account for it was closed");
			flowCustomerAccountOptions();
		}catch(AccountPendingException e) {
			mainView.printResponse("Can't choose that account for it is awaiting approval decision");
			flowCustomerAccountOptions();
		}catch(Exception e) {
			mainView.printResponse("Something went wrong, no worries try again.");
			flowCustomerAccountOptions();
		}
		flowCustomerAccountOptions();
	}
	
	public static void flowAccountChoice() {
		try {
			int result = mainView.getAccountActionChoices();
			
			switch(result) {
			  case 1:
				     float amount  = mainView.getLoanRequestAmount();
				     int result_of_loan_request_insert = bankCustomerDatabase.applyForALoan(amount, session_username, bankAccounts.get(current_account_index).getId(), UUID.randomUUID().toString());
				     if(result_of_loan_request_insert == 1) {
				    	 mainView.printResponse("Successfully applied for loan");
				     }else {
				    	 mainView.printResponse("Failed to apply for loan");
				     }
				     break;
			  case 2:
				    ArrayList<BankLoan> loans = bankCustomerDatabase.getAllLoansOnBankAccount(bankAccounts.get( current_account_index).getId());
				    //loans = (ArrayList<BankLoan>) (new ArrayList(loans.stream().filter((s) -> {
				    // 	return s.getStatus().equals("approved");
				   // }).collect(Collectors.toList())));
				    int loan_index = mainView.getLoanToPayOn(loans);
				    loans.get(0);
				    if(loans.get(loan_index).getStatus().equalsIgnoreCase("pending")){
				    	mainView.printResponse("\nLoan is pending, no action is needed");
				    	break;
				    }
				    if(loans.get(loan_index).getPending_amount() <= 0.00){
				    	mainView.printResponse("\nLoan has been paid off, no action is needed");
				    	break;
				    }
				    if(loans.get(loan_index).getStatus().equalsIgnoreCase("disapproved")){
				    	mainView.printResponse("\nLoan is disapproved, no action is needed");
				    	break;
				    }
				    float amount_to_pay = mainView.getPaymentAmount();
				    Boolean result_ = bankCustomerDatabase.payOffALoan(loans.get(loan_index).getIdOfLoan(), amount_to_pay,bankAccounts.get(current_account_index).getId());
				    if(result_ == null) {
				    	mainView.printResponse("Server Error");
				    	
				    }
				    else if(result_ == true) {
				    	loans.get(loan_index).setAmount(((float)loans.get(loan_index).getAmount()) - (float)amount_to_pay);
				    	mainView.printResponse(String.format("Successfullly pay %f on loan", amount_to_pay));
				    }else if(result_ == false) {
				    	mainView.printResponse("Unable to pay on loan, insufficient funds in account");
				    }
				  	break;
			  case 3:
				     float withdrawal_amount = mainView.getWithdrawalAmount();
				     Boolean result_of_withdrawal = bankCustomerDatabase.withdrawFromAccount(bankAccounts.get(current_account_index).getId(), withdrawal_amount);
				     if(result_of_withdrawal == null) {
				    	 mainView.printResponse("\nServer error");
				     }
				     else if(result_of_withdrawal == true) {
				    	 mainView.printResponse("\nSuccessfully withdrew " + withdrawal_amount); 
				    	 bankAccounts.get(current_account_index).setBalance( bankAccounts.get(current_account_index).getBalance() - withdrawal_amount );
				     }else if(result_of_withdrawal == false) {
				    	 mainView.printResponse("\nInsufficient funds"); 
				     }
				     break;
			  case 4:
				  	float deposit_amount = mainView.getDepositAmount();
				  	Boolean result_of_deposit = bankCustomerDatabase.addBalancetoBankAccount(session_username, bankAccounts.get(current_account_index).getId(), deposit_amount);
				  	if(result_of_deposit == true) {
				  		mainView.printResponse("\nSuccessfully deposit the requested amount.");
				  		bankAccounts.get(current_account_index).setBalance(bankAccounts.get(current_account_index).getBalance() + deposit_amount);
				  	}else {
				  		mainView.printResponse("\nFailed to deposit the requested amount.");
				  	}
				  	break;
			  case 5:
				  	return;
				  	
			
			}
			 flowAccountChoice();
		}catch(Exception e) {
			mainView.printResponse("Something went wrong, trying again");
			flowAccountChoice();
		}
	}
	
	
	

}
