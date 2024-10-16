package org.employeemanagement.service.interfaces;

import org.employeemanagement.entities.LeaveRequest;

import java.util.List;

public interface LeaveRequestService {
    LeaveRequest submitLeaveRequest( LeaveRequest leaveRequest);
    List<LeaveRequest> getAllLeaveRequests();
    void approveLeaveRequest(Long requestId);
}
