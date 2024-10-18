import org.employeemanagement.entities.Employee;
import org.employeemanagement.entities.LeaveRequest;
import org.employeemanagement.repository.interfaces.EmployeeRepository;
import org.employeemanagement.repository.interfaces.LeaveRequestRepository;
import org.employeemanagement.service.LeaveRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LeaveRequestServiceTest {

    private LeaveRequestServiceImpl leaveRequestService;
    private LeaveRequestRepository leaveRequestRepository;
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() {
        leaveRequestRepository = mock(LeaveRequestRepository.class);
        employeeRepository = mock(EmployeeRepository.class);
        leaveRequestService = new LeaveRequestServiceImpl(leaveRequestRepository, employeeRepository);
    }

    /**
     * Teste la soumission d'une demande de congé avec succès.
     * Vérifie que le solde de congés est mis à jour et que la demande de congé est enregistrée.
     *
     * @throws Exception si une erreur se produit lors de la soumission de la demande.
     */
    @Test
    public void testSubmitLeaveRequest_Success() throws Exception {
        // Arrange
        Long employeeId = 1L;
        Date startDate = Date.valueOf("2024-10-01");
        Date endDate = Date.valueOf("2024-10-05");
        String reason = "Vacation";
        String documentPath = "path/to/document";

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setSoldConge(10);

        when(employeeRepository.findById(employeeId)).thenReturn(employee);

        // Act
        leaveRequestService.submitLeaveRequest(employeeId, startDate, endDate, reason, documentPath);

        // Assert
        verify(leaveRequestRepository, times(1)).save(any(LeaveRequest.class));
        assertEquals(5, employee.getSoldConge());
    }

    /**
     * Teste la soumission d'une demande de congé lorsqu'un employé n'est pas trouvé.
     * Vérifie que l'exception "Employee not found" est levée.
     */
    @Test
    public void testSubmitLeaveRequest_EmployeeNotFound() {
        // Arrange
        Long employeeId = 1L;
        Date startDate = Date.valueOf("2024-10-01");
        Date endDate = Date.valueOf("2024-10-05");

        when(employeeRepository.findById(employeeId)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () ->
                leaveRequestService.submitLeaveRequest(employeeId, startDate, endDate, "Vacation", "path/to/document")
        );

        assertEquals("Employee not found", exception.getMessage());
    }

    /**
     * Teste la soumission d'une demande de congé avec un solde insuffisant.
     * Vérifie que l'exception "Insufficient leave balance" est levée.
     */
    @Test
    public void testSubmitLeaveRequest_InsufficientLeaveBalance() {
        // Arrange
        Long employeeId = 1L;
        Date startDate = Date.valueOf("2024-10-01");
        Date endDate = Date.valueOf("2024-10-05");

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setSoldConge(3);

        when(employeeRepository.findById(employeeId)).thenReturn(employee);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () ->
                leaveRequestService.submitLeaveRequest(employeeId, startDate, endDate, "Vacation", "path/to/document")
        );

        assertEquals("Insufficient leave balance", exception.getMessage());
    }

    /**
     * Teste la récupération de toutes les demandes de congé.
     * Vérifie que la liste de toutes les demandes est renvoyée correctement.
     */
    @Test
    public void testGetAllLeaveRequests() {
        // Arrange
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(new LeaveRequest());
        leaveRequests.add(new LeaveRequest());

        when(leaveRequestRepository.findAll()).thenReturn(leaveRequests);

        // Act
        List<LeaveRequest> result = leaveRequestService.getAllLeaveRequests();

        // Assert
        assertEquals(2, result.size());
    }

    /**
     * Teste l'approbation d'une demande de congé avec succès.
     * Vérifie que la demande est marquée comme approuvée.
     */
    @Test
    public void testApproveLeaveRequest_Success() {
        // Arrange
        Long requestId = 1L;
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(requestId);
        leaveRequest.setApproved(false);

        when(leaveRequestRepository.findById(requestId)).thenReturn(leaveRequest);

        // Act
        leaveRequestService.approveLeaveRequest(requestId);

        // Assert
        verify(leaveRequestRepository, times(1)).update(leaveRequest);
        assertTrue(leaveRequest.isApproved());
    }

    /**
     * Teste l'approbation d'une demande de congé qui n'existe pas.
     * Vérifie que l'exception "Demande de congé non trouvée" est levée.
     */
    @Test
    public void testApproveLeaveRequest_NotFound() {
        // Arrange
        Long requestId = 1L;

        when(leaveRequestRepository.findById(requestId)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                leaveRequestService.approveLeaveRequest(requestId)
        );

        assertEquals("Demande de congé non trouvée", exception.getMessage());
    }

    /**
     * Teste la soumission d'une demande de congé avec des dates invalides.
     * Vérifie que l'exception "Invalid leave dates" est levée.
     */
    @Test
    public void testSubmitLeaveRequest_InvalidDates() {
        // Arrange
        Long employeeId = 1L;
        Date startDate = Date.valueOf("2024-10-06");
        Date endDate = Date.valueOf("2024-10-05");

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setSoldConge(10);

        when(employeeRepository.findById(employeeId)).thenReturn(employee);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                leaveRequestService.submitLeaveRequest(employeeId, startDate, endDate, "Vacation", "path/to/document")
        );

        assertEquals("Start date cannot be after end date", exception.getMessage());
    }


    /**
     * Teste la soumission d'une demande de congé sans raison.
     * Vérifie que l'exception "Reason for leave request cannot be empty" est levée.
     */
    @Test
    public void testSubmitLeaveRequest_EmptyReason() {
        Long employeeId = 1L;
        Date startDate = Date.valueOf("2024-10-01");
        Date endDate = Date.valueOf("2024-10-05");

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setSoldConge(10);

        when(employeeRepository.findById(employeeId)).thenReturn(employee);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                leaveRequestService.submitLeaveRequest(employeeId, startDate, endDate, "", "path/to/document")
        );

        assertEquals("Reason for leave request cannot be empty", exception.getMessage());
    }

    /**
     * Teste l'approbation d'une demande de congé déjà approuvée.
     * Vérifie que l'exception "Leave request is already approved" est levée.
     */
    @Test
    public void testApproveLeaveRequest_AlreadyApproved() {
        // Arrange
        Long requestId = 1L;
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(requestId);
        leaveRequest.setApproved(true);

        when(leaveRequestRepository.findById(requestId)).thenReturn(leaveRequest);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                leaveRequestService.approveLeaveRequest(requestId)
        );

        assertEquals("Leave request is already approved", exception.getMessage());
    }

    /**
     * Teste la récupération d'une demande de congé par ID.
     * Vérifie que la demande de congé est renvoyée si elle existe.
     */
    @Test
    public void testGetLeaveRequestById_Success() {
        // Arrange
        Long requestId = 1L;
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(requestId);

        when(leaveRequestRepository.findById(requestId)).thenReturn(leaveRequest);

        // Act
        LeaveRequest result = leaveRequestService.findLeaveRequestById(requestId);

        // Assert
        assertNotNull(result);
        assertEquals(requestId, result.getId());
    }

    /**
     * Teste la récupération d'une demande de congé inexistante par ID.
     * Vérifie que l'exception "Leave request not found" est levée.
     */
    @Test
    public void testGetLeaveRequestById_NotFound() {
        Long requestId = 1L;

        when(leaveRequestRepository.findById(requestId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                leaveRequestService.findLeaveRequestById(requestId)
        );

        assertEquals("Leave request not found", exception.getMessage());
    }


    /**
     * Teste la mise à jour d'une demande de congé avec succès.
     * Vérifie que les détails de la demande de congé sont mis à jour correctement.
     */
    @Test
    public void testUpdateLeaveRequest_Success() {
        // Arrange
        Long requestId = 1L;
        java.util.Date startDate = new java.util.Date();
        java.util.Date endDate = new java.util.Date();
        String reason = "Medical Leave";
        String documentPath = "path/to/document";
        boolean approved = false;

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(requestId);
        leaveRequest.setApproved(approved);

        when(leaveRequestRepository.findById(requestId)).thenReturn(leaveRequest);

        // Act
        leaveRequestService.updateLeaveRequest(requestId, startDate, endDate, reason, documentPath, approved);

        // Assert
        verify(leaveRequestRepository, times(1)).update(leaveRequest);
        assertEquals(startDate, leaveRequest.getStartDate());
        assertEquals(endDate, leaveRequest.getEndDate());
        assertEquals(reason, leaveRequest.getReason());
        assertEquals(documentPath, leaveRequest.getDocumentPath());
        assertEquals(approved, leaveRequest.isApproved());
    }

    /**
     * Teste le ajouuut d'une demande de congé avec des dates null.
     * Vérifie que l'exception "Start date and end date cannot be null" est levée.
     */
    @Test
    public void testSubmiteLeaveRequest_NullDates() {
        // Arrange
        Long requestId = 1L;

        // Create a mock employee and configure the repository to return it
        Employee mockEmployee = new Employee();
        mockEmployee.setSoldConge(10);
        when(employeeRepository.findById(requestId)).thenReturn(mockEmployee);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                leaveRequestService.submitLeaveRequest(requestId, null, null, "Medical Leave", "path/to/document")
        );

        assertEquals("Start date and end date cannot be null", exception.getMessage());
    }


    /**
     * Teste la mise à jour d'une demande de congé inexistante.
     * Vérifie que l'exception "Leave request not found" est levée.
     */
    @Test
    public void testUpdateLeaveRequest_NotFound() {
        // Arrange
        Long requestId = 1L;

        when(leaveRequestRepository.findById(requestId)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                leaveRequestService.updateLeaveRequest(requestId, new java.util.Date(), new java.util.Date(), "Medical Leave", "path/to/document", false)
        );

        assertEquals("Leave request not found", exception.getMessage());
    }


}