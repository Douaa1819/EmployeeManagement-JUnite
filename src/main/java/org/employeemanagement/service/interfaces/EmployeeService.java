package org.employeemanagement.service.interfaces;

import org.employeemanagement.entities.Employee;

import java.util.List;

public interface EmployeeService {

    Employee addEmployee(Employee employee);
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Employee employee);
    List<Employee> getAllEmployees();
    void deleteEmployee(Long id);
}
