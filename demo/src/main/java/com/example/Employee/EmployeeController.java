package com.example.Employee;

import com.example.models.Employee;
import com.example.models.EmployeeDTO;
import com.example.models.LeaveRequest;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService service;
    private final RateLimiterService rateLimiterService;


    public EmployeeController(EmployeeService service,
                              RateLimiterService rateLimiterService) {
        this.service = service;
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<EmployeeDTO>> getAll() {
        return ResponseEntity.ok(service.getAllEmployees());
    }

    @GetMapping("/findAllEmployees")
    public ResponseEntity<?> getAll(HttpServletRequest request) {

        // Use IP address as key (you can also use API Key)
        String clientKey = request.getRemoteAddr();
        logger.info("Incoming request for findAllEmployees from IP: {}", clientKey);
      Bucket bucket = rateLimiterService.resolveBucket(clientKey);

        // Consume 1 request
        if (bucket.tryConsume(1)) {

            List<EmployeeDTO> data = service.getAllEmployees();
            return ResponseEntity.ok(data);
        }

        // If limit exceeded
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests - Rate limit exceeded. Try again later.");
    }
    @PostMapping("/CreateEmployee")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO employee) {
        try {
            Employee savedEmployee = service.saveEmployee(employee);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Employee created successfully with ID: " + savedEmployee.getId());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating employee: " + e.getMessage());
        }
    }


    @PostMapping("/applyLeave")
    public LeaveRequest apply(@RequestBody LeaveRequest request) {
        return service.applyLeave(request);
    }


}
