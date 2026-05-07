package com.example.Employee;

import com.example.models.LeaveRequest;

public class TeamLead extends Approver{

    @Override
    public void approve(LeaveRequest request) {

        if (request.getDays() <= 2) {
            request.setStatus("Approved by Team Lead");
        } else if (nextApprover != null) {
            nextApprover.approve(request);
        }
    }
}
