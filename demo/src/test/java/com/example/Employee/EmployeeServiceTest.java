package com.example.Employee;

import com.example.models.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeServiceTest {
    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEmployee() {

        Employee employee = new Employee("Nitish","IT",250);
        when(repository.save(employee)).thenReturn(employee);
        Employee result = service.saveEmployee(employee);
        assertNotNull(result);
        assertEquals("Nitish",result.getName());

        verify(repository).save(employee);

    }

}