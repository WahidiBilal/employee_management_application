package com.example.employee_management_application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employee_management_application.dao.DepartmentRepository;
import com.example.employee_management_application.dao.EmployeeRepository;
import com.example.employee_management_application.dto.EmployeeDTO;
import com.example.employee_management_application.exception.CustomCreateException;
import com.example.employee_management_application.model.Department;
import com.example.employee_management_application.model.Employee;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;

	private final DepartmentRepository departmentRepository;

	public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
	}

	public List<EmployeeDTO> getAllEmployees() {
		return employeeRepository.findAll().stream().map(EmployeeDTO::fromEntity).toList();
	}

	public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
		Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
				.orElseThrow(() -> new CustomCreateException("Department not found"));

		Employee employee = new Employee(employeeDTO.getEname(), employeeDTO.getEage(), employeeDTO.getEmail(),
				employeeDTO.getEsalary(), department);

		try {
			Employee savedEmployee = employeeRepository.save(employee);
			return EmployeeDTO.fromEntity(savedEmployee);
		} catch (Exception e) {
			throw new CustomCreateException("Failed to create employee", e);
		}

	}

	public EmployeeDTO getEmployeeById(Integer eid) {
		Employee employee = employeeRepository.findById(eid)
				.orElseThrow(() -> new CustomCreateException("Employee not found with id: " + eid));
		return EmployeeDTO.fromEntity(employee);
	}

	

	public void deleteEmployee(Integer eid) {
		if (!employeeRepository.existsById(eid)) {
			throw new CustomCreateException("Employee not found");
		}
		employeeRepository.deleteById(eid);
	}

}
