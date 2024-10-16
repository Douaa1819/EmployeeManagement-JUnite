package org.employeemanagement.repository.interfaces;

import org.employeemanagement.entities.LeaveRequest;

import java.util.List;

public interface LeaveRequestRepository {
    LeaveRequest save(LeaveRequest leaveRequest);
    LeaveRequest update(LeaveRequest leaveRequest);
    LeaveRequest findById(Long id);
    List<LeaveRequest> findAll();
}
