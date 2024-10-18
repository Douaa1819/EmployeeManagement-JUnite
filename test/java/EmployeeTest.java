import org.employeemanagement.entities.Employee;
import org.employeemanagement.exception.DatabaseOperationException;
import org.employeemanagement.exception.IdNotFoundException;
import org.employeemanagement.exception.InvalidInputException;
import org.employeemanagement.repository.interfaces.EmployeeRepository;
import org.employeemanagement.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class EmployeeTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the addition of a valid employee.
     */
    @Test
    public void testAddEmployee() {
        Employee employee = createEmployee(null, "Douaa Doe", "douaa.doe@example.com", "securePassword", "IT", "Developer", "123 Main St", "1234567890", 60000.00, 10, "123-45-6789");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee createdEmployee = employeeService.addEmployee(employee);

        assertNotNull(createdEmployee);
        assertEquals("Douaa Doe", createdEmployee.getName());
        verify(employeeRepository).save(employee);
    }

    /**
     * Test addition of an employee with an invalid name (empty).
     */
    @Test
    public void testAddEmployeeInvalidName() {
        Employee employee = createEmployee(null, "", "douaa.doe@example.com", "securePassword", "IT", "Developer", "123 Main St", "1234567890", 60000.00, 10, "123-45-6789");

        Exception exception = assertThrows(InvalidInputException.class, () -> employeeService.addEmployee(employee));
        assertEquals("Invalid input: Employee name cannot be empty", exception.getMessage());
    }

    /**
     * Test addition of an employee with an invalid email (empty).
     */
    @Test
    public void testAddEmployeeInvalidEmail() {
        Employee employee = createEmployee(null, "Douaa Doe", "", "securePassword", "IT", "Developer", "123 Main St", "1234567890", 60000.00, 10, "123-45-6789");

        Exception exception = assertThrows(InvalidInputException.class, () -> employeeService.addEmployee(employee));
        assertEquals("Invalid input: Employee email cannot be empty", exception.getMessage());
    }

    /**
     * Test updating an existing employee.
     */
    @Test
    public void testUpdateEmployee() {
        Employee employee = createEmployee(1L, "Douaa Doe", "douaa.doe@example.com", "securePassword", "IT", "Developer", "123 Main St", "1234567890", 60000.00, 10, "123-45-6789");
        when(employeeRepository.update(any(Employee.class))).thenReturn(employee);

        Employee updatedEmployee = employeeService.updateEmployee(employee);

        assertNotNull(updatedEmployee);
        assertEquals("Douaa Doe", updatedEmployee.getName());
        verify(employeeRepository).update(employee);
    }

    /**
     * Test updating an employee that does not exist.
     */
    @Test
    public void testUpdateNonExistentEmployee() {
        Employee employee = createEmployee(1L, "Douaa Doe", "douaa.doe@example.com", "securePassword", "IT", "Developer", "123 Main St", "1234567890", 60000.00, 10, "123-45-6789");
        when(employeeRepository.update(any(Employee.class))).thenThrow(new DatabaseOperationException("Failed to update the employee."));

        Exception exception = assertThrows(DatabaseOperationException.class, () -> employeeService.updateEmployee(employee));
        assertEquals("A database error occurred: Failed to update the employee.", exception.getMessage());
    }

    /**
     * Test getting an employee by ID that exists.
     */
    @Test
    public void testGetEmployeeById() {
        Employee employee = createEmployee(1L, "Douaa Doe", "douaa.doe@example.com", "securePassword", "IT", "Developer", "123 Main St", "1234567890", 60000.00, 10, "123-45-6789");
        when(employeeRepository.findById(eq(1L))).thenReturn(employee);

        Employee foundEmployee = employeeService.getEmployeeById(1L);

        assertEquals("Douaa Doe", foundEmployee.getName());
        verify(employeeRepository).findById(1L);
    }
    /**
     * Test getting an employee by ID that does not exist.
     */
    @Test
    public void testGetEmployeeByIdNotFound() {
        when(employeeRepository.findById(eq(999L))).thenReturn(null);

        Exception exception = assertThrows(IdNotFoundException.class, () -> employeeService.getEmployeeById(999L));
        assertEquals("The ID 999 was not found.", exception.getMessage());
    }

    /**
     * Test getting all employees.
     */
    @Test
    public void testGetAllEmployees() {
        Employee employee1 = createEmployee(1L, "Douaa Doe", "douaa.doe@example.com", "securePassword", "IT", "Developer", "123 Main St", "1234567890", 60000.00, 10, "123-45-6789");
        Employee employee2 = createEmployee(2L, "Jane Doe", "jane.doe@example.com", "securePassword", "HR", "Manager", "456 Another St", "0987654321", 70000.00, 12, "987-65-4321");

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> foundEmployees = employeeService.getAllEmployees();

        assertEquals(2, foundEmployees.size());
        verify(employeeRepository).findAll();
    }

    /**
     * Test getting all employees when none exist.
     */
    @Test
    public void testGetAllEmployeesEmpty() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Employee> foundEmployees = employeeService.getAllEmployees();

        assertTrue(foundEmployees.isEmpty());
        verify(employeeRepository).findAll();
    }

    /**
     * Test deleting an existing employee.
     */
    @Test
    public void testDeleteEmployee() {
        doNothing().when(employeeRepository).delete(eq(1L));

        assertDoesNotThrow(() -> employeeService.deleteEmployee(1L));
        verify(employeeRepository).delete(1L);
    }

    /**
     * Test deleting an employee that does not exist.
     */
    @Test
    public void testDeleteNonExistentEmployee() {
        doThrow(new DatabaseOperationException("Failed to delete the employee.")).when(employeeRepository).delete(eq(999L));

        Exception exception = assertThrows(DatabaseOperationException.class, () -> employeeService.deleteEmployee(999L));
        assertEquals("A database error occurred: Failed to delete the employee", exception.getMessage());
    }

    /**
     * Helper method to create an Employee instance with the given parameters.
     */
    private Employee createEmployee(Long id, String name, String email, String password, String department, String position, String address, String phoneNumber, double salary, int soldConge, String socialSecurityNumber) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        employee.setEmail(email);
        employee.setPassword(password);
        employee.setBirthDate(new java.util.Date());
        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setAddress(address);
        employee.setPhoneNumber(phoneNumber);
        employee.setSalary(salary);
        employee.setSoldConge(soldConge);
        employee.setSocialSecurityNumber(socialSecurityNumber);
        return employee;
    }


    @Test
    public void testAddEmployeeInvalidPassword() {
        Employee employee = createEmployee(null, "Douaa", "douaa@example.com", "", "IT", "Developer", "123 Main St", "1234567890", 60000.00, 10, "123-45-6789");

        Exception exception = assertThrows(InvalidInputException.class, () -> employeeService.addEmployee(employee));
        assertEquals("Invalid input: Employee password cannot be empty.", exception.getMessage());
    }


    @Test
    public void testAddEmployeeInvalidBirthDate() {
        Employee employee = createEmployee(null, "Douaa", "douaa@example.com", "securePassword", "IT", "Developer", "123 Main St", "1234567890", 60000.00, 10, "123-45-6789");
        employee.setBirthDate(null);

        Exception exception = assertThrows(InvalidInputException.class, () -> employeeService.addEmployee(employee));
        assertEquals("Invalid input: Employee birth date cannot be null.", exception.getMessage());
    }

    @Test
    public void testAddEmployeeInvalidDepartment() {
        Employee employee = createEmployee(null, "Douaa", "douaa@example.com", "securePassword", "", "Developer", "123 Main St", "1234567890", 60000.00, 10, "123-45-6789");

        Exception exception = assertThrows(InvalidInputException.class, () -> employeeService.addEmployee(employee));
        assertEquals("Invalid input: Employee department cannot be empty.", exception.getMessage());
    }

    @Test
    public void testAddEmployeeInvalidPosition() {
        Employee employee = createEmployee(null, "Douaa", "douaa@example.com", "securePassword", "IT", "", "123 Main St", "1234567890", 60000.00, 10, "123-45-6789");

        Exception exception = assertThrows(InvalidInputException.class, () -> employeeService.addEmployee(employee));
        assertEquals("Invalid input: Employee position cannot be empty.", exception.getMessage());
    }

    @Test
    public void testAddEmployeeInvalidAddress() {
        Employee employee = createEmployee(null, "Douaa", "douaa@example.com", "securePassword", "IT", "Developer", "", "1234567890", 60000.00, 10, "123-45-6789");

        Exception exception = assertThrows(InvalidInputException.class, () -> employeeService.addEmployee(employee));
        assertEquals("Invalid input: Employee address cannot be empty.", exception.getMessage());
    }

    @Test
    public void testAddEmployeeInvalidPhoneNumber() {
        Employee employee = createEmployee(null, "Douaa", "douaa@example.com", "securePassword", "IT", "Developer", "123 Main St", "", 60000.00, 10, "123-45-6789");

        Exception exception = assertThrows(InvalidInputException.class, () -> employeeService.addEmployee(employee));
        assertEquals("Invalid input: Employee phone number cannot be empty.", exception.getMessage());
    }

    @Test
    public void testAddEmployeeInvalidSalary() {
        Employee employee = createEmployee(null, "Douaa", "douaa@example.com", "securePassword", "IT", "Developer", "123 Main St", "1234567890", 0, 10, "123-45-6789");

        Exception exception = assertThrows(InvalidInputException.class, () -> employeeService.addEmployee(employee));
        assertEquals("Invalid input: Employee salary must be greater than zero.", exception.getMessage());
    }

    @Test
    public void testAddEmployeeInvalidSoldConge() {
        Employee employee = createEmployee(null, "Douaa", "douaa@example.com", "securePassword", "IT", "Developer", "123 Main St", "1234567890", 60000.00, 0, "123-45-6789");

        Exception exception = assertThrows(InvalidInputException.class, () -> employeeService.addEmployee(employee));
        assertEquals("Invalid input: Employee soldConge cannot than zero.", exception.getMessage());
    }

    @Test
    public void testAddEmployeeInvalidSocialSecurityNumber() {
        Employee employee = createEmployee(null, "Douaa", "douaa@example.com", "securePassword", "IT", "Developer", "123 Main St", "1234567890", 60000.00, 10, "");

        Exception exception = assertThrows(InvalidInputException.class, () -> employeeService.addEmployee(employee));
        assertEquals("Invalid input: Employee social security number cannot be empty.", exception.getMessage());
    }




}
