package com.example.Employee;

import com.example.models.LeaveRequest;

public class Director extends Approver{


    @Override
    public void approve(LeaveRequest request) {

        if (request.getDays() > 5 && request.getDays() <= 10) {
            request.setStatus("Approved by Director");
        } else {
            request.setStatus("Rejected (More than 10 days)");
        }
    }
}
