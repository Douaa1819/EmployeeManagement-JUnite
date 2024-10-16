package org.employeemanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.employeemanagement.entities.Employee;
import org.employeemanagement.entities.LeaveRequest;
import org.employeemanagement.repository.EmployeeRepositoryImpl;
import org.employeemanagement.repository.LeaveRequestRepositoryImpl;
import org.employeemanagement.repository.interfaces.EmployeeRepository;
import org.employeemanagement.repository.interfaces.LeaveRequestRepository;
import org.employeemanagement.service.EmployeeServiceImpl;
import org.employeemanagement.service.LeaveRequestServiceImpl;
import org.employeemanagement.service.interfaces.EmployeeService;
import org.employeemanagement.service.interfaces.LeaveRequestService;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/leaveRequest")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class LeaveRequestServlet extends HttpServlet {

    private LeaveRequestService leaveRequestService;
    private EmployeeService employeeService;
    private LeaveRequestRepository leaveRequestRepository;
    private EmployeeRepository employeeRepository;

    @Override
    public void init() throws ServletException {
        LeaveRequestRepository leaveRequestRepository = new LeaveRequestRepositoryImpl();
        this.employeeRepository = new EmployeeRepositoryImpl();
        this.employeeService = new EmployeeServiceImpl(this.employeeRepository);
        this.leaveRequestService = new LeaveRequestServiceImpl(leaveRequestRepository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/leaveRequestForm.jsp").forward(request, response);
    }




    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // Assuming employeeId is hardcoded for now (you can modify to fetch it from session or request)
            Long employeeId = 3L;
            if (employeeId == null) {
                throw new IllegalArgumentException("Employee ID is required.");
            }

            // Retrieve form parameters
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String reason = request.getParameter("reason");

            // Debugging outputs
            System.out.println("Employee ID: " + employeeId);
            System.out.println("Start Date: " + startDateStr);
            System.out.println("End Date: " + endDateStr);
            System.out.println("Reason: " + reason);
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            // Validate form inputs
            if (startDate == null) {
                throw new IllegalArgumentException("Start date is required.");
            }
            if (endDate == null || endDateStr.isEmpty()) {
                throw new IllegalArgumentException("End date is required.");
            }
            if (reason == null || reason.isEmpty()) {
                throw new IllegalArgumentException("Reason is required.");
            }

            Part filePart = request.getPart("certificate");
            if (filePart == null || filePart.getSize() == 0) {
                throw new IllegalArgumentException("File upload is required.");
            }

            String fileName = filePart.getSubmittedFileName();
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String filePath = uploadPath + File.separator + fileName;
            filePart.write(filePath);

            // Fetch employee details
            Employee employee = employeeService.getEmployeeById(employeeId);
            if (employee == null) {
                throw new IllegalArgumentException("Employee not found for ID: " + employeeId);
            }

            // Create and save the leave request
            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setEmployee(employee);
            leaveRequest.setStartDate(startDate);
            leaveRequest.setEndDate(endDate);
            leaveRequest.setReason(reason);
            leaveRequest.setDocumentPath(filePath);

            System.out.println("Attempting to save leave request: " + leaveRequest);
            leaveRequestService.submitLeaveRequest(leaveRequest);

            // Set success message in session
            HttpSession session = request.getSession();
            session.setAttribute("message", "Leave request submitted successfully!");

        } catch (IllegalArgumentException e) {
            // Handle specific error related to form input
            HttpSession session = request.getSession();
            session.setAttribute("message", "Error: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            // Handle generic error during processing
            HttpSession session = request.getSession();
            session.setAttribute("message", "Error submitting leave request: " + e.getMessage());
            System.out.println("Error submitting leave request: " + e.getMessage());
        }

        // Redirect back to the leave request form
        response.sendRedirect("leaveRequest");
    }
}