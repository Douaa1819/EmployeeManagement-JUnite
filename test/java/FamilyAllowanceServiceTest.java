import org.employeemanagement.entities.Employee;
import org.employeemanagement.entities.FamilyAllowance;
import org.employeemanagement.repository.interfaces.EmployeeRepository;
import org.employeemanagement.repository.interfaces.FamilyAllowanceRepository;
import org.employeemanagement.service.EmployeeServiceImpl;
import org.employeemanagement.service.FamilyAllowanceServiceImpl;
import org.employeemanagement.service.interfaces.FamilyAllowanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This class tests the functionality of the FamilyAllowanceService.
 * It includes tests for successful cases, handling of different salary ranges,
 * and handling when an allowance is not found for a specific employee.
 */

public class FamilyAllowanceServiceTest {


        @Mock
        private FamilyAllowanceRepository familyAllowanceRepository;

        @InjectMocks
        private FamilyAllowanceServiceImpl familyAllowanceService;

        /**
         * Initialize mocks before each test.
         */
        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        /**
         * This test verifies the successful calculation and saving of family allowances
         * when the employee's salary is below 6000. It checks if the total allowance
         * is correctly calculated and the result is successfully saved.
         */
        @Test
        public void testCalculateAndSaveAllowance_Success() {
            // Arrange: Set up mock employee and expected allowance values
            Employee employee = new Employee();
            employee.setId(1L);
            double salary = 5000;
            int numberOfChildren = 3;
            String month = "October";
            FamilyAllowance expectedFamilyAllowance = new FamilyAllowance(employee, numberOfChildren, 5900, month);

            // Mock repository behavior
            when(familyAllowanceRepository.save(any(FamilyAllowance.class))).thenReturn(expectedFamilyAllowance);

            // Act: Call the service method to calculate and save the allowance
            FamilyAllowance result = familyAllowanceService.calculateAndSaveAllowance(numberOfChildren, salary, employee, month);

            // Assert: Verify the result and that the repository's save method was called once
            assertNotNull(result);
            assertEquals(5900, result.getTotalAmount());
            verify(familyAllowanceRepository, times(1)).save(any(FamilyAllowance.class));
        }

        /**
         * This test verifies that the family allowance for a given employee ID is correctly returned.
         */
        @Test
        public void testGetAllowanceByEmployeeId_Success() {
            // Arrange: Set up an employee and mock expected allowance data
            Employee employee = new Employee();
            employee.setId(1L);
            FamilyAllowance expectedFamilyAllowance = new FamilyAllowance(employee, 2, 6500, "September");

            // Mock repository behavior
            when(familyAllowanceRepository.findByEmployeeId(1L)).thenReturn(expectedFamilyAllowance);

            // Act: Retrieve the allowance by employee ID
            FamilyAllowance result = familyAllowanceService.getAllowanceByEmployeeId(1L);

            // Assert: Check if the returned allowance matches the expected values
            assertNotNull(result);
            assertEquals(6500, result.getTotalAmount());
            assertEquals("September", result.getMonth());
            verify(familyAllowanceRepository, times(1)).findByEmployeeId(1L);
        }

        /**
         * This test verifies the behavior when no allowance is found for a given employee ID.
         */
        @Test
        public void testGetAllowanceByEmployeeId_NotFound() {
            // Arrange: Mock the repository to return null when the employee ID is not found
            when(familyAllowanceRepository.findByEmployeeId(1L)).thenReturn(null);

            // Act: Try to retrieve the allowance
            FamilyAllowance result = familyAllowanceService.getAllowanceByEmployeeId(1L);

            // Assert: The result should be null, and the repository method should have been called once
            assertNull(result);
            verify(familyAllowanceRepository, times(1)).findByEmployeeId(1L);
        }

        /**
         * This test checks the calculation and saving of family allowances
         * when the employee's salary is low (less than 6000).
         */
        @Test
        public void testCalculateAndSaveAllowance_LowSalary() {
            // Arrange: Set up mock data for an employee with a lower salary and multiple children
            Employee employee = new Employee();
            employee.setId(1L);
            double salary = 4500;
            int numberOfChildren = 2;
            String month = "October";
            FamilyAllowance expectedFamilyAllowance = new FamilyAllowance(employee, numberOfChildren, 5100, month);

            // Mock repository behavior
            when(familyAllowanceRepository.save(any(FamilyAllowance.class))).thenReturn(expectedFamilyAllowance);

            // Act: Call the service to calculate and save the allowance
            FamilyAllowance result = familyAllowanceService.calculateAndSaveAllowance(numberOfChildren, salary, employee, month);

            // Assert: Check that the total amount is correctly calculated and saved
            assertEquals(5100, result.getTotalAmount());
            verify(familyAllowanceRepository, times(1)).save(any(FamilyAllowance.class));
        }


