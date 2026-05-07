package com.example.Employee;

import com.example.models.LeaveRequest;

public class Manager extends Approver{
    @Override
    public void approve(LeaveRequest request) {

        if (request.getDays() > 2 && request.getDays() <= 5) {
            request.setStatus("Approved by Manager");
        } else if (nextApprover != null) {
            nextApprover.approve(request);
        }
    }
}
