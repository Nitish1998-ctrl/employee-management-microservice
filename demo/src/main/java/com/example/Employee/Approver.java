package com.example.Employee;

import com.example.models.LeaveRequest;

public abstract class Approver {

    protected Approver nextApprover;

    public void setNextApprover(Approver nextApprover) {
        this.nextApprover = nextApprover;
    }

    public abstract void approve(LeaveRequest request);
}
