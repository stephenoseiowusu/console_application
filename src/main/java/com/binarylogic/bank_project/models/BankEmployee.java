package com.binarylogic.bank_project.models;

public class BankEmployee {
	
	private String employeeName; 
	private String employeeStatus;
   
	public BankEmployee(String employeeName, String employeeStatus) {
		this.employeeName = employeeName;
		this.employeeStatus = employeeStatus;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeStatus() {
		return employeeStatus;
	}

	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}
	
	
}
