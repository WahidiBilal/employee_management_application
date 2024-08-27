package com.example.employee_management_application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.example.employee_management_application.dto.EmployeeDTO;
import com.example.employee_management_application.service.DepartmentService;
import com.example.employee_management_application.service.EmployeeService;

class EmployeeWebControllerTest {

	@Mock
	private EmployeeService employeeService;

	@Mock
	private DepartmentService departmentService;

	@Mock
	private Model model;

	@InjectMocks
	private EmployeeWebController employeeWebController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testListEmployees() {
		// Given
		when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

		// When
		String viewName = employeeWebController.listEmployees(model);

		// Then
		assertEquals("employees/list", viewName);

		// Verify
		verify(employeeService, times(1)).getAllEmployees();
		verify(model, times(1)).addAttribute("employees", Collections.emptyList());
	}

	@Test
	void testShowCreateForm() {
		// Given
		when(departmentService.getAllDepartments()).thenReturn(Collections.emptyList());

		// When
		String viewName = employeeWebController.showCreateForm(model);

		// Then
		assertEquals("employees/create", viewName);

		// Verify
		verify(model, times(1)).addAttribute("departments", Collections.emptyList());
	}

	@Test
	void testCreateEmployee() {
		// Given
		EmployeeDTO employeeDTO = new EmployeeDTO();

		// When
		String viewName = employeeWebController.createEmployee(employeeDTO);

		// Then
		assertEquals("redirect:/employees", viewName);

		// Verify
		verify(employeeService, times(1)).createEmployee(employeeDTO);
	}

	@Test
	void testShowEditForm() {
		// Given
		EmployeeDTO employeeDTO = new EmployeeDTO();
		when(employeeService.getEmployeeById(anyInt())).thenReturn(employeeDTO);
		when(departmentService.getAllDepartments()).thenReturn(Collections.emptyList());

		// When
		String viewName = employeeWebController.showEditForm(1, model);

		// Then
		assertEquals("employees/edit", viewName);

		// Verify
		verify(employeeService, times(1)).getEmployeeById(1);
		verify(departmentService, times(1)).getAllDepartments();
		verify(model, times(1)).addAttribute("employee", employeeDTO);
		verify(model, times(1)).addAttribute("departments", Collections.emptyList());
	}

	@Test
	void testDeleteEmployee() {
		// Given
		int employeeId = 1;

		// When
		String viewName = employeeWebController.deleteEmployee(employeeId);

		// Then
		assertEquals("redirect:/employees", viewName);

		// Verify
		verify(employeeService, times(1)).deleteEmployee(employeeId);
	}

}
