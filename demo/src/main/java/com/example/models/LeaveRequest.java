package com.example.models;

import lombok.Data;

@Data
public class LeaveRequest {

    private Long employeeId;
    private int days;
    private String status;
}
