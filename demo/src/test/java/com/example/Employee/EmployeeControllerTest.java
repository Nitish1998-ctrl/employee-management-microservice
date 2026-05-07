package com.example.Employee;

import com.example.models.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeControllerTest {

    @Mock
    private EmployeeService service;

    @InjectMocks
    private EmployeeController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEndpoint(){

        String response = controller.test();
        assertEquals("Working",response);
    }

    @Test
    void testCreateEmployee_Success(){

        Employee employee = new Employee();
        employee.setName("Nitish");
        employee.setDepartment("IT");
        employee.setSalary(5000);

        Employee employee1 = new Employee();
        employee1.setId(1l);
        employee1.setName("John");

        when(service.saveEmployee(any(Employee.class))).thenReturn(employee1);
        ResponseEntity<?> response = controller.createEmployee(employee);

        assertEquals(201,response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Employee created successfully"));

        verify(service).saveEmployee(employee);
    }


    @Test
    void testCreateEmployee_Exception() {

        Employee emp = new Employee();
        emp.setName("John");

        when(service.saveEmployee(any(Employee.class)))
                .thenThrow(new RuntimeException("DB Error"));

        ResponseEntity<?> response = controller.createEmployee(emp);

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Error while creating employee"));

        verify(service).saveEmployee(emp);
    }
}