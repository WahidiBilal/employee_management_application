package com.example.employee_management_application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

import com.example.employee_management_application.dto.DepartmentDTO;
import com.example.employee_management_application.service.DepartmentService;

class DepartmentwebControllerTest {

    @Mock
    private DepartmentService departmentService;

    @Mock
    private Model model;

    @InjectMocks
    private DepartmentwebController departmentWebController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListDepartments() {
        // Given
        when(departmentService.getAllDepartments()).thenReturn(Collections.emptyList());

        // When
        String viewName = departmentWebController.listDepartments(model);

        // Then
        assertEquals("departments/list", viewName);

        // Verify
        verify(departmentService, times(1)).getAllDepartments();
    }


    @Test
    void testShowCreateForm() {
        // When
        String viewName = departmentWebController.showCreateForm(model);

        // Then
        assertEquals("departments/create", viewName);
    }



    @Test
    void testCreateDepartment() {
        // Given
        DepartmentDTO departmentDTO = new DepartmentDTO();

        // When
        String viewName = departmentWebController.createDepartment(departmentDTO);

        // Then
        assertEquals("redirect:/departments", viewName);

        // Verify
        verify(departmentService, times(1)).createDepartment(any(DepartmentDTO.class));
    }

    @Test
    void testShowEditForm() {
        // Given
        DepartmentDTO departmentDTO = new DepartmentDTO();
        when(departmentService.getDepartmentById(anyInt())).thenReturn(departmentDTO);

        // When
        String viewName = departmentWebController.showEditForm(1, model);

        // Then
        assertEquals("departments/edit", viewName);

        // Verify
        verify(departmentService, times(1)).getDepartmentById(1);
        verify(model, times(1)).addAttribute("department", departmentDTO);
    }

   

    @Test
    void testDeleteDepartment() {
        // When
        String viewName = departmentWebController.deleteDepartment(1);

        // Then
        assertEquals("redirect:/departments", viewName);

        // Verify
        verify(departmentService, times(1)).deleteDepartment(1);
    }
}
