package org.employeemanagement.service;

import org.employeemanagement.entities.Employee;
import org.employeemanagement.entities.LeaveRequest;
import org.employeemanagement.repository.interfaces.EmployeeRepository;
import org.employeemanagement.repository.interfaces.LeaveRequestRepository;
import org.employeemanagement.service.interfaces.EmployeeService;
import org.employeemanagement.service.interfaces.LeaveRequestService;

import java.util.List;

public class LeaveRequestServiceImpl implements LeaveRequestService {
    private  LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Override
    public LeaveRequest submitLeaveRequest(LeaveRequest leaveRequest) {

        return leaveRequestRepository.save(leaveRequest);
    }


    @Override
    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    @Override
    public void approveLeaveRequest(Long requestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId);
        if (leaveRequest == null) {
            throw new IllegalArgumentException("Demande de congé non trouvée");
        }

        leaveRequest.setApproved(true);
        leaveRequestRepository.update(leaveRequest);
    }
}
