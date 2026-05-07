package com.example.Employee;
import com.example.models.Employee;
import com.example.models.EmployeeDTO;

import com.example.models.LeaveRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final EmployeeLogRepository logRepo;
    private final KafkaProducerService kafkaProducerService;

    public EmployeeService(EmployeeRepository employeeRepo,
                           EmployeeLogRepository logRepo,
                           KafkaProducerService kafkaProducerService) {
        this.employeeRepo = employeeRepo;
        this.logRepo = logRepo;
        this.kafkaProducerService=kafkaProducerService;
    }

    @CacheEvict(value = "employees", key = "'allEmployees'", beforeInvocation = true)
    public Employee saveEmployee(EmployeeDTO dto) {

        // Convert DTO → Entity
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());

        // Save in PostgreSQL
        Employee savedEmployee = employeeRepo.save(employee);

        // Send log to Kafka
        String logMessage = "Employee Created: ID=" + savedEmployee.getId();
        //String logMessage ="fail";
        kafkaProducerService.sendLog(logMessage);

        return savedEmployee;

//        // Create log for MongoDB
//        EmployeeLog log = new EmployeeLog();
//        log.setEmployeeId(savedEmployee.getId());
//        log.setAction("EMPLOYEE_CREATED");
//        log.setTimestamp(java.time.LocalDateTime.now().toString());
//
//        // Save in MongoDB
//        logRepo.save(log);
//
//        return savedEmployee;
    }

//    public Employee saveEmployee(EmployeeDTO dto) {
//
//        Employee employee = new Employee();
//        employee.setName(dto.getName());
//        employee.setDepartment(dto.getDepartment());
//        employee.setSalary(dto.getSalary());
//
//        return repository.save(employee);
//    }

    @Cacheable(value = "employees", key = "'allEmployees'")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepo.findAll()
                .stream()
                .map(employee -> {

                    EmployeeDTO dto = new EmployeeDTO();
                    dto.setName(employee.getName());
                    dto.setDepartment(employee.getDepartment());
                    dto.setSalary(employee.getSalary());

                    return dto;
                })
                .toList();
    }

    public LeaveRequest applyLeave(LeaveRequest request) {

        // Build chain
        Approver teamLead = new TeamLead();
        Approver manager = new Manager();
        Approver director = new Director();

        teamLead.setNextApprover(manager);
        manager.setNextApprover(director);

        // Start approval process
        teamLead.approve(request);

        return request;
    }
}
