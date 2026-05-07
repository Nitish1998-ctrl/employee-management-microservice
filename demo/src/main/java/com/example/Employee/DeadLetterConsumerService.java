package com.example.Employee;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeadLetterConsumerService {

    private final EmployeeLogRepository logRepository;

    public DeadLetterConsumerService(EmployeeLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @KafkaListener(topics = "employee-logs.DLT", groupId = "dlq-group")
    public void handleDLQ(ConsumerRecord<String, String> record) {

        System.out.println("DLQ message: " + record.value());

        EmployeeLog log = new EmployeeLog();
        log.setAction(record.value());
        log.setTimestamp(LocalDateTime.now().toString());

        // Add metadata
//        log.setPartition(String.valueOf(record.partition()));
//        log.setOffset(String.valueOf(record.offset()));

        try {
            logRepository.save(log);
        } catch (Exception e) {
            System.err.println("Failed to save DLQ message: " + record.value());
        }
    }
}
