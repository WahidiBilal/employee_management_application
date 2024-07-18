package com.example.employee_management_application.dto;

import java.util.List;

public class DepartmentDTO {
	private Integer did;
	private String dname;
	private List<EmployeeDTO> employees;

	// Getters and Setters

	public Integer getDid() {
		return did;
	}

	public void setDid(Integer did) {
		this.did = did;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public List<EmployeeDTO> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeDTO> employees) {
		this.employees = employees;
	}
}