    /**
     * This test verifies the calculation of family allowance for 4 children and a salary less than 6000.
     */
    @Test
    public void testCalculateAllowance_4Children_LowSalary() {
        // Arrange
        Employee employee = new Employee();
        employee.setId(1L);
        double salary = 5000; // Salary < 6000
        int numberOfChildren = 4; // More than 3 children
        String month = "November";

        // Expected calculation: (3 * 300) + (1 * 150) = 1050
        double expectedTotal = 5000 + 1050; // Salary + Allowance = 6050
        FamilyAllowance expectedFamilyAllowance = new FamilyAllowance(employee, numberOfChildren, expectedTotal, month);

        when(familyAllowanceRepository.save(any(FamilyAllowance.class))).thenReturn(expectedFamilyAllowance);

        // Act
        FamilyAllowance result = familyAllowanceService.calculateAndSaveAllowance(numberOfChildren, salary, employee, month);

        // Assert
        assertEquals(expectedTotal, result.getTotalAmount());
        verify(familyAllowanceRepository, times(1)).save(any(FamilyAllowance.class));
    }

    /**
     * This test verifies the calculation of family allowance for 6 children and a salary less than 6000.
     */
    @Test
    public void testCalculateAllowance_6Children_LowSalary() {
        // Arrange
        Employee employee = new Employee();
        employee.setId(1L);
        double salary = 5500; // Salary < 6000
        int numberOfChildren = 6; // Max children for this case
        String month = "December";

        // Expected calculation: (3 * 300) + (3 * 150) = 1350
        double expectedTotal = salary + 1350; // Salary + Allowance = 6850
        FamilyAllowance expectedFamilyAllowance = new FamilyAllowance(employee, numberOfChildren, expectedTotal, month);

        when(familyAllowanceRepository.save(any(FamilyAllowance.class))).thenReturn(expectedFamilyAllowance);

        // Act
        FamilyAllowance result = familyAllowanceService.calculateAndSaveAllowance(numberOfChildren, salary, employee, month);

        // Assert
        assertEquals(6850, result.getTotalAmount());
        verify(familyAllowanceRepository, times(1)).save(any(FamilyAllowance.class));
    }

    /**
     * This test verifies the calculation of family allowance for 2 children and a salary greater than 8000.
     */
    @Test
    public void testCalculateAllowance_2Children_HighSalary() {
        // Arrange
        Employee employee = new Employee();
        employee.setId(1L);
        double salary = 8500; // Salary > 8000
        int numberOfChildren = 2; // Less than 3 children
        String month = "January";

        // Expected calculation: (2 * 200) = 400
        double expectedTotal = salary + 400; // Salary + Allowance = 8900
        FamilyAllowance expectedFamilyAllowance = new FamilyAllowance(employee, numberOfChildren, expectedTotal, month);

        when(familyAllowanceRepository.save(any(FamilyAllowance.class))).thenReturn(expectedFamilyAllowance);

        // Act
        FamilyAllowance result = familyAllowanceService.calculateAndSaveAllowance(numberOfChildren, salary, employee, month);

        // Assert
        assertEquals(8900, result.getTotalAmount());
        verify(familyAllowanceRepository, times(1)).save(any(FamilyAllowance.class));
    }

    /**
     * This test verifies the calculation of family allowance for 5 children and a salary greater than 8000.
     */
    @Test
    public void testCalculateAllowance_5Children_HighSalary() {
        // Arrange
        Employee employee = new Employee();
        employee.setId(1L);
        double salary = 9000; // Salary > 8000
        int numberOfChildren = 5; // More than 3 children
        String month = "February";

        // Expected calculation: (3 * 200) + (2 * 110) = 730
        double expectedTotal = salary + 730; // Salary + Allowance = 9730
        FamilyAllowance expectedFamilyAllowance = new FamilyAllowance(employee, numberOfChildren, expectedTotal, month);

        when(familyAllowanceRepository.save(any(FamilyAllowance.class))).thenReturn(expectedFamilyAllowance);

        // Act
        FamilyAllowance result = familyAllowanceService.calculateAndSaveAllowance(numberOfChildren, salary, employee, month);

        // Assert
        assertEquals(9730, result.getTotalAmount());
        verify(familyAllowanceRepository, times(1)).save(any(FamilyAllowance.class));
    }

    }