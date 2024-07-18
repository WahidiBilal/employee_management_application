package com.example.employee_management_application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.employee_management_application.dao.EmployeeRepository;
import com.example.employee_management_application.dto.EmployeeDTO;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public List<EmployeeDTO> getAllEmployees() {
		return employeeRepository.findAll().stream().map(EmployeeDTO::fromEntity).collect(Collectors.toList());
	}

}