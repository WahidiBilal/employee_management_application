package com.example.employee_management_application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.employee_management_application.dao.DepartmentRepository;
import com.example.employee_management_application.dto.DepartmentDTO;
import com.example.employee_management_application.dto.EmployeeDTO;
import com.example.employee_management_application.exception.CustomCreateException;
import com.example.employee_management_application.model.Department;
import com.example.employee_management_application.model.Employee;

class DepartmentServiceTest {

	@Mock
	private DepartmentRepository departmentRepository;

	@InjectMocks
	private DepartmentService departmentService;

	@Captor
	private ArgumentCaptor<Department> departmentCaptor;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAllDepartments() {
		// Given
		Department department1 = new Department("HR");
		department1.setDid(1);

		Department department2 = new Department("IT");
		department2.setDid(2);

		Employee employee1 = new Employee("employee1", 25, "employee1@example.com", 50000.0, department1);
		employee1.setEid(1);

		Employee employee2 = new Employee("employee2", 30, "employee2@example.com", 60000.0, department2);
		employee2.setEid(2);

		department1.setEmployees(List.of(employee1));
		department2.setEmployees(List.of(employee2));

		// When
		when(departmentRepository.findAll()).thenReturn(List.of(department1, department2));

		List<DepartmentDTO> result = departmentService.getAllDepartments();

		// Then
		assertEquals(2, result.size());

		DepartmentDTO departmentDTO1 = result.get(0);
		assertEquals(1, departmentDTO1.getDid());
		assertEquals("HR", departmentDTO1.getDname());

		DepartmentDTO departmentDTO2 = result.get(1);
		assertEquals(2, departmentDTO2.getDid());
		assertEquals("IT", departmentDTO2.getDname());

		// Verify
		verify(departmentRepository, times(1)).findAll();
	}

	@Test
    void testCreateDepartment_Success() {
        // Given
        EmployeeDTO employeeDTO1 = new EmployeeDTO("employee1", 25, "employee1@example.com", 50000.0);
        EmployeeDTO employeeDTO2 = new EmployeeDTO("employee2", 30, "employee2@example.com", 60000.0);

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDname("IT");
        departmentDTO.setEmployees(List.of(employeeDTO1, employeeDTO2));

        Department savedDepartment = new Department("IT");
        savedDepartment.setDid(1);

        Employee employee1 = new Employee("employee1", 25, "employee1@example.com", 50000.0, savedDepartment);
        Employee employee2 = new Employee("employee2", 30, "employee2@example.com", 60000.0, savedDepartment);

        savedDepartment.setEmployees(List.of(employee1, employee2));

        // When
        when(departmentRepository.save(any(Department.class))).thenReturn(savedDepartment);

        DepartmentDTO result = departmentService.createDepartment(departmentDTO);

        // Then
        assertNotNull(result);
        assertEquals("IT", result.getDname());
        assertEquals(2, result.getEmployees().size());

        // Verify
        verify(departmentRepository, times(1)).save(departmentCaptor.capture());

        Department capturedDepartment = departmentCaptor.getValue();
        assertEquals("IT", capturedDepartment.getDname());
        assertEquals(2, capturedDepartment.getEmployees().size());

        // Assert 
        assertTrue(capturedDepartment.getEmployees().stream()
            .allMatch(e -> e.getDepartment().equals(capturedDepartment)));
    }

    @Test
    void testCreateDepartment_EmptyEmployees() {
        // Given
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDname("HR");
        departmentDTO.setEmployees(List.of());

        Department savedDepartment = new Department("HR");
        savedDepartment.setDid(2);
        savedDepartment.setEmployees(List.of());

        // When
        when(departmentRepository.save(any(Department.class))).thenReturn(savedDepartment);

        DepartmentDTO result = departmentService.createDepartment(departmentDTO);

        // Then
        assertNotNull(result);
        assertEquals("HR", result.getDname());
        assertEquals(0, result.getEmployees().size());

        // Verify
        verify(departmentRepository, times(1)).save(departmentCaptor.capture());

        Department capturedDepartment = departmentCaptor.getValue();
        assertEquals("HR", capturedDepartment.getDname());
        assertEquals(0, capturedDepartment.getEmployees().size());
    }

    @Test
    void testCreateDepartment_Failure() {
        // Given
        EmployeeDTO employeeDTO = new EmployeeDTO("employee1", 25, "employee1@example.com", 50000.0);
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDname("IT");
        departmentDTO.setEmployees(List.of(employeeDTO));

        // When
        when(departmentRepository.save(any(Department.class))).thenThrow(new RuntimeException("Database error"));

        // Then
        CustomCreateException exception = assertThrows(CustomCreateException.class, () -> {
            departmentService.createDepartment(departmentDTO);
        });

        assertEquals("Failed to create department", exception.getMessage());

        // Verify
        verify(departmentRepository, times(1)).save(any(Department.class));
    }
}
