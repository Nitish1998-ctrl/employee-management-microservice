package com.example.Employee;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "employee_logs")
@Data
public class EmployeeLog {

    @Id
    private String id;

    private Long employeeId;
    private String action;
    private String timestamp;
}
