package com.example.Employee;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final EmployeeLogRepository logRepository;

    public KafkaConsumerService(EmployeeLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @KafkaListener(topics = "employee-logs", groupId = "employee-group")
    public void consume(String message) {


//        if (message.contains("fail")) {
//            throw new RuntimeException("Simulated failure");
//        }

        EmployeeLog log = new EmployeeLog();
        log.setAction(message);
        log.setTimestamp(java.time.LocalDateTime.now().toString());

        logRepository.save(log);
    }
}
